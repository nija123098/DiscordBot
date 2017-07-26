package com.github.kaaz.emily.automoderation.teams;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 7/23/2017.
 */
public class TeamMakeCommand extends AbstractCommand {
    public TeamMakeCommand() {
        super(TeamCommand.class, "make", null, null, null, "Makes a team");
    }
    @Override
    public BotRole getBotRole() {
        return BotRole.GUILD_TRUSTEE;
    }
    @Command
    public void command(@Argument(info = "team captain") User user, @Argument(info = "team name") String s, Guild guild){
        guild.createRole().changeName("");
        ConfigHandler.alterSetting(GuildTeamsConfig.class, guild, strings -> strings.add(s));
    }
}
