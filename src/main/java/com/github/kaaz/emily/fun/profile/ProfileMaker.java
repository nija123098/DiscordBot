package com.github.kaaz.emily.fun.profile;

import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.FileHelper;
import com.github.kaaz.emily.util.GraphicsHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileMaker {
    private static final int SIDE_OFFSET = 10, WIDTH = 300, HEIGHT = 200, PROFILE_SIZE = 64, BADGE_SPACING = 4;
    private static final Color OPAQUE = new Color(0, 0, 0, 0);
    private static final List<Point> CIRCLE = new ArrayList<>((int) (PROFILE_SIZE * PROFILE_SIZE - Math.pow(PROFILE_SIZE / 2, 2) * Math.PI + PROFILE_SIZE));
    static {
        for (int i = 0; i < PROFILE_SIZE; i++) {
            for (int j = 0; j < PROFILE_SIZE; j++) {
                if (Math.pow(i * i + j * j, .5F) >= 1) CIRCLE.add(new Point(i, j));
            }
        }
        ((ArrayList<Point>) CIRCLE).trimToSize();
    }
    public static File getProfileIcon(User user){
        return FileHelper.getTempFile("profile", "png", "profile-" + user.getID(), file -> {
            HttpURLConnection connection = ((HttpURLConnection) new URL(user.getAvatarURL().replace(".webp", ".png")).openConnection());
            connection.setRequestProperty("User-Agent", BotConfig.USER_AGENT);
            BufferedImage image = ImageIO.read(connection.getInputStream());
            CIRCLE.forEach(point -> image.setRGB((int) point.getX(), (int) point.getY(), OPAQUE.getRGB()));

            BufferedImage profile = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = ((Graphics2D) profile.getGraphics());

            graphics.setColor(GraphicsHelper.getGradient(.4F, Color.WHITE, OPAQUE));
            graphics.fillRect(SIDE_OFFSET, SIDE_OFFSET, WIDTH / 3, (HEIGHT - 2 * SIDE_OFFSET) / 2);
            graphics.fillRect(WIDTH / 3 * 2 - SIDE_OFFSET, SIDE_OFFSET, WIDTH / 3, HEIGHT - 2 * SIDE_OFFSET);

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
