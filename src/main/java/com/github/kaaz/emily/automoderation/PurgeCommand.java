package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeCommand extends AbstractCommand {
    public PurgeCommand() {
        super("purge", BotRole.GUILD_TRUSTEE, ModuleLevel.ADMINISTRATIVE, null, null, "Deletes old messages");
    }
    @Command
    public void command(@Argument(optional = true) Integer count, Channel channel){
        if (count == null) count = 100;
        if (count > 2500) count = 2500;
        MessageDeleteService.delete(channel.getMessages(count));
    }
}
