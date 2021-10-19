package com.github.tobiasmiosczka.cinema.kdmmanager;

import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.FtpLogin;

import java.util.List;

public interface IUpdateGui {
    void onErrorOccurred(String message);
    void onUpdateEmailLogins(List<EmailLogin> emailLogins);
    void onUpdateFtpLogins(List<FtpLogin> ftpLogins);
}
