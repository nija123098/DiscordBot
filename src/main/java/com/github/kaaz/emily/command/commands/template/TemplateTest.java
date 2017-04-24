package com.github.kaaz.emily.command.commands.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.template.KeyPhrase;
import com.github.kaaz.emily.template.Template;

/**
 * Made by nija123098 on 4/23/2017.
 */
public class TemplateTest extends AbstractCommand {
    public TemplateTest() {
        super(null, "template", "temp", null, null);
    }
    @Command
    public void command(MessageMaker maker, String s, User user, Shard shard, Message message, Guild guild, Channel channel, Reaction reaction){
        maker.append(new Template(s, KeyPhrase.TEST).interpret(user, shard, channel, guild, message, reaction));
    }
}
