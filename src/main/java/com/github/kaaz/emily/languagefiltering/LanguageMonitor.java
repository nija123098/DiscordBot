package com.github.kaaz.emily.languagefiltering;

import com.github.kaaz.emily.automoderation.LanguageFilteringLevelConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.favor.FavorHandler;
import com.github.kaaz.emily.favor.configs.LanguageLevelViolationFactorConfig;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.StringIterator;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class LanguageMonitor {
    private static final AtomicReference<Map<LanguageLevel, Set<CheckingString>>> MAP = new AtomicReference<>();
    static {
        load(ConfigHandler.getSetting(LanguageFilteringLevelWordsConfig.class, GlobalConfigurable.GLOBAL));
    }
    private static synchronized void load(Map<LanguageLevel, Set<String>> map){
        for (LanguageLevel level : LanguageLevel.values()) map.computeIfAbsent(level, l -> new HashSet<>());
        Map<LanguageLevel, Set<CheckingString>> outMap = new HashMap<>();
        map.forEach((languageLevel, strings) -> outMap.put(languageLevel, strings.stream().map(CheckingString::new).collect(Collectors.toSet())));
        int length = LanguageLevel.values().length;
        for (int i = 1; i < length; i++) outMap.get(LanguageLevel.values()[i]).addAll(outMap.get(LanguageLevel.values()[i - 1]));
        MAP.set(outMap);
        EventDistributor.register(LanguageMonitor.class);
    }
    @EventListener
    public static void handle(ConfigValueChangeEvent event){
        if (event.getConfigType().equals(LanguageFilteringLevelWordsConfig.class)) load((Map<LanguageLevel, Set<String>>) event.getNewValue());
    }
    @EventListener
    public static synchronized void handle(DiscordMessageReceivedEvent event){
        if (event.getMessage().getGuild() == null || event.getAuthor().isBot() || BotRole.GUILD_TRUSTEE.hasRequiredRole(event.getAuthor(), event.getGuild())) return;
        StringBuilder builder = new StringBuilder();
        new StringIterator(event.getMessage().getContent()).forEachRemaining(character -> {
            if (Character.isLetter(character)) builder.append(character);
        });
        Set<CheckingString> strings = MAP.get().get(ConfigHandler.getSetting(LanguageFilteringLevelConfig.class, event.getGuild()));
        try{new StringIterator(builder.toString()).forEachRemaining(character -> strings.forEach(checkingString -> checkingString.check(character)));
        } catch (SpellException e) {
            onBlocked(event.getMessage(), e.string);
        }
        strings.forEach(CheckingString::reset);
    }
    private static void onBlocked(Message message, String blocked){
        message.delete();
        FavorHandler.addFavorLevel(GuildUser.getGuildUser(message.getGuild(), message.getAuthor()), -Math.abs(ConfigHandler.getSetting(LanguageLevelViolationFactorConfig.class, message.getGuild())));
        FavorHandler.addFavorLevel(message.getAuthor(), -ConfigHandler.getConfig(LanguageLevelViolationFactorConfig.class).getDefault(message.getGuild()));
        new MessageMaker(message).withDM().append("Words used in a recent message you sent have been blocked in " + message.getGuild().getName()).withColor(Color.RED).send();
    }
    private static class CheckingString {
        private String check;
        private int location = -1;
        CheckingString(String check) {
            this.check = check;
        }
        void check(char c){
            if (this.check.charAt(++this.location) != c) this.reset();
            else if (this.location > this.check.length()) throw new SpellException(this.check);
        }
        void reset(){
            this.location = -1;
        }
    }
    private static class SpellException extends RuntimeException {
        private String string;
        SpellException(String string) {
            this.string = string;
        }
    }
}
