package com.github.tobiasmiosczka.cinema.KDMManager.gui;

import com.github.tobiasmiosczka.cinema.KDMManager.helper.EmailHelper;
import com.github.tobiasmiosczka.cinema.KDMManager.helper.FtpHelper;
import com.github.tobiasmiosczka.cinema.KDMManager.helper.XmlHelper;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.*;
import org.jdom2.JDOMException;

import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

public class Window extends JFrame implements IUpdate {

    private JList<EmailLogin>   lEmailLoginList;
    private JList<FtpLogin>     lFtpLoginList;

    private static final Font headerFont = new Font("Arial", Font.BOLD, 20);

    private final DefaultListModel<FtpLogin> dlmFtpLogins = new DefaultListModel<>();
    private final DefaultListModel<EmailLogin> dlmEmailLogin = new DefaultListModel<>();

    private JProgressBar    pbMajor,
                            pbMinor;
    private JButton btLoadKdms,
                    btAddEmailLogin,
                    btEditEmailLogin,
                    btDeleteEmailLogin,
                    btAddFtpLogin,
                    btEditFtpLogin,
                    btDeleteFtpLogin;

    private JCheckBox cbIgnoreExpiredKdms;

    private Config config = new Config();

    private void loadConfig() throws JDOMException {
        try {
            this.config = XmlHelper.loadConfig(new FileInputStream("config.xml"));
        } catch (IOException e) {
            this.config = new Config();
        }
        updateEmailLoginList();
        updateFtpLoginList();
    }

    private void saveConfig() {
        try {
            XmlHelper.saveConfig(config, new FileOutputStream("config.xml"));
        } catch (IOException e) {
            //TODO: implement
            e.printStackTrace();
        }
    }

    public Window() throws IOException, JDOMException {
        loadConfig();
        this.init();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
    }

    @Override
    public void onUpdateEmailLoading(int current, int total) {
        EventQueue.invokeLater(() -> {
            pbMinor.setString(current + "/" + total);
            pbMinor.setMaximum(total);
            pbMinor.setValue(current);
        });
    }

    @Override
    public void onUpdateSending(int current, int total) {
        EventQueue.invokeLater(() -> {
            pbMajor.setString(current + "/" + total);
            pbMajor.setMaximum(total);
            pbMajor.setValue(current);
        });
    }

    @Override
    public void onUpdateEmailBox(int current, int total, String host) {
        EventQueue.invokeLater(() -> {
            pbMajor.setMaximum(total);
            pbMajor.setValue(current);
            pbMajor.setString("Loading Emails from: " + host);
        });
    }

    @Override
    public void onDone() {
        EventQueue.invokeLater(() -> setUiEnabled(true));
    }

    private void setUiEnabled(boolean enabled) {
        btLoadKdms.setEnabled(enabled);
        btAddEmailLogin.setEnabled(enabled);
        btEditEmailLogin.setEnabled(enabled);
        btDeleteEmailLogin.setEnabled(enabled);
        btAddFtpLogin.setEnabled(enabled);
        btEditFtpLogin.setEnabled(enabled);
        btDeleteFtpLogin.setEnabled(enabled);
        cbIgnoreExpiredKdms.setEnabled(enabled);
    }

