package com.github.tobiasmiosczka.cinema.KDMManager.pojo;

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
