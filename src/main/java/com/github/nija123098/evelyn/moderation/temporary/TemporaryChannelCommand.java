package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TemporaryChannelCommand extends AbstractCommand {
    private static final int TEXT_INACTIVITY = 3_600_000, VOICE_INACTIVITY = 1_800_000, CHECKING_INTERVAL = 60_000, ITERATIONS = VOICE_INACTIVITY / CHECKING_INTERVAL;
    private static final Map<VoiceChannel, Integer> INACTIVITY_MAP = new ConcurrentHashMap<>();// int in 10 min intervals
    private static final Map<Channel, ScheduleService.ScheduledTask> DELETE_MAP = new ConcurrentHashMap<>();
    public TemporaryChannelCommand() {
        super("tempchannel", ModuleLevel.ADMINISTRATIVE, "temporary channel, temporarychannel, tempc, tempchan", null, "Makes a temporary channel which is deleted when activity dwindles");
        Launcher.registerStartup(() -> {
            Set<Channel> channels = ConfigHandler.getNonDefaultSettings(TemporaryChannelConfig.class).keySet();
            channels.stream().filter(channel -> channel instanceof VoiceChannel).forEach(channel -> INACTIVITY_MAP.put((VoiceChannel) channel, 0));
            channels.removeAll(INACTIVITY_MAP.keySet());
            channels.forEach(TemporaryChannelCommand::updateTask);
            ScheduleService.scheduleRepeat(600_000, 600_000, () -> INACTIVITY_MAP.forEach((channel, integer) -> INACTIVITY_MAP.compute(channel, (chan, count) -> {
                if (!chan.getConnectedUsers().isEmpty() || chan.getGuild().getUsers().stream().map(User::getPresence).filter(presence -> presence.getStatus() == Presence.Status.ONLINE).map(Presence::getOptionalPlayingText).filter(Optional::isPresent).filter(s -> FormatHelper.filtering(s.get().toLowerCase().replace(" ", "-"), Character::isLetterOrDigit).equals(chan.getName())).count() > ConfigHandler.getSetting(TemporaryGameChannelsConfig.class, chan.getGuild())) return 0;
                if (count > ITERATIONS) {
                    INACTIVITY_MAP.remove(chan);
                    chan.delete();
                }
                return count;
            })));
        });
    }
    @Command
    public static void command(@Argument(optional = true, replacement = ContextType.NONE, info = "if it's a text channel") Boolean textChannel, @Argument(info = "name") String name, Guild guild){
        Channel channel;
        if (name.isEmpty()) throw new ArgumentException("Please specify a name for the channel");
        if (textChannel == null) textChannel = false;
        if (textChannel) {
            channel = guild.createChannel(name);
            updateTask(channel);
        } else {
            channel = guild.createVoiceChannel(name);
            INACTIVITY_MAP.put((VoiceChannel) channel, 0);
        }
        ConfigHandler.setSetting(TemporaryChannelConfig.class, channel, true);
    }
    @EventListener
    public void listen(DiscordMessageReceived event){
        if (ConfigHandler.getSetting(TemporaryChannelConfig.class, event.getChannel())) updateTask(event.getChannel());
    }
    private static void updateTask(Channel channel){
        ScheduleService.ScheduledTask task = DELETE_MAP.get(channel);
        if (task != null) task.cancel();
        DELETE_MAP.put(channel, ScheduleService.schedule(TEXT_INACTIVITY, channel::delete));
    }
}
