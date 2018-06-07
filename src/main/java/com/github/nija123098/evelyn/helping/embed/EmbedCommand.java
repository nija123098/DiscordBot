package com.github.nija123098.evelyn.helping.embed;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.awt.*;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class EmbedCommand extends AbstractCommand {
    // private static final LoadingCache<User, Map<Channel, Pair<Message, Message>>> EMBED_CACHE = CacheHelper.getLoadingCache(4, 20, 60_000L, );

    public EmbedCommand() {
        super("embed", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Embeds the given string, must use the format in description.");
    }

    @Command
    public void embed(@Argument String s, MessageMaker maker, Channel channel, User user, Message msg) {
        //configure maker
        maker.withAutoSend(false);

        //configure code message maker
        MessageMaker code = new MessageMaker(maker);
        code.getHeader().clear().appendRaw("```css\n");
        boolean codeSend = false;

        //split original string into embed fields
        s = s.replaceAll("]] ", "]]");
        s = s.replace("\n", "\\n");
        String[] s2 = s.split("]]");

        //initialize reaction array
        String[] r = null;

        //save url to color
        String cThumb = null;
        String cImg = null;
        String cNote = null;
        String cAuth = null;

        //assign fields
        for (String str: s2) {
            if (str.startsWith("m[[") || str.startsWith(" m[[")) {
                str = str.replace("m[[", "");
                maker.getHeader().clear().append(translateSpecialCharacters(str));
                code.getHeader().appendRaw("maker.getHeader().clear().append(\"" + cleanString(str) + "\");\n");
            } else if (str.startsWith("t[[") || str.startsWith(" t[[")) {
                str = str.replace("t[[", "");
                maker.getTitle().clear().append(translateSpecialCharacters(str));
                code.getHeader().appendRaw("maker.getTitle().clear().append(\"" + str + "\");\n");
            } else if (str.startsWith("foo[[") || str.startsWith(" foo[[")) {
                str = str.replace("foo[[", "");
                maker.getFooter().clear().append(translateSpecialCharacters(str));
                code.getHeader().appendRaw("maker.getFooter().clear().append(\"" + cleanString(str) + "\");\n");
            } else if (str.startsWith("n[[") || str.startsWith(" n[[")) {
                str = str.replace("n[[", "");
                maker.getNote().clear().append(translateSpecialCharacters(str));
                code.getHeader().appendRaw("maker.getNote().clear().append(\"" + str + "\");\n");
            } else if (str.startsWith("img[[") || str.startsWith(" img[[")) {
                str = str.replace("img[[", "");
                str = str.replaceAll(" ", "");
                maker.withImage(str);
                cImg = str;
                code.getHeader().appendRaw("maker.withImage(\"" + str + "\");\n");
            } else if (str.startsWith("nimg[[") || str.startsWith(" nimg[[")) {
                str = str.replace("nimg[[", "");
                str = str.replaceAll(" ", "");
                maker.withFooterIcon(str);
                cNote = str;
                code.getHeader().appendRaw("maker.withFooterIcon(\"" + str + "\");\n");
            } else if (str.startsWith("a[[") || str.startsWith(" a[[")) {
                str = str.replace("a[[", "");
                maker.getAuthorName().append(translateSpecialCharacters(str));
                code.getHeader().appendRaw("maker.getAuthorName().append(\"" + str + "\");\n");
            } else if (str.startsWith("aimg[[") || str.startsWith(" aimg[[")) {
                str = str.replace("aimg[[", "");
                str = str.replaceAll(" ", "");
                maker.withAuthorIcon(str);
                cAuth = str;
                code.getHeader().appendRaw("maker.withAuthorIcon(\"" + str + "\");\n");
            } else if (str.startsWith("fp[[") || str.startsWith(" fp[[")) {
                str = str.replace("fp[[", "");
                String[] sfp = str.split(",,,");
                maker.getNewFieldPart().withBoth(translateSpecialCharacters(sfp[0]), translateSpecialCharacters(sfp[1])).withInline(false);
                code.getHeader().appendRaw("maker.getNewFieldPart().withBoth(\"" + sfp[0] + "\", \"" + cleanString(sfp[1]) + "\").withInline(false);\n");
            } else if (str.startsWith("fpi[[") || str.startsWith(" fpi[[")) {
                str = str.replace("fpi[[", "");
                String[] sfp = str.split(",,,");
                maker.getNewFieldPart().withBoth(translateSpecialCharacters(sfp[0]), translateSpecialCharacters(sfp[1])).withInline(true);
                code.getHeader().appendRaw("maker.getNewFieldPart().withBoth(\"" + sfp[0] + "\", \"" + cleanString(sfp[1]) + "\").withInline(true);\n");
            } else if (str.startsWith("thumb[[") || str.startsWith(" thumb[[")) {
                str = str.replace("thumb[[", "");
                str = str.replaceAll(" ", "");
                maker.withThumb(str);
                cThumb = str;
                code.getHeader().appendRaw("maker.withThumb(\"" + str + "\");\n");
            } else if (str.startsWith("c[[") || str.startsWith(" c[[")) {
                str = str.replace("c[[", "");
                str = str.replaceAll(" ", "");
                String[] rgb = str.split(",");
                maker.withColor(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                code.getHeader().appendRaw("maker.withColor(new Color(" + rgb[0] + ", " + rgb[1] + ", " + rgb[2] + "));\n");
            } else if (str.startsWith("r[[") || str.startsWith(" r[[")) {
                str = str.replace("r[[", "");
                str = str.replaceAll(" ", "");
                r = str.split(",");
            } else if (str.startsWith("opt[[") || str.startsWith(" opt[[")) {
                str = str.replace("opt[[", "");
                str = str.toLowerCase();
                str = str.replaceAll(" ", "");
                String[] opt = str.split(",");
                for (String stro:opt){
                    if (stro.contains("userauthor")) {
                        maker.withAuthor(user);
                        code.getHeader().appendRaw("maker.withAuthor(user);\nmaker.withAuthorIcon(user.getAvatarURL());\n");
                    } else if (stro.contains("timestamp")) {
                        maker.withTimestamp(System.currentTimeMillis());
                        code.getHeader().appendRaw("maker.withTimestamp(System.currentTimeMillis());\n");
                    } else if (stro.contains("code")) {
                        codeSend = true;
                    } else if (stro.contains("deletecommand")) {
                        msg.delete();
                    } else if (stro.contains("editorigin")) {
                        //maker.setMessage(EMBED_CACHE.getUnchecked(user).get(channel).getKey());
                       //code.setMessage(EMBED_CACHE.getUnchecked(user).get(channel).getValue());
                    } else if (stro.contains("usercolor")) {
                        maker.withColor(user);
                        code.getHeader().appendRaw("maker.withColor(user);\n");
                    } else if (stro.contains("thumbcolor")) {
                        maker.withColor(cThumb);
                        code.getHeader().appendRaw("maker.withColor(\"" + cThumb + "\");\n");
                    } else if (stro.contains("imgcolor")) {
                        maker.withColor(cImg);
                        code.getHeader().appendRaw("maker.withColor(\"" + cImg + "\");\n");
                    } else if (stro.contains("authcolor")) {
                        maker.withColor(cAuth);
                        code.getHeader().appendRaw("maker.withColor(\"" + cAuth + "\");\n");
                    } else if (stro.contains("notecolor")) {
                        maker.withColor(cNote);
                        code.getHeader().appendRaw("maker.withColor(\"" + cNote + "\");\n");
                    }
                }
            }
        }
        maker.send();
        if (codeSend) {
            code.getHeader().appendRaw("```");
            code.send();
        }
            //EMBED_CACHE.getUnchecked(user).put(channel, new Pair<>(maker.sentMessage(), code.sentMessage()));
//        } else {
//            EMBED_CACHE.getUnchecked(user).put(channel, new Pair<>(maker.sentMessage(), null));
//        }

        //add reactions for the sent message if available
        if (r != null) {
            for (String rstr:r){
                rstr = rstr.replaceAll("<", "");
                rstr = rstr.replaceAll(">", "");
                maker.sentMessage().addReaction(rstr);
            }
        }
    }

    @Override
    public String getHelp() {
        return "#  Do not add a new line or \\n outside the brackets.\n\n#  Special character replacement:\n\n// \"\\u200b\": Zero width character.\n// \"\\n\": New line.\n\n#  Format help:\n\n// t[[TITLE]]\n// m[[MESSAGE]]\n// foo[[FOOTER]]\n// n[[NOTE]]\n// nimg[[NOTE IMAGE]]\n// img[[IMAGE]]\n// a[[AUTHOR]]\n// aimg[[AUTHOR IMAGE]]\n// fp[[FIELD PART TITLE,,,TEXT]]\n// fpi[[INLINE FIELD PART TITLE,,,TEXT]]\n// thumb[[THUMBNAIL]]\n// c[[R,G,B]]\n// r[[EMOTE,EMOTE]]\n// opt[[OPTION,OPTION]]\n\n#  Options:\n\n// \"userauthor\": Insert the user as the author.\n// \"timestamp\": Add timestamp.\n// \"code\": Post the raw code for the embed.\n// \"deletecommand\": Delete the user message.\n// \"editorigin\": Edit the embed instead.\n// \"usercolor\": Use user color as embed color.\n// \"thumbcolor\": Use thumbnail color as embed color.\n// \"authcolor\": Use author image color as embed color.\n// \"imgcolor\": Use image color as embed color.\n// \"notecolor\": Use note image color as embed color.";
    }

    private String translateSpecialCharacters(String s) {
        s = s.replace("\\u200b", "\u200b");
        s = s.replace("\\n", "\n");
        return s;
    }

    private String cleanString(String s) {
        s = s.replace("```", "{CB}");
        return s;
    }
}