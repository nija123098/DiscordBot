package com.github.nija123098.evelyn.information.subsription;

import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.information.configs.SubscriptionsConfig;

/**
 * @author nija123098
 * @since 1.0.0
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
