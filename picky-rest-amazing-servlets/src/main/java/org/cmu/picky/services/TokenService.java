package org.cmu.picky.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TokenService {

    private static final String MD5 = "MD5";

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);


    public String generateRandomTokenValue() {
        String randomUUID = UUID.randomUUID().toString();
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        messageDigest.update(randomUUID.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : digest) {
            stringBuilder.append(String.format("%02x", b & 0xff));
        }
        return stringBuilder.toString();
    }

    public String getImageMD5(BufferedImage bufferedImage, String imageType) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, imageType, outputStream);
        } catch (IOException e) {
            return null;
        }
        byte[] data = outputStream.toByteArray();
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        md.update(data);
        byte[] hash = md.digest();

        return returnHex(hash) + "." + imageType;
    }

    private String returnHex(byte[] inBytes) {
        String hexString = "";
        for (int i=0; i < inBytes.length; i++) {
            hexString += Integer.toString( ( inBytes[i] & 0xff ) + 0x100, 16).substring(1);
        }
        return hexString;
    }

}
