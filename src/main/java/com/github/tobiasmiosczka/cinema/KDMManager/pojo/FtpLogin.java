package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

public class FtpLogin {
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String serial;

    public FtpLogin(String host, int port, String user, String password, String serial) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.serial = serial;
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
        return this.getUser() + "@" + this.getHost() + ":" + this.getPort();
    }
}
