package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;

import java.awt.*;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class EmbedCommand extends AbstractCommand {

    //constructor
    public EmbedCommand() {
        super("embed", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Embeds the given string, must use the format in description.");
    }

    @Command
    public void embed(@Argument String s, MessageMaker maker) {
        //configure maker
        maker.withAutoSend(false);
        maker.mustEmbed();

        //account for special characters
        s = s.replaceAll("u200b", "\u200B");

        //split original string into embed fields
        String[] s2 = s.split("]]");

        //initialize reaction array
        String[] r = null;

        //assign fields
        for (int i = 0; i < s2.length; i++){
            if (s2[i].startsWith("m[[") || s2[i].startsWith(" m[[")) {
                s2[i] = s2[i].replace("m[[", "");
                maker.getHeader().clear().append(s2[i]);
            } else if (s2[i].startsWith("t[[") || s2[i].startsWith(" t[[")) {
                s2[i] = s2[i].replace("t[[", "");
                maker.getTitle().clear().append(s2[i]);
            } else if (s2[i].startsWith("foo[[") || s2[i].startsWith(" foo[[")) {
                s2[i] = s2[i].replace("foo[[", "");
                maker.getFooter().clear().append(s2[i]);
            } else if (s2[i].startsWith("n[[") || s2[i].startsWith(" n[[")) {
                s2[i] = s2[i].replace("n[[", "");
                maker.getNote().clear().append(s2[i]);
            } else if (s2[i].startsWith("img[[") || s2[i].startsWith(" img[[")) {
                s2[i] = s2[i].replace("img[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withImage(s2[i]);
            } else if (s2[i].startsWith("nimg[[") || s2[i].startsWith(" nimg[[")) {
                s2[i] = s2[i].replace("nimg[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withFooterIcon(s2[i]);
            } else if (s2[i].startsWith("a[[") || s2[i].startsWith(" a[[")) {
                s2[i] = s2[i].replace("a[[", "");
                maker.getAuthorName().append(s2[i]);
            } else if (s2[i].startsWith("aimg[[") || s2[i].startsWith(" aimg[[")) {
                s2[i] = s2[i].replace("aimg[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withAuthorIcon(s2[i]);
            } else if (s2[i].startsWith("fp[[") || s2[i].startsWith(" fp[[")) {
                s2[i] = s2[i].replace("fp[[", "");
                String[] sfp = s2[i].split(",,,");
                maker.getNewFieldPart().withBoth(sfp[0], sfp[1]).withInline(false);
            } else if (s2[i].startsWith("fpi[[") || s2[i].startsWith(" fpi[[")) {
                s2[i] = s2[i].replace("fpi[[", "");
                String[] sfp = s2[i].split(",,,");
                maker.getNewFieldPart().withBoth(sfp[0], sfp[1]).withInline(true);
            } else if (s2[i].startsWith("thumb[[") || s2[i].startsWith(" thumb[[")) {
                s2[i] = s2[i].replace("thumb[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withThumb(s2[i]);
            } else if (s2[i].startsWith("c[[") || s2[i].startsWith(" c[[")) {
                s2[i] = s2[i].replace("c[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                String[] rgb = s2[i].split(",,,");
                maker.withColor(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            } else if (s2[i].startsWith("r[[") || s2[i].startsWith(" r[[")) {
                s2[i] = s2[i].replace("r[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                r = s2[i].split(",,,");
            }
        }
        maker.send();

        //add reactions for the sent message if available
        if (r != null) {
            for (int i = 0; i < r.length; i++){
                r[i] = r[i].replaceAll("<", "");
                r[i] = r[i].replaceAll(">", "");
                maker.sentMessage().addReaction(r[i]);
            }
        }
    }

    //help command override description
    @Override
    public String getHelp() {

        //command description:
        return
                "#  Do not add \\n outside the brackets for an option\n\n#  You can use \"u200b\" to add a zero width character\n\n#  Format help:\n\n// t[[TITLE]]\n// m[[MESSAGE]]\n// foo[[FOOTER]]\n// n[[NOTE]]\n// nimg[[NOTE IMAGE]]\n// img[[IMAGE]]\n// a[[AUTHOR]]\n// aimg[[AUTHOR IMAGE]]\n// fp[[FIELD PART TITLE,,,TEXT]]\n// fpi[[INLINE FIELD PART TITLE,,,TEXT]]\n// thumb[[THUMBNAIL]]\n// c[[R,,,G,,,B]]\n// r[[EMOTE,,,EMOTE]]";
    }
}