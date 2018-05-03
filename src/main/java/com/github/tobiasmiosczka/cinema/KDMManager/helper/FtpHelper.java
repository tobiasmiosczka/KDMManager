package com.github.tobiasmiosczka.cinema.KDMManager.helper;

import com.github.tobiasmiosczka.cinema.KDMManager.gui.IUpdate;
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

    private final Map<String, FTPClient>  serverMap = new HashMap<>();

    public FtpHelper(Collection<FtpLogin> ftpLogins) throws IOException, FtpException {
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

    public void uploadFiles(Collection<KDM> kdms, IUpdate iUpdate) throws IOException, FtpException {
        int current = 0;
        int total = kdms.size();
        for (KDM kdm : kdms) {
            iUpdate.onUpdateSending(current++, total);
            FTPClient ftpClient = serverMap.get(kdm.getServer());
            if (ftpClient == null) {//what should be done, if the kdm is for an unknown server? probably just skip it
                continue;
            }
            if (!ftpClient.storeFile(kdm.getFileName(), StringHelper.toInputStream(kdm.getData()))) {
                throw new FtpException(ftpClient.getReplyCode());
            }
        }
        iUpdate.onUpdateSending(current, total);
    }
}
