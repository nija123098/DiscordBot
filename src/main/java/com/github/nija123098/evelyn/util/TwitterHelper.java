package com.github.nija123098.evelyn.util;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.AUTH_KEYS;
import static com.github.nija123098.evelyn.util.Log.log;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TwitterHelper {
    public static final Twitter APPLICATION;

    static {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        APPLICATION = new TwitterFactory(builder.build()).getInstance();
        APPLICATION.setOAuthConsumer(AUTH_KEYS.twitter_key(), AUTH_KEYS.twitter_secret());
        try {
            APPLICATION.getOAuth2Token();
        } catch (TwitterException e) {
            log("Could not load Twitter", e);
        }
    }
}
