package com.github.kaaz.emily.template.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.template.KeyPhrase;
import com.github.kaaz.emily.template.Template;

/**
 * Made by nija123098 on 4/23/2017.
 */
public class TemplateTestCommand extends AbstractCommand {
    public TemplateTestCommand() {
        super("template", ModuleLevel.NONE, "temp", null, "Used in testing templates with no arguments");
    }
    @Command
    public void command(MessageMaker maker, String s, User user, Shard shard, Message message, Guild guild, Channel channel, Reaction reaction){
        maker.append(new Template(s, KeyPhrase.TEST).interpret(user, shard, channel, guild, message, reaction));
    }
    @Override
    public boolean isTemplateCommand(){
        return false;
    }
}
