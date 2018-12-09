package com.github.nija123098.evelyn.discordobjects.helpers.profileimage;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.CallBuffer;
import com.github.nija123098.evelyn.util.ColorSummery;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class UserAvatar {
    private static final String LOCATION_PREFIX = (ConfigProvider.FOLDER_SETTINGS.profileIconsFolder().endsWith(File.pathSeparator) ? ConfigProvider.FOLDER_SETTINGS.profileIconsFolder() + File.pathSeparator : ConfigProvider.FOLDER_SETTINGS.profileIconsFolder()) + "profile-";
    static {
        new File(ConfigProvider.FOLDER_SETTINGS.profileIconsFolder()).mkdirs();
    }
    private final Map<Integer, BufferedImage> sizeMap = new TreeMap<>();
    private final User user;
    private final Integer defaultSize;
    private final ColorSummery colorSummery;
    public UserAvatar(User user) {
        this.user = user;
        String hash = user.getAvatar();
        File file = new File(getImageLocation(user));
        BufferedImage image;
        if (!Objects.equals(hash, ConfigHandler.getSetting(UserAvatarHashConfig.class, user)) || !file.exists()) {
            ConfigHandler.setSetting(UserAvatarHashConfig.class, user, hash);
            image = downloadUserAvatar(user);
        } else {
            image = readImage(file);
        }
        this.colorSummery = new ColorSummery(image);
        this.defaultSize = image.getHeight();
        this.sizeMap.put(this.defaultSize, image);
    }

    public BufferedImage getImage() {
        return getImage(this.defaultSize);
    }

    public ColorSummery getColorSummery() {
        return this.colorSummery;
    }

    public BufferedImage getImage(int size) {
        return this.sizeMap.computeIfAbsent(size, integer -> {
            File file = new File(getImageLocation(this.user, integer));
            if (file.exists()) return readImage(file);
            BufferedImage image = new BufferedImage(integer, integer, BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(getImage(), 0, 0, integer, integer, 0, 0, this.defaultSize, this.defaultSize, null);
            try {
                if (!ImageIO.write(image, "PNG", file)) {
                    throw new DevelopmentException("No driver found for PNG");
                }
            } catch (IOException e) {
                throw new DevelopmentException("Unable to write image avatar from disk", e);
            }
            return image;
        });
    }

    private static BufferedImage downloadUserAvatar(User user) {
        try {
            HttpURLConnection connection = ((HttpURLConnection) new URL(user.getAvatarURL().replace(".webp", ".png")).openConnection());
            connection.setRequestProperty("User-Agent", ConfigProvider.BOT_SETTINGS.userAgent());
            BufferedImage image = ImageIO.read(connection.getInputStream());
            if (!ImageIO.write(image, "PNG", new File(getImageLocation(user)))) {
                throw new DevelopmentException("No driver found for PNG");
            }
            return image;
        } catch (IOException e) {
            throw new DevelopmentException("Unable to read or write image avatar from disk for profile for " + user.getID(), e);
        }
    }

    public static String getImageLocation(User user) {
        return LOCATION_PREFIX + user.getID() + ".png";
    }

    public static String getImageLocation(User user, int width) {
        return LOCATION_PREFIX + user.getID() + "-" + width + ".png";
    }

    public static BufferedImage readImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (Throwable e) {
            throw new DevelopmentException("Unable to read image avatar from disk for file: " + file, e);
        }
    }

    private static final Long DELAY = 1000L;
    private static final CallBuffer CALL_BUFFER = new CallBuffer("Profile Download", DELAY);

    /**
     * Fills the provided {@link List <UserAvatar>}
     * with the {@link UserAvatar} instances.
     *
     * @param users the users to get {@link UserAvatar} instances for.
     * @param userAvatars the list to fill with {@link UserAvatar} instances.
     * @return the amount of time it will take in milliseconds to fill all {@link UserAvatar} instances.
     */
    public static long getUserAvatars(List<User> users, List<UserAvatar> userAvatars) {
        UserAvatarHashConfig config = ConfigHandler.getConfig(UserAvatarHashConfig.class);
        users.forEach(u -> {
            String hash = u.getAvatar();
            if (!Objects.equals(hash, config.getValue(u)) || !new File(getImageLocation(u)).exists()) {
                userAvatars.add(new UserAvatar(u));
            } else {
                CALL_BUFFER.call(() -> userAvatars.add(new UserAvatar(u)));
            }
        });
        return CALL_BUFFER.getDelay();
    }
}
