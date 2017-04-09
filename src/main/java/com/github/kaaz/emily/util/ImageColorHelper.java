package com.github.kaaz.emily.util;

import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.programconfig.BotConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 4/5/2017.
 */
public class ImageColorHelper {
    private static final Map<String, Color> URL_COLOR_MAP = new ConcurrentHashMap<>();
    public static Color getUserColor(User user){
        try {
            return URL_COLOR_MAP.computeIfAbsent(user.getAvatarURL(), s -> {
                try {
                    HttpURLConnection connection = ((HttpURLConnection) new URL(s.replace(".webp", ".png")).openConnection());
                    connection.setRequestProperty("User-Agent", "");
                    BufferedImage image = ImageIO.read(connection.getInputStream());
                    int r = 0, g = 0, b = 0;
                    for (int i = 0; i < image.getHeight(); ++i) {
                        for (int j = 0; j < image.getWidth(); ++j) {
                            int val = image.getRGB(i, j);
                            r += (val >> 16) & 0xFF;
                            g += (val >> 8) & 0xFF;
                            b += val & 0xFF;
                        }
                    }
                    int pixels = image.getHeight() * image.getWidth();
                    return new Color(r / pixels, g / pixels, b / pixels);

                } catch (Exception e){
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException e){
            return Color.BLACK;
        }
    }
}
