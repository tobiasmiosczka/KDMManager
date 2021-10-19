package com.github.tobiasmiosczka.cinema.KDMManager.gui;

import com.github.tobiasmiosczka.cinema.KDMManager.IUpdateGui;
import com.github.tobiasmiosczka.cinema.KDMManager.Program;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.KDMManager.pojo.FtpLogin;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;

public class Window extends JFrame implements IUpdateGui {

    private Program program;

    private JList<EmailLogin>   lEmailLoginList;
    private JList<FtpLogin>     lFtpLoginList;

    private static final Font headerFont = new Font("Arial", Font.BOLD, 20);

    private final DefaultListModel<FtpLogin> dlmFtpLogins = new DefaultListModel<>();
    private final DefaultListModel<EmailLogin> dlmEmailLogin = new DefaultListModel<>();

    private JCheckBox cbIgnoreExpiredKdms;

    public Window() {
        this.init();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    private void init() {
        this.setTitle("KDMManager V 1.1.0");
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

        JButton btAddEmailLogin = new JButton("Add");
        btAddEmailLogin.addActionListener(a -> program.addEmailLogin(EmailLoginDialog.getEmailLogin(new EmailLogin("","", 995, "", "", "pop3s", "INBOX", false))));
        btAddEmailLogin.setBounds(5, 145, 110, 30);
        c.add(btAddEmailLogin);

        JButton btEditEmailLogin = new JButton("Edit");
        btEditEmailLogin.addActionListener(a -> {
            if (lEmailLoginList.getSelectedIndices().length == 1)
                program.updateEmailLogin(lEmailLoginList.getSelectedIndex(), EmailLoginDialog.getEmailLogin(lEmailLoginList.getSelectedValue()));
        });
        btEditEmailLogin.setBounds(120, 145, 110, 30);
        c.add(btEditEmailLogin);

        JButton btDeleteEmailLogin = new JButton("Delete");
        btDeleteEmailLogin.addActionListener(a -> {
            int count = lEmailLoginList.getSelectedValuesList().size();
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "This action will delete the selected " + count + " Email login(s). Are you sure?",
                    "Delete email logins",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                program.deleteEmailLogins(lEmailLoginList.getSelectedValuesList());
            }
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

        JButton btAddFtpLogin = new JButton("Add");
        btAddFtpLogin.addActionListener(a -> program.addFtpLogin(FtpLoginDialog.getFtpLogin(new FtpLogin("","", 21, "", "", ""))));
        btAddFtpLogin.setBounds(5, 320, 110, 30);
        c.add(btAddFtpLogin);

        JButton btEditFtpLogin = new JButton("Edit");
        btEditFtpLogin.addActionListener(a -> {
            if (lFtpLoginList.getSelectedIndices().length != 1)
                return;
            program.updateFtpLogin(lFtpLoginList.getSelectedIndex(), FtpLoginDialog.getFtpLogin(lFtpLoginList.getSelectedValue()));
        });
        btEditFtpLogin.setBounds(120, 320, 110, 30);
        c.add(btEditFtpLogin);

        JButton btDeleteFtpLogin = new JButton("Delete");
        btDeleteFtpLogin.addActionListener(a -> {
            int count = lFtpLoginList.getSelectedValuesList().size();
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "This action will delete the selected " + count + " FTP login(s). Are you sure?",
                    "Delete FTP logins",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                program.deleteFtpLogins(lFtpLoginList.getSelectedValuesList());
            }
        });
        btDeleteFtpLogin.setBounds(235, 320, 110, 30);
        c.add(btDeleteFtpLogin);

        cbIgnoreExpiredKdms = new JCheckBox("Ignore expired KDMs");
        cbIgnoreExpiredKdms.setSelected(true);
        cbIgnoreExpiredKdms.setBounds(5, 355, 340, 30);
        c.add(cbIgnoreExpiredKdms);

        JButton btLoadKdms = new JButton("Load KDMs");
        btLoadKdms.addActionListener(a -> program.loadKdms(cbIgnoreExpiredKdms.isSelected()));
        btLoadKdms.setBounds(5, 460, 340, 30);
        c.add(btLoadKdms);
    }

    @Override
    public void onUpdateEmailLogins(List<EmailLogin> emailLogins) {
        EventQueue.invokeLater(() -> {
                    dlmEmailLogin.removeAllElements();
                    emailLogins.forEach(dlmEmailLogin::addElement);
                }
        );
    }

    @Override
    public void onUpdateFtpLogins(List<FtpLogin> ftpLogins) {
        EventQueue.invokeLater(() -> {
                    dlmFtpLogins.removeAllElements();
                    ftpLogins.forEach(dlmFtpLogins::addElement);
                }
        );
    }

    @Override
    public void onErrorOccurred(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
