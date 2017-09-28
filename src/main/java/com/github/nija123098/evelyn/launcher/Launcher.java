package com.github.nija123098.evelyn.launcher;

import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.service.ServiceHandler;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.Care;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ThreadProvider;
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
    public static final Set<User> USERS = new HashSet<>();
    private static final Set<Runnable> STARTUPS = new HashSet<>();
    private static final Set<Runnable> ASYNC_STARTUPS = new HashSet<>();
    private static final Set<Runnable> SHUTDOWNS = new HashSet<>();
    private static final AtomicBoolean IS_READY = new AtomicBoolean(), IS_STARTING_UP = new AtomicBoolean();
    private static final AtomicReference<ScheduleService.ScheduledTask> SHUTDOWN_TASK = new AtomicReference<>();
    static {
        try {
            new ConfigurationBuilder(BotConfig.class, new File("bot_config.cfg")).build();
        } catch (Exception e) {
            Log.log("Failed to initialize configuration", e);
            System.exit(-1);
        }
    }
    public static void registerStartup(Runnable runnable){
        if (IS_STARTING_UP.get()) runnable.run();
        else STARTUPS.add(runnable);
    }
    public static void registerAsyncStartup(Runnable runnable){
        if (IS_STARTING_UP.get()) runnable.run();
        else ASYNC_STARTUPS.add(runnable);
    }
    public static void registerShutdown(Runnable runnable){
        SHUTDOWNS.add(runnable);
    }
    public static boolean isReady(){
        return IS_READY.get();
    }
    public static void grantSystemAccess(User user){
        USERS.add(user);
        ScheduleService.schedule(300_000, () -> USERS.remove(user));
    }
    public static boolean hasSystemAccess(User user){
        return USERS.contains(user);
    }
    public synchronized static void shutdown(Integer code, long delay){
        ScheduleService.ScheduledTask task = SHUTDOWN_TASK.get();
        if (task != null) {
            Log.log("Canceling a shutdown");
            task.cancel();
            if (code == null) SHUTDOWN_TASK.set(null);
            DiscordAdapter.PLAY_TEXT_UPDATER.setSkipping(false);
        }
        if (code != null) {
            DiscordAdapter.PLAY_TEXT_UPDATER.setSkipping(true);
            DiscordClient.dnd("with a quick restart!");
            Log.log("Scheduled shutdown with code: " + code);
            SHUTDOWN_TASK.set(ScheduleService.schedule(delay, () -> {
                Log.log("Shutting down with code: " + code);
                IS_READY.set(false);
                SHUTDOWNS.forEach(Runnable::run);
                ConfigHandler.saveAndDumpCaches();
                DiscordClient.logout();
                Care.lessSleep(2_000);
                System.exit(code);
            }));
        }
    }
    public static void main(String[] args) {
        TemplateHandler.initialize();
        InvocationObjectGetter.initialize();
        ConfigHandler.initialize();//  changing
        ServiceHandler.initialize();// this order
        CommandHandler.initialize();// could break
        DiscordAdapter.initialize();// EVERYTHING
        IS_STARTING_UP.set(true);
        STARTUPS.forEach(Runnable::run);
        ASYNC_STARTUPS.forEach(ThreadProvider::sub);
        IS_READY.set(true);
        DiscordClient.online("with users!");
        Log.log("Bot finished initializing");
    }
}
