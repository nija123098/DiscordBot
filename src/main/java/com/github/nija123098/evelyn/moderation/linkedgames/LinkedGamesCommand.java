package com.github.nija123098.evelyn.moderation.linkedgames;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class LinkedGamesCommand extends AbstractCommand {
    public LinkedGamesCommand() {
        super("linkedgames", ModuleLevel.ADMINISTRATIVE, "linked games, lg", null, "Shows the linked games of your server");
    }
    @Command
    public void command(MessageMaker maker, Guild guild) {
        maker.getTitle().append("Linked Games");
        Set<String> games = ConfigHandler.getSetting(GuildLinkedGamesConfig.class, guild);
        if (games.isEmpty()) maker.appendAlternate(false, "You have no linked games!  Add some by doing `", "@Evelyn linkedgames add myGame", "`");
        else games.forEach(s -> maker.getNewListPart().appendRaw(s));
    }
}
