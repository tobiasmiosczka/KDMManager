package com.github.tobiasmiosczka.cinema.kdmmanager.gui;

import com.github.tobiasmiosczka.cinema.kdmmanager.IUpdateProgress;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.EmailLogin;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.FtpLogin;
import com.github.tobiasmiosczka.cinema.kdmmanager.pojo.KDM;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.format.DateTimeFormatter;

public class ProgressWindow extends JFrame implements IUpdateProgress {

    private JProgressBar    pbMajor,
                            pbMinor;
    private JScrollPane     spDebug;
    private JTextArea       taDebug;
    private JButton         btOk;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public ProgressWindow() {
        this.init();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
    }

    private void init() {
        this.setTitle("Loading...");
        this.setLayout(null);
        this.setResizable(false);
        Container c = this.getContentPane();
        c.setPreferredSize(new Dimension(550, 470));

        pbMajor = new JProgressBar();
        pbMajor.setStringPainted(true);
        pbMajor.setBounds(5, 5, 540, 30);
        c.add(pbMajor);

        pbMinor = new JProgressBar();
        pbMinor.setStringPainted(true);
        pbMinor.setBounds(5, 40, 540, 30);
        c.add(pbMinor);

        taDebug = new JTextArea();
        taDebug.setEditable(false);
        spDebug = new JScrollPane(taDebug);
        spDebug.setBounds(5, 75, 540, 350);
        c.add(spDebug);

        btOk = new JButton("Ok");
        btOk.setBounds(5, 430, 540, 30);
        btOk.addActionListener(a -> this.setVisible(false));
        c.add(btOk);
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
    public void onKdmUploaded(KDM kdm, FtpLogin ftpLogin, int current, int total) {
        EventQueue.invokeLater(() -> {
            logMessage("KDM uploaded:"
                    + "\n    Server: " + ftpLogin.getDescription()
                    + "\n    Valid : [" + kdm.getValidFrom().format(formatter) + " - " + kdm.getValidTo().format(formatter) + "]"
                    + "\n    DCP   : " + kdm.getTitle());
            pbMajor.setString(current + "/" + total);
            pbMajor.setMaximum(total);
            pbMajor.setValue(current);
        });
    }

    @Override
    public void onKdmFound(KDM kdm) {
        logMessage("KDM found: " + " Valid: [" + kdm.getValidFrom().format(formatter) + " - " + kdm.getValidTo().format(formatter) + "] " + kdm.getTitle());
    }

    @Override
    public void onDoneLoading(int count) {
        logMessage("Done loading " + count + " KDMs.");
    }

    @Override
    public void onDoneUploading(int count) {
        pbMajor.setValue(pbMajor.getMaximum());
        logMessage("Done uploading " + count + " KDMs.");
    }

    @Override
    public void onUpdateEmailBox(int current, int total, EmailLogin emailLogin) {
        EventQueue.invokeLater(() -> {
            pbMajor.setMaximum(total);
            pbMajor.setValue(current);
            pbMajor.setString("Loading Emails from: " + emailLogin.toString());
            logMessage("Loading Emails from " + emailLogin + ".");
        });
    }

    @Override
    public void onDone(long timeInMilliseconds) {
        EventQueue.invokeLater(() -> {
            logMessage("Done after " + timeInMilliseconds / 1000 + "s.");
            btOk.setEnabled(true);
        });
    }

    @Override
    public void onErrorOccurred(String message, Throwable throwable) {
        EventQueue.invokeLater(() -> {
            this.logMessage(message + throwable.getMessage());
            btOk.setEnabled(true);
        });
    }

    public void logMessage(String message) {
        taDebug.append(message + "\n");
        spDebug.getVerticalScrollBar().setValue(spDebug.getVerticalScrollBar().getMaximum());
    }

    @Override
    public void onBegin() {
        this.setVisible(true);
        btOk.setEnabled(false);
        taDebug.setText("");
    }
}
