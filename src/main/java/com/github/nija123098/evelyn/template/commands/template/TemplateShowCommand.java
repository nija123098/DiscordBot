package com.github.nija123098.evelyn.template.commands.template;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.TemplateHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 8/9/2017.
 */
public class TemplateShowCommand extends AbstractCommand {
    public TemplateShowCommand() {
        super(TemplateCommand.class, "show", null, null, null, "Shows registered templates for a KeyPhrase");
    }
    @Command
    public void command(@Argument KeyPhrase keyPhrase, @Context(softFail = true) Guild guild, MessageMaker maker){
        AtomicInteger integer = new AtomicInteger(-1);
        if (!keyPhrase.getDefinition().getAvailableContext().contains(ContextRequirement.GUILD)) guild = null;// lets all ignore the fact that I wrote the pagination code
        TemplateHandler.getTemplates(keyPhrase, guild).forEach(template -> maker.appendRaw("\n" + integer.incrementAndGet() + "   " + template.getText()));
    }
}
