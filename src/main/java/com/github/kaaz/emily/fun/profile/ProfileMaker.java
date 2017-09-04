package com.github.kaaz.emily.fun.profile;

import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.FileHelper;
import com.github.kaaz.emily.util.GraphicsHelper;
import com.github.kaaz.emily.util.Rand;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileMaker {
    private static final int SIDE_OFFSET = 30, WIDTH = 900, HEIGHT = 600, PROFILE_SIZE = 128, BADGE_SPACING = 6;
    private static final Color OPAQUE = new Color(0, 0, 0, 0);
    private static final List<Point> CIRCLE = new ArrayList<>((int) (PROFILE_SIZE * PROFILE_SIZE - Math.pow(PROFILE_SIZE / 2, 2) * Math.PI + PROFILE_SIZE));
    static {
        for (int i = 0; i < PROFILE_SIZE; i++) {
            for (int j = 0; j < PROFILE_SIZE; j++) {
                if (Math.pow(Math.pow(PROFILE_SIZE / 2 - i, 2) + Math.pow(PROFILE_SIZE / 2 - j, 2), .5F) >= PROFILE_SIZE / 2) CIRCLE.add(new Point(i, j));
            }
        }
        ((ArrayList<Point>) CIRCLE).trimToSize();
    }
    public static File getProfileIcon(User user){
        BufferedImage im = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        for (int i = 0; i < WIDTH; i++) {
            g.setColor(new Color(Rand.getRand(0xFFFFFF)));
            g.drawLine(0, i, WIDTH, i);
        }
        return FileHelper.getTempFile("profile", "png", "profile-" + user.getID(), file -> {
            HttpURLConnection connection = ((HttpURLConnection) new URL(user.getAvatarURL().replace(".webp", ".png")).openConnection());
            connection.setRequestProperty("User-Agent", BotConfig.USER_AGENT);
            BufferedImage fullImage = ImageIO.read(connection.getInputStream());
            BufferedImage image = new BufferedImage(PROFILE_SIZE, PROFILE_SIZE, BufferedImage.TYPE_INT_ARGB);
            image.createGraphics().drawImage(fullImage, AffineTransform.getScaleInstance(PROFILE_SIZE / (double) fullImage.getWidth(), PROFILE_SIZE / (double) fullImage.getHeight()), null);
            CIRCLE.forEach(point -> image.setRGB((int) point.getX(), (int) point.getY(), OPAQUE.getRGB()));

            BufferedImage profile = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = ((Graphics2D) profile.getGraphics());
            graphics.drawImage(im, 0, 0, null);
            graphics.setColor(GraphicsHelper.getGradient(.45F, Color.WHITE, OPAQUE));
            graphics.fillRect(SIDE_OFFSET + PROFILE_SIZE / 2, SIDE_OFFSET, WIDTH / 3 * 2 - 2 * SIDE_OFFSET - PROFILE_SIZE / 2, PROFILE_SIZE);
            graphics.fillRect(SIDE_OFFSET + PROFILE_SIZE / 2, SIDE_OFFSET + PROFILE_SIZE, WIDTH / 3, HEIGHT - 2 * SIDE_OFFSET - PROFILE_SIZE);

            AtomicInteger x = new AtomicInteger(SIDE_OFFSET + BADGE_SPACING), y = new AtomicInteger(profile.getWidth());
            Badge.getBadgeImages(user).forEach(bufferedImage -> {
                if (y.getAndAdd(bufferedImage.getWidth() + BADGE_SPACING) > profile.getWidth() - SIDE_OFFSET - BADGE_SPACING) y.set(WIDTH / 3 * 2 - SIDE_OFFSET + BADGE_SPACING);
                graphics.drawImage(bufferedImage, y.get(), x.getAndAdd(bufferedImage.getHeight() + BADGE_SPACING), null);
            });

            graphics.drawImage(image, SIDE_OFFSET, SIDE_OFFSET, null);
            graphics.setColor(Color.BLACK);
            graphics.drawOval(SIDE_OFFSET, SIDE_OFFSET, PROFILE_SIZE, PROFILE_SIZE);

            ImageIO.write(profile, "png", file);
        });
    }
}
