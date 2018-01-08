package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.BotConfig.ReadConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringAdditionsConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringExceptionsConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.launcher.Reference;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.StringChecker;
import org.reflections.Reflections;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitor {
    private static final Map<MessageMonitoringLevel, MessageFilter> FILTER_MAP = new HashMap<>();
    private static final Map<Channel, Set<MessageFilter>> CHANNEL_MAP = new HashMap<>();
    static {
        new Reflections(Reference.BASE_PACKAGE).getSubTypesOf(MessageFilter.class).stream().filter(aClass -> !aClass.getSimpleName().isEmpty()).forEach(clazz -> {
            try {
                MessageFilter filter = clazz.newInstance();
                FILTER_MAP.put(filter.getType(), filter);
            } catch (InstantiationException | IllegalAccessException e) {
                Log.log("Exception lading new MessageFilter", e);
            }
        });
        try {
            Path path = Paths.get(ReadConfig.LANGUAGE_FILTERING_NAME);
            if (Files.exists(path)) {
                Files.readAllLines(path).forEach(s -> {
                    MessageMonitoringLevel type = MessageMonitoringLevel.valueOf(s.split(" ")[0].toUpperCase());
                    Consumer<String> policy = s1 -> {throw new MessageMonitoringException("banned phrase: " + s1);};
                    FILTER_MAP.put(type, new MessageFilter() {
                        @Override
                        public void checkFilter(DiscordMessageReceived event) {
                            StringChecker.checkoutString(FormatHelper.filtering(event.getMessage().getContent(), Character::isLetterOrDigit).toLowerCase(), Arrays.asList(s.substring(type.name().length(), s.length()).split(",")), policy);
                        }
                        @Override
                        public MessageMonitoringLevel getType() {return type;}
                    });
                });
            }
        } catch (Exception e) {
            Log.log("Could not load language filtering", e);
        }
    }
    public static boolean monitor(DiscordMessageReceived received){
        if (received.getChannel().isPrivate() || received.getGuild().getUserSize() < ReadConfig.MESSAGE_FILTERING_SERVER_SIZE || received.getChannel().isNSFW() || received.getMessage().getContent() == null || received.getMessage().getContent().isEmpty()) return false;
        try{CHANNEL_MAP.computeIfAbsent(received.getChannel(), MessageMonitor::calculate).forEach(filter -> filter.checkFilter(received));
        } catch (MessageMonitoringException exception){
            new MessageMaker(received.getMessage()).withDM().append("Your message on ").appendRaw(received.getGuild().getName()).append(" in ").appendRaw(received.getChannel().mention()).append(" has been deleted.  Reason: " + exception.getMessage()).send();
            received.getMessage().delete();
            return true;
        }
        return false;
    }
    private static Set<MessageFilter> calculate(Channel channel){
        Set<MessageMonitoringLevel> strings = ConfigHandler.getSetting(MessageMonitoringConfig.class, channel.getGuild());
        strings.addAll(ConfigHandler.getSetting(MessageMonitoringAdditionsConfig.class, channel));
        strings.removeAll(ConfigHandler.getSetting(MessageMonitoringExceptionsConfig.class, channel));
        return strings.stream().filter(strings::contains).map(FILTER_MAP::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    public static void recalculate(Channel channel){
        CHANNEL_MAP.put(channel, calculate(channel));
    }
    public static Set<MessageMonitoringLevel> getLevels(Channel channel){
        return CHANNEL_MAP.computeIfAbsent(channel, MessageMonitor::calculate).stream().map(MessageFilter::getType).collect(Collectors.toSet());
    }
}
