package com.github.kaaz.emily.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextRequirement;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        List<List<String>> body = new ArrayList<>();
        AtomicInteger integer = new AtomicInteger(-1);
        if (!keyPhrase.getAvailableContext().contains(ContextRequirement.GUILD)) guild = null;
        TemplateHandler.getTemplates(keyPhrase, guild).forEach(template -> body.add(Arrays.asList(integer.incrementAndGet() + "", template.getText())));
        maker.append("Templates for ").appendRaw(keyPhrase.name() + "\n");
        maker.appendRaw(FormatHelper.makeAsciiTable(Arrays.asList("Index", "template"), body, null));
    }
}
