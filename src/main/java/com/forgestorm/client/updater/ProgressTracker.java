package com.forgestorm.client.updater;

public class ProgressTracker {

    private ProgressState progressState = ProgressState.REQUESTING_INFORMATION;

    public ProgressTracker() {
        trackProgress();
    }

    private void trackProgress() {
        ClientUpdaterMain.getInstance().getUserInterface().updateProgressState(progressState);
        switch (progressState) {
            case REQUESTING_INFORMATION:
                requestInformation();
                break;
            case PARSING_FILE_TREE:
                parseFileTree();
                break;
            case CHECKING_FILE_HASH:
                checkFileHash();
                break;
            case PREPARING_DOWNLOAD_STATE:
                prepareFileDownloads();
                break;
            case DOWNLOADING_FILES:
                downloadFiles();
                break;
            case FINISH_UPDATE:

                return; // Finished, Stop recursive loop...
            case ERROR:
            default:
                return; // Stop progress
        }
        progressState = progressState.nextState();
        trackProgress();
    }

    private void requestInformation() {

    }

    private void parseFileTree() {

    }

    private void checkFileHash() {

    }

    private void prepareFileDownloads() {

    }

    private void downloadFiles() {
        final int max = 50;
        for (int i = 0; i < max; i++) {
            Network network = ClientUpdaterMain.getInstance().getNetwork();
            network.downloadFile("maps.zip", i, max);
        }
    }
}
