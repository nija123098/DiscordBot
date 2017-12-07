package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Region;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.RESOURCE_FILES;
import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient.getGuilds;
import static com.github.nija123098.evelyn.util.FileHelper.getTempFile;
import static com.github.nija123098.evelyn.util.Rand.getRand;
import static java.nio.file.Paths.get;
import static java.util.Collections.addAll;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MapCommand extends AbstractCommand {
    private static final int DOT_SIZE = 2;
    private static final File FILE = getTempFile("map", "png", "map");

    public MapCommand() {
        super("map", FUN, null, null, "Displays a map of Evelyn's server distribution");
    }

    @Command
    public void command(MessageMaker maker) {
        try {
            Map<Region, Integer> map = new HashMap<>();
            getGuilds().forEach(guild -> map.compute(guild.getRegion(), (region, integer) -> integer == null ? 1 : integer + 1));
            BufferedImage image = read(get(RESOURCE_FILES.finalMap()).toFile());
            Map<ColorAria, List<Pair<Integer, Integer>>> setMap = new HashMap<>();
            for (int i = 0; i < image.getWidth(); i++)
                for (int j = 0; j < image.getHeight(); j++)
                    setMap.computeIfAbsent(getColorAria(image.getRGB(i, j)), region -> new ArrayList<>(1000)).add(new Pair<>(i, j));
            Set<Pair<Integer, Integer>> pairs = new HashSet<>();
            map.forEach((region, integer) -> {
                List<Pair<Integer, Integer>> list = setMap.get(getColorAria(region.getName()));
                if (list == null) return;
                for (int i = 0; i < integer; i++) {
                    Pair<Integer, Integer> pair = getRand(list, true);
                    for (int x = 0; x < DOT_SIZE; x++) {
                        for (int y = 0; y < DOT_SIZE; y++) {
                            pairs.add(new Pair<>(pair.getKey() + x, pair.getValue() + y));
                        }
                    }
                }
            });
            pairs.forEach(cord -> image.setRGB(cord.getKey(), cord.getValue(), 0xFFFFFFFF));
            write(image, "PNG", FILE);
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
        EU(-4128768, "Central Europe", "Western Europe", "Amsterdam", "London", "Frankfurt"),
        RU(-11231522, "Russia"),
        EMA(-836095, "Hong Kong"),
        SEA(-5220838, "Singapore"),
        AUS(-4177792, "Sydney"),
        AN(-16760577),;
        private int color;
        Set<String> names;

        ColorAria(int color, String... names) {
            this.color = color;
            this.names = new HashSet<>();
            addAll(this.names, names);
        }
    }

    private static final Map<Integer, Integer> COLOR = new HashMap<>();

    private static ColorAria getColorAria(int color) {
        COLOR.compute(new Color(color).getRGB(), (i, in) -> in == null ? 1 : in + 1);
        for (ColorAria aria : ColorAria.values()) {
            if (aria.color == new Color(color).getRGB()) {
                return aria;
            }
        }
        return null;
    }

    private static ColorAria getColorAria(String name) {
        for (ColorAria aria : ColorAria.values()) {
            if (aria.names.contains(name)) return aria;
        }
        return null;
    }
}
