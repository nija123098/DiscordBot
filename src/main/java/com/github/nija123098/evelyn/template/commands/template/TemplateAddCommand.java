package com.github.nija123098.evelyn.template.commands.template;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.TemplateHandler;

import java.awt.*;

/**
 * Made by nija123098 on 8/8/2017.
 */
public class TemplateAddCommand extends AbstractCommand {
    public TemplateAddCommand() {
        super(TemplateCommand.class, "add", null, null, null, "Adds a template");
    }
    @Command
    public void command(@Argument KeyPhrase keyPhrase, @Context(softFail = true) Guild guild, @Argument(info = "text") String s, User user, MessageMaker maker){
        TemplateCommand.checkPerms(user, guild, keyPhrase);
        TemplateHandler.addTemplate(keyPhrase, guild, s);
        maker.appendRaw("You've added " + s + " to " + keyPhrase).mustEmbed().withColor(new Color(0, 206, 209));
    }
    @Override
    public BotRole getBotRole() {
        return BotRole.GUILD_TRUSTEE;
    }
}
