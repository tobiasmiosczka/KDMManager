package com.github.tobiasmiosczka.cinema.kdmmanager.pojo;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private final List<FtpLogin> ftpLogins;
    private final List<EmailLogin> emailLogins;

    public Config() {
        this.ftpLogins = new ArrayList<>();
        this.emailLogins = new ArrayList<>();
    }

    public List<FtpLogin> getFtpLogins() {
        return ftpLogins;
    }

    public List<EmailLogin> getEmailLogins() {
        return emailLogins;
    }
}
