package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class WarningsCommand extends AbstractCommand {
    public WarningsCommand() {
        super("warnings", ModuleLevel.ADMINISTRATIVE, null, null, "Lists the warnings a user has");
    }
    @Command
    public void command(@Argument GuildUser user, MessageMaker maker) {
        List<String> list = ConfigHandler.getSetting(WarningLogConfig.class, user);
        maker.withAuthor(user.getUser());
        if (list.isEmpty()) {
            maker.append("That user has no previous warnings!  Add one!  " + EmoticonHelper.getChars("smiling_imp", false));
            return;
        }
        list.forEach(s -> maker.getNewListPart().appendRaw(s));
    }
}
