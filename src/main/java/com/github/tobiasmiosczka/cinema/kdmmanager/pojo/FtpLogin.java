package com.github.tobiasmiosczka.cinema.kdmmanager.pojo;

public record FtpLogin(String description, String host, int port, String user,
                       String password, String serial) {

    public String getDescription() {
        return description;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSerial() {
        return serial;
    }

    public String toString() {
        return this.getDescription();
    }
}
