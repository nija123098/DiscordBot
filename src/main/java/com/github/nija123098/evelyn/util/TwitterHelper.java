package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Made by nija123098 on 7/15/2017.
 */
public class TwitterHelper {
    public static final Twitter APPLICATION;
    static {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        APPLICATION = new TwitterFactory(builder.build()).getInstance();
        APPLICATION.setOAuthConsumer(ConfigProvider.authKeys.twitter_key(), ConfigProvider.authKeys.twitter_secret());
        try{APPLICATION.getOAuth2Token();
        } catch (TwitterException e) {Log.log("Could not load Twitter", e);}
    }
}
