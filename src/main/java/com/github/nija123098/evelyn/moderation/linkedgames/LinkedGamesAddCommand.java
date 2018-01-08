package com.github.nija123098.evelyn.moderation.linkedgames;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;

public class LinkedGamesAddCommand extends AbstractCommand {
    public LinkedGamesAddCommand() {
        super(LinkedGamesCommand.class, "add", null, null, null, "Adds a game to the linked games list");
    }
    @Command
    public void command(@Argument(info = "the game") String s, Guild guild){
        ConfigHandler.alterSetting(GuildLinkedGamesConfig.class, guild, strings -> {
            if (FormatHelper.reduce(strings).contains(FormatHelper.reduce(s))) throw new ArgumentException("That game is already linked though");
            strings.add(s);
        });
    }
}
