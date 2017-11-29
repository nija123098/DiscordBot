package com.github.nija123098.evelyn.fun.profile;

import com.github.nija123098.evelyn.botConfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Log;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Badge {
    USER(user -> true);
    private static final int SPACING = 3;
    private Function<User, Integer> levelFunction;
    private List<BufferedImage> images = new ArrayList<>();
    Badge(int max){
        try{BufferedImage badge = ImageIO.read(new File(ConfigProvider.folderSettings.badge_folder() + this.name().toLowerCase() + ".png"));
            Font font = new Font("Times New Roman", Font.PLAIN, badge.getHeight() / 3 * 2);
            BufferedImage image;
            Graphics2D graphics;
            String test;
            for (int i = 1; i < max; i++) {
                test = FormatHelper.repeat('I', i);
                image = new BufferedImage(badge.getHeight() + (int) font.getStringBounds(test, null).getWidth() + 3 * SPACING, 2 * SPACING + badge.getHeight(), BufferedImage.TYPE_INT_ARGB);
                graphics = image.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(new Color(badge.getRGB(0, 0)));
                graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
                graphics.setColor(Color.BLACK);
                graphics.drawRect(0, 0, image.getWidth(), image.getHeight());
                graphics.drawImage(badge, SPACING, SPACING, null);
                graphics.drawString(test, SPACING * 2 + badge.getWidth(), SPACING);
                this.images.add(image);
            }
            if (this.images.isEmpty()){
                image = new BufferedImage(SPACING * 2 + badge.getWidth(), SPACING * 2 + badge.getHeight(), BufferedImage.TYPE_INT_ARGB);
                graphics = image.createGraphics();
                graphics.setColor(new Color(badge.getRGB(0, 0)));
                graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
                graphics.drawImage(badge, SPACING, SPACING, null);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(0, 0, image.getWidth(), image.getHeight());
            }
        } catch (IOException e) {
            Log.log("Could not load badge icon for " + this.name(), e);
        }
    }
    Badge(int max, Function<User, Integer> levelFunction) {
        this(max);
        this.levelFunction = levelFunction;
    }
    Badge(Function<User, Boolean> levelFunction) {
        this(1);
        this.levelFunction = user -> levelFunction.apply(user) ? 1 : 0;
    }
    public static Set<Pair<Badge, Integer>> getBadges(User user){
        Set<Pair<Badge, Integer>> set = new HashSet<>();
        int level;
        for (Badge badge : Badge.values()) {
            level = Math.min(badge.levelFunction.apply(user), badge.images.size());
            if (level > 0) set.add(new Pair<>(badge, level));
        }
        return set;
    }
    public static Set<BufferedImage> getBadgeImages(User user){
        return getBadges(user).stream().map(pairs -> pairs.getKey().images.get(pairs.getValue())).collect(Collectors.toSet());
    }
}
