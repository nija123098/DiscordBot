package com.github.kaaz.emily.information.subsription;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.information.configs.SubscriptionsConfig;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class SubscribeCommand extends AbstractCommand {
    public SubscribeCommand() {
        super("subscribe", ModuleLevel.INFO, "sub", null, "Subscribe this channel to certain events");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE, info = "The thing to subscribe to") SubscriptionLevel level, Channel channel, MessageMaker maker){
        if (level == null) SubscribeCurrentCommand.command(channel, maker);
        else{
            ConfigHandler.alterSetting(SubscriptionsConfig.class, channel, levels -> levels.add(level));
            maker.withOK();
        }
    }
}
