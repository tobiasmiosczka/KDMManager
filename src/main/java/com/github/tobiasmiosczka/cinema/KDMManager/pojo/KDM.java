package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

import java.util.Date;

public class KDM {

    private final String    title,
                            fileName,
                            data,
                            server;
    private final Date  validFrom,
                        validTo;

    public KDM(String title, String fileName, String data, String server, Date validFrom, Date validTo) {
        this.title = title;
        this.fileName = fileName;
        this. data = data;
        this.server = server;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }

    public String getData() {
        return data;
    }

    public String getServer() {
        return server;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }
}
