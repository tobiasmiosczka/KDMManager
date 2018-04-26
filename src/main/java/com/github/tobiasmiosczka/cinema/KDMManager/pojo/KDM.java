package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

import com.github.tobiasmiosczka.cinema.KDMManager.XmlHelper;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

public class KDM {

    private String fileName;
    private String data;
    private String server;
    private Date validFrom;
    private Date validTo;

    public KDM(InputStream inputStream, String fileName) throws JDOMException, IOException, ParseException {
        //TODO: improve performance
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
        this.data = scanner.useDelimiter("\\A").next();

        this.fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        Document document = XmlHelper.getDocument(this.getInputStream());
        this.server = XmlHelper.getKdmServer(document);
        this.validFrom = XmlHelper.getKdmValidFrom(document);
        this.validTo = XmlHelper.getKdmValidTo(document);
    }

    public String getFileName() {
        return this.fileName;
    }

    public InputStream getInputStream() {
        return IOUtils.toInputStream(data, StandardCharsets.UTF_8);
    }

    public String getServer() {
        return this.server;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }
}
