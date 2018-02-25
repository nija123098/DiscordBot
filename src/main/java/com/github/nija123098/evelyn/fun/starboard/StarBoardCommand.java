package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient.getOurUser;
import static com.github.nija123098.evelyn.fun.starboard.StarLevel.level;
import static com.github.nija123098.evelyn.util.NetworkHelper.isValid;
import static java.lang.System.currentTimeMillis;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StarBoardCommand extends AbstractCommand {
    private static final Map<String, MessageMaker> MAP = new ConcurrentHashMap<>();
    private static final long startupTime = currentTimeMillis();

    public StarBoardCommand() {
        super("star", FUN, null, "star, stars", "Vote to put a command on the star board");
    }

    @Command
    public void command(Message message, Reaction reaction, MessageMaker m, Guild guild, User user) {
        if (message.getAuthor().equals(getOurUser()) || user.equals(message.getAuthor())) return;
        Channel channel = getSetting(StarBoardLocationConfig.class, guild);
        if (channel == null)
            return;// m.append("Star board isn't setup in this guild.  Make a channel named star_board");
        if (reaction == null) {
            m.append("You can only use this command by reacting to other messages");
            return;
        }
        if (message.getTime() < startupTime) return;
        StarLevel starLevel = level(reaction.getCount(), guild);
        if (starLevel == null) return;
        MessageMaker maker = MAP.computeIfAbsent(message.getID(), s -> new MessageMaker(channel).withAuthorIcon(message.getAuthor().getAvatarURL()));
        if ((message.getContent().endsWith(".png") || message.getContent().endsWith(".gif")) && isValid(message.getContent()))
            maker.withImage(message.getContent());
        else maker.append(message.getMentionCleanedContent());
        maker.getAuthorName().clear().appendRaw(message.getAuthor().getDisplayName(guild) + " is a " + starLevel.getEmoticon() + " in " + message.getChannel().getName());
        maker.withColor(starLevel.getColor()).forceCompile().send();
        if (!message.getAttachments().isEmpty()) maker.withImage(message.getAttachments().get(0).getUrl());
    }

    @Override
    public boolean useReactions() {
        return true;
    }

    @Override
    public boolean shouldLog() {
        return false;
    }

    @Override
    public String getExample() {
        return "// Starlevel defaults:\n" +
                "SPARKLE   | 3\n" +
                "DWARF     | 5\n" +
                "YELLOW    | 7\n" +
                "SOLAR     | 10\n" +
                "EXPLOSIVE | 15";
    }
}
