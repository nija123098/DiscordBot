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
import com.github.nija123098.evelyn.util.CareLess;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class AnnounceCommand extends AbstractCommand {
    public AnnounceCommand() {
        super("announce", ModuleLevel.BOT_ADMINISTRATIVE, null, null, null);
    }
    @Command
    public void command(@Argument(info = "The stuff to say") String text, MessageMaker maker, User user, Message message) {
        // configure maker
        maker.withAuthorIcon(DiscordClient.getOurUser().getAvatarURL()).getAuthorName().clear().appendRaw("Evelyn Announcement");
        String[] textWithoutUrl;
        if (!message.getAttachments().isEmpty()) {
            maker.withImage(message.getAttachments().get(0).getUrl());
        } else if (text.contains("|")) {
            textWithoutUrl = text.split("\\|",2);
            maker.withImage(textWithoutUrl[0]);
            text = textWithoutUrl[1];
        }
        maker.withThumb(ConfigProvider.URLS.announceThumb());
        maker.withColor(new Color(175, 30,5));
        maker.withAutoSend(false);

        // format text to use \n
        text = text.replace("\\n","\n");

        // check if the text would use a field part
        if (text.contains(";")) {
            String[] textWithSplit = text.split(";",2);
            maker.forceCompile().getHeader().clear().appendRaw("\u200B");
            maker.forceCompile().getNewFieldPart().withBoth(textWithSplit[0], textWithSplit[1] + "\n\n- " + user.getName()).getValue().appendRaw("\n\nClick ").appendEmbedLink("here", ConfigProvider.URLS.discordInviteUrl()).append(" to join the support server.");
        }else {
            maker.forceCompile().getFooter().clear().appendRaw("Click ").appendEmbedLink("here", ConfigProvider.URLS.discordInviteUrl()).append(" to join the support server.");
            maker.forceCompile().getHeader().clear().appendRaw("\u200B\n" + text + "\n\n- " + user.getName());
        }

        // confirm message with user
        // denied
        maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
            // remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) { }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));
        }));

        // confirmed
        maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

            // remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) { }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

            // send message to all servers
            Set<Channel> channels = DiscordClient.getGuilds().stream().map((guild -> ConfigHandler.getSetting(BotChannelConfig.class, guild))).filter(Channel::canPost).collect(Collectors.toSet());
            channels.remove(null);
            new MessageMaker(message.getChannel()).append("Sending announcement to " + channels.size() + " guilds").send();
            maker.clearMessage();
            maker.withoutReactionBehavior("green_tick");
            for (Channel channel : channels) {
                CareLess.lessSleep(105);// respect the rate limit
                maker.forceCompile().withChannel(channel).send();
                maker.forceCompile().clearMessage();
            }
        }));
        maker.forceCompile().send();
    }

    @Override
    public String getUsages() {
        return "#  announce <text> // Send <text> to all servers\n" +
                "#  announce <title;text> // Send <title;text> to all servers\n" +
                "#  announce <url[SPACE]|title;text> // Send <title;text> with image to all servers";
    }

    @Override
    public String getHelp() {
        return "#  Attaching an image to the command message will include it in the announcement\n\n#  Format Help:\n// \\n: Go to next line\n// [text](link): Add a link to a piece of text\n// *text*: Italics\n// **text**: Bold\n// __text__: Underline\n// ~~text~~: Strikethrough";
    }

}
