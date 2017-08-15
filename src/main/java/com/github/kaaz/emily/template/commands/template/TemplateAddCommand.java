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
public class TemplateAddCommand extends AbstractCommand {
    public TemplateAddCommand() {
        super(TemplateCommand.class, "add", null, null, null, "Adds a template");
    }
    @Command
    public void command(@Argument KeyPhrase keyPhrase, @Context(softFail = true) Guild guild, @Argument(info = "text") String s, User user){
        TemplateCommand.checkPerms(user, guild, keyPhrase);
        TemplateHandler.addTemplate(keyPhrase, guild, s);
    }
    @Override
    public BotRole getBotRole() {
        return BotRole.GUILD_TRUSTEE;
    }
}
