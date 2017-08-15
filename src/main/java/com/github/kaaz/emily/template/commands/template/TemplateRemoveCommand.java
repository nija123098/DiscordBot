package com.github.kaaz.emily.template.commands.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.template.KeyPhrase;
import com.github.kaaz.emily.template.TemplateHandler;

/**
 * Made by nija123098 on 8/8/2017.
 */
public class TemplateRemoveCommand extends AbstractCommand {
    public TemplateRemoveCommand() {
        super(TemplateCommand.class, "remove", null, null, null, "Removes a template based on index");
    }
    @Command
    public void command(@Argument KeyPhrase keyPhrase, @Context(softFail = true) Guild guild, @Argument(info = "index") Integer i, User user){
        TemplateCommand.checkPerms(user, guild, keyPhrase);
        TemplateHandler.removeTemplate(keyPhrase, guild, i);
    }
    @Override
    public BotRole getBotRole() {
        return BotRole.GUILD_TRUSTEE;
    }
}
