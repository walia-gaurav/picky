package org.cmu.picky.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Util class to handle images.
 */
public class ImageUtils {

    /**
     * Decodes base 64 String and creates a BufferedImage from it.
     */
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

}