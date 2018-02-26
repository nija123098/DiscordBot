package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.moderation.logging.BotChannelConfig;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class AnnounceCommand extends AbstractCommand {
    public AnnounceCommand() {
        super("announce", ModuleLevel.BOT_ADMINISTRATIVE, null, null, null);
    }
    @Command
    public void command(@Argument(info = "The stuff to say") String text, MessageMaker maker, User user, Message message) {

        //configure maker
        maker.withAuthorIcon(DiscordClient.getOurUser().getAvatarURL()).getAuthorName().clear().appendRaw("Evelyn Announcement");
        String[] textWithoutUrl;
        if (!message.getAttachments().isEmpty()){
            maker.withImage(message.getAttachments().get(0).getUrl());
        } else if (text.contains("|")){
            textWithoutUrl = text.split("\\|",2);
            maker.withImage(textWithoutUrl[0]);
            text = textWithoutUrl[1];
        }
        maker.withThumb(ConfigProvider.URLS.announceThumb());
        maker.withColor(new Color(175, 30,5));
        maker.withAutoSend(false).mustEmbed();

        //format text to use \n
        text = text.replace("\\n","\n");

        //check if the text would use a field part
        if (text.contains(";")){
            String[] textWithSplit = text.split(";",2);
            maker.forceCompile().getHeader().clear().appendRaw("\u200B");
            maker.forceCompile().getNewFieldPart().withBoth(textWithSplit[0],textWithSplit[1] + "\n\n- " + user.getName()).getValue().appendRaw("\n\nClick " + FormatHelper.embedLink("here", ConfigProvider.URLS.discordInviteUrl()) + " to join the support server.");
        }else {
            maker.forceCompile().getFooter().clear().appendRaw("Click " + FormatHelper.embedLink("here", ConfigProvider.URLS.discordInviteUrl()) + " to join the support server.");
            maker.forceCompile().getHeader().clear().appendRaw("\u200B\n" + text + "\n\n- " + user.getName());
        }

        //confirm message with user
        //if no send
        maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException IGNORE){ }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

        }));

        //if send
        maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException IGNORE){ }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

            //announce the message on all servers
            Set<Channel> channels = DiscordClient.getGuilds().stream().map((guild -> ConfigHandler.getSetting(BotChannelConfig.class, guild))).collect(Collectors.toSet());
            channels.remove(null);
            maker.clearMessage();
            MessageMaker maker2 = maker;
            maker2.withoutReactionBehavior("green_tick");
            for (Channel channel : channels) {
                if (channel.canPost()){
                    maker2.forceCompile().withChannel(channel).send();
                    maker2.forceCompile().clearMessage();
                }
            }
        }));
        maker.forceCompile().send();

    }

    //help command override usages
    @Override
    public String getUsages() {

        //command usage:
        return
                "#  announce <text> // Send <text> to all servers\n" +
                "#  announce <title;text> // Send <title;text> to all servers\n" +
                "#  announce <url[SPACE]|title;text> // Send <title;text> with image to all servers";
    }

    //help command override description
    @Override
    public String getHelp() {

        //command description:
        return
                "#  Attaching an image to the command message will include it in the announcement\n\n#  Format Help:\n// \\n: Go to next line\n// [text](link): Add a link to a piece of text\n// *text*: Italics\n// **text**: Bold\n// __text__: Underline\n// ~~text~~: Strikethrough";
    }

}
