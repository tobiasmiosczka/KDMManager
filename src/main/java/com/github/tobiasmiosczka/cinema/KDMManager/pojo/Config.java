package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private List<FtpLogin> ftpLogins;
    private List<EmailLogin> emailLogins;

    public Config() {
        this.ftpLogins = new ArrayList<>();
        this.emailLogins = new ArrayList<>();
    }

    public List<FtpLogin> getFtpLogins() {
        return ftpLogins;
    }

    public void setFtpLoginMap(List<FtpLogin> ftpLoginMap) {
        this.ftpLogins = ftpLoginMap;
    }

    public List<EmailLogin> getEmailLogins() {
        return emailLogins;
    }

    public void setEmailLogins(List<EmailLogin> emailLogins) {
        this.emailLogins = emailLogins;
    }
}
