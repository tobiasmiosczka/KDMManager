package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.FtpLogin;

import java.util.List;

public interface IUpdateGui {
    void onErrorOccurred(String message);
    void onUpdateEmailLogins(List<EmailLogin> emailLogins);
    void onUpdateFtpLogins(List<FtpLogin> ftpLogins);
}