    private void init() {
        this.setTitle("KDMManager");
        this.setLayout(null);
        this.setResizable(false);
        Container c = this.getContentPane();
        c.setPreferredSize(new Dimension(350, 495));

        JLabel lbEmailLogins = new JLabel("Email Logins:", SwingConstants.CENTER);
        lbEmailLogins.setFont(headerFont);
        lbEmailLogins.setBounds(5, 5, 340, 30);
        c.add(lbEmailLogins);

        lEmailLoginList = new JList<>();
        lEmailLoginList.setBorder(new LineBorder(Color.BLACK, 1));
        lEmailLoginList.setModel(dlmEmailLogin);
        lEmailLoginList.setBounds(5, 40, 340, 100);
        c.add(lEmailLoginList);

        btAddEmailLogin = new JButton("Add");
        btAddEmailLogin.addActionListener(a -> {
            EmailLogin emailLogin = EmailLoginDialog.getEmailLogin(new EmailLogin("", 21, "", "", "pop3s", "INBOX", false));
            if (emailLogin != null){
                config.getEmailLogins().add(emailLogin);
                saveConfig();
                updateEmailLoginList();
            }
        });
        btAddEmailLogin.setBounds(5, 145, 110, 30);
        c.add(btAddEmailLogin);

        btEditEmailLogin = new JButton("Edit");
        btEditEmailLogin.addActionListener(a -> {
            if (lEmailLoginList.getSelectedIndices().length != 1)
                return;
            EmailLogin emailLogin = EmailLoginDialog.getEmailLogin(lEmailLoginList.getSelectedValue());
            if (emailLogin != null) {
                config.getEmailLogins().set(lEmailLoginList.getSelectedIndex(), emailLogin);
                saveConfig();
                updateEmailLoginList();
            }
        });
        btEditEmailLogin.setBounds(120, 145, 110, 30);
        c.add(btEditEmailLogin);

        btDeleteEmailLogin = new JButton("Delete");
        btDeleteEmailLogin.addActionListener(a -> {
            lEmailLoginList.getSelectedValuesList().forEach(config.getEmailLogins()::remove);
            saveConfig();
            updateEmailLoginList();
        });
        btDeleteEmailLogin.setBounds(235, 145, 110, 30);
        c.add(btDeleteEmailLogin);

        JLabel lbFtpLogins = new JLabel("FTP Logins:", SwingConstants.CENTER);
        lbFtpLogins.setFont(headerFont);
        lbFtpLogins.setBounds(5, 180, 340, 30);
        c.add(lbFtpLogins);

        lFtpLoginList = new JList<>();
        lFtpLoginList.setBorder(new LineBorder(Color.BLACK, 1));
        lFtpLoginList.setModel(dlmFtpLogins);
        lFtpLoginList.setBounds(5, 215, 340, 100);
        c.add(lFtpLoginList);

        btAddFtpLogin = new JButton("Add");
        btAddFtpLogin.addActionListener(a -> {
            FtpLogin ftpLogin = FtpLoginDialog.getFtpLogin(new FtpLogin("", 995, "", "", ""));
            if (ftpLogin != null) {
                config.getFtpLogins().add(ftpLogin);
                saveConfig();
                updateFtpLoginList();
            }
        });
        btAddFtpLogin.setBounds(5, 320, 110, 30);
        c.add(btAddFtpLogin);

        btEditFtpLogin = new JButton("Edit");
        btEditFtpLogin.addActionListener(a -> {
            if (lFtpLoginList.getSelectedIndices().length != 1)
                return;
            FtpLogin ftpLogin = FtpLoginDialog.getFtpLogin(lFtpLoginList.getSelectedValue());
            if (ftpLogin != null) {
                config.getFtpLogins().set(lFtpLoginList.getSelectedIndex(), ftpLogin);
                saveConfig();
                updateFtpLoginList();
            }
        });
        btEditFtpLogin.setBounds(120, 320, 110, 30);
        c.add(btEditFtpLogin);

        btDeleteFtpLogin = new JButton("Delete");
        btDeleteFtpLogin.addActionListener(a -> {
            lFtpLoginList.getSelectedValuesList().forEach(config.getFtpLogins()::remove);
            saveConfig();
            updateFtpLoginList();
        });
        btDeleteFtpLogin.setBounds(235, 320, 110, 30);
        c.add(btDeleteFtpLogin);

        cbIgnoreExpiredKdms = new JCheckBox("Ignore expired KDMs");
        cbIgnoreExpiredKdms.setSelected(true);
        cbIgnoreExpiredKdms.setBounds(5, 355, 340, 30);
        c.add(cbIgnoreExpiredKdms);

        pbMajor = new JProgressBar();
        pbMajor.setStringPainted(true);
        pbMajor.setVisible(false);
        pbMajor.setBounds(5, 390, 340, 30);
        c.add(pbMajor);

        pbMinor = new JProgressBar();
        pbMinor.setStringPainted(true);
        pbMinor.setVisible(false);
        pbMinor.setBounds(5, 420, 340, 30);
        c.add(pbMinor);

        btLoadKdms = new JButton("Load KDMs");
        btLoadKdms.addActionListener(a -> loadKdms());
        btLoadKdms.setBounds(5, 460, 340, 30);
        c.add(btLoadKdms);
    }

    private void loadKdms() {
        btLoadKdms.setEnabled(false);
        setUiEnabled(false);
        new Thread(() -> {
                Collection<KDM> kdms = null;
                long start = System.currentTimeMillis();
                pbMajor.setVisible(true);
                pbMinor.setVisible(true);
                try {
                    kdms = EmailHelper.getKdmsFromEmail(config.getEmailLogins(), this);
                } catch (MessagingException | JDOMException | ParseException | IOException e) {
                    //TODO: implement
                    e.printStackTrace();
                }
                if(kdms == null) {
                    //TODO: implement
                    return;
                }
                if (cbIgnoreExpiredKdms.isSelected()) {
                    Date now = new Date();
                    kdms.removeIf(kdm -> kdm.getValidTo().before(now));
                }
                try {
                    FtpHelper ftpHelper = new FtpHelper(config.getFtpLogins());
                    ftpHelper.uploadFiles(kdms, this);
                } catch (IOException | FtpException e) {
                    //TODO: implement
                    e.printStackTrace();
                }
            long diff = System.currentTimeMillis() - start;
                pbMajor.setString("Loaded " + kdms.size() + " KDMs after " + diff / 1000 + " seconds.");
                this.onDone();
        }).start();
    }

    private void updateEmailLoginList() {
        dlmEmailLogin.removeAllElements();
        config.getEmailLogins().forEach(dlmEmailLogin::addElement);
    }

    private void updateFtpLoginList() {
        dlmFtpLogins.removeAllElements();
        config.getFtpLogins().forEach(dlmFtpLogins::addElement);
    }
}
