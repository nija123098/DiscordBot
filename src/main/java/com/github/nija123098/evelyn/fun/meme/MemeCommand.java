package com.github.nija123098.evelyn.fun.meme;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.DevelopmentException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.fun.meme.MemeTypesCommand.MEME_TYPES;
import static com.github.nija123098.evelyn.util.FileHelper.getTempFile;
import static com.google.common.base.Joiner.on;
import static com.mashape.unirest.http.Unirest.get;
import static java.net.URLEncoder.encode;
import static java.util.Arrays.copyOfRange;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MemeCommand extends AbstractCommand {
    public MemeCommand() {
        super("meme", FUN, null, null, "Generate a meme!");
    }

    @Command
    public void command(MessageMaker maker, String[] args) {
        maker.mustEmbed();
        if (args.length == 0) {
            maker.getTitle().clear().appendRaw("Meme Types");
            maker.getNote().clear().appendRaw("Do @Evelyn <meme type> [top text] | [bottom text]");
            MemeTypesCommand.command(maker);
            return;
        }
        String type = args[0].toLowerCase();
        if (!MEME_TYPES.contains(type)) {
            maker.append("Sorry, `" + type + "` is an invalid meme type\n");
            MemeTypesCommand.command(maker);
            return;
        }
        String topText = "-", botText = "-";
        if (args.length > 1) {
            String[] memeText = on("-").join(copyOfRange(args, 1, args.length)).replaceAll("/", "").split("\\|");
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
            maker.withImage("https://memegen.link/" + type + "/" + encode(topText, "UTF-8") + "/" + encode(botText, "UTF-8") + ".jpg");

        } catch (IOException/* | ExecutionException | InterruptedException*/ e) {
            throw new DevelopmentException("Our meme service is having trouble right now", e);
        }
    }

    @Override
    protected String getLocalUsages() {
        return "#  meme <meme type> [top text] | [bottom text] // replace the top and bottom text for immense hilarity";
    }
}
