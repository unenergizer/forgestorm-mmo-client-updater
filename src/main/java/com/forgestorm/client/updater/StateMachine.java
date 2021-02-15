package com.forgestorm.client.updater;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StateMachine {

    private static final String DOTS = "*************************************";

    private final List<FileData> fileList = new ArrayList<>();

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
            case VERIFY_FILES:
                checkFileIntegrity();
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
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("");
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(DOTS);
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("       Parsing File Directories");
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(DOTS);
        try {
            URL url = new URL(ClientUpdaterMain.FILE_URL + "files.txt");
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                String downloadURL = scanner.next();
                String md5Hash = scanner.next();
                fileList.add(new FileData(downloadURL, md5Hash));
                ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(" - " + downloadURL);
            }
        } catch (IOException e) {
            ClientUpdaterMain.getInstance().getUserInterface().printError(e);
        }
    }

    private void checkFileIntegrity() {
        final File filePath = ClientUpdaterMain.getInstance().getFileDownloader().getJarFilePath();

        for (FileData fileData : fileList) {
            File file = new File(filePath + File.separator + fileData.getDownloadURL());

            if (!file.exists()) continue;
            try {
                String hex = ChecksumUtil.generate(file);
                if (hex.equalsIgnoreCase(fileData.getMd5Hash())) fileData.setMd5Matched(true);
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadFiles() {
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("");
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(DOTS);
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("         Downloading Game Files");
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(DOTS);
        for (int i = 0; i < fileList.size(); i++) {
            FileDownloader network = ClientUpdaterMain.getInstance().getFileDownloader();
            network.downloadFile(fileList.get(i), i, fileList.size());
        }
    }

    private void finishUpdate() {
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("");
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(DOTS);
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("               Update Finished");
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(DOTS);
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo("Starting game client...");

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

    @Getter
    static class FileData {
        private final String downloadURL;
        private final String md5Hash;

        /**
         * If this value is set to true, the file
         * will NOT be downloaded.
         */
        @Setter
        private boolean md5Matched = false;

        FileData(String downloadURL, String md5Hash) {
            this.downloadURL = downloadURL;
            this.md5Hash = md5Hash;
        }
    }
}
