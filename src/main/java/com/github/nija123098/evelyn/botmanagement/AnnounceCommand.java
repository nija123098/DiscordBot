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
import com.github.nija123098.evelyn.moderation.logging.BotChannelConfig;
import com.github.nija123098.evelyn.util.NetworkHelper;

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
    public void command(@Argument(info = "The stuff to say") String text, MessageMaker maker) {
        maker.withAuthorIcon(DiscordClient.getOurUser().getAvatarURL()).getAuthorName().clear().appendRaw("Evelyn Announcement");
        maker.withThumb(ConfigProvider.URLS.announceThumb());
        maker.withAutoSend(false);

        text = text.replace("\\n","\n");
        String[] split = text.split("|");
        for (int i = 0; i < split.length - 1; i++) {
            if (NetworkHelper.isValid(split[i])) {
                if (split[i].endsWith("png") || split[i].endsWith("svg") || split[i].endsWith("jpeg")) maker.withThumb(split[i]);
                else maker.withUrl(split[i]);
            }
            else maker.getTitle().append(split[i]);
        }
        maker.append(split[split.length - 1]);
        maker.send();
        Message message = maker.sentMessage();
        maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {
            maker.clearReactionBehaviors();
            Set<Channel> channels = DiscordClient.getGuilds().stream().map((guild -> ConfigHandler.getSetting(BotChannelConfig.class, guild))).filter(Channel::canPost).collect(Collectors.toSet());
            channels.remove(null);
            maker.clearMessage();
            channels.forEach(channel -> maker.clearMessage().withChannel(channel).send());
            message.addReaction("ok_hand");
        }));
    }

    @Override
    public String getUsages() {
        return  "#  announce [url]|[png link]|[title]|<text> // Send <text> to all servers";
    }

    @Override
    public String getHelp() {
        return "#  Attaching an image to the command message will include it in the announcement\n\n#  Format Help:\n// \\n: Go to next line\n// [text](link): Add a link to a piece of text\n// *text*: Italics\n// **text**: Bold\n// __text__: Underline\n// ~~text~~: Strikethrough";
    }

}
