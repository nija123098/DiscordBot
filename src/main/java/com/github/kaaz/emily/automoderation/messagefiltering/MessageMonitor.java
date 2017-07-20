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
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.StringIterator;
import org.reflections.Reflections;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitor {
    private static final Map<MessageMonitoringType, MessageFilter> ID_FILTER_MAP = new HashMap<>();
    private static final Map<Channel, Set<MessageFilter>> CHANNEL_MAP = new HashMap<>();
    static {
        try {
            Files.readAllLines(Paths.get(BotConfig.LANGUAGE_FILTERING_PATH)).forEach(s -> {
                MessageMonitoringType type = MessageMonitoringType.valueOf(s.split(" ")[0].toUpperCase());
                Set<CheckingString> checkingStrings = Stream.of(s.substring(type.name().length(), s.length()).split(",")).map(st -> FormatHelper.filtering(st, Character::isLetterOrDigit).toLowerCase()).map(CheckingString::new).collect(Collectors.toSet());
                ID_FILTER_MAP.put(type, new MessageFilter() {
                    @Override
                    public void checkFilter(DiscordMessageReceived event) {
                        new StringIterator(FormatHelper.filtering(event.getMessage().getContent(), Character::isLetterOrDigit).toLowerCase()).forEachRemaining(character -> checkingStrings.forEach(checkingString -> checkingString.check(character)));
                    }
                    @Override
                    public MessageMonitoringType getType() {return type;}
                });
            });
        } catch (Exception e) {
            Log.log("Could not load language filtering", e);
        }
        new Reflections(Reference.BASE_PACKAGE).getSubTypesOf(MessageFilter.class).stream().filter(aClass -> !aClass.getSimpleName().isEmpty()).forEach(clazz -> {
            try {
                MessageFilter filter = clazz.newInstance();
                ID_FILTER_MAP.put(filter.getType(), filter);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
    public static boolean monitor(DiscordMessageReceived received){
        if (received.getGuild() == null || BotRole.GUILD_TRUSTEE.hasRequiredRole(received.getAuthor(), received.getGuild())) return false;
        try{CHANNEL_MAP.computeIfAbsent(received.getChannel(), MessageMonitor::calculate).forEach(filter -> filter.checkFilter(received));
        } catch (MessageMonitoringException exception){
            received.getMessage().delete();
            new MessageMaker(received.getMessage()).withDM().append("Your message on " + received.getGuild().getName() + " in " + received.getChannel().getName() + " has been deleted.  Reason: " + exception.getMessage()).send();
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
    private static class CheckingString {
        private String check;
        private int location = -1;
        CheckingString(String check) {
            this.check = check;
        }
        void check(char c){
            if (this.check.charAt(++this.location) != c) this.reset();
            else if (this.location == this.check.length() - 1) {
                this.reset();
                throw new MessageMonitoringException("banned phrase: " + this.check);
            }
        }
        void reset(){
            this.location = -1;
        }
    }
}
