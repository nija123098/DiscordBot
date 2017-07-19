package com.github.kaaz.emily.launcher;

import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.InvocationObjectGetter;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.DiscordAdapter;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.service.ServiceHandler;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.template.TemplateHandler;
import com.github.kaaz.emily.util.Log;
import com.wezinkhof.configuration.ConfigurationBuilder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Launcher {
    private static final Set<Runnable> STARTUPS = new HashSet<>();
    private static final Set<Runnable> SHUTDOWNS = new HashSet<>();
    private static final AtomicBoolean IS_READY = new AtomicBoolean();
    private static final AtomicReference<ScheduleService.ScheduledTask> SHUTDOWN_TASK = new AtomicReference<>();
    static {
        try {
            new ConfigurationBuilder(BotConfig.class, new File("bot_config.cfg")).build();
        } catch (Exception e) {
            Log.log("Failed to initialize configuration", e);
            System.exit(-1);
        }
        TemplateHandler.initialize();
        InvocationObjectGetter.initialize();
        ConfigHandler.initialize();
        ServiceHandler.initialize();
        CommandHandler.initialize();
        DiscordAdapter.initialize();
        STARTUPS.forEach(Runnable::run);
        IS_READY.set(true);
        ScheduleService.schedule(10_000, () -> DiscordClient.online("with users!"));
        Log.log("Bot finished initializing");
    }
    public static void registerStartup(Runnable runnable){
        STARTUPS.add(runnable);
    }
    public static void registerShutdown(Runnable runnable){
        SHUTDOWNS.add(runnable);
    }
    public static boolean isReady(){
        return IS_READY.get();
    }
    public synchronized static void shutdown(Integer code, long delay){
        ScheduleService.ScheduledTask task = SHUTDOWN_TASK.get();
        if (task != null) {
            Log.log("Canceling a shutdown");
            task.cancel();
            if (code == null) SHUTDOWN_TASK.set(null);
        }
        if (code != null) {
            Log.log("Scheduled shutdown with code: " + code);
            SHUTDOWN_TASK.set(ScheduleService.schedule(delay, () -> {
                Log.log("Shutting down with code: " + code);
                IS_READY.set(false);
                SHUTDOWNS.forEach(Runnable::run);
                DiscordClient.logout();
                System.exit(code);
            }));
        }
    }
    public static void main(String[] args) {

    }
}
