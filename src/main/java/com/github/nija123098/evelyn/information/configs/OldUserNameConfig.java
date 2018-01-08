package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserNameChangeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class OldUserNameConfig extends AbstractConfig<Set<String>, User> {
    public OldUserNameConfig() {
        super("current_money", "old_name_config", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "A list of old names used by a user");
    }
    @EventListener
    public void handle(DiscordUserNameChangeEvent event){
        this.alterSetting(event.getUser(), strings -> {
            strings.add(event.getOldName());
            strings.add(event.getNewName());
        });
    }
}
