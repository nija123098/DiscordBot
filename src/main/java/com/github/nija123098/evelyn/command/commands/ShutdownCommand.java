package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextPack;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.information.subsription.SubscriptionLevel;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;

import java.util.Collections;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class ShutdownCommand extends AbstractCommand {
    public ShutdownCommand() {
        super("reboot", ModuleLevel.BOT_ADMINISTRATIVE, "restart, shutdown, logout", null, "Restarts the bot");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer val, String remaining, ContextPack pack){
        remaining = remaining.toLowerCase();
        if (remaining.contains("now")) System.exit(val == null ? -1 : val);
        Template template = TemplateHandler.getTemplate(KeyPhrase.REBOOT_NOTIFICATION, null, Collections.emptyList());
        SubscriptionLevel.BOT_STATUS.send(new MessageMaker((Channel) null).append(template == null ? "I'm going to go reboot" : template.interpret(pack)));
        Launcher.shutdown(remaining.contains("c") ? null : val == null ? 0 : val, remaining.contains("q") ? 5_000 : 30_000, !remaining.contains("s"));
    }
}
