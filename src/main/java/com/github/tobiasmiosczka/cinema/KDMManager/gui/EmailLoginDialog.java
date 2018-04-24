package com.github.tobiasmiosczka.cinema.KDMManager.gui;

import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;

import javax.swing.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

public class EmailLoginDialog extends JDialog {

    private JButton btOk,
                    btCancel;
    private JTextField  tfHost,
                        tfPort,
                        tfUsername,
                        tfPassword,
                        tfProtocol,
                        tfFolder;
    private JCheckBox cbTls;
    private EmailLogin result;

    public EmailLoginDialog(EmailLogin emailLogin) {
        super((java.awt.Frame) null, true);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.init(emailLogin);
        this.setPreferredSize(new Dimension(330, 240));
        this.pack();
    }

    private EmailLogin getValue() {
        int port;
        try {
            port = Integer.parseInt(tfPort.getText());
        } catch (NumberFormatException e) {
            tfPort.setBackground(Color.RED);
            return null;
        }
        return new EmailLogin(
                tfHost.getText(),
                port,
                tfUsername.getText(),
                tfPassword.getText(),
                tfProtocol.getText(),
                tfFolder.getText(),
                cbTls.isSelected()
        );
    }

    public EmailLogin showDialog() {
        this.setVisible(true);
        return this.result;
    }

    private void init(EmailLogin emailLogin) {
        this.setTitle("Email Login Editor");

        this.setLayout(null);
        Container c = this.getContentPane();
        c.setPreferredSize(new Dimension(330, 240));

        JLabel lHost = new JLabel("Host:Port: ");
        lHost.setBounds(5, 5, 100, 20);
        c.add(lHost);
        tfHost = new JTextField(emailLogin.getHost());
        tfHost.setBounds(110, 5, 150, 20);
        c.add(tfHost);

        JLabel lPort = new JLabel(":");
        lPort.setBounds(265, 5, 10, 20);
        c.add(lPort);
        tfPort = new JTextField("" + emailLogin.getPort());
        tfPort.setBounds(275, 5, 35, 20);
        c.add(tfPort);

        JLabel lUsername = new JLabel("Username: ");
        lUsername.setBounds(5, 30, 100, 20);
        c.add(lUsername);
        tfUsername = new JTextField(emailLogin.getUser());
        tfUsername.setBounds(110, 30, 200, 20);
        c.add(tfUsername);

        JLabel lPassword = new JLabel("Password: ");
        lPassword.setBounds(5, 55, 100, 20);
        c.add(lPassword);
        tfPassword = new JTextField(emailLogin.getPassword());
        tfPassword.setBounds(110, 55, 200, 20);
        c.add(tfPassword);

        JLabel lProtocol = new JLabel("Protocol: ");
        lProtocol.setBounds(5, 80, 100, 20);
        c.add(lProtocol);
        tfProtocol = new JTextField(emailLogin.getProtocol());
        tfProtocol.setBounds(110, 80, 200, 20);
        c.add(tfProtocol);

        JLabel lFolder = new JLabel("Folder: ");
        lFolder.setBounds(5, 105, 100, 20);
        c.add(lFolder);
        tfFolder = new JTextField(emailLogin.getFolder());
        tfFolder.setBounds(110, 105, 200, 20);
        c.add(tfFolder);

        JLabel lTls = new JLabel("Tls: ");
        lTls.setBounds(5, 130, 100, 20);
        c.add(lTls);
        cbTls = new JCheckBox();
        cbTls.setSelected(emailLogin.isTls());
        cbTls.setBounds(110, 130, 200, 20);
        c.add(cbTls);

        btOk = new JButton("Ok");
        btOk.addActionListener(e -> {
            result = getValue();
            if (result == null)
                return;
            this.setVisible(false);
            this.dispose();
        });
        btOk.setBounds(5, 165, 145, 30);
        this.getContentPane().add(btOk);

        btCancel = new JButton("Cancel");
        btCancel.addActionListener(e -> {
            result = null;
            this.setVisible(false);
            this.dispose();
        });
        btCancel.setBounds(165, 165, 145, 30);
        this.getContentPane().add(btCancel);
    }
}
