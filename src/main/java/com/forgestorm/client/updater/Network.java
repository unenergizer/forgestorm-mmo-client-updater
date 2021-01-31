package com.forgestorm.client.updater;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class Network {

    private static final int BUFFER_SIZE = 1024;

    private File jarFilePath;

    public Network() {
        try {
            jarFilePath = new File(new File(Network.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent());
        } catch (URISyntaxException e) {
            ClientUpdaterMain.getInstance().getUserInterface().printError(e);
        }
    }

    public void downloadFile(String file, int index, int max) {
        try {
            URL url = new URL("https://forgestorm.com/book/" + file);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            String s = "Downloading: " + file + " Size: " + httpConnection.getContentLength();
            ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(s);

            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(jarFilePath + File.separator + file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER_SIZE);
            byte[] data = new byte[BUFFER_SIZE];
            long downloadedFileSize = 0;
            int x;
            while ((x = inputStream.read(data, 0, BUFFER_SIZE)) >= 0) {
                downloadedFileSize += x;

                // calculate progress
                final int currentProgress = (int) ((((double) index + 1) / ((double) max)) * 100);

                // update progress bar
                ClientUpdaterMain.getInstance().getUserInterface().updateProgressBar(currentProgress);

                bufferedOutputStream.write(data, 0, x);
            }
            bufferedOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            ClientUpdaterMain.getInstance().getUserInterface().printError(e);
        }
    }
}
