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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class XmlHelper {

    private static final Namespace ns1 = Namespace.getNamespace("", "http://www.smpte-ra.org/schemas/430-3/2006/ETM");
    private static final Namespace ns2 = Namespace.getNamespace("", "http://www.smpte-ra.org/schemas/430-1/2006/KDM");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static final SAXBuilder saxBuilder = new SAXBuilder();
    private static final XMLOutputter xmlOutputter = new XMLOutputter();

    static {
        xmlOutputter.setFormat(Format.getPrettyFormat());
    }

    private static String getStringValue(Element element, String name) throws ConfigParseException {
        try {
            return element.getChild(name).getValue();
        } catch (NullPointerException e) {
            throw new ConfigParseException(element, name, "Could not parse string value.");
        }
    }

    private static int getIntegerValue(Element element, String name) throws ConfigParseException {
        try {
            return Integer.parseInt(element.getChild(name).getValue());
        } catch (NullPointerException|NumberFormatException e) {
            throw new ConfigParseException(element, name, "Could not parse integer value.");
        }
    }

    private static boolean getBooleanValue(Element element, String name) throws ConfigParseException {
        try {
             String string = element.getChild(name).getValue();
             switch (string) {
                 case "true": return true;
                 case "false": return false;
                 default: throw new ConfigParseException(element, name, "Could not parse boolean value.");
             }
        } catch (NullPointerException e) {
            throw new ConfigParseException(element, name, "Could not parse boolean value.");
        }
    }

    public static Document getDocument(InputStream inputStream) throws JDOMException, IOException {
        return saxBuilder.build(inputStream);
    }

    public static String getKdmTitle(Document document) throws InvalidKdmException {
        try {
            return document.getRootElement()
                    .getChild("AuthenticatedPublic", ns1)
                    .getChild("RequiredExtensions", ns1)
                    .getChild("KDMRequiredExtensions", ns2)
                    .getChild("ContentTitleText", ns2)
                    .getValue();
        } catch (NullPointerException e) {
            throw new InvalidKdmException();
        }
    }

    public static String getKdmServer(Document document) throws InvalidKdmException {
        try {
            return document.getRootElement()
                    .getChild("AuthenticatedPublic", ns1)
                    .getChild("RequiredExtensions", ns1)
                    .getChild("KDMRequiredExtensions", ns2)
                    .getChild("Recipient", ns2)
                    .getChild("X509SubjectName", ns2)
                    .getValue();
        } catch (NullPointerException e) {
            throw new InvalidKdmException();
        }
    }

    public static LocalDateTime getKdmValidFrom(Document document) throws InvalidKdmException {
        try {
            String value = document.getRootElement()
                    .getChild("AuthenticatedPublic", ns1)
                    .getChild("RequiredExtensions", ns1)
                    .getChild("KDMRequiredExtensions", ns2)
                    .getChild("ContentKeysNotValidBefore", ns2)
                    .getValue();
            return LocalDateTime.parse(value, formatter);
        } catch (NullPointerException e) {
            throw new InvalidKdmException();
        }
    }

    public static LocalDateTime getKdmValidTo(Document document) throws InvalidKdmException {
        try {
            String value = document.getRootElement()
                    .getChild("AuthenticatedPublic", ns1)
                    .getChild("RequiredExtensions", ns1)
                    .getChild("KDMRequiredExtensions", ns2)
                    .getChild("ContentKeysNotValidAfter", ns2)
                    .getValue();
            return LocalDateTime.parse(value, formatter);
        } catch (NullPointerException e) {
            throw new InvalidKdmException();
        }
    }

    private static Element ftpLoginToElement(FtpLogin ftpLogin) {
        Element ftpLoginElement = new Element("ftpLogin");
        ftpLoginElement.addContent(new Element("description").setText(ftpLogin.getDescription()));
        ftpLoginElement.addContent(new Element("serial").setText(ftpLogin.getSerial()));
        ftpLoginElement.addContent(new Element("host").setText(ftpLogin.getHost()));
        ftpLoginElement.addContent(new Element("port").setText(""+ftpLogin.getPort()));
        ftpLoginElement.addContent(new Element("user").setText(ftpLogin.getUser()));
        ftpLoginElement.addContent(new Element("password"). setText(ftpLogin.getPassword()));
        return ftpLoginElement;
    }

    private static Element emailLoginToElement(EmailLogin emailLogin) {
        Element emailLoginElement = new Element("emailLogin");
        emailLoginElement.addContent(new Element("description").setText(emailLogin.getDescription()));
        emailLoginElement.addContent(new Element("host").setText(emailLogin.getHost()));
        emailLoginElement.addContent(new Element("port").setText(""+emailLogin.getPort()));
        emailLoginElement.addContent(new Element("user").setText(emailLogin.getUser()));
        emailLoginElement.addContent(new Element("password").setText(emailLogin.getPassword()));
        emailLoginElement.addContent(new Element("protocol").setText(emailLogin.getProtocol()));
        emailLoginElement.addContent(new Element("folder").setText(emailLogin.getFolder()));
        emailLoginElement.addContent(new Element("tls").setText(""+emailLogin.isTls()));
        return emailLoginElement;
    }

    private static FtpLogin elementToFtpLogin(Element element) throws ConfigParseException {
        return new FtpLogin(
                getStringValue(element, "description"),
                getStringValue(element, "host"),
                getIntegerValue(element, "port"),
                getStringValue(element, "user"),
                getStringValue(element, "password"),
                getStringValue(element, "serial")
        );
    }

    private static EmailLogin elementToEmailLogin(Element element) throws ConfigParseException {
        return new EmailLogin(
                getStringValue(element, "description"),
                getStringValue(element, "host"),
                getIntegerValue(element, "port"),
                getStringValue(element, "user"),
                getStringValue(element, "password"),
                getStringValue(element, "protocol"),
                getStringValue(element, "folder"),
                getBooleanValue(element, "tls")
        );
    }

    public static Config loadConfig(Document document) throws ConfigParseException {
        Config config = new Config();
        Collection<FtpLogin> ftpLoginList = config.getFtpLogins();
        Element ftpLogins = document.getRootElement().getChild("ftpLogins");
        if (ftpLogins != null) {
            for (Element element1 : ftpLogins.getChildren()) {
                ftpLoginList.add(elementToFtpLogin(element1));
            }
        }
        Collection<EmailLogin> emailLoginList = config.getEmailLogins();
        Element emailLogins = document.getRootElement().getChild("emailLogins");
        if (emailLogins != null) {
            for (Element element : emailLogins.getChildren()) {
                emailLoginList.add(elementToEmailLogin(element));
            }
        }
        return config;
    }

    public static Config loadConfig(InputStream inputStream) throws JDOMException, IOException, ConfigParseException {
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
        xmlOutputter.output(document, outputStream);
    }
}
