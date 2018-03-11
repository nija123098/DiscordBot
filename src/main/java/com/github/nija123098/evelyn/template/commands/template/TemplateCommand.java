package com.github.nija123098.evelyn.template.commands.template;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.commands.HelpCommand;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.template.KeyPhrase;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TemplateCommand extends AbstractCommand {
    public TemplateCommand() {
        super("template", ModuleLevel.ADMINISTRATIVE, null, null, "Displays the help for it's sub-commands");
    }
    @Command
    public void command(MessageMaker maker, User user, Channel channel, Guild guild) {
        HelpCommand.command(CommandHandler.getCommand(TemplateCommand.class), maker, user, channel, guild, null, "");
    }
    public static void checkPerms(User user, Guild guild, KeyPhrase keyPhrase) {
        if (guild == null) {
            if (!BotRole.BOT_ADMIN.hasRequiredRole(user, null)) throw new PermissionsException("You must be a BOT_ADMIN to edit global templates, do this in a server to edit your templates");
        }else{
            if (!keyPhrase.getDefinition().getAvailableContext().contains(ContextRequirement.GUILD)) throw new PermissionsException("You can't edit that template here, it is a global template");
        }
    }
}
