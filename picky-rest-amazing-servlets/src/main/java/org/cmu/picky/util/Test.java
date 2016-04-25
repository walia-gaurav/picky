package org.cmu.picky.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Test {

    public static void main(String[] args) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/home/federico/test.png"));
        } catch (IOException e) {
            return;
        }
        URL url = null;
        try {
            url = new URL("http://localhost:8080/test");
        } catch (MalformedURLException e) {
            return;
        }
        HttpURLConnection urlConnection = null;
        try {
            String image = ImageUtils.encodeToString(img, "png");
            String urlParameters = String.format("image=%s", URLEncoder.encode(image, "UTF-8"));
            byte[] postData = urlParameters.getBytes("UTF-8");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", "UTF-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            urlConnection.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

            wr.write(postData);
            wr.flush();
            wr.close();
            System.out.println(urlConnection.getResponseCode());


        } catch (IOException ex) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
