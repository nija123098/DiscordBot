package com.github.kaaz.emily.config.configs.global;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordGuildLeave;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordGuildUpdate;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class GuildNamesConfig extends AbstractConfig<Set<String>, GlobalConfigurable> {
    public GuildNamesConfig() {
        super("guild_names", BotRole.BOT_OWNER, new HashSet<>(),
                "The list of guild names for optimization which is automatically managed");
    }
    @Override
    public boolean shouldSave(){
        return false;
    }
    @EventListener
    public void handle(DiscordGuildJoin join){
        Set<String> set = ConfigHandler.getSetting(GuildNamesConfig.class, GlobalConfigurable.GLOBAL);
        set.add(join.getGuild().getName());
        ConfigHandler.setSetting(GuildNamesConfig.class, GlobalConfigurable.GLOBAL, set);
    }
    @EventListener
    public void handle(DiscordGuildLeave leave){
        Set<String> set = ConfigHandler.getSetting(GuildNamesConfig.class, GlobalConfigurable.GLOBAL);
        set.remove(leave.getGuild().getName());
        ConfigHandler.setSetting(GuildNamesConfig.class, GlobalConfigurable.GLOBAL, set);
    }
    @EventListener
    public void handle(DiscordGuildUpdate change){
        if (change.nameChange()){
            Set<String> set = ConfigHandler.getSetting(GuildNamesConfig.class, GlobalConfigurable.GLOBAL);
            set.remove(change.oldName());
            set.add(change.newName());
            ConfigHandler.setSetting(GuildNamesConfig.class, GlobalConfigurable.GLOBAL, set);
        }
    }
    @EventListener
    public void handle(DiscordDataReload reload){
        ConfigHandler.setSetting(GuildNamesConfig.class, GlobalConfigurable.GLOBAL, DiscordClient.getGuilds().stream().map(Guild::getName).collect(Collectors.toSet()));
    }
}
