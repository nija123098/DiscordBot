package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class DiscordCommand extends AbstractCommand {
    public DiscordCommand() {
        super("guild", ModuleLevel.INFO, "discord", null, "Brings you to my support server");
    }
    @Command
    public void command(MessageMaker maker){
        maker.withDeleteDelay(30_000L).append("If you need help or have questions/suggestions feel free to drop by at ").appendRaw("https://discord.gg/5jqGXQH");
    }
}
