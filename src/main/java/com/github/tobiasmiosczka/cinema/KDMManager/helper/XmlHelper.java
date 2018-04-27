package com.github.tobiasmiosczka.cinema.KDMManager.helper;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.FtpLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.Config;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XmlHelper {

    private static Namespace ns1 = Namespace.getNamespace("", "http://www.smpte-ra.org/schemas/430-3/2006/ETM");
    private static Namespace ns2 = Namespace.getNamespace("", "http://www.smpte-ra.org/schemas/430-1/2006/KDM");
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static SAXBuilder saxBuilder = new SAXBuilder();

    private static XMLOutputter outputter = new XMLOutputter();
    private static DocumentBuilder db;

    static {
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        outputter.setFormat(Format.getPrettyFormat());
    }

    public static Document getDocument(InputStream inputStream) throws JDOMException, IOException {
        return saxBuilder.build(inputStream);
    }

    public static Document getDocument(String string) throws IOException, SAXException {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(string));
        return (Document) db.parse(is);
    }

    public static String getKdmServer(Document document) {
        return document.getRootElement()
                .getChild("AuthenticatedPublic", ns1)
                .getChild("RequiredExtensions", ns1)
                .getChild("KDMRequiredExtensions", ns2)
                .getChild("Recipient", ns2)
                .getChild("X509SubjectName", ns2).getValue();
    }

    public static Date getKdmValidFrom(Document document) throws ParseException {
        String value = document.getRootElement()
                .getChild("AuthenticatedPublic", ns1)
                .getChild("RequiredExtensions", ns1)
                .getChild("KDMRequiredExtensions", ns2)
                .getChild("ContentKeysNotValidBefore", ns2)
                .getValue();
        return dateFormat.parse(value);
    }

    public static Date getKdmValidTo(Document document) throws ParseException {
        String value = document.getRootElement()
                .getChild("AuthenticatedPublic", ns1)
                .getChild("RequiredExtensions", ns1)
                .getChild("KDMRequiredExtensions", ns2)
                .getChild("ContentKeysNotValidAfter", ns2)
                .getValue();
        return dateFormat.parse(value);
    }

    private static Map<String, FtpLogin> loadFtpLogins(Document document) throws JDOMException, IOException {
        Map<String, FtpLogin> result = new HashMap<>();
        for (Element child : document.getRootElement().getChild("ftpLogins").getChildren()) {
            FtpLogin ftpLogin = new FtpLogin(
                    child.getChild("host").getValue(),
                    Integer.getInteger(child.getChild("port").getValue()),
                    child.getChild("user").getValue(),
                    child.getChild("password").getValue(),
                    child.getChild("serial").getValue()
            );
            String serial = child.getChild("serial").getValue();
            result.put(serial, ftpLogin);
        }
        return result;
    }

    private static Element ftpLoginToElement(FtpLogin ftpLogin) {
        Element ftpLoginElement = new Element("ftpLogin");
        ftpLoginElement.addContent(new Element("serial").setText(ftpLogin.getSerial()));
        ftpLoginElement.addContent(new Element("host").setText(ftpLogin.getHost()));
        ftpLoginElement.addContent(new Element("port").setText(""+ftpLogin.getPort()));
        ftpLoginElement.addContent(new Element("user").setText(ftpLogin.getUser()));
        ftpLoginElement.addContent(new Element("password"). setText(ftpLogin.getPassword()));
        return ftpLoginElement;
    }

    private static Element emailLoginToElement(EmailLogin emailLogin) {
        Element emailLoginElement = new Element("emailLogin");
        emailLoginElement.addContent(new Element("host").setText(emailLogin.getHost()));
        emailLoginElement.addContent(new Element("port").setText(""+emailLogin.getPort()));
        emailLoginElement.addContent(new Element("user").setText(emailLogin.getUser()));
        emailLoginElement.addContent(new Element("password").setText(emailLogin.getPassword()));
        emailLoginElement.addContent(new Element("protocol").setText(emailLogin.getProtocol()));
        emailLoginElement.addContent(new Element("folder").setText(emailLogin.getFolder()));
        emailLoginElement.addContent(new Element("tls").setText(""+emailLogin.isTls()));
        return emailLoginElement;
    }

    private static FtpLogin elementToFtpLogin(Element element) {
        return new FtpLogin(
                element.getChild("host").getValue(),
                Integer.parseInt(element.getChild("port").getValue()),
                element.getChild("user").getValue(),
                element.getChild("password").getValue(),
                element.getChild("serial").getValue()
        );
    }

    private static EmailLogin elementToEmailLogin(Element element) {
        return new EmailLogin(
                element.getChild("host").getValue(),
                Integer.parseInt(element.getChild("port").getValue()),
                element.getChild("user").getValue(),
                element.getChild("password").getValue(),
                element.getChild("protocol").getValue(),
                element.getChild("folder").getValue(),
                Boolean.parseBoolean(element.getChild("tls").getValue())
        );
    }

    public static Config loadConfig(Document document) {
        Config config = new Config();
        Collection<FtpLogin> ftpLoginMap = config.getFtpLogins();
        document.getRootElement().getChild("ftpLogins").getChildren().stream().forEach(element -> {
            ftpLoginMap.add(elementToFtpLogin(element));
        });
        Collection<EmailLogin> emailLogins = config.getEmailLogins();
        document.getRootElement().getChild("emailLogins").getChildren().stream().forEach(element -> {
            emailLogins.add(elementToEmailLogin(element));
        });
        return config;
    }

    public static Config loadConfig(InputStream inputStream) throws JDOMException, IOException {
        Document document = getDocument(inputStream);
        return loadConfig(document);
    }

    private static Document saveToDocument(Config config) {
        Document document = new Document();
        Element root = new Element("save");
        Element ftpLoginsElement = new Element("ftpLogins");
        for (FtpLogin ftpLogin: config.getFtpLogins()) {
            ftpLoginsElement.addContent(ftpLoginToElement(ftpLogin));
        }
        root.addContent(ftpLoginsElement);
        Element emailLoginsElement = new Element("emailLogins");
        for (EmailLogin emailLogin : config.getEmailLogins()) {
            emailLoginsElement.addContent(emailLoginToElement(emailLogin));
        }
        root.addContent(emailLoginsElement);
        document.setRootElement(root);
        return document;
    }

    public static void saveConfig(Config config, OutputStream outputStream) throws IOException {
        Document document = saveToDocument(config);
        outputter.output(document, outputStream);
    }
}