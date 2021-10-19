package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

import java.time.LocalDateTime;

public class KDM {

    private final String    title,
                            fileName,
                            data,
                            server;
    private final LocalDateTime validFrom,
                                validTo;

    public KDM(String title, String fileName, String data, String server, LocalDateTime validFrom, LocalDateTime validTo) {
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

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }
}
