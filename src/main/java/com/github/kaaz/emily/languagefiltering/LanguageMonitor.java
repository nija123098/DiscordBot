package com.github.kaaz.emily.languagefiltering;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.automoderation.LanguageFilteringLevelConfig;
import com.github.kaaz.emily.favor.configs.LanguageLevelViolationFactorConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.favor.FavorHandler;
import com.github.kaaz.emily.util.StringIterator;

import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class LanguageMonitor {
    private static final AtomicReference<Map<LanguageLevel, Set<String>>> MAP = new AtomicReference<>();
    static {
        load(ConfigHandler.getSetting(LanguageFilteringLevelWordsConfig.class, GlobalConfigurable.GLOBAL));
    }
    private static void load(Map<LanguageLevel, Set<String>> map){
        for (int i = 0; i < LanguageLevel.values().length; i++) {
            Set<String> strings = map.get(LanguageLevel.values()[i]);
            for (int j = i - 1; j > -1; --j) {
                strings.addAll(map.get(LanguageLevel.values()[i]));
            }
        }
        MAP.set(map);
    }
    @EventListener
    public void handle(ConfigValueChangeEvent event){
        if (event.getConfigType().equals(LanguageFilteringLevelWordsConfig.class)){
            load((Map<LanguageLevel, Set<String>>) event.getNewValue());
        }
    }
    @EventListener
    public void handle(DiscordMessageReceivedEvent event){
        if (event.getMessage().getGuild() == null) return;
        Set<String> contents = new HashSet<>(), blocked = MAP.get().get(ConfigHandler.getSetting(LanguageFilteringLevelConfig.class, event.getGuild()));
        String builder = "";
        StringIterator iterator = new StringIterator(event.getMessage().getContent());
        while (iterator.hasNext()){
            Character c = iterator.next();
            if (Character.isLetter(c)) builder += c;
            else if (builder.length() > 0) {
                contents.add(builder);
                builder = "";
            }
        }
        for (String string : contents){
            if (blocked.contains(string)){
                onBlocked(event.getMessage());
                break;
            }
        }
    }
    private static void onBlocked(Message message){
        message.delete();
        FavorHandler.addFavorLevel(GuildUser.getGuildUser(message.getGuild(), message.getAuthor()), -Math.abs(ConfigHandler.getSetting(LanguageLevelViolationFactorConfig.class, message.getGuild())));
        FavorHandler.addFavorLevel(message.getAuthor(), -ConfigHandler.getConfig(LanguageLevelViolationFactorConfig.class).getDefault());
        new MessageMaker(message).withDM().append("Words used in a recent message you sent have been banned in " + message.getGuild().getName()).withColor(Color.RED).send();
    }
}
