package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

import java.util.Date;

public class KDM {

    private final String fileName,
                         data,
                         server;
    private final Date  validFrom,
                        validTo;

    public KDM(String fileName, String data, String server, Date validFrom, Date validTo) {
        this.fileName = fileName;
        this. data = data;
        this.server = server;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getData() {
        return data;
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

    @Override
    public String toString() {
        return "KDM for " + this.getServer() + "Valid: " + this.getValidFrom() + " - " + this.getValidTo();
    }
}
