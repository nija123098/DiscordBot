package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextPack;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.information.subsription.SubscriptionLevel;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;

import java.util.Collections;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class RestartCommand extends AbstractCommand {
    public RestartCommand() {
        super("reboot", ModuleLevel.BOT_ADMINISTRATIVE, "restart", null, "Restarts the bot");
    }
    @Command
    public void command(ContextPack pack, MessageMaker maker) {
        maker.append("I am going to start the latest version of the bot and then shutdown this instance when the new one is online.");
        Template template = TemplateHandler.getTemplate(KeyPhrase.REBOOT_NOTIFICATION, null, Collections.emptyList());
        SubscriptionLevel.BOT_STATUS.send(new MessageMaker((Channel) null).append(template == null ? "I'm going to go reboot" : template.interpret(pack)));
        ExecuteShellCommand.commandToExecute(ConfigProvider.BOT_SETTINGS.startCommand(), ConfigProvider.BOT_SETTINGS.botFolder());
    }
}
