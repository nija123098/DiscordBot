package com.github.nija123098.evelyn.information.subsription;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SubscribeInfoCommand extends AbstractCommand {
    public SubscribeInfoCommand() {
        super(SubscribeCommand.class, "info", null, null, null, "Shows the current subscriptions for the channel");
    }
    @Command
    public static void command(@Argument(optional = true, replacement = ContextType.NONE, info = "The subscription to get info for") SubscriptionLevel level, MessageMaker maker){
        if (level != null) maker.append("Info on ").appendRaw(level.name() + ":\n").append(level.getInfo());
        else {
            maker.getTitle().append("All Possible Subscriptions");
            for (SubscriptionLevel l : SubscriptionLevel.values()){
                maker.getNewListPart().appendRaw(l.name() + ": ").append(l.getInfo());
            }
        }
    }
}
