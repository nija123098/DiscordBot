package com.github.kaaz.emily.fun.call;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Rand;
import com.google.api.client.repackaged.com.google.common.base.Joiner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class CallCommand extends AbstractCommand {
    private static final String CALL_ICON = EmoticonHelper.getChars("telephone", false) + " ";
    private static final Map<Channel, Integer> CALL_IDS = new ConcurrentHashMap<>();
    private static final Map<Integer, Set<Channel>> CALLS = new ConcurrentHashMap<>();
    private static final AtomicInteger IDS = new AtomicInteger(-1);
    public CallCommand() {
        super("call", BotRole.GUILD_TRUSTEE, ModuleLevel.FUN, null, null, "Sends your messages to anouther server durring the call.");
    }
    @Command
    public void command(@Argument(info = "The call ID", optional = true, replacement = ContextType.NONE) Integer callID, Channel channel, Guild guild, MessageMaker maker){
        if (callID == null){
            if (CALL_IDS.containsKey(channel)) {
                maker.append("Currently in call with ID: " + CALL_IDS.get(channel));
                return;
            }
            callID = IDS.addAndGet(Rand.getRand(100) + 52);
            CALLS.put(callID, new HashSet<>(Collections.singletonList(channel)));
            CALL_IDS.put(channel, callID);
            maker.withDM().append("Opening call to with ID: " + callID + "\nGive the ID to another server so they may join you by doing `@Emily call " + callID + "`");
            return;
        }
        if (CALL_IDS.containsKey(channel)) throw new ArgumentException("You can't be in more than one call at a time, it might confuse me.  You must hang up first");
        if (!CALLS.containsKey(callID)) throw new ArgumentException("That's an invalid call ID");
        maker.append("Opening call to " + Joiner.on(", ").join(CALLS.get(callID).stream().map(chan -> chan.getGuild().getName()).collect(Collectors.toSet())));
        CALLS.get(callID).forEach(chan -> new MessageMaker(chan).append(channel.getGuild().getName() + " has joined the call.").send());
        CALL_IDS.put(channel, callID);
        CALLS.get(callID).add(channel);
    }
    @EventListener
    public static void handle(DiscordMessageReceived send){
        if (send.getAuthor().isBot()) return;
        Integer callID = CALL_IDS.get(send.getChannel());
        if (callID == null) return;
        CALLS.get(callID).stream().filter(channel -> !channel.equals(send.getChannel())).forEach(channel -> new MessageMaker(channel).appendRaw(CALL_ICON + send.getAuthor().getNameAndDiscrim() + "\n").append(send.getMessage().getContent()).send());
    }
    static void hangUp(Channel channel){
        Integer integer = CALL_IDS.remove(channel);
        if (integer == null) throw new ArgumentException("You are not currently in a call");
        Set<Channel> set = CALLS.get(integer);
        set.remove(channel);
        if (set.size() == 1) {
            CALLS.remove(integer);
            set.forEach(chan -> new MessageMaker(chan).append("Hanging up call.").send());
        }
        else set.forEach(chan -> new MessageMaker(chan).appendRaw(channel.getGuild().getName() + " ").append("has hung up.").send());
    }
    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(Guild.class)) return 5_000;
        return super.getCoolDown(clazz);
    }
}
