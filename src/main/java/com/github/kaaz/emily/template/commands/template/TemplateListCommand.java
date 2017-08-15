package com.github.kaaz.emily.template.commands.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.template.KeyPhrase;

import java.util.stream.Stream;

/**
 * Made by nija123098 on 8/8/2017.
 */
public class TemplateListCommand extends AbstractCommand {
    public TemplateListCommand() {
        super(TemplateCommand.class, "list", null, null, null, "Lists all template key phrases");
    }
    @Command
    public static void command(MessageMaker maker){
        maker.getTitle().append("Template KeyPhrases");
        Stream.of(KeyPhrase.values()).forEach(keyPhrase -> maker.getNewListPart().appendRaw(keyPhrase.name()));
    }
}