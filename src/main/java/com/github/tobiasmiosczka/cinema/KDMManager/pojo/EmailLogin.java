package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

public class EmailLogin {
    private String host;
    private int port;
    private String user;
    private String password;
    private String protocol;
    private String folder;
    private boolean tls;

    public EmailLogin(String host, int port, String user, String password, String protocol, String folder, boolean tls) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.protocol = protocol;
        this.folder = folder;
        this.tls = tls;
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

    public String getProtocol() {
        return protocol;
    }

    public String getFolder() {
        return folder;
    }

    public boolean isTls() {
        return tls;
    }

    public String toString() {
        return this.getUser() + "@" + this.host + ":" + this.port;
    }
}
