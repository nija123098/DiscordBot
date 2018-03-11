package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.util.CacheHelper;
import com.google.common.cache.LoadingCache;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IPresence;
import sx.blah.discord.handle.obj.StatusType;

import java.util.Optional;

/**
 * Wraps a Discord4j {@link IPresence} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Presence {
    private static final LoadingCache<IPresence, Presence> MAP = CacheHelper.getLoadingCache(4, ConfigProvider.CACHE_SETTINGS.presenceSize(), 30_000, Presence::new);
    public static Presence getPresence(IPresence presence) {
        if (presence == null) return null;
        return MAP.getUnchecked(presence);
    }
    private IPresence iPresence;
    private Presence(IPresence iPresence) {
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
        return this.iPresence.getText().isPresent() ? this.iPresence.getText().get().hashCode() : 0;
    }

    public Optional<String> getOptionalPlayingText() {
        return iPresence.getText();
    }

    public String getPlayingText() {
        return iPresence.getText().orElse(null);
    }

    public Optional<String> getOptionalStreamingUrl() {
        return iPresence.getStreamingUrl();
    }

    public String getStreamingUrl() {
        return iPresence.getStreamingUrl().orElse(null);
    }

    public Status getStatus() {
        return get(iPresence.getStatus());
    }

    public Activity getActivity() {
        return get(iPresence.getActivity().orElse(null));
    }

    private static Status get(StatusType type) {
        return Status.values()[type.ordinal()];
    }

    private static Activity get(ActivityType type) {
        return type == null ? null : Activity.values()[type.ordinal()];
    }

    public enum Status {
        ONLINE,
        DND,
        IDLE,
        INVISIBLE,
        OFFLINE,
        UNKNOWN,;
        public StatusType convert() {
            return StatusType.values()[this.ordinal()];
        }
    }
    public enum Activity {
        PLAYING,// playing ____
        STREAMING,// streaming ____
        LISTENING,// listening to ____
        WATCHING,;// watching ____
        public ActivityType convert() {
            return ActivityType.values()[this.ordinal()];
        }
    }
}
