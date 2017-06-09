package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.*;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrentMoneyConfig extends AbstractConfig<Float, Configurable> {
    public CurrentMoneyConfig() {
        super("current_money", BotRole.BOT_ADMIN, 0F, "The amount of money a guild user has");
    }
    @EventListener
    public void exchange(ConfigValueChangeEvent<Object, Configurable> event){
        if (event.getConfig().getClass().equals(CurrentMoneyConfig.class)){
            Float value = ((Float) event.getNewValue()) - ((Float) event.getOldValue());
            if (event.getConfigurable() instanceof GuildUser){
                if (value > 0) ConfigHandler.changeSetting(CurrentMoneyConfig.class, ((GuildUser) event.getConfigurable()).getUser(), aFloat -> aFloat + value);
                if (value > 0) ConfigHandler.changeSetting(CurrentMoneyConfig.class, ((GuildUser) event.getConfigurable()).getGuild(), aFloat -> aFloat + value);
            }
        }
    }
}
