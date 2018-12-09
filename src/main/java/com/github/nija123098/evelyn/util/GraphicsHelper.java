package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.google.api.client.util.Joiner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GraphicsHelper {
    public static void drawString(String text, int x, int y, Font font, Color color, Graphics2D graphics, boolean center) {
        Color oldColor = graphics.getColor();
        Font oldFont = graphics.getFont();
        graphics.setFont(font);
        graphics.setColor(color);
        if (center) {
            Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(text, graphics);
            x -= bounds.getCenterX();
            y -= bounds.getCenterY();
        }
        graphics.drawString(text, x, y);
        graphics.setColor(oldColor);
        graphics.setFont(oldFont);
    }
    public static void drawStringShallowShadow(String text, int x, int y, Font font, Color color, Graphics2D graphics, boolean center) {
        drawString(text, x + 1, y, font, color, graphics, center);
        drawString(text, x - 1, y, font, color, graphics, center);
        drawString(text, x, y + 1, font, color, graphics, center);
        drawString(text, x, y - 1, font, color, graphics, center);
    }
    public static List<String> splitForBox(String text, Graphics2D graphics, Font font, int boxSize) {
        List<String> words = new ArrayList<>(), split = new ArrayList<>(), line = new ArrayList<>();
        Collections.addAll(words, text.split(" "));
        String last = words.remove(0);
        while (!words.isEmpty() || last != null) {
            line.add(last);
            while (graphics.getFontMetrics(font).getStringBounds(Joiner.on(' ').join(line), graphics).getWidth() <= boxSize && !words.isEmpty()) line.add(words.remove(0));
            if (graphics.getFontMetrics(font).getStringBounds(Joiner.on(' ').join(line), graphics).getWidth() > boxSize) last = line.remove(line.size() - 1);
            else last = null;
            split.add(Joiner.on(' ').join(line));
            line.clear();
        }
        return split;
    }
    public static void drawStringBox(String text, int x, int y, int leftExtension, int rightExtension, float clearExtension, Font font, Color color, Color fade, Graphics2D graphics, boolean center) {
        clearExtension += 0.175;
        Rectangle2D bounds = graphics.getFontMetrics(font).getStringBounds(text, graphics);
        bounds.setRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
        bounds.setRect(x + bounds.getX() - leftExtension, bounds.getY() + y + bounds.getHeight() / 2, bounds.getWidth() + leftExtension + rightExtension, bounds.getHeight() / 4);
        Color oldColor = graphics.getColor();
        Font oldFont = graphics.getFont();
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.fill(bounds);
        bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth() - 1, bounds.getHeight() - 1);
        int iterations = (int) (clearExtension * bounds.getWidth());
        for (float i = iterations - 1; i > 0; --i) {
            bounds.setRect(bounds.getX() - 1, bounds.getY() - 1, bounds.getWidth() + 2, bounds.getHeight() + 2);
            graphics.setColor(getGradient((float) Math.pow(i / iterations, .5), color, fade));
            graphics.draw(bounds);
        }
        graphics.setColor(oldColor);
        graphics.setFont(oldFont);
    }
    public static BufferedImage loadImage(String url) {
        try {
            HttpURLConnection connection = ((HttpURLConnection) new URL(url.replace(".webp", ".png")).openConnection());
            connection.setRequestProperty("User-Agent", ConfigProvider.BOT_SETTINGS.userAgent());
            return ImageIO.read(connection.getInputStream());
        } catch (IOException e) {
            throw new ArgumentException("Unable to load image for URL: " + url, e);
        }
    }
    public static Color getGradient(float grade, Color first, Color second) {
        return new Color((int) (first.getRed() * grade + second.getRed() * (1 - grade)), (int) (first.getGreen() * grade + second.getGreen() * (1 - grade)), (int) (first.getBlue() * grade + second.getBlue() * (1 - grade)), (int) (first.getAlpha() * grade + second.getAlpha() * (1 - grade)));
    }
}
