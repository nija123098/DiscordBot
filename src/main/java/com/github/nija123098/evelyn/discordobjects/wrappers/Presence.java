package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import sx.blah.discord.handle.obj.IPresence;
import sx.blah.discord.handle.obj.StatusType;

import java.util.Map;
import java.util.Optional;

/**
 * Wraps a Discord4j {@link IPresence} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Presence {
    private static final Map<IPresence, Presence> MAP = new MemoryManagementService.ManagedMap<>(60000);
    public static Presence getPresence(IPresence presence){
        if (presence == null) return null;
        return MAP.computeIfAbsent(presence, p -> new Presence(presence));
    }
    private IPresence iPresence;
    private Presence(IPresence iPresence){
        this.iPresence = iPresence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Presence presence = (Presence) o;

        return iPresence != null ? iPresence.equals(presence.iPresence) : presence.iPresence == null;
    }

    @Override
    public int hashCode() {
        return this.iPresence.getPlayingText().isPresent() ? this.iPresence.getPlayingText().get().hashCode() : 0;
    }

    public Optional<String> getOptionalPlayingText() {
        return iPresence.getPlayingText();
    }

    public String getPlayingText(){
        return iPresence.getPlayingText().orElse(null);
    }

    public Optional<String> getOptionalStreamingUrl() {
        return iPresence.getStreamingUrl();
    }

    public String getStreamingUrl(){
        return iPresence.getStreamingUrl().orElse(null);
    }

    public Status getStatus() {
        return get(iPresence.getStatus());
    }

    private static Status get(StatusType type) {
        return Status.values()[type.ordinal()];
    }

    public enum Status {
        ONLINE,
        DND,
        IDLE,
        INVISIBLE,
        OFFLINE,
        STREAMING,
        UNKNOWN,;
    }
}
