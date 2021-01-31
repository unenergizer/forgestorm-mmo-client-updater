package com.forgestorm.client.updater;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StateMachine {

    private final List<String> fileList = new ArrayList<>();

    private ProgressState progressState = ProgressState.REQUEST_INFORMATION;

    public StateMachine() {
        trackProgress();
    }

    private void trackProgress() {
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressState(progressState);
        switch (progressState) {
            case REQUEST_INFORMATION:
                requestInformation();
                break;
            case DOWNLOAD_FILES:
                downloadFiles();
                break;
            case FINISH_UPDATE:
                finishUpdate();
                return; // Finished, Stop recursive loop...
            case ERROR:
                return; // Stop progress
        }
        progressState = progressState.nextState();
        trackProgress();
    }

    private void requestInformation() {
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("Parsing File Directories:");
        try {
            URL url = new URL("https://forgestorm.com/test/files.txt");
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                String file = scanner.next();
                fileList.add(file);
                ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(" - " + file);
            }
        } catch (IOException e) {
            ClientUpdaterMain.getInstance().getUserInterface().printError(e);
        }
    }

    private void downloadFiles() {
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("Downloading Game Files:");
        for (int i = 0; i < fileList.size(); i++) {
            FileDownloader network = ClientUpdaterMain.getInstance().getNetwork();
            network.downloadFile(fileList.get(i), i, fileList.size());

        }
    }

    private void finishUpdate() {
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("Update finished! Starting game client...");

        // Open game client
        try {
            if ((new File("RetroMMO.jar")).exists()) {
                Runtime.getRuntime().exec("java -jar RetroMMO.jar");
            } else {
                File s = new File("RetroMMO");
                if (s.exists()) {
                    Runtime.getRuntime().exec("java -jar ./RetroMMO");
                } else {
                    Runtime.getRuntime().exec("java -jar ../MacOS/RetroMMO");
                }
            }
        } catch (IOException e) {
            ClientUpdaterMain.getInstance().getUserInterface().printError(e);
        }

        // Close the updater (unless ran from IDE)
        if (!ClientUpdaterMain.ideRun) System.exit(0);
    }
}
