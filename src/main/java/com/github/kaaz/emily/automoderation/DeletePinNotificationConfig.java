package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordPermission;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessagePin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.MemoryManagementService;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class DeletePinNotificationConfig extends AbstractConfig<Boolean, Guild> {
    private static final List<DiscordMessagePin> PIN_ENTRIES = new MemoryManagementService.ManagedList<>(5_000);
    private static final List<DiscordMessageReceivedEvent> MESSAGE_ENTRIES = new MemoryManagementService.ManagedList<>(5_000);
    public DeletePinNotificationConfig() {
        super("delete_pin_notification", BotRole.GUILD_TRUSTEE, false, "Deletes the <user> has pined a message notification");
    }
    @EventListener
    public synchronized void handle(DiscordMessageReceivedEvent event){
        if (!event.getMessage().getContent().equals("") || !this.getValue(event.getGuild()) || !DiscordPermission.MANAGE_MESSAGES.hasPermission(event.getAuthor(), event.getGuild()) || !DiscordPermission.MANAGE_MESSAGES.hasPermission(DiscordClient.getOurUser(), event.getGuild())) return;
        AtomicReference<DiscordMessagePin> reference = new AtomicReference<>();
        PIN_ENTRIES.forEach(messages -> {
            if (reference.get() == null && event.getAuthor().equals(messages.getAuthor())){
                reference.set(messages);
                event.getMessage().delete();
            }
        });
        if (reference.get() != null) PIN_ENTRIES.remove(reference.get());
        else MESSAGE_ENTRIES.add(event);
    }
    @EventListener
    public synchronized void handle(DiscordMessagePin event){
        if (!this.getValue(event.getGuild()) || !DiscordPermission.MANAGE_MESSAGES.hasPermission(event.getAuthor(), event.getGuild()) || !DiscordPermission.MANAGE_MESSAGES.hasPermission(DiscordClient.getOurUser(), event.getGuild())) return;
        AtomicReference<DiscordMessageReceivedEvent> reference = new AtomicReference<>();
        MESSAGE_ENTRIES.forEach(pin -> {
            if (reference.get() == null && event.getAuthor().equals(pin.getAuthor())){
                reference.set(pin);
                pin.getMessage().delete();
            }
        });
        if (reference.get() != null) MESSAGE_ENTRIES.remove(reference.get());
        else PIN_ENTRIES.add(event);
    }
}
