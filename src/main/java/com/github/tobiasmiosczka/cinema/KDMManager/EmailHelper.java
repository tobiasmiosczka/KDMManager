package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.KDM;
import org.jdom2.JDOMException;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

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

    private static Set<KDM> handleMessages(Collection<Message> messages) throws IOException, JDOMException, ParseException, MessagingException {
        Set<KDM> kdms = new HashSet<>();
        for (Message message : messages) {
            if (message.getContentType().contains("multipart")) {
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); ++i) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
                            || !StringHelper.isBlank(bodyPart.getFileName())) {
                        String fileName = bodyPart.getFileName();
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
