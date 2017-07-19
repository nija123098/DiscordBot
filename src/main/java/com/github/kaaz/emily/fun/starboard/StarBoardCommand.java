package com.github.kaaz.emily.fun.starboard;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarBoardCommand extends AbstractCommand {
    private static final Map<String, MessageMaker> MAP = new ConcurrentHashMap<>();
    public StarBoardCommand() {
        super("star", ModuleLevel.FUN, null, "star", "Vote to put a command on the star board");
    }
    @Command
    public void command(Message message, Reaction reaction, MessageMaker m, Guild guild){
        if (message.getAuthor().equals(DiscordClient.getOurUser())) return;
        Channel channel = ConfigHandler.getSetting(StarBoardLocationConfig.class, guild);
        if (channel == null) {
            m.append("Star board isn't setup in this guild.  Make a channel named star_board");
            return;
        }
        if (reaction == null) {
            m.append("You can only use this command by reacting to other messages");
            return;
        }
        StarLevel starLevel = StarLevel.level(reaction.getCount(), guild);
        MessageMaker maker = MAP.computeIfAbsent(message.getID(), s -> new MessageMaker(channel)
                .withAuthorIcon(message.getAuthor().getAvatarURL())
                .append(message.getContent()));
        maker.getAuthorName().clear().appendRaw(message.getAuthor() + " " + starLevel.getEmoticon() + " " + message.getChannel().mention());
        maker.withColor(starLevel.getColor()).forceCompile();
        if (!message.getAttachments().isEmpty()) maker.withImage(message.getAttachments().get(0).getUrl());
    }
}
