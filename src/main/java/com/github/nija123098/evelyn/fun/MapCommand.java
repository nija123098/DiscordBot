package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.FileHelper;
import com.github.nija123098.evelyn.util.Rand;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MapCommand extends AbstractCommand {
    private static final int DOT_SIZE = 2;
    private static final File FILE = FileHelper.getTempFile("map", "png", "map");
    private static final Map<String, Integer> REGION_MAP = new HashMap<>();
    private static final Map<Integer, List<Point>> POINT_MAP = new HashMap<>();
    private static final AtomicInteger GUILD_COUNT = new AtomicInteger();
    private static final int[] COLOR_ARRAY = new int[DOT_SIZE * 2];
    static {
        try {
            BufferedImage image = ImageIO.read(Paths.get(ConfigProvider.RESOURCE_FILES.finalMap()).toFile());
            for (int i = 0; i < image.getWidth() / DOT_SIZE; i++) {
                for (int j = 0; j < image.getHeight() / DOT_SIZE; j++) {
                    POINT_MAP.computeIfAbsent(image.getRGB(i * DOT_SIZE, j * DOT_SIZE), l -> new ArrayList<>(1000)).add(new Point(i * DOT_SIZE, j * DOT_SIZE));
                }
            }
        } catch (IOException e) {
            throw new DevelopmentException("Unable to read map template", e);
        }
        Arrays.fill(COLOR_ARRAY, 0xFFFFFFFF);
        // AF -76498 AN -16760577
        registerColor(-16724992, "US Central", "US East", "US West", "US South");
        registerColor(-16744448, "Brazil");
        registerColor(-4128768, "Central Europe", "Western Europe", "Amsterdam", "London", "Frankfurt");
        registerColor(-11231522, "Russia");
        registerColor(-836095, "Hong Kong", "Japan");
        registerColor(-5220838, "Singapore");
        registerColor(-4177792, "Sydney");
    }

    private static void registerColor(int color, String...regionNames) {
        for (String name : regionNames) REGION_MAP.put(name, color);
    }

    public MapCommand() {
        super("map", ModuleLevel.FUN, null, null, "Displays a map of Evelyn's server distribution");
    }

    @Command
    public void command(MessageMaker maker) {
        List<Guild> guilds = DiscordClient.getGuilds();
        if (GUILD_COUNT.getAndSet(guilds.size()) != guilds.size()) {
            try {// color, count
                Map<Integer, Integer> regionGuildCountMap = guilds.stream().collect(Collectors.toMap(g -> REGION_MAP.get(g.getRegion().getName()), g -> 1, (one, two) -> one + two));
                BufferedImage img = ImageIO.read(Paths.get(ConfigProvider.RESOURCE_FILES.finalMap()).toFile());
                regionGuildCountMap.forEach((color, count) -> {
                    Set<Point> use = new HashSet<>(200, 1);
                    for (int i = 0; i < count; i++) {
                        if (use.add(Rand.getRand(POINT_MAP.get(color), false))) --count;
                    }
                    use.forEach(point -> img.setRGB(point.x, point.y, DOT_SIZE, DOT_SIZE, COLOR_ARRAY, 0, 1));
                });
                if (!FILE.exists()) FILE.createNewFile();
                ImageIO.write(img, "PNG", FILE);
            } catch (IOException e) {
                throw new DevelopmentException("Issue making map", e);
            }
        }
        maker.shouldEmbed(false).withFile(FILE);
    }

    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(User.class) || clazz.equals(Guild.class) || clazz.equals(Channel.class)) return 60_000;
        return super.getCoolDown(clazz);
    }
}
