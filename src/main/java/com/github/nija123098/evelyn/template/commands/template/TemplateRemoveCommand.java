package com.github.nija123098.evelyn.template.commands.template;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.TemplateHandler;

/**
 * @author nija123098
 * @since 1.0.0
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
