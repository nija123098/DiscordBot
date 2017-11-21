package com.github.nija123098.evelyn.fun.call;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Rand;
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
        super("call", BotRole.GUILD_TRUSTEE, ModuleLevel.FUN, null, null, "Sends your messages to another server during the call.");
    }
    @Command
    public void command(@Argument(info = "The call ID", optional = true, replacement = ContextType.NONE) Integer callID, Channel channel, MessageMaker maker, Guild guild){// guild for requirement
        if (callID == null){
            if (CALL_IDS.containsKey(channel)) {
                maker.append("Currently in call with ID: " + CALL_IDS.get(channel));
                return;
            }
            callID = IDS.addAndGet(Rand.getRand(100) + 52);
            CALLS.put(callID, new HashSet<>(Collections.singletonList(channel)));
            CALL_IDS.put(channel, callID);
            maker.withDM().append("Opening call to with ID: " + callID + "\nGive the ID to another server so they may join you by doing `@Evelyn call " + callID + "`");
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
        if (send.getAuthor().isBot() || send.isCommand()) return;
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
            set.forEach(CALL_IDS::remove);
            set.forEach(chan -> new MessageMaker(chan).append("Ending the call.").send());// no point in making an iterator object
        }else set.forEach(chan -> new MessageMaker(chan).appendRaw(channel.getGuild().getName() + " ").append("has hung up.").send());
    }
    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(Guild.class)) return 5_000;
        return super.getCoolDown(clazz);
    }

    @Override
    protected String getLocalUsages() {
        return "#  call [call id] // use call without the ID to initialize the call and get the ID, then share the ID with the server you want to call";
    }
}
