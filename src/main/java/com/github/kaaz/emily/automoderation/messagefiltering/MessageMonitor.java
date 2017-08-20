package com.github.kaaz.emily.automoderation.messagefiltering;

import com.github.kaaz.emily.automoderation.messagefiltering.configs.MessageMonitoringAdditionsConfig;
import com.github.kaaz.emily.automoderation.messagefiltering.configs.MessageMonitoringConfig;
import com.github.kaaz.emily.automoderation.messagefiltering.configs.MessageMonitoringExceptionsConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.StringChecker;
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
    private static final Map<MessageMonitoringType, MessageFilter> ID_FILTER_MAP = new HashMap<>();
    private static final Map<Channel, Set<MessageFilter>> CHANNEL_MAP = new HashMap<>();
    static {
        new Reflections(Reference.BASE_PACKAGE).getSubTypesOf(MessageFilter.class).stream().filter(aClass -> !aClass.getSimpleName().isEmpty()).forEach(clazz -> {
            try {
                MessageFilter filter = clazz.newInstance();
                ID_FILTER_MAP.put(filter.getType(), filter);
            } catch (InstantiationException | IllegalAccessException e) {
                Log.log("Exception lading new MessageFilter", e);
            }
        });
        try {
            Path path = Paths.get(BotConfig.LANGUAGE_FILTERING_PATH);
            if (Files.exists(path)) {
                Files.readAllLines(path).forEach(s -> {
                    MessageMonitoringType type = MessageMonitoringType.valueOf(s.split(" ")[0].toUpperCase());
                    Consumer<String> policy = s1 -> {throw new MessageMonitoringException("banned phrase: " + s1);};
                    ID_FILTER_MAP.put(type, new MessageFilter() {
                        @Override
                        public void checkFilter(DiscordMessageReceived event) {
                            StringChecker.checkoutString(FormatHelper.filtering(event.getMessage().getContent(), Character::isLetterOrDigit).toLowerCase(), Arrays.asList(s.substring(type.name().length(), s.length()).split(",")), policy);
                        }
                        @Override
                        public MessageMonitoringType getType() {return type;}
                    });
                });
            }
        } catch (Exception e) {
            Log.log("Could not load language filtering", e);
        }
    }
    public static boolean monitor(DiscordMessageReceived received){
        if (received.getGuild().getUserSize() < BotConfig.MESSAGE_FILTERING_SERVER_SIZE || received.getChannel().isPrivate() || received.getChannel().isNSFW() || received.getMessage().getContent() == null || received.getMessage().getContent().isEmpty()) return false;
        try{CHANNEL_MAP.computeIfAbsent(received.getChannel(), MessageMonitor::calculate).forEach(filter -> filter.checkFilter(received));
        } catch (MessageMonitoringException exception){
            new MessageMaker(received.getMessage()).withDM().append("Your message on ").appendRaw(received.getGuild().getName()).append(" in ").appendRaw(received.getChannel().mention()).append(" has been deleted.  Reason: " + exception.getMessage()).send();
            received.getMessage().delete();
            return true;
        }
        return false;
    }
    public static void recalculate(Channel channel){
        CHANNEL_MAP.put(channel, calculate(channel));
    }
    private static Set<MessageFilter> calculate(Channel channel){
        Set<MessageMonitoringType> strings = ConfigHandler.getSetting(MessageMonitoringConfig.class, channel.getGuild());
        strings.addAll(ConfigHandler.getSetting(MessageMonitoringAdditionsConfig.class, channel));
        strings.removeAll(ConfigHandler.getSetting(MessageMonitoringExceptionsConfig.class, channel));
        return strings.stream().filter(strings::contains).map(ID_FILTER_MAP::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

}
