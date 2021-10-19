package com.github.tobiasmiosczka.cinema.kdmmanager.helper;

public class FtpException extends Throwable {

    private final int code;

    public FtpException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "FTP Error Code: " + getCode();
    }
}
