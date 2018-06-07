package com.github.nija123098.evelyn.fun.meme;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MemeCommand extends AbstractCommand {
    public MemeCommand() {
        super("meme", ModuleLevel.FUN, null, null, "Generate a meme!");
    }

    @Command
    public void command(MessageMaker maker, String[] args, Guild guild) {
        if (args.length == 0) {
            maker.getTitle().clear().appendRaw("Meme Types");
            maker.getNote().clear().appendRaw("Use " + ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "meme <meme type> [top text] | [bottom text]");
            MemeTypesCommand.command(maker);
            return;
        }
        String type = args[0].toLowerCase();
        if (!MemeTypesCommand.MEME_TYPES.contains(type)) {
            maker.append("Sorry, `" + type + "` is an invalid meme type\n");
            MemeTypesCommand.command(maker);
            return;
        }
        String topText = "-", botText = "-";
        if (args.length > 1) {
            String[] memeText = Joiner.on("-").join(Arrays.copyOfRange(args, 1, args.length)).replaceAll("/", "").split("\\|");
            if (memeText.length > 0) {
                if (memeText.length > 1) {
                    botText = memeText[1];
                }
                topText = memeText[0];
            }
        }
        try {// let exceptions get thrown
            //BufferedImage image = read(get("https://memegen.link/" + type + "/" + encode(topText, "UTF-8") + "/" + encode(botText, "UTF-8") + ".jpg").asStringAsync().get().getRawBody());
            //maker.withFile(getTempFile("memes", "jpg", type + "_" + encode(topText, "UTF-8") + "_" + encode(botText, "UTF-8"), file -> write(image, "png", file)));
            maker.withImage("https://memegen.link/" + type + "/" + URLEncoder.encode(topText, "UTF-8") + "/" + URLEncoder.encode(botText, "UTF-8") + ".jpg");
        } catch (IOException/* | ExecutionException | InterruptedException*/ e) {
            throw new DevelopmentException("Our meme service is unavailable right now", e);
        }
    }

    @Override
    protected String getLocalUsages() {
        return "#  meme <meme type> [top text] | [bottom text] // replace the top and bottom text for immense hilarity";
    }
}
