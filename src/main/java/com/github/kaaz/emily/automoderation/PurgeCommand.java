package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeCommand extends AbstractCommand {
    public PurgeCommand() {
        super("purge", ModuleLevel.ADMINISTRATIVE, null, null, "Deletes old messages");
        this.okOnSuccess = false;
    }
    @Command
    public void command(@Argument Integer count, Channel channel){
        MessageDeleteService.delete(channel.getMessages(count + 1));
    }
}
