package com.github.tobiasmiosczka.cinema.KDMManager.helper;

import com.github.tobiasmiosczka.cinema.KDMManager.IUpdateProgress;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.KDM;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EmailHelper {

    private static Collection<KDM> unzip(IUpdateProgress iUpdateProgress, InputStream inputStream) throws IOException, JDOMException, ParseException {
        Collection<KDM> result = new HashSet<>();
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            if (fileName.endsWith(".xml")) {
                result.add(getKdmFromInputStream(iUpdateProgress, zis, fileName));
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return result;
    }

    private static KDM getKdmFromInputStream(IUpdateProgress iUpdateProgress, InputStream inputStream, String fileName) throws JDOMException, IOException, ParseException {
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
        String sData = scanner.useDelimiter("\\A").next();
        Document document = XmlHelper.getDocument(StringHelper.toInputStream(sData));
        KDM kdm = new KDM(
            XmlHelper.getKdmTitle(document),
            fileName.substring(fileName.lastIndexOf("/") + 1),
            sData,
            XmlHelper.getKdmServer(document),
            XmlHelper.getKdmValidFrom(document),
            XmlHelper.getKdmValidTo(document)
        );
        iUpdateProgress.onKdmFound(kdm);
        return kdm;
    }

    private static String decode(String s) {
        if(s == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(s);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static Collection<KDM> handleMessages(Message[] messages, IUpdateProgress iUpdateProgress) throws IOException, JDOMException, ParseException, MessagingException {
        Collection<KDM> kdms = new HashSet<>();
        for (int i = 0; i < messages.length; ++i) {
            Message message = messages[i];
            iUpdateProgress.onUpdateEmailLoading(i, messages.length);
            if (message.getContentType().contains("multipart")) {
                Multipart multipart = (Multipart) message.getContent();
                for (int part = 0; part < multipart.getCount(); ++part) {
                    BodyPart bodyPart = multipart.getBodyPart(part);
                    String fileName = decode(bodyPart.getFileName());
                    if (fileName != null && (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) || !StringHelper.isBlank(fileName))) {
                        if (fileName.endsWith(".zip"))
                            kdms.addAll(unzip(iUpdateProgress, bodyPart.getInputStream()));
                        else if (fileName.endsWith(".xml"))
                            kdms.add(getKdmFromInputStream(iUpdateProgress, bodyPart.getInputStream(), fileName));
                    }
                }
            }
        }
        iUpdateProgress.onUpdateEmailLoading(messages.length, messages.length);
        return kdms;
    }

    public static Collection<KDM> getKdmsFromEmail(Collection<EmailLogin> emailLogins, IUpdateProgress iUpdateProgress) throws MessagingException, IOException, JDOMException, ParseException {
        Collection<KDM> kdms = new HashSet<>();
        int current = 0;
        for (EmailLogin emailLogin : emailLogins) {
            iUpdateProgress.onUpdateEmailBox(current++, emailLogins.size(), emailLogin);
            Properties properties = new Properties();
            properties.put("mail.pop3.host", emailLogin.getHost());
            properties.put("mail.pop3.port", emailLogin.getPort());
            properties.put("mail.pop3.starttls.enable", emailLogin.isTls());
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore(emailLogin.getProtocol());
            store.connect(emailLogin.getHost(), emailLogin.getUser(), emailLogin.getPassword());
            Folder emailFolder = store.getFolder(emailLogin.getFolder());
            emailFolder.open(Folder.READ_ONLY);
            kdms.addAll(handleMessages(emailFolder.getMessages(), iUpdateProgress));
            emailFolder.close(false);
            store.close();
        }
        iUpdateProgress.onDoneLoading(kdms.size());
        return kdms;
    }
}
