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
        JScrollPane jScrollPane = new JScrollPane(progressInfoTextArea);
        DefaultCaret caret = (DefaultCaret) progressInfoTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Add everything to the window
        frame.getContentPane().add(BorderLayout.NORTH, jPanel);
        frame.getContentPane().add(BorderLayout.CENTER, jScrollPane);
        frame.setVisible(true);
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
        updateProgressState(ProgressState.ERROR);
        progressInfoTextArea.append(exception + "\n");
    }
}
