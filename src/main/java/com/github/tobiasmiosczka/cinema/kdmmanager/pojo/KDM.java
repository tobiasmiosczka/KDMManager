package com.github.tobiasmiosczka.cinema.kdmmanager.pojo;

import java.time.LocalDateTime;

public record KDM(String title, String fileName, String data, String server,
                  LocalDateTime validFrom, LocalDateTime validTo) {

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
