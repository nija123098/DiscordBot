package com.github.nija123098.evelyn.fun.tag;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class TagDeleteCommand extends AbstractCommand {
    public TagDeleteCommand() {
        super(TagCommand.class, "delete", "deleteuser", null, null, "Deletes a tag");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) User user, @Argument String s, Guild guild, MessageMaker maker){
        if (user == null && s.isEmpty()) throw new ArgumentException("Chose either a user or a tag for tag deletion");
        if (s.contains(" ")) throw new ArgumentException("Tags are all one name");
        if (user == null){
            ConfigHandler.alterSetting(TagConfig.class, guild, stringTagMap -> {
                if (stringTagMap.containsKey(s)) stringTagMap.remove(s);
                else throw new ArgumentException("No such tag called: " + s);
            });
        } else {
            if (!TagConfig.deleteOfUser(user, guild)) maker.append("That user doesn't own any tags");
        }
    }
}
