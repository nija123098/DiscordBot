package com.github.kaaz.emily.fun.starboard;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;

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
