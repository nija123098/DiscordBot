package com.github.kaaz.emily.perms.configs.specialperms;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/13/2017.
 */
public class GuildSpecialPermsConfig extends AbstractConfig<SpecialPermsContainer, Guild> {
    private static String RESET_ENDING;
    public GuildSpecialPermsConfig() {
        super("guild_special_perms", BotRole.GUILD_TRUSTEE, null, "Changes the permissions on Emily commands");
        Launcher.registerStartup(() -> RESET_ENDING = DiscordClient.getOurUser().getID() + "> reset yesimsure");
    }
    @EventListener
    public void handle(DiscordMessageReceived event){
        if (!event.getChannel().isPrivate() && event.getMessage().getContent().endsWith(RESET_ENDING)) this.setValue(event.getGuild(), null);
    }
}
