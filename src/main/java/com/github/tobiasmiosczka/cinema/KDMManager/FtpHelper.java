package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.FtpException;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.FtpLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.KDM;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FtpHelper {

    private Map<String, FTPClient> serverMap;

    public FtpHelper(Collection<FtpLogin> ftpLogins) throws IOException, FtpException {
        serverMap = new HashMap<>();
        for (FtpLogin ftpLogin : ftpLogins) {
            serverMap.put(ftpLogin.getSerial(), getFtpClient(ftpLogin));
        }
    }

    private FTPClient getFtpClient(FtpLogin ftpLogin) throws IOException, FtpException {
        FTPClient ftpClient = new FTPClient();

        ftpClient.connect(ftpLogin.getHost(), ftpLogin.getPort());
        if (!ftpClient.login(ftpLogin.getUser(), ftpLogin.getPassword()))
            throw new FtpException(ftpClient.getReplyCode());
        ftpClient.enterLocalPassiveMode();
        if (!ftpClient.setFileType(FTP.BINARY_FILE_TYPE))
            throw new FtpException(ftpClient.getReplyCode());

        return ftpClient;
    }

    public void uploadFiles(Collection<KDM> files) throws IOException, FtpException {
        for (KDM file : files) {
            FTPClient ftpClient = serverMap.get(file.getServer());
            if (!ftpClient.storeFile(file.getFileName(), file.getInputStream())) {
                throw new FtpException(ftpClient.getReplyCode());
            }
        }
    }
}
