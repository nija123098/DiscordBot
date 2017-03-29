package com.github.kaaz.emily.launcher;

import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.InvocationObjectGetter;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.DiscordAdapter;
import com.github.kaaz.emily.template.TemplateHandler;
import com.github.kaaz.emily.util.Log;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Launcher {
    static {
        TemplateHandler.initialize();
        InvocationObjectGetter.initialize();
        ConfigHandler.initialize();
        // ServiceHandler.initialize();
        CommandHandler.initialize();
        DiscordAdapter.initialize();
        Log.log("Bot finished initializing");
    }
    public static void main(String[] args) {

    }
}
