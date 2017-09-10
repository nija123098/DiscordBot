package com.github.nija123098.evelyn.fun.meme;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.util.FileHelper;
import com.google.common.base.Joiner;
import com.mashape.unirest.http.Unirest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Made by nija123098 on 6/4/2017.
 */
public class MemeCommand extends AbstractCommand {
    public MemeCommand() {
        super("meme", ModuleLevel.FUN, null, null, "Generate a meme!");
    }
    @Command
    public void command(MessageMaker maker, String[] args){
        if (args.length == 0) {
            maker.append("Do @Emily <meme type> [top text] | [bottom text]\n");
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
            BufferedImage image = ImageIO.read(Unirest.get("https://memegen.link/" + type + "/" + URLEncoder.encode(topText, "UTF-8") + "/" + URLEncoder.encode(botText, "UTF-8") + ".jpg").asStringAsync().get().getRawBody());
            maker.withFile(FileHelper.getTempFile("memes", "jpg", type + "_" + URLEncoder.encode(topText, "UTF-8") + "_" + URLEncoder.encode(botText, "UTF-8"), file -> ImageIO.write(image, "png", file)));
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new DevelopmentException("Our meme service is having trouble right now", e);
        }
    }

    @Override
    protected String getLocalUsages() {
        return "meme <meme type> [top text] | [bottom text] // replace the top and bottom text for immense hilarity";
    }
}
