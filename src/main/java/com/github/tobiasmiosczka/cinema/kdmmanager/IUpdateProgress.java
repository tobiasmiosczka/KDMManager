package com.github.tobiasmiosczka.cinema.kdmmanager;

import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.FtpLogin;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.KDM;

public interface IUpdateProgress {
    void onBegin();
    void onUpdateEmailBox(int current, int total, EmailLogin emailLogin);
    void onUpdateEmailLoading(int current, int total);
    void onKdmUploaded(KDM kdm, FtpLogin ftpLogin, int current, int total);
    void onKdmFound(KDM kdm);
    void onDoneLoading(int count);
    void onDoneUploading(int count);
    void onDone(long timeInMilliseconds);
    void onErrorOccurred(String message, Throwable throwable);
    void logMessage(String message);
}