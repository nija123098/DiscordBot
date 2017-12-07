package com.github.nija123098.evelyn.botconfiguration.configinterfaces.botconfig;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public interface AudioSettings {
    String audioFileTypes();
    String audioFormat();
    Integer musicDownloadThreads();
    Long trackExpirationTime();
    Integer requiredPlaysToDownload();
}
