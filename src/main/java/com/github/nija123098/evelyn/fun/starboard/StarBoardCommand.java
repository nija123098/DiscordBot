package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.util.NetworkHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarBoardCommand extends AbstractCommand {
    private static final Map<String, MessageMaker> MAP = new ConcurrentHashMap<>();
    private static final long startupTime = System.currentTimeMillis();
    public StarBoardCommand() {
        super("star", ModuleLevel.FUN, null, "star, stars", "Vote to put a command on the star board");
    }
    @Command
    public void command(Message message, Reaction reaction, MessageMaker m, Guild guild, User user){
        if (message.getAuthor().equals(DiscordClient.getOurUser()) || user.equals(message.getAuthor())) return;
        Channel channel = ConfigHandler.getSetting(StarBoardLocationConfig.class, guild);
        if (channel == null) return;// m.append("Star board isn't setup in this guild.  Make a channel named star_board");
        if (reaction == null) {
            m.append("You can only use this command by reacting to other messages");
            return;
        }
        if (message.getTime() < startupTime) return;
        StarLevel starLevel = StarLevel.level(reaction.getCount(), guild);
        if (starLevel == null) return;
        MessageMaker maker = MAP.computeIfAbsent(message.getID(), s -> new MessageMaker(channel).withAuthorIcon(message.getAuthor().getAvatarURL()));
        if ((message.getContent().endsWith(".png") || message.getContent().endsWith(".gif")) && NetworkHelper.isValid(message.getContent())) maker.withUrl(message.getContent());
        else maker.append(message.getContent());
        maker.getAuthorName().clear().appendRaw(message.getAuthor().getDisplayName(guild) + " is a " + starLevel.getEmoticon() + " in " + message.getChannel().getName());
        maker.withColor(starLevel.getColor()).forceCompile().send();
        if (!message.getAttachments().isEmpty()) maker.withImage(message.getAttachments().get(0).getUrl());
    }
}
