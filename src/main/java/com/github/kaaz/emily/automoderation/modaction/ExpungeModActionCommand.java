package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class ExpungeModActionCommand extends AbstractCommand {
    public ExpungeModActionCommand() {
        super(ModActionCommand.class, "expunge", "expunge", null, "e", "Totally eradicates a user's history on a server");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "The user to be kicked") User target, @Argument(optional = true, info = "The reason") String reason){
        guild.banUser(user);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.BAN, target, user, reason);
        MessageMaker maker = new MessageMaker(user).getTitle().append("Name Removal").getMaker()
                .append("The following names were used by the user while present in the guild.  " +
                        "Select the corresponding letter to remove the name from all message history.  " +
                        "Press the ok reaction to finalize these decisions.  " +
                        "This is irreversible, chose wisely.");

    }
}
