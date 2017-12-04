package com.github.nija123098.evelyn.perms.configs.specialperms;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.launcher.Launcher;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildSpecialPermsConfig extends AbstractConfig<SpecialPermsContainer, Guild> {
    private static String RESET_ENDING;
    public GuildSpecialPermsConfig() {
        super("guild_special_perms", "", ConfigCategory.MODERATION, (SpecialPermsContainer) null, "Changes the permissions on Evelyn commands");
        Launcher.registerStartup(() -> RESET_ENDING = DiscordClient.getOurUser().getID() + "> reset yesimsure");
    }
    @EventListener
    public void handle(DiscordMessageReceived event){
        if (!event.getChannel().isPrivate() && event.getMessage().getContent().endsWith(RESET_ENDING)) this.setValue(event.getGuild(), null, false);
    }
}
