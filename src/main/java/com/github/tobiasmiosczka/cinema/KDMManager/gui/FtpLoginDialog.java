package com.github.tobiasmiosczka.cinema.KDMManager.gui;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.FtpLogin;

import javax.swing.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

public class FtpLoginDialog extends JDialog {

    private JTextField
            tfDescription,
            tfHost,
            tfPort,
            tfUsername,
            tfPassword,
            tfSerial;
    private FtpLogin result;

    private FtpLoginDialog(FtpLogin ftpLogin) {
        super((java.awt.Frame) null, true);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.init(ftpLogin);
        this.setPreferredSize(new Dimension(330, 215));
        this.pack();
    }

    private FtpLogin getValue() {
        int port;
        try {
            port = Integer.parseInt(tfPort.getText());
        } catch (NumberFormatException e) {
            tfPort.setBackground(Color.RED);
            return null;
        }
        return new FtpLogin(
                tfDescription.getText(),
                tfHost.getText(),
                port,
                tfUsername.getText(),
                tfPassword.getText(),
                tfSerial.getText()
        );
    }

    private FtpLogin getResult() {
        return result;
    }

    public static FtpLogin getFtpLogin(FtpLogin preview) {
        FtpLoginDialog dialog = new FtpLoginDialog(preview);
        dialog.setVisible(true);
        return dialog.getResult();
    }

    private void init(FtpLogin ftpLogin) {
        this.setTitle("FTP Login Editor");

        this.setLayout(null);
        Container c = this.getContentPane();
        c.setPreferredSize(new Dimension(330, 265));

        JLabel lbDescription = new JLabel("Description: ");
        lbDescription.setBounds(5, 5, 100, 20);
        c.add(lbDescription);
        tfDescription = new JTextField(ftpLogin.getDescription());
        tfDescription.setBounds(110, 5, 200, 20);
        c.add(tfDescription);

        JLabel lHost = new JLabel("Host:Port: ");
        lHost.setBounds(5, 30, 100, 20);
        c.add(lHost);
        tfHost = new JTextField(ftpLogin.getHost());
        tfHost.setBounds(110, 30, 150, 20);
        c.add(tfHost);

        JLabel lPort = new JLabel(":");
        lPort.setBounds(265, 30, 10, 20);
        c.add(lPort);
        tfPort = new JTextField("" + ftpLogin.getPort());
        tfPort.setBounds(275, 30, 35, 20);
        c.add(tfPort);

        JLabel lUsername = new JLabel("Username: ");
        lUsername.setBounds(5, 55, 100, 20);
        c.add(lUsername);
        tfUsername = new JTextField(ftpLogin.getUser());
        tfUsername.setBounds(110, 55, 200, 20);
        c.add(tfUsername);

        JLabel lPassword = new JLabel("Password: ");
        lPassword.setBounds(5, 80, 100, 20);
        c.add(lPassword);
        tfPassword = new JTextField(ftpLogin.getPassword());
        tfPassword.setBounds(110, 80, 200, 20);
        c.add(tfPassword);

        JLabel lSerial = new JLabel("Serial: ");
        lSerial.setBounds(5, 105, 100, 20);
        c.add(lSerial);
        tfSerial = new JTextField(ftpLogin.getSerial());
        tfSerial.setBounds(110, 105, 200, 20);
        c.add(tfSerial);

        JButton btOk = new JButton("Ok");
        btOk.addActionListener(e -> {
            result = getValue();
            if (result == null)
                return;
            this.setVisible(false);
            this.dispose();
        });
        btOk.setBounds(5, 140, 145, 30);
        this.getContentPane().add(btOk);

        JButton btCancel = new JButton("Cancel");
        btCancel.addActionListener(e -> {
            result = null;
            this.setVisible(false);
            this.dispose();
        });
        btCancel.setBounds(165, 140, 145, 30);
        this.getContentPane().add(btCancel);
    }
}
