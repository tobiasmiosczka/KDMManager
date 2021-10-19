package com.github.tobiasmiosczka.cinema.kdmmanager;

import com.github.tobiasmiosczka.cinema.kdmmanager.helper.*;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.Config;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.FtpLogin;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.KDM;
import org.jdom2.JDOMException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Program {

    private Config config;
    private final IUpdateProgress iUpdateProgress;
    private final IUpdateGui iUpdateGui;
    private final String filename;

    public Program(IUpdateGui iUpdateGui, IUpdateProgress iUpdateProgress, String filename) {
        this.iUpdateGui = iUpdateGui;
        this.iUpdateProgress = iUpdateProgress;
        this.filename = filename;
        loadConfig(filename);
    }

    private void loadConfig(String filename) {
        try {
            this.config = XmlHelper.loadConfig(new FileInputStream(filename));
        } catch (IOException e) {
            this.config = new Config();
            iUpdateGui.onErrorOccurred("Couldn't find " + filename + ". Starting with default configuration.");
        } catch (JDOMException|ConfigParseException e) {
            iUpdateGui.onErrorOccurred("Error occurred while loading config.xml: " + e.getMessage());
        }
        iUpdateGui.onUpdateEmailLogins(config.getEmailLogins());
        iUpdateGui.onUpdateFtpLogins(config.getFtpLogins());
    }

    public void addEmailLogin(EmailLogin emailLogin) {
        if (emailLogin != null){
            config.getEmailLogins().add(emailLogin);
            saveConfig();
            iUpdateGui.onUpdateEmailLogins(config.getEmailLogins());
        }
    }

    public void updateEmailLogin(int selected, EmailLogin newEmailLogin) {
        if (newEmailLogin != null) {
            config.getEmailLogins().set(selected, newEmailLogin);
            saveConfig();
            iUpdateGui.onUpdateEmailLogins(config.getEmailLogins());
        }
    }

    public void deleteEmailLogins(List<EmailLogin> selected) {
        selected.forEach(config.getEmailLogins()::remove);
        saveConfig();
        iUpdateGui.onUpdateEmailLogins(config.getEmailLogins());
    }

    public void addFtpLogin(FtpLogin ftpLogin) {
        if (ftpLogin != null){
            config.getFtpLogins().add(ftpLogin);
            saveConfig();
            iUpdateGui.onUpdateFtpLogins(config.getFtpLogins());
        }
    }

    public void updateFtpLogin(int selected, FtpLogin newFtpLogin) {
        if (newFtpLogin != null) {
            config.getFtpLogins().set(selected, newFtpLogin);
            saveConfig();
            iUpdateGui.onUpdateFtpLogins(config.getFtpLogins());
        }
    }

    public void deleteFtpLogins(List<FtpLogin> selected) {
        selected.forEach(config.getFtpLogins()::remove);
        saveConfig();
        iUpdateGui.onUpdateFtpLogins(config.getFtpLogins());
    }

    private void saveConfig() {
        try {
            XmlHelper.saveConfig(config, new FileOutputStream(filename));
        } catch (IOException e) {
            iUpdateGui.onErrorOccurred("Error while saving config.xml: " + e.getMessage());
        }
    }

    public void loadKdms(boolean ignoreExpiredKdms) {

        if (config.getEmailLogins().isEmpty()) {
            iUpdateGui.onErrorOccurred("No e-mail login specified.");
            return;
        }

        if (config.getFtpLogins().isEmpty()) {
            iUpdateGui.onErrorOccurred("No ftp login specified.");
            return;
        }

        new Thread(() -> {
            Collection<KDM> kdms;
            long start = System.currentTimeMillis();
            iUpdateProgress.onBegin();
            try {
                kdms = EmailHelper.getKdmsFromEmail(config.getEmailLogins(), iUpdateProgress);
            } catch (Exception e) {
                iUpdateProgress.onErrorOccurred("Error occurred while loading KDMs from Email: ", e);
                return;
            }
            if (ignoreExpiredKdms) {
                LocalDateTime now = LocalDateTime.now();
                kdms.removeIf(kdm -> kdm.getValidTo().isBefore(now));
            }
            try {
                FtpHelper ftpHelper = new FtpHelper(config.getFtpLogins());
                ftpHelper.uploadFiles(kdms, iUpdateProgress);
            } catch (IOException | FtpException e) {
                iUpdateProgress.onErrorOccurred("Error occurred while uploading KDMs: ", e);
                return;
            }
            iUpdateProgress.onDone(System.currentTimeMillis() - start);
        }).start();
    }

}
