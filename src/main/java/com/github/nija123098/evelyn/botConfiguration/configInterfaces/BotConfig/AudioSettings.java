package com.github.nija123098.evelyn.botConfiguration.configInterfaces.BotConfig;

/**
 * @Author: Celestialdeath99
 * Made on 11/28/2017
 */

public interface AudioSettings {
    String audio_file_types();
    String audio_format();
    Integer music_download_threads();
    Long track_expiration_time();
    Integer required_plays_to_download();
}
