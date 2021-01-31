package com.forgestorm.client.updater;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class FileDownloader {

    private static final int BUFFER_SIZE = 1024;

    private File jarFilePath;

    public FileDownloader() {
        try {
            jarFilePath = new File(new File(FileDownloader.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent());
        } catch (URISyntaxException e) {
            ClientUpdaterMain.getInstance().getUserInterface().printError(e);
        }
    }

    public void downloadFile(String file, int index, int max) {
        File path = new File(jarFilePath + File.separator + file);
        File directory = path.getParentFile();

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                ClientUpdaterMain.getInstance().getUserInterface().printError("Directory could not be made.");
            }
        }

        try {
            URL url = new URL("https://forgestorm.com/test/" + file);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());

            String string = " - " + file + " Size: " + httpConnection.getContentLength();
            ClientUpdaterMain.getInstance().getUserInterface().updateProgressInfo(string);

            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER_SIZE);

            byte[] data = new byte[BUFFER_SIZE];
            int x;
            while ((x = inputStream.read(data, 0, BUFFER_SIZE)) >= 0) {
                // calculate and update progress
                final int currentProgress = (int) ((((double) index + 1) / ((double) max)) * 100);
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
