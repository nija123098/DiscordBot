package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.DevelopmentException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

public class PlayTextOverrideCommand extends AbstractCommand {
    public PlayTextOverrideCommand() {
        super("playtext", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Overrides the playtext of the bot");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Presence.Status stats, @Argument String s){
        DiscordAdapter.PLAY_TEXT_UPDATER.setSkipping(true);
        if (stats == null) stats = Presence.Status.ONLINE;
        Presence.Status status = stats;
        if (status == Presence.Status.STREAMING){
            DiscordClient.streaming(s, "https://twitch.tv/nija123098");
            return;
        }
        Optional<Method> method = Stream.of(DiscordClient.class.getMethods()).filter(m -> m.getName().equalsIgnoreCase(status.name())).filter(m -> s.isEmpty() ? m.getParameterTypes().length == 0 : m.getParameterTypes().length == 1).findAny();
        if (!method.isPresent()) throw new ArgumentException("I don't think you know what you are doing");
        try {
            if (s.isEmpty()) method.get().invoke(null);
            else method.get().invoke(null, s);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DevelopmentException("Exception changing playtext", e);
        }
    }
}
