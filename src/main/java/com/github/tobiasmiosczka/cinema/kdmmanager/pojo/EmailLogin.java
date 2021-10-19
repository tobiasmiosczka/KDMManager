package com.github.tobiasmiosczka.cinema.kdmmanager.pojo;

public record EmailLogin(String description, String host, int port, String user,
                         String password, String protocol, String folder, boolean tls) {

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
        return this.getDescription();
    }
}
