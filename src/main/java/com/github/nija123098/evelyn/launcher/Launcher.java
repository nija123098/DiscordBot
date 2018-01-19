package com.github.nija123098.evelyn.launcher;

import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.ServiceHandler;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.Care;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.LogColor;
import com.github.nija123098.evelyn.util.ThreadProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The main class of the bot from which everything launches.
 *
 * This class takes care initializing order,
 * registration of startup and shutdown {@link Runnable}s,
 * and the shutdown process.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Launcher {
    public static final String EVELYN_VERSION = "1.0.0";
    public static final String BASE_PACKAGE = "com.github.nija123098.evelyn";
    private static final Set<User> SYSTEM_PERM_USERS = new MemoryManagementService.ManagedSet<>(300_000);// 5 min
    private static final Set<Runnable> STARTUPS = new HashSet<>();
    private static final Set<Runnable> ASYNC_STARTUPS = new HashSet<>();
    private static final Set<Runnable> SHUTDOWNS = new HashSet<>();
    private static final AtomicBoolean IS_READY = new AtomicBoolean(), IS_STARTING_UP = new AtomicBoolean();
    private static final AtomicReference<ScheduleService.ScheduledTask> SHUTDOWN_TASK = new AtomicReference<>();

    /**
     * Registers a {@link Runnable} which will be run on startup
     * which will block the bot's responding until complete.
     *
     * @param runnable the {@link Runnable} to run on startup.
     */
    public static void registerStartup(Runnable runnable){
        if (IS_STARTING_UP.get()) runnable.run();
        else STARTUPS.add(runnable);
    }

    /**
     * Registers a {@link Runnable} which will be run on startup
     * which will not block the bot's responding until complete.
     *
     * @param runnable the {@link Runnable} to run on startup.
     */
    public static void registerAsyncStartup(Runnable runnable){
        if (IS_STARTING_UP.get()) runnable.run();
        else ASYNC_STARTUPS.add(runnable);
    }
    /**
     * Registers a shutdown {@link Runnable} which will be
     * ran before the bot is disconnected from Discord.
     *
     * @param runnable the {@link Runnable} to run on shutdown.
     */
    public static void registerShutdown(Runnable runnable){
        SHUTDOWNS.add(runnable);
    }

    /**
     * Gets if the bot is ready to respond to commands.
     *
     * @return if the bot is ready to respond to commands.
     */
    public static boolean isReady(){
        return IS_READY.get();
    }

    /**
     * Grants the {@link User} specified the {@link BotRole#SYSTEM} role.
     *
     * @param user the user to give {@link BotRole#SYSTEM} permissions to.
     */
    public static void grantSystemAccess(User user){
        SYSTEM_PERM_USERS.add(user);
    }

    /**
     * Gets if the specified {@link User} has {@link BotRole#SYSTEM} rights.
     *
     * @param user the user to check {@link BotRole#SYSTEM} permissions for.
     * @return if the specified {@link User} has {@link BotRole#SYSTEM} rights.
     */
    public static boolean hasSystemAccess(User user){
        return SYSTEM_PERM_USERS.contains(user);
    }

    /**
     * Registers a new shutdown or cancels a scheduled shutdown.
     * The bot will be forcibly shutdown two seconds after shutdown should have
     * occurred should a thread not have been properly marked a daemon Thread.
     *
     * The bot will be logged out of Discord before this.
     *
     * @param code the code to shutdown the JVM.
     * @param delay the amount of time before the shutdown begins.
     */
    public synchronized static void shutdown(Integer code, long delay, boolean firm){
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
                DiscordClient.logout();
                Care.lessSleep(2_000);
                System.exit(code);
            }));
        }
        if (firm) {
            Thread firmThread = new Thread(() -> {
                try{Thread.sleep(150_000);
                } catch (InterruptedException e) {
                    Log.log("Ended end sleep early", e);
                    e.printStackTrace();
                }
                Thread halt = new Thread(() -> {
                    try{Thread.sleep(150_000);
                    } catch (InterruptedException e) {
                        Log.log("Ended halt sleep early", e);
                        e.printStackTrace();
                    }
                    if (SHUTDOWN_TASK.get() != null) Runtime.getRuntime().halt(code == null ? -1 : code);
                }, "Halt-Thread");
                halt.setDaemon(true);
                halt.start();
                if (SHUTDOWN_TASK.get() != null) System.exit(code == null ? -1 : code);
            }, "Firm-Thread");
            firmThread.setDaemon(true);
            firmThread.start();
        }
    }

    /**
     * Starts and logs the bot in.
     *
     * @param args the program arguments.
     */
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
        Log.log(LogColor.blue("Bot finished initializing.") + LogColor.yellow(" Burn the heretic. Kill the Mee6. Purge the unclean."));
    }
}
