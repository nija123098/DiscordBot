package com.github.nija123098.evelyn.moderation.linkedgames;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;

public class LinkedGamesRemoveCommand extends AbstractCommand {
    public LinkedGamesRemoveCommand() {
        super(LinkedGamesCommand.class, "remove", null, null, null, "Removes a game from the linked games list");
    }
    @Command
    public void command(@Argument(info = "the game") String s, Guild guild){
        ConfigHandler.alterSetting(GuildLinkedGamesConfig.class, guild, strings -> {
            if (!strings.contains(s)) throw new ArgumentException("That game isn't linked though");
            strings.remove(s);
        });
    }
}
