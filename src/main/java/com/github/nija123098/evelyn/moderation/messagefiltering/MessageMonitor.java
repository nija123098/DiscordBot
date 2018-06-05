package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringAdditionsConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringExceptionsConfig;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.StringChecker;
import org.reflections.Reflections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitor {
    private static final Map<MessageMonitoringLevel, MessageFilter> FILTER_MAP = new HashMap<>(MessageMonitoringLevel.values().length + 1, 1);
    private static final Map<Channel, Set<MessageFilter>> CHANNEL_MAP = new HashMap<>();
    private static final Set<String> WORDS = new HashSet<>();
    static {
        try {
            WORDS.addAll(Files.readAllLines(Paths.get(ConfigProvider.RESOURCE_FILES.words())).stream().map(String::toLowerCase).collect(Collectors.toSet()));
        } catch (IOException e) {
            Log.log("Could not read words file", e);
        }
        new Reflections(Launcher.BASE_PACKAGE).getSubTypesOf(MessageFilter.class).stream().filter(aClass -> !aClass.getSimpleName().isEmpty()).forEach(clazz -> {
            try {
                MessageFilter filter = clazz.newInstance();
                FILTER_MAP.put(filter.getType(), filter);
            } catch (InstantiationException | IllegalAccessException e) {
                Log.log("Exception loading new MessageFilter", e);
            }
        });
        try {
            Path path = Paths.get(ConfigProvider.RESOURCE_FILES.languageFiltering());
            if (Files.exists(path)) {
                Files.readAllLines(path).forEach(s -> {
                    MessageMonitoringLevel type = MessageMonitoringLevel.valueOf(s.split(" ")[0].toUpperCase());
                    Consumer<String> policy = s1 -> {throw new MessageMonitoringException("banned phrase: " + s1, s1);};
                    FILTER_MAP.put(type, new MessageFilter() {
                        @Override
                        public void checkFilter(DiscordMessageReceived event) {
                            try {
                                StringChecker.checkoutString(FormatHelper.filtering(event.getMessage().getContent(), Character::isLetterOrDigit).toLowerCase(), Arrays.asList(s.substring(type.name().length(), s.length()).split(",")), policy);
                            } catch (MessageMonitoringException e) {
                                for (String word : FormatHelper.filtering(FormatHelper.reduceRepeats(event.getMessage().getContent().toLowerCase(), ' '), Character::isLetter).split(" ")) {
                                    if (!WORDS.contains(word) && !e.getBlocked().equals(word)) throw e;
                                }
                            }
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
    public static void init() {
    }
    public static boolean monitor(DiscordMessageReceived received) {
        if (received.getChannel().isPrivate() || received.getGuild().getUserSize() < ConfigProvider.BOT_SETTINGS.messageFilteringServerSize() || received.getChannel().isNSFW() || received.getMessage().getContent() == null || received.getMessage().getContent().isEmpty() || BotRole.GUILD_TRUSTEE.hasRequiredRole(received.getAuthor(), received.getGuild())) return false;
        try{CHANNEL_MAP.computeIfAbsent(received.getChannel(), MessageMonitor::calculate).forEach(filter -> filter.checkFilter(received));
        } catch (MessageMonitoringException exception) {
            new MessageMaker(received.getMessage()).withDM().append("Your message on ").appendRaw(received.getGuild().getName()).append(" in ").appendRaw(received.getChannel().mention()).append(" has been deleted.  Reason: " + exception.getMessage()).send();
            received.getMessage().delete();
            return true;
        }
        return false;
    }
    private static Set<MessageFilter> calculate(Channel channel) {
        Set<MessageMonitoringLevel> strings = ConfigHandler.getSetting(MessageMonitoringConfig.class, channel.getGuild());
        strings.addAll(ConfigHandler.getSetting(MessageMonitoringAdditionsConfig.class, channel));
        strings.removeAll(ConfigHandler.getSetting(MessageMonitoringExceptionsConfig.class, channel));
        return strings.stream().filter(strings::contains).map(FILTER_MAP::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    public static void recalculate(Channel channel) {
        CHANNEL_MAP.put(channel, calculate(channel));
    }
    public static void recalculate(Guild guild) {// in reality just allow recalculation as necessary
        guild.getChannels().forEach(CHANNEL_MAP::remove);
    }
    public static Set<MessageMonitoringLevel> getLevels(Channel channel) {
        return CHANNEL_MAP.computeIfAbsent(channel, MessageMonitor::calculate).stream().map(MessageFilter::getType).collect(Collectors.toSet());
    }
}
