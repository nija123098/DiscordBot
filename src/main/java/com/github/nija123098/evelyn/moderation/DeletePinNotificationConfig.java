package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessagePin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.util.CacheHelper;

import static com.github.nija123098.evelyn.config.ConfigCategory.GUILD_PERSONALIZATION;
import static com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient.getOurUser;
import static com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission.MANAGE_MESSAGES;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DeletePinNotificationConfig extends AbstractConfig<Boolean, Guild> {
    private static DeletePinNotificationConfig config;
    private static final CacheHelper.ContainmentCache<DiscordMessagePin> PIN_ENTRIES = new CacheHelper.ContainmentCache<>(5_000);
    private static final CacheHelper.ContainmentCache<DiscordMessageReceived> MESSAGE_ENTRIES = new CacheHelper.ContainmentCache<>(5_000);

    public DeletePinNotificationConfig() {
        super("delete_pin_notification", "Delete Pin Message", GUILD_PERSONALIZATION, true, "Deletes the <user> has pined a message notification");
        config = this;
    }

    // @EventListener
    public static synchronized void handle(DiscordMessageReceived event) {
        if (!event.getMessage().getContent().isEmpty() || !config.getValue(event.getGuild()) || !MANAGE_MESSAGES.hasPermission(event.getAuthor(), event.getGuild()) || !MANAGE_MESSAGES.hasPermission(getOurUser(), event.getGuild()))
            return;
        for (DiscordMessagePin message : PIN_ENTRIES) {
            if (event.getAuthor().equals(message.getAuthor())) {
                event.getMessage().delete();
                PIN_ENTRIES.remove(message);
                return;
            }
        }
        MESSAGE_ENTRIES.add(event);
    }

    @EventListener
    public synchronized void handle(DiscordMessagePin event) {
        if (!this.getValue(event.getGuild()) || !MANAGE_MESSAGES.hasPermission(event.getAuthor(), event.getGuild()) || !MANAGE_MESSAGES.hasPermission(getOurUser(), event.getGuild()))
            return;
        Channel channel = event.getChannel();
        for (DiscordMessageReceived send : MESSAGE_ENTRIES) {
            if (send.getChannel().equals(channel)) {
                send.getMessage().delete();
                MESSAGE_ENTRIES.remove(send);
                return;
            }
        }
        PIN_ENTRIES.add(event);
    }
}
