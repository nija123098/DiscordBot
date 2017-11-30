package com.github.nija123098.evelyn.botconfiguration.configinterfaces.botconfig;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */

public interface AudioSettings {
    String audio_file_types();
    String audio_format();
    Integer music_download_threads();
    Long track_expiration_time();
    Integer required_plays_to_download();
}
