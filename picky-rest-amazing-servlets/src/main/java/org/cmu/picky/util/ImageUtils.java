package org.cmu.picky.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageUtils {

    public static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;

		imageString = imageString.replace(" ", "+");
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