package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextPack;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.information.subsription.SubscriptionLevel;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;

import java.util.Collections;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ShutdownCommand extends AbstractCommand {
    public ShutdownCommand() {
        super("shutdown", ModuleLevel.BOT_ADMINISTRATIVE, "kill", null, "Shuts down the bot");
    }
    @Command
    public void command(ContextPack pack, MessageMaker maker) {
        maker.appendRaw("The bot will now shutdown.\n Please note that you will need to **Manually** start the bot again.");
        Template template = TemplateHandler.getTemplate(KeyPhrase.REBOOT_NOTIFICATION, null, Collections.emptyList());
        SubscriptionLevel.BOT_STATUS.send(new MessageMaker((Channel) null).append(template == null ? "I'm going to go reboot" : template.interpret(pack)));
        Launcher.shutdown(1, 0, false);
    }
}
