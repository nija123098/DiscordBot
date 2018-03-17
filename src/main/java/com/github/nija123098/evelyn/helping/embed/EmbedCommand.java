package com.github.nija123098.evelyn.helping.embed;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.util.Log;

import java.awt.*;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class EmbedCommand extends AbstractCommand {
    public EmbedCommand() {
        super("embed", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Embeds the given string, must use the format in description.");
    }

    @Command
    public void embed(@Argument String s, MessageMaker maker, GuildUser guser, Message msg) {
        //configure maker
        maker.withAutoSend(false);
        maker.mustEmbed();

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
        for (int i = 0; i < s2.length; i++) {
            if (s2[i].startsWith("m[[") || s2[i].startsWith(" m[[")) {
                s2[i] = s2[i].replace("m[[", "");
                maker.getHeader().clear().append(translateSpecialCharacters(s2[i]));
                code.getHeader().appendRaw("maker.getHeader().clear().append(\"" + cleanString(s2[i]) + "\");\n");
            } else if (s2[i].startsWith("t[[") || s2[i].startsWith(" t[[")) {
                s2[i] = s2[i].replace("t[[", "");
                maker.getTitle().clear().append(translateSpecialCharacters(s2[i]));
                code.getHeader().appendRaw("maker.getTitle().clear().append(\"" + s2[i] + "\");\n");
            } else if (s2[i].startsWith("foo[[") || s2[i].startsWith(" foo[[")) {
                s2[i] = s2[i].replace("foo[[", "");
                maker.getFooter().clear().append(translateSpecialCharacters(s2[i]));
                code.getHeader().appendRaw("maker.getFooter().clear().append(\"" + cleanString(s2[i]) + "\");\n");
            } else if (s2[i].startsWith("n[[") || s2[i].startsWith(" n[[")) {
                s2[i] = s2[i].replace("n[[", "");
                maker.getNote().clear().append(translateSpecialCharacters(s2[i]));
                code.getHeader().appendRaw("maker.getNote().clear().append(\"" + s2[i] + "\");\n");
            } else if (s2[i].startsWith("img[[") || s2[i].startsWith(" img[[")) {
                s2[i] = s2[i].replace("img[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withImage(s2[i]);
                cImg = s2[i];
                code.getHeader().appendRaw("maker.withImage(\"" + s2[i] + "\");\n");
            } else if (s2[i].startsWith("nimg[[") || s2[i].startsWith(" nimg[[")) {
                s2[i] = s2[i].replace("nimg[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withFooterIcon(s2[i]);
                cNote = s2[i];
                code.getHeader().appendRaw("maker.withFooterIcon(\"" + s2[i] + "\");\n");
            } else if (s2[i].startsWith("a[[") || s2[i].startsWith(" a[[")) {
                s2[i] = s2[i].replace("a[[", "");
                maker.getAuthorName().append(translateSpecialCharacters(s2[i]));
                code.getHeader().appendRaw("maker.getAuthorName().append(\"" + s2[i] + "\");\n");
            } else if (s2[i].startsWith("aimg[[") || s2[i].startsWith(" aimg[[")) {
                s2[i] = s2[i].replace("aimg[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withAuthorIcon(s2[i]);
                cAuth = s2[i];
                code.getHeader().appendRaw("maker.withAuthorIcon(\"" + s2[i] + "\");\n");
            } else if (s2[i].startsWith("fp[[") || s2[i].startsWith(" fp[[")) {
                s2[i] = s2[i].replace("fp[[", "");
                String[] sfp = s2[i].split(",,,");
                maker.getNewFieldPart().withBoth(translateSpecialCharacters(sfp[0]), translateSpecialCharacters(sfp[1])).withInline(false);
                code.getHeader().appendRaw("maker.getNewFieldPart().withBoth(\"" + sfp[0] + "\", \"" + cleanString(sfp[1]) + "\").withInline(false);\n");
            } else if (s2[i].startsWith("fpi[[") || s2[i].startsWith(" fpi[[")) {
                s2[i] = s2[i].replace("fpi[[", "");
                String[] sfp = s2[i].split(",,,");
                maker.getNewFieldPart().withBoth(translateSpecialCharacters(sfp[0]), translateSpecialCharacters(sfp[1])).withInline(true);
                code.getHeader().appendRaw("maker.getNewFieldPart().withBoth(\"" + sfp[0] + "\", \"" + cleanString(sfp[1]) + "\").withInline(true);\n");
            } else if (s2[i].startsWith("thumb[[") || s2[i].startsWith(" thumb[[")) {
                s2[i] = s2[i].replace("thumb[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                maker.withThumb(s2[i]);
                cThumb = s2[i];
                code.getHeader().appendRaw("maker.withThumb(\"" + s2[i] + "\");\n");
            } else if (s2[i].startsWith("c[[") || s2[i].startsWith(" c[[")) {
                s2[i] = s2[i].replace("c[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                String[] rgb = s2[i].split(",");
                maker.withColor(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                code.getHeader().appendRaw("maker.withColor(new Color(" + rgb[0] + ", " + rgb[1] + ", " + rgb[2] + "));\n");
            } else if (s2[i].startsWith("r[[") || s2[i].startsWith(" r[[")) {
                s2[i] = s2[i].replace("r[[", "");
                s2[i] = s2[i].replaceAll(" ", "");
                r = s2[i].split(",");
            } else if (s2[i].startsWith("opt[[") || s2[i].startsWith(" opt[[")) {
                s2[i] = s2[i].replace("opt[[", "");
                s2[i] = s2[i].toLowerCase();
                s2[i] = s2[i].replaceAll(" ", "");
                String[] opt = s2[i].split(",");
                for (int j = 0; j < opt.length; j++){
                    if (opt[j].contains("userauthor")) {
                        maker.withAuthor(guser.getUser());
                        code.getHeader().appendRaw("maker.withAuthor(user);\nmaker.withAuthorIcon(user.getAvatarURL());\n");
                    } else if (opt[j].contains("timestamp")) {
                        maker.withTimestamp(System.currentTimeMillis());
                        code.getHeader().appendRaw("maker.withTimestamp(System.currentTimeMillis());\n");
                    } else if (opt[j].contains("code")) {
                        codeSend = true;
                    } else if (opt[j].contains("deletecommand")) {
                        msg.delete();
                    } else if (opt[j].contains("editorigin")) {
                        if (ConfigHandler.getSetting(LastEmbedConfig.class, guser) != null) {
                            if ((System.currentTimeMillis() - ConfigHandler.getSetting(LastEmbedMillisConfig.class, guser))/60_000L < 15) {
                                maker.setMessage(ConfigHandler.getSetting(LastEmbedConfig.class, guser));
                            }
                        }
                    } else if (opt[j].contains("usercolor")) {
                        maker.withColor(guser.getUser());
                        code.getHeader().appendRaw("maker.withColor(user);\n");
                    } else if (opt[j].contains("thumbcolor")) {
                        maker.withColor(cThumb);
                        code.getHeader().appendRaw("maker.withColor(\"" + cThumb + "\");\n");
                    } else if (opt[j].contains("imgcolor")) {
                        maker.withColor(cImg);
                        code.getHeader().appendRaw("maker.withColor(\"" + cImg + "\");\n");
                    } else if (opt[j].contains("authcolor")) {
                        maker.withColor(cAuth);
                        code.getHeader().appendRaw("maker.withColor(\"" + cAuth + "\");\n");
                    } else if (opt[j].contains("notecolor")) {
                        maker.withColor(cNote);
                        code.getHeader().appendRaw("maker.withColor(\"" + cNote + "\");\n");
                    }
                }
            }
        }
        maker.send();
        ConfigHandler.setSetting(LastEmbedConfig.class, guser, maker.sentMessage());
        ConfigHandler.setSetting(LastEmbedMillisConfig.class, guser, System.currentTimeMillis());
        if (codeSend) {
            code.getHeader().appendRaw("```");
            code.send();
        }

        //add reactions for the sent message if available
        if (r != null) {
            for (int i = 0; i < r.length; i++){
                r[i] = r[i].replaceAll("<", "");
                r[i] = r[i].replaceAll(">", "");
                maker.sentMessage().addReaction(r[i]);
            }
        }
    }

    //help command override
    @Override
    public String getHelp() {

        //command description:
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