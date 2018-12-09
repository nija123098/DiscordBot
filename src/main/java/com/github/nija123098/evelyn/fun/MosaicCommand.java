package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.profileimage.UserAvatar;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.exception.UserIssueException;
import com.github.nija123098.evelyn.util.*;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MosaicCommand extends AbstractCommand {
    private static final ScheduledExecutorService MOSAIC_EXECUTOR = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() / 4 + 1, r -> new Thread(r, "Mosaic Maker"), (r, executor) -> {
        throw new UserIssueException("Please try again later");
    });
    private static final Set<User> IN_PROCESS = new HashSet<>();
    public MosaicCommand() {
        super("mosaic", ModuleLevel.FUN, null, null, "Produces a mosaic using server member profile pictures provided am attached image or URL");
    }
    @Command
    public void command(Guild guild, Message message, MessageMaker messageMaker, User sender, @Argument(optional = true, replacement = ContextType.NONE, info = "base image pixel summery size") Integer baseSummerySize, @Argument(optional = true, replacement = ContextType.NONE, info = "tile size") Integer avatarResolution, String url) {
        BufferedImage image;
        if (!message.getAttachments().isEmpty()) {
            url = message.getAttachments().get(0).getUrl();
        }
        try {
            HttpURLConnection connection = ((HttpURLConnection) new URL(url.replace(".webp", ".png")).openConnection());
            connection.setRequestProperty("User-Agent", ConfigProvider.BOT_SETTINGS.userAgent());
            try {
                image = ImageIO.read(connection.getInputStream());
            } catch (Exception e) {
                throw new UserIssueException("Only PNG images are supported");
            }
        } catch (IOException e) {
            throw new DevelopmentException("Invalid URL for attachment", e);
        }
        List<UserAvatar> avatars = new LinkedList<>();//                                         Remove bots,                         default avatar users
        long estimatedTime = (long) (UserAvatar.getUserAvatars(guild.getUsers().parallelStream().filter(user -> !user.isBot()).filter(user -> user.getAvatar() != null).collect(Collectors.toList()), avatars) * 1.05 + 500);
        if (estimatedTime > 2_000) {
            messageMaker.append("It will take " + Time.getAbbreviated(estimatedTime) + " to download all profile images.\nThe mosaic will then take a bit to make, it will be posted here when it is ready.");
        } else {
            messageMaker.append("It may take a bit to make the mosaic, it will be posted here when complete.");
            CareLess.lessSleep(estimatedTime);
        }
        IN_PROCESS.add(sender);
        MOSAIC_EXECUTOR.schedule(() -> {
            try {
                BufferedImage mosaic = makeMosaic(image, avatars, baseSummerySize, avatarResolution);
                File file = FileHelper.getTempFile("mosaic", "png");
                try {
                    ImageIO.write(mosaic, "PNG", file);
                } catch (IOException e) {
                    throw new DevelopmentException("Unable to write mosaic to file", e);
                }
                new MessageMaker(message.getChannel()).withFile(file).shouldEmbed(false).append(sender.mention() + " your mosaic is done!").send();
                IN_PROCESS.remove(sender);
            } catch (Throwable t) {
                IN_PROCESS.remove(sender);
                new DevelopmentException("Unable to complete mosaic for some reason", t).makeMessage(message.getChannel()).send();
            }
        }, estimatedTime, TimeUnit.MILLISECONDS);
    }
    public static List<Pair<ColorSummery, Pair<Integer, Integer>>> getColorSummery(BufferedImage image, Integer baseSummerySize, Integer avatarResolution) {
        List<Pair<ColorSummery, Pair<Integer, Integer>>> targetColorMap = new ArrayList<>(image.getHeight() / baseSummerySize * image.getWidth() / baseSummerySize);
        int x = 0, y = 0, xTarget, yTarget;
        while (true) {
            xTarget = Math.min(x + baseSummerySize, image.getWidth());
            while (true) {
                yTarget = Math.min(y + baseSummerySize, image.getHeight());
                targetColorMap.add(new Pair<>(new ColorSummery(image, x, y, xTarget, yTarget), new Pair<>(x / baseSummerySize * avatarResolution, y / baseSummerySize * avatarResolution)));
                if (yTarget == image.getHeight()) break;
                y = yTarget;
            }
            y = 0;
            if (xTarget == image.getWidth()) break;
            x = xTarget;
        }
        return targetColorMap;
    }
    public static BufferedImage makeMosaic(BufferedImage baseImage, List<UserAvatar> avatars, Integer baseSummerySize, Integer avatarResolution) {
        float ratio = (float) avatarResolution / baseSummerySize;
        BufferedImage result = new BufferedImage((int) (ratio * baseImage.getWidth()), (int) (ratio * baseImage.getHeight()), BufferedImage.TYPE_INT_RGB);
        List<Pair<ColorSummery, Pair<Integer, Integer>>> colorSummery = getColorSummery(baseImage, baseSummerySize, avatarResolution);
        HashMap<ColorSummery, TilePool> tilePoolMap = new HashMap<>(avatars.size());
        avatars.forEach(userAvatar -> tilePoolMap.compute(userAvatar.getColorSummery(), (colorSummery1, tilePool) -> {
            if (tilePool == null) tilePool = new TilePool(colorSummery1);
            tilePool.add(userAvatar.getImage(avatarResolution));
            return tilePool;
        }));
        Graphics2D graphics2D = result.createGraphics();
        colorSummery.forEach(colorSummeryPairPair -> graphics2D.drawImage(tilePoolMap.computeIfAbsent(colorSummeryPairPair.getKey(), cs -> getBestTilePool(cs, tilePoolMap)).getTile(), colorSummeryPairPair.getValue().getKey(), colorSummeryPairPair.getValue().getValue(), colorSummeryPairPair.getValue().getKey() + avatarResolution, colorSummeryPairPair.getValue().getValue() + avatarResolution, 0, 0, avatarResolution, avatarResolution, null));
        return result;
    }

    public static TilePool getBestTilePool(ColorSummery summery, HashMap<ColorSummery, TilePool> tilePoolMap) {
        return tilePoolMap.values().parallelStream().reduce((cs1, cs2) -> cs1.getColorSummery().compare(summery) < cs2.getColorSummery().compare(summery) ? cs1 : cs2).orElse(null);
    }

    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(User.class)) return 10_000;
        if (clazz.equals(Guild.class)) return 10_000;
        if (clazz.equals(GlobalConfigurable.class)) return 5_000;
        return super.getCoolDown(clazz);
    }


    public static class TilePool {
        private final AtomicInteger index = new AtomicInteger();
        private final ArrayList<BufferedImage> tiles = new ArrayList<>(1);
        private final ColorSummery colorSummery;


        public TilePool(ColorSummery colorSummery) {
            this.colorSummery = colorSummery;
        }

        public ColorSummery getColorSummery() {
            return this.colorSummery;
        }

        public void add(BufferedImage image) {
            this.tiles.add(image);
        }

        public BufferedImage getTile() {
            if (this.index.get() >= this.tiles.size()) this.index.set(0);
            return this.tiles.get(this.index.getAndIncrement());
        }
    }
}
