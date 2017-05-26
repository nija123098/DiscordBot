package com.github.kaaz.emily.information.subsription;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/24/2017.
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
        CommandHandler.getCommand(SubscribeInfoCommand.class).getNames().forEach(System.out::println);
    }
}
