package com.github.kaaz.emily.information.subsription;

import com.github.kaaz.emily.command.annotations.LaymanName;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.information.configs.SubscriptionsConfig;

/**
 * Made by nija123098 on 5/24/2017.
 */
@LaymanName("Subscription")
public enum SubscriptionLevel {
    CODE_UPDATES("Updates related to the code of the bot, this may include minor changes"),
    BOT_VERSION("Updates on feature changes and additions to the bot"),
    BOT_STATUS("Updates to the status of the bot regarding restarts and maintenance times"),;
    private String info;
    SubscriptionLevel(String info) {
        this.info = info;
    }
    public String getInfo() {
        return this.info;
    }
    public void send(MessageMaker maker){
        ConfigHandler.getNonDefaultSettings(SubscriptionsConfig.class).forEach((channel, subscriptionLevels) -> {
            if (subscriptionLevels.contains(this)) maker.clearMessage().withChannel(channel).send();
        });
    }
}
