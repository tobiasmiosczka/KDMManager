package com.github.tobiasmiosczka.cinema.KDMManager.helper;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.KDM;
import org.jdom2.JDOMException;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

public class EmailHelper {

    private static Collection<KDM> getKdmsFromZip(InputStream inputStream) throws IOException, JDOMException, ParseException {
        Collection<KDM> files = ZipHelper.unzip(inputStream);
        for (KDM file : files) {
            if (!file.getFileName().endsWith(".xml")) {
                files.remove(file);
            }
        }
        return files;
    }

    private static Collection<KDM> handleMessages(Collection<Message> messages) throws IOException, JDOMException, ParseException, MessagingException {
        Collection<KDM> kdms = new HashSet<>();
        for (Message message : messages) {
            if (message.getContentType().contains("multipart")) {
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); ++i) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    String fileName = bodyPart.getFileName();
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
                            || !StringHelper.isBlank(fileName)) {
                        if(fileName == null)
                            continue;
                        if (fileName.endsWith(".zip"))
                            kdms.addAll(getKdmsFromZip(bodyPart.getInputStream()));
                        if (fileName.endsWith(".xml"))
                            kdms.add(new KDM(bodyPart.getInputStream(), fileName));
                    }
                }
            }
        }
        return kdms;
    }

    public static Collection<KDM> getKdmsFromEmail(Collection<EmailLogin> emailLogins) throws MessagingException, IOException, JDOMException, ParseException {
        Collection<KDM> kdms = new HashSet<>();
        for (EmailLogin emailLogin : emailLogins) {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", emailLogin.getHost());
            properties.put("mail.pop3.port", emailLogin.getPort());
            properties.put("mail.pop3.starttls.enable", emailLogin.isTls());
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore(emailLogin.getProtocol());
            store.connect(emailLogin.getHost(), emailLogin.getUser(), emailLogin.getPassword());
            Folder emailFolder = store.getFolder(emailLogin.getFolder());
            emailFolder.open(Folder.READ_ONLY);
            kdms.addAll(handleMessages(Arrays.asList(emailFolder.getMessages())));
            emailFolder.close(false);
            store.close();
        }
        return kdms;
    }
}
