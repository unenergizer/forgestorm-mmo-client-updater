package com.forgestorm.client.updater;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class UserInterface {

    private final JLabel downloadStateLabel = new JLabel();
    private final JProgressBar overallProgress = new JProgressBar();
    private final JProgressBar fileProgress = new JProgressBar();
    private final JTextArea progressInfoTextArea = new JTextArea();

    public UserInterface() {
        JFrame frame = new JFrame("RetroMMO Updater");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Center window

        // Progress Area
        JPanel mainProgressPanel = new JPanel();
        overallProgress.setStringPainted(true);
        mainProgressPanel.add(downloadStateLabel);
        mainProgressPanel.add(overallProgress);
        overallProgress.setIndeterminate(true);

        JPanel fileProgressPanel = new JPanel();
        fileProgress.setStringPainted(true);
        fileProgressPanel.add(new JLabel("File Progress:"));
        fileProgressPanel.add(fileProgress);
        fileProgress.setIndeterminate(true);

        JPanel progressContainer = new JPanel();
        progressContainer.setLayout(new BoxLayout(progressContainer, BoxLayout.PAGE_AXIS));
        progressContainer.add(BorderLayout.NORTH, mainProgressPanel);
        progressContainer.add(BorderLayout.CENTER, fileProgressPanel);

        // File progress description area
        progressInfoTextArea.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(progressInfoTextArea);
        DefaultCaret caret = (DefaultCaret) progressInfoTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Add everything to the window
        frame.getContentPane().add(BorderLayout.NORTH, progressContainer);
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

    public void updateOverallProgressBar(int progress) {
        overallProgress.setIndeterminate(false);
        overallProgress.setValue(progress);
    }

    public void updateFileProgressBar(int progress) {
        fileProgress.setIndeterminate(false);
        fileProgress.setValue(progress);
    }

    public void printError(Exception exception) {
        printError(exception.toString());
    }

    public void printError(String error) {
        ClientUpdaterMain.getInstance().getStateMachine().setErrorHappened(true);
        updateProgressState(ProgressState.ERROR);
        progressInfoTextArea.append(error + "\n");
        progressInfoTextArea.append("\n");
        progressInfoTextArea.append("Please report this bug. Copy and paste the entire output here: \n");
        progressInfoTextArea.append("https://forgestorm.com/forums/bug-reports/");
        progressInfoTextArea.append("\n");
    }
}
