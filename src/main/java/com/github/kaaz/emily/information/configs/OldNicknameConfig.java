package com.github.kaaz.emily.information.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordNicknameChange;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class OldNicknameConfig extends AbstractConfig<Set<String>, GuildUser> {
    public OldNicknameConfig() {
        super("old_nickname_config", BotRole.GUILD_TRUSTEE, new HashSet<>(0), "A list of old nicknames used by a user");
    }
    @EventListener
    public void handle(DiscordNicknameChange event){
        if (event.getNewUsername() == null) return;
        this.alterSetting(GuildUser.getGuildUser(event.getGuild(), event.getUser()), strings -> strings.add(event.getNewUsername()));
    }
    @EventListener
    public void handle(DiscordDataReload reload){
        ConfigHandler.getTypeInstances(GuildUser.class).forEach(guildUser -> {
            Set<String> names = this.getValue(guildUser);
            String name = guildUser.getUser().getDisplayName(guildUser.getGuild());
            if (!names.contains(name)){
                names.add(name);
                this.setValue(guildUser, names);
            }
        });
    }
}
