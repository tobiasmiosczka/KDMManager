package com.github.tobiasmiosczka.cinema.KDMManager.gui;

import com.github.tobiasmiosczka.cinema.KDMManager.EmailHelper;
import com.github.tobiasmiosczka.cinema.KDMManager.FtpHelper;
import com.github.tobiasmiosczka.cinema.KDMManager.XmlHelper;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.*;
import org.jdom2.JDOMException;

import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Container;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public class Window extends JFrame {

    JList<EmailLogin>   lEmailLoginList;
    JList<FtpLogin>     lFtpLoginList;

    private final DefaultListModel<FtpLogin> dlmFtpLogins = new DefaultListModel<>();
    private final DefaultListModel<EmailLogin> dlmEmailLogin = new DefaultListModel<>();


    Config config = new Config();

    public static void main(String[] args) throws IOException, JDOMException {
        Window window = new Window();
        window.loadConfig();
    }

    public void loadConfig() throws IOException, JDOMException {
        this.config = XmlHelper.loadConfig(new FileInputStream("config.xml"));
        updateEmailLoginList();
        updateFtpLoginList();
    }

    public void saveConfig() throws IOException {
        XmlHelper.saveConfig(config, new FileOutputStream("config.xml"));
    }

    public Window() {
        this.init();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void init() {
        this.setTitle("KDMManager");
        this.setLayout(null);
        this.setResizable(false);
        Container c = this.getContentPane();
        c.setPreferredSize(new Dimension(600, 635));

        JLabel lbEmailLogins = new JLabel("Email Logins");
        lbEmailLogins.setBounds(5, 5, 190, 30);
        c.add(lbEmailLogins);

        lEmailLoginList = new JList<>();
        lEmailLoginList.setModel(dlmEmailLogin);
        lEmailLoginList.setBounds(5, 40, 590, 200);
        c.add(lEmailLoginList);

        JButton btAddEmailLogin = new JButton("Add");
        btAddEmailLogin.addActionListener(a -> {
            EmailLogin emailLogin = new EmailLoginDialog(new EmailLogin("", 21, "", "", "pop3s", "INNOX", false)).showDialog();
            if (emailLogin != null){
                config.getEmailLogins().add(emailLogin);
                updateEmailLoginList();
            }
        });
        btAddEmailLogin.setBounds(5, 245, 190, 30);
        c.add(btAddEmailLogin);

        JButton btEditEmailLogin = new JButton("Edit");
        btEditEmailLogin.addActionListener(a -> {
            if (lEmailLoginList.getSelectedIndices().length != 1)
                return;
            EmailLogin emailLogin = new EmailLoginDialog(lEmailLoginList.getSelectedValue()).showDialog();
            if (emailLogin != null) {
                int index = lEmailLoginList.getSelectedIndex();
                config.getEmailLogins().remove(index);
                config.getEmailLogins().add(index, emailLogin);
                updateEmailLoginList();
            }
        });
        btEditEmailLogin.setBounds(205, 245, 190, 30);
        c.add(btEditEmailLogin);

        JButton btDeleteEmailLogin = new JButton("Delete");
        btDeleteEmailLogin.addActionListener(a -> {
            lEmailLoginList.getSelectedValuesList().stream().forEach(config.getEmailLogins()::remove);
            updateEmailLoginList();
        });
        btDeleteEmailLogin.setBounds(405, 245, 190, 30);
        c.add(btDeleteEmailLogin);

        JLabel lbFtpLogins = new JLabel("FTP Logins");
        lbFtpLogins.setBounds(5, 280, 190, 30);
        c.add(lbFtpLogins);

        lFtpLoginList = new JList<>();
        lFtpLoginList.setModel(dlmFtpLogins);
        lFtpLoginList.setBounds(5, 315, 590, 200);
        c.add(lFtpLoginList);

        JButton btAddFtpLogin = new JButton("Add");
        btAddFtpLogin.addActionListener(a -> {
            FtpLogin ftpLogin = new FtpLoginDialog(new FtpLogin("", 995, "", "", "")).showDialog();
            if (ftpLogin != null) {
                config.getFtpLogins().add(ftpLogin);
                updateFtpLoginList();
            }
        });
        btAddFtpLogin.setBounds(5, 520, 190, 30);
        c.add(btAddFtpLogin);

        JButton btEditFtpLogin = new JButton("Edit");
        btEditFtpLogin.addActionListener(a -> {
            if (lFtpLoginList.getSelectedIndices().length != 1)
                return;
            FtpLogin ftpLogin = new FtpLoginDialog(lFtpLoginList.getSelectedValue()).showDialog();
            if (ftpLogin != null) {
                int index = lFtpLoginList.getSelectedIndex();
                config.getFtpLogins().remove(index);
                config.getFtpLogins().add(index, ftpLogin);
                updateFtpLoginList();
            }
        });
        btEditFtpLogin.setBounds(205, 520, 190, 30);
        c.add(btEditFtpLogin);

        JButton btDeleteFtpLogin = new JButton("Delete");
        btDeleteFtpLogin.addActionListener(a -> {
            lFtpLoginList.getSelectedValuesList().stream().forEach(config.getFtpLogins()::remove);
            updateFtpLoginList();
        });
        btDeleteFtpLogin.setBounds(405, 520, 190, 30);
        c.add(btDeleteFtpLogin);

        JButton btLoadKdms = new JButton("Load KDMs");
        btLoadKdms.addActionListener(a -> {
            loadKdms();
        });
        btLoadKdms.setBounds(5, 600, 290, 30);
        c.add(btLoadKdms);
    }

    private void loadKdms() {
        Collection<KDM> kdms = null;
        try {
            kdms = EmailHelper.getKdmsFromEmail(config.getEmailLogins());
        } catch (MessagingException | JDOMException | ParseException | IOException e) {
            //TODO: implement
            e.printStackTrace();
        }
        if(kdms == null) {
            //TODO: implement
            return;
        }
        FtpHelper ftpHelper = null;
        try {
            ftpHelper = new FtpHelper(config.getFtpLogins());
        } catch (IOException | FtpException e) {
            //TODO: implement
            e.printStackTrace();
        }
        try {
            ftpHelper.uploadFiles(kdms);
        } catch (IOException | FtpException e) {
            //TODO: implement
            e.printStackTrace();
        }

    }

    private void updateEmailLoginList() {
        dlmEmailLogin.removeAllElements();
        config.getEmailLogins().stream().forEach(dlmEmailLogin::addElement);
    }

    private void updateFtpLoginList() {
        dlmFtpLogins.removeAllElements();
        config.getFtpLogins().stream().forEach(dlmFtpLogins::addElement);
    }
}
