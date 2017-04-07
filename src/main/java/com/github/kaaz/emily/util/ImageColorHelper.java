package com.github.kaaz.emily.util;

import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.programconfig.BotConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 4/5/2017.
 */
public class ImageColorHelper {
    private static final Map<String, File> URL_FILE_MAP = new ConcurrentHashMap<>();
    private static final Map<File, Color> FILE_COLOR_MAP = new ConcurrentHashMap<>();
    private static final File PARENT = new File(BotConfig.TEMP_PATH);
    public static File getImage(String url){
        return URL_FILE_MAP.computeIfAbsent(url, s -> {
            try {
                File original = new File(new URL(url).toURI());
                File dest = File.createTempFile("image", "png", PARENT);
                ImageIO.write(ImageIO.read(original), "png", dest);
                return dest;
            } catch (IOException e) {
                throw new RuntimeException("Error during file downloading", e);
            } catch (URISyntaxException e) {// this should never happen
                throw new RuntimeException("Error with invalid URL: " + url, e);
            }
        });
    }
    private static final int SKIP_WIDTH = 3;
    public static Color getUserColor(User user){
        try {
            return getAverage(new File(new URL(user.getAvatarURL()).toURI()));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            return Color.BLACK;
        }
    }
    public static Color getAverage(File file){
        return FILE_COLOR_MAP.computeIfAbsent(file, f -> {
            long r = 0, g = 0, b = 0;
            try {
                BufferedImage image = ImageIO.read(file);
                for (int i = 0; i < image.getHeight(); i+=SKIP_WIDTH) {
                    for (int j = 0; j < image.getWidth(); j+=SKIP_WIDTH) {
                        int val = image.getRGB(i, j);
                        r += (val >> 16) & 0xFF;
                        g += (val >> 8) & 0xFF;
                        b += val & 0xFF;
                    }
                }
                int pixels = image.getHeight() * image.getWidth() / SKIP_WIDTH / SKIP_WIDTH;
                return new Color(r / pixels, g / pixels, b / pixels);
            } catch (IOException e) {
                Log.log("IOException while reading image: " + file.getPath(), e);
                return Color.RED;
            }
        });
    }
}
