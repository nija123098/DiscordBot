package com.github.kaaz.emily.launcher;

import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.InvocationObjectGetter;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.DiscordAdapter;
import com.github.kaaz.emily.programconfig.BotConfig;
import com.github.kaaz.emily.service.ServiceHandler;
import com.github.kaaz.emily.template.TemplateHandler;
import com.github.kaaz.emily.util.Log;
import com.wezinkhof.configuration.ConfigurationBuilder;

import java.io.File;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Launcher {
    static {
        try {
            new ConfigurationBuilder(BotConfig.class, new File("bot_config.cfg")).build();
        } catch (Exception e) {
            Log.log("Failed initializing configuration");
            e.printStackTrace();
        }
        DiscordAdapter.initialize();
        TemplateHandler.initialize();
        InvocationObjectGetter.initialize();
        ConfigHandler.initialize();
        ServiceHandler.initialize();
        CommandHandler.initialize();
        Log.log("Bot finished initializing");
    }

    public static void main(String[] args) {

    }
}
