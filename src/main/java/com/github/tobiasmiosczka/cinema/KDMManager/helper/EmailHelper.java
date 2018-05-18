package com.github.tobiasmiosczka.cinema.KDMManager.helper;

import com.github.tobiasmiosczka.cinema.KDMManager.gui.IUpdate;
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EmailHelper {

    public static Collection<KDM> unzip(InputStream inputStream) throws IOException, JDOMException, ParseException {
        Collection<KDM> result = new HashSet<>();
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            if (fileName.endsWith(".xml")) {
                result.add(getKdmFromInputStream(zis, fileName));
                zipEntry = zis.getNextEntry();
            }
        }
        zis.closeEntry();
        zis.close();
        return result;
    }

    private static KDM getKdmFromInputStream(InputStream inputStream, String fileName) throws JDOMException, IOException, ParseException {
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
        String sData = scanner.useDelimiter("\\A").next();
        Document document = XmlHelper.getDocument(StringHelper.toInputStream(sData));
        return new KDM(
            fileName.substring(fileName.lastIndexOf("/") + 1),
            sData,
            XmlHelper.getKdmServer(document),
            XmlHelper.getKdmValidFrom(document),
            XmlHelper.getKdmValidTo(document)
        );
    }

    private static String decode(String s) {
        if (s == null)
            return null;
        if (s.matches("^=\\?.*\\?.*\\?.*\\?=$")) {
            String cypher = s.replaceAll("^=\\?.*?\\?", "").replaceAll("\\?.*?\\?=$", "");
            //String charset = s.replaceAll("^=\\?", "").replaceAll("\\?.*?\\?.*?\\?=$", "");
            String text = s.replaceAll("^=\\?.*?\\?.*?\\?", "").replaceAll("\\?=$", "");
            switch (cypher) {
                case "Q":
                    return text;
                case "B":
                    return new String(Base64.getDecoder().decode(text));
                default:
                    return s;
            }
        } else {
            return s;
        }
    }

    private static Collection<KDM> handleMessages(Message[] messages, IUpdate iUpdate) throws IOException, JDOMException, ParseException, MessagingException {
        Collection<KDM> kdms = new HashSet<>();
        for (int i = 0; i < messages.length; ++i) {
            Message message = messages[i];
            iUpdate.onUpdateEmailLoading(i, messages.length);

            if (message.getContentType().contains("multipart")) {
                Multipart multipart = (Multipart) message.getContent();
                for (int j = 0; j < multipart.getCount(); ++j) {
                    BodyPart bodyPart = multipart.getBodyPart(j);
                    String fileName = decode(bodyPart.getFileName());
                    if (fileName != null && (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) || !StringHelper.isBlank(fileName))) {
                        if (fileName.endsWith(".zip"))
                            kdms.addAll(unzip(bodyPart.getInputStream()));
                        if (fileName.endsWith(".xml"))
                            kdms.add(getKdmFromInputStream(bodyPart.getInputStream(), fileName));
                    }
                }
            }
        }
        iUpdate.onUpdateEmailLoading(messages.length, messages.length);
        return kdms;
    }

    public static Collection<KDM> getKdmsFromEmail(Collection<EmailLogin> emailLogins, IUpdate iUpdate) throws MessagingException, IOException, JDOMException, ParseException {
        Collection<KDM> kdms = new HashSet<>();
        int current = 0;
        for (EmailLogin emailLogin : emailLogins) {
            iUpdate.onUpdateEmailBox(current++, emailLogins.size(), emailLogin.toString());
            Properties properties = new Properties();
            properties.put("mail.pop3.host", emailLogin.getHost());
            properties.put("mail.pop3.port", emailLogin.getPort());
            properties.put("mail.pop3.starttls.enable", emailLogin.isTls());
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore(emailLogin.getProtocol());
            store.connect(emailLogin.getHost(), emailLogin.getUser(), emailLogin.getPassword());
            Folder emailFolder = store.getFolder(emailLogin.getFolder());
            emailFolder.open(Folder.READ_ONLY);
            kdms.addAll(handleMessages(emailFolder.getMessages(),iUpdate));
            emailFolder.close(false);
            store.close();
        }
        iUpdate.onUpdateEmailBox(current, emailLogins.size(), "");
        return kdms;
    }
}
