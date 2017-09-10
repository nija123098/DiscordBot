package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarBoardLevelCommand extends AbstractCommand {
    public StarBoardLevelCommand() {
        super(StarBoardCommand.class, "level", null, null, null, "Sets the minimum level requirement for a star level");
    }
    @Command
    public void command(@Argument StarLevel level, @Argument Integer integer, Guild guild){
        ConfigHandler.alterSetting(StarLevelRequirementConfig.class, guild, map -> map.put(level, integer));
    }
}
