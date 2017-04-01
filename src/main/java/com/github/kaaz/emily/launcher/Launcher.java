package com.github.kaaz.emily.launcher;

import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.InvocationObjectGetter;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.DiscordAdapter;
import com.github.kaaz.emily.service.ServiceHandler;
import com.github.kaaz.emily.template.TemplateHandler;
import com.github.kaaz.emily.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Launcher {
    private static final Set<Runnable> STARTUPS = new HashSet<>();
    private static final Set<Runnable> SHUTDOWNS = new HashSet<>();
    static {
        TemplateHandler.initialize();
        InvocationObjectGetter.initialize();
        ConfigHandler.initialize();
        ServiceHandler.initialize();
        CommandHandler.initialize();
        DiscordAdapter.initialize();
        STARTUPS.forEach(Runnable::run);
        Log.log("Bot finished initializing");
    }
    public static void registerStartup(Runnable runnable){
        STARTUPS.add(runnable);
    }
    public static void registerShutdown(Runnable runnable){
        SHUTDOWNS.add(runnable);
    }
    public static void main(String[] args) {

    }
}
