package com.github.kaaz.emily.audio.configs.track;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 7/4/2017.
 */
public class BannedTrackConfig extends AbstractConfig<Boolean, Track> {
    private static BannedTrackConfig CONFIG;
    public BannedTrackConfig() {
        super("banned_track", BotRole.BOT_ADMIN, false, "If the track is banned from being played on the global playlist");
    }
    @Override
    protected void onLoad() {
        CONFIG = ConfigHandler.getConfig(BannedTrackConfig.class);
    }
    public static void ban(Track track){
        CONFIG.setValue(track, true);
    }
}
