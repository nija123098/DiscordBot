package com.github.nija123098.evelyn.moderation.linkedgames;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class LinkedGamesCommand extends AbstractCommand {
    public LinkedGamesCommand() {
        super("linkedgames", ModuleLevel.ADMINISTRATIVE, "linked games, lg", null, "Shows the linked games of your server");
    }
    @Command
    public void command(MessageMaker maker, Guild guild){
        maker.getTitle().append("Linked Games");
        ConfigHandler.getSetting(GuildLinkedGamesConfig.class, guild).forEach(s -> maker.getNewListPart().appendRaw(s));
    }
}
