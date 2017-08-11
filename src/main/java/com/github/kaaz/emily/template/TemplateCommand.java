package com.github.kaaz.emily.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.ContextRequirement;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.commands.HelpCommand;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 8/8/2017.
 */
public class TemplateCommand extends AbstractCommand {
    public TemplateCommand() {
        super("template", ModuleLevel.ADMINISTRATIVE, null, null, "Displays the help for it's sub-commands");
    }
    @Command
    public void command(MessageMaker maker, User user, Channel channel, Guild guild){
        HelpCommand.command(CommandHandler.getCommand(TemplateCommand.class), maker, user, channel, guild, null, "");
    }
    public static void checkPerms(User user, Guild guild, KeyPhrase keyPhrase){
        if (guild == null){
            if (!BotRole.BOT_ADMIN.hasRequiredRole(user, null)) throw new PermissionsException("You must be a BOT_ADMIN to edit global templates, do this in a server to edit your templates");
        }else{
            if (!keyPhrase.getAvailableContext().contains(ContextRequirement.GUILD)) throw new PermissionsException("You can't edit that template here, it is a global template");
        }
    }
}
