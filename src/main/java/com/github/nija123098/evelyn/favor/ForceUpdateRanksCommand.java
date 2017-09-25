package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

public class ForceUpdateRanksCommand extends AbstractCommand  {
    public ForceUpdateRanksCommand() {
        super(RanksCommand.class, "forceupdate", null, null, null, "Forces all ranks on a server to update");
    }

    @Command
    public void command(Guild guild){
        guild.getUsers().stream().map(user -> GuildUser.getGuildUser(guild, user)).forEach(guildUser -> FavorChangeEvent.process(guildUser, () -> {}));
    }

    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        return clazz.equals(Guild.class) ? 604_800_000 : super.getCoolDown(clazz);// 7 days
    }
}
