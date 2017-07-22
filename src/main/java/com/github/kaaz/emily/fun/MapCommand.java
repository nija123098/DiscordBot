package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Region;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.FileHelper;
import com.github.kaaz.emily.util.Rand;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class MapCommand extends AbstractCommand {
    private static final int DOT_SIZE = 3;
    private static final File FILE = FileHelper.getTempFile("map", "png", "map");
    public MapCommand() {
        super("map", ModuleLevel.FUN, null, null, "Displays a map of Emily's server distribution");
    }
    @Command
    public void command(MessageMaker maker){
        try {
            Map<Region, Integer> map = new HashMap<>();
            DiscordClient.getGuilds().forEach(guild -> map.compute(guild.getRegion(), (region, integer) -> integer == null ? 1 : integer + 1));
            BufferedImage image = ImageIO.read(Paths.get(BotConfig.CONTAINER_PATH, "final_map.png").toFile());
            Map<ColorAria, List<Pair<Integer, Integer>>> setMap = new HashMap<>();
            for (int i = 0; i < image.getWidth(); i++) for (int j = 0; j < image.getHeight(); j++) setMap.computeIfAbsent(getColorAria(image.getRGB(i, j)), region -> new ArrayList<>(1000)).add(new Pair<>(i, j));
            Set<Pair<Integer, Integer>> pairs = new HashSet<>();
            map.forEach((region, integer) -> {
                List<Pair<Integer, Integer>> list = setMap.get(getColorAria(region.getName()));
                if (list == null) return;
                for (int i = 0; i < integer; i++) {
                    Pair<Integer, Integer> pair = list.remove(Rand.getRand(list.size()) - 1);
                    for (int x = 0; x < DOT_SIZE; x++) {
                        for (int y = 0; y < DOT_SIZE; y++) {
                            pairs.add(new Pair<>(pair.getKey() + x, pair.getValue() + y));
                        }
                    }
                }
            });
            pairs.forEach(cord -> image.setRGB(cord.getKey(), cord.getValue(), 0xFFFFFFFF));
            ImageIO.write(image, "PNG", FILE);
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
        maker.withFile(FILE);
    }

    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(GlobalConfigurable.class)) return 30_000;
        return super.getCoolDown(clazz);
    }

    private enum ColorAria {// AF -76498
        NA(-16724992, "US Central", "US East", "US West", "US South"),
        SA(-16744448, "Brazil"),
        EU(-4128768, "Central Europe", "Western Europe"),
        RU(-11231522, "Russia"),
        EMA(-836095, "Hong Kong"),
        SEA(-5220838, "Singapore"),
        AUS(-4177792, "Sydney"),
        AN(-16760577),;
        private int color;
        Set<String> names;
        ColorAria(int color, String...names) {
            this.color = color;
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
    }
    private static final Map<Integer, Integer> COLOR = new HashMap<>();
    private static ColorAria getColorAria(int color){
        COLOR.compute(new Color(color).getRGB(), (i, in) -> in == null ? 1 : in + 1);
        for (ColorAria aria : ColorAria.values()){
            if (aria.color == new Color(color).getRGB()) {
                return aria;
            }
        }
        return null;
    }
    private static ColorAria getColorAria(String name){
        for (ColorAria aria : ColorAria.values()){
            if (aria.names.contains(name)) return aria;
        }
        return null;
    }
}
