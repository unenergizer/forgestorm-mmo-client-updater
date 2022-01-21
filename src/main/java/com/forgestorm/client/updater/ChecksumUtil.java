package com.forgestorm.client.updater;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;


public class ChecksumUtil {

    /**
     * Generates an MD5 checksum as a String.
     *
     * @param file The file that is being checksummed.
     * @return Hex string of the checksum value.
     */
    public static String generate(File file) throws NoSuchAlgorithmException, IOException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(Files.readAllBytes(file.toPath()));
        byte[] hash = messageDigest.digest();

        return DatatypeConverter.printHexBinary(hash).toUpperCase();
    }
}
