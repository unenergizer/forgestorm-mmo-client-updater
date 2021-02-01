package com.forgestorm.client.updater;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class UserInterface {

    private final JLabel downloadStateLabel = new JLabel();
    private final JProgressBar jProgressBar = new JProgressBar();
    private final JTextArea progressInfoTextArea = new JTextArea();

    public UserInterface() {
        JFrame frame = new JFrame("RetroMMO Updater");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Center window

        // Progress Area
        JPanel jPanel = new JPanel();
        jProgressBar.setStringPainted(true);
        jPanel.add(downloadStateLabel);
        jPanel.add(jProgressBar);
        jProgressBar.setIndeterminate(true);

        // File progress description area
        progressInfoTextArea.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(progressInfoTextArea);
        DefaultCaret caret = (DefaultCaret) progressInfoTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Add everything to the window
        frame.getContentPane().add(BorderLayout.NORTH, jPanel);
        frame.getContentPane().add(BorderLayout.CENTER, jScrollPane);
        frame.setVisible(true);

        updateProgressInfo("Beginning update process...");
    }

    public void updateProgressState(ProgressState progressState) {
        downloadStateLabel.setText(progressState.toString() + "...");
    }

    public void updateProgressInfo(String progressInfo) {
        progressInfoTextArea.append(progressInfo + "\n");
    }

    public void updateProgressBar(int progress) {
        jProgressBar.setIndeterminate(false);
        jProgressBar.setValue(progress);
    }

    public void printError(Exception exception) {
        printError(exception.toString());
    }

    public void printError(String error) {
        updateProgressState(ProgressState.ERROR);
        progressInfoTextArea.append(error + "\n");
        progressInfoTextArea.append("\n");
        progressInfoTextArea.append("Please report this bug. Copy and paste the entire output here: \n");
        progressInfoTextArea.append("https://forgestorm.com/forums/bug-reports/");
        progressInfoTextArea.append("\n");
    }
}
