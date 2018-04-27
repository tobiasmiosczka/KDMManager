package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

public class EmailLogin {
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String protocol;
    private final String folder;
    private final boolean tls;

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
