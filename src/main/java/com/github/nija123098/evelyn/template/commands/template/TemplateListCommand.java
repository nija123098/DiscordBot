package com.github.nija123098.evelyn.template.commands.template;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.template.KeyPhrase;

import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
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
