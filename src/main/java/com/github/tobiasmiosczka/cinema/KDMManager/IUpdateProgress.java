package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.FtpLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.KDM;

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