package org.cmu.picky.util;

import java.io.IOException;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;

        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception ex) {}
        return image;
    }

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, type, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
            byteArrayOutputStream.close();
        } catch (IOException e) {}
        return imageString;
    }

}