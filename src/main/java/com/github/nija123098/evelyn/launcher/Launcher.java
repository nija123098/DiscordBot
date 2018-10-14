package com.github.nija123098.evelyn.launcher;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.ServiceHandler;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.util.BlockingArrayQueue;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
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
    private static final CacheHelper.ContainmentCache<User> SYSTEM_PERM_USERS = new CacheHelper.ContainmentCache<>(300_000);// 5 min
    private static final Map<Runnable, Exception> STARTUPS = new ConcurrentHashMap<>();// the alternative is a set backed by a concurrent hash map
    private static final Set<Runnable> POST_STARTUPS = new HashSet<>();
    private static final Set<Runnable> SHUTDOWNS = new HashSet<>();
    private static final AtomicBoolean IS_READY = new AtomicBoolean(), IS_STARTING_UP = new AtomicBoolean();
    private static final AtomicReference<ScheduledFuture<?>> SHUTDOWN_TASK = new AtomicReference<>();

    /**
     * Registers a {@link Runnable} which will be run on startup
     * which will block the bot's responding until complete.
     *
     * @param runnable the {@link Runnable} to run on startup.
     */
    public static void registerStartup(Runnable runnable) {
        if (IS_STARTING_UP.get()) {
            runnable.run();
            Log.log("Running startup after started up on registration", new Exception("Stack Trace Helper"));
        } else STARTUPS.put(runnable, new Exception("Stack Trace Helper"));
    }

    /**
     * Registers a {@link Runnable} which will be run on startup
     * which will not block the bot's responding until complete.
     *
     * @param runnable the {@link Runnable} to run on startup.
     */
    public static void registerPostStartup(Runnable runnable) {
        if (IS_STARTING_UP.get()) {
            runnable.run();
            Log.log("Running post startup after started up on registration", new Exception("Stack Trace Helper"));
        } else POST_STARTUPS.add(runnable);
    }
    /**
     * Registers a shutdown {@link Runnable} which will be
     * ran before the bot is disconnected from Discord.
     *
     * @param runnable the {@link Runnable} to run on shutdown.
     */
    public static void registerShutdown(Runnable runnable) {
        SHUTDOWNS.add(runnable);
    }

    /**
     * Gets if the bot is ready to respond to commands.
     *
     * @return if the bot is ready to respond to commands.
     */
    public static boolean isReady() {
        return IS_READY.get();
    }

    /**
     * Grants the {@link User} specified the {@link BotRole#SYSTEM} role.
     *
     * @param user the user to give {@link BotRole#SYSTEM} permissions to.
     */
    public static void grantSystemAccess(User user) {
        SYSTEM_PERM_USERS.add(user);
    }

    /**
     * Gets if the specified {@link User} has {@link BotRole#SYSTEM} rights.
     *
     * @param user the user to check {@link BotRole#SYSTEM} permissions for.
     * @return if the specified {@link User} has {@link BotRole#SYSTEM} rights.
     */
    public static boolean hasSystemAccess(User user) {
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
    public synchronized static void shutdown(Integer code, long delay, boolean firm) {
        ScheduledFuture<?> task = SHUTDOWN_TASK.get();
        if (task != null) {
            Log.log("Canceling a shutdown");
            if (!task.cancel(false)) {
                Log.log("Too late");
                return;
            }
            if (code == null) SHUTDOWN_TASK.set(null);
            DiscordAdapter.PLAY_TEXT_UPDATE.set(false);
        }
        if (code != null) {
            DiscordAdapter.PLAY_TEXT_UPDATE.set(true);
            DiscordClient.changePresence(Presence.Status.DND, Presence.Activity.PLAYING, "with a quick restart!");
            Log.log("Scheduled shutdown with code: " + code);
            Thread thread = new Thread(() -> {
                CareLess.lessSleep(delay);
                Log.log("Shutting down with code: " + code);
                IS_READY.set(false);
                SHUTDOWNS.forEach(Runnable::run);
                Log.log("Shutdowns complete");
                DiscordClient.logout();
                CareLess.lessSleep(2_000);
                System.exit(code);
            });
            thread.setDaemon(true);
            thread.start();
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
        if (Locale.getDefault().getCountry().isEmpty()) {
            Locale.setDefault(Locale.US);
        }
        TemplateHandler.initialize();
        InvocationObjectGetter.initialize();
        ConfigHandler.initialize();//  changing
        ServiceHandler.initialize();// this order
        CommandHandler.initialize();// could break
        ShiftChangeManager.waitForPredecessorShutdown();
        DiscordAdapter.initialize();// EVERYTHING
        IS_STARTING_UP.set(true);
        Object startupLock = new Object();
        STARTUPS.forEach((runnable, e) -> ThreadHelper.getDemonThread(() -> {
            long time = System.currentTimeMillis();
            runnable.run();
            if (System.currentTimeMillis() - time > 10_000) Log.log("Startup took longer then 10_000 millis, showing registration stack trace", e);
            STARTUPS.remove(runnable);
            if (STARTUPS.isEmpty()) synchronized (startupLock) {
                CareLess.lessSleep(10);// wait for lock
                startupLock.notifyAll();
            }
        }, "Pre-Startup-Task").start());
        synchronized (startupLock) {
            try {
                startupLock.wait();
            } catch (InterruptedException e) {
                Log.log("Wait interrupted for startups, current complete: " + STARTUPS.size() + " resuming", e);
            }
        }
        ExecutorService executorService = new ThreadPoolExecutor(0, POST_STARTUPS.size(), 0, TimeUnit.MILLISECONDS, new BlockingArrayQueue<>(POST_STARTUPS.size()), r -> ThreadHelper.getDemonThread(r, "Async-Startup-Thread"));
        POST_STARTUPS.forEach(runnable -> executorService.submit(() -> {
            runnable.run();
            POST_STARTUPS.remove(runnable);
            if (POST_STARTUPS.isEmpty()) {
                Log.log("All async start ups completed");
                executorService.shutdown();
            }
        }));
        IS_READY.set(true);
        DiscordClient.changePresence(Presence.Status.ONLINE, Presence.Activity.PLAYING, "with users!");
        Log.log(LogColor.blue("Bot finished initializing.") + LogColor.yellow(" Burn the heretic. Kill the Mee6. Purge the unclean."));

        //post message in logging channel
        /*
        MessageMaker maker = new MessageMaker(Channel.getChannel(ConfigProvider.BOT_SETTINGS.loggingChannel()));
        maker.withColor(new Color(175, 30,5));
        String slog = null;
        try {
            slog = FileUtils.readFileToString(Log.LOG_PATH.toFile(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        maker.getHeader().clear().append("Startup Log:\n" + HasteBin.post(slog));
        maker.getTitle().clear().append("⚠ Evelyn Started ⚠");
        maker.withTimestamp(System.currentTimeMillis());
        maker.withImage(ConfigProvider.URLS.startupGif());
        maker.send();
        */
    }
}
