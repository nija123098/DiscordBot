package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.*;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;
import javafx.util.Pair;

/**
 * Made by nija123098 on 4/30/2017.
 */
public class ChangeContextCommand extends AbstractCommand {
    public ChangeContextCommand() {
        super("changecontext", BotRole.BOT_ADMIN, ModuleLevel.NONE, null, null, "Changes the context of the command that follows.  End context deceleration with \"command\"");
    }
    @Command
    public void command(@Context(softFail = true) String s, @Context(softFail = true) User user, @Context(softFail = true) Shard shard, @Context(softFail = true) Channel channel, @Context(softFail = true) Guild guild, @Context(softFail = true) Message message, @Context(softFail = true) Reaction reaction){
        Object[] contexts = new Object[]{user, shard, channel, guild, message, reaction};
        String next;
        while (!(next = s.split(" ")[0]).equalsIgnoreCase("command")){
            try {
                ContextRequirement requirement = ContextRequirement.valueOf(next);
                s = FormatHelper.trimFront(s.substring(next.length()));
                Pair<?, Integer> pair = InvocationObjectGetter.convert(requirement.getType(), user, shard, channel, guild, message, reaction, s);
                contexts[requirement.ordinal()] = pair.getKey();
                s = s.substring(pair.getValue());
            } catch (IllegalArgumentException e){
                throw new ArgumentException("Unrecognized context: " + next, e);
            }
            s = FormatHelper.trimFront(s);
        }
        s = FormatHelper.trimFront(s.substring(7));
        // if (user.equals(contexts[0]) || !BotRole.BOT_ADMIN.hasRequiredRole(user, null) || BotRole.BOT_ADMIN.hasRequiredRole(((User) contexts[0]), null)) throw new PermissionsException("You may not impersonate that user");// todo check permissions
        Pair<AbstractCommand, String> ret = CommandHandler.getMessageCommand(s);
        if (ret == null) throw new ArgumentException("Unrecognized command: " + s);
        ret.getKey().invoke((User) contexts[0], (Shard) contexts[1], (Channel) contexts[2], (Guild) contexts[3], (Message) contexts[4], (Reaction) contexts[5], ret.getValue());
    }
}
