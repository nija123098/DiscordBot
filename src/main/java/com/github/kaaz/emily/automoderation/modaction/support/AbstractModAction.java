package com.github.kaaz.emily.automoderation.modaction.support;

import com.github.kaaz.emily.automoderation.ModLogConfig;
import com.github.kaaz.emily.config.ComplexObject;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class AbstractModAction extends ComplexObject {
    private static final Map<Guild, Integer> OLDEST_CASE_MAP = new ConcurrentHashMap<>();
    private static final Map<GuildUser, Integer> LAST_CASE = new ConcurrentHashMap<>();
    public static int lastCase(GuildUser user){
        Integer i = LAST_CASE.get(user);
        if (i == null) throw new ArgumentException("There are no recent cases by you, please enter a case number");
        return i;
    }
    public static void updateCase(Guild guild, int cas, String reason){
        AbstractModAction action = ConfigHandler.getSetting(ModActionConfig.class, guild).get(cas);
        if (action == null || OLDEST_CASE_MAP.getOrDefault(guild, Integer.MAX_VALUE) < cas) throw new ArgumentException("The entered case is too old to modify or doesn't exist (yet)");
        action.update(reason);
    }
    private MessageMaker logMaker, warningMaker;
    private ModActionLevel level;
    private User offender, invoker;
    public AbstractModAction(Guild guild, ModActionLevel level, User offender, User invoker, String reason) {
        this.level = level;
        this.offender = offender;
        this.invoker = invoker;
        this.logMaker = new MessageMaker(ConfigHandler.getSetting(ModLogConfig.class, guild))
                .withColor(level.color)
                .getAuthorName().append(level.name()).getMaker()
                .withAuthorIcon(offender.getAvatarURL());
        int cas = CaseNumberConfig.incrament(guild);
        this.logMaker.getNote().append("case " + cas);
        this.logMaker.send();
        this.warningMaker = new MessageMaker(offender).mustEmbed().withColor(Color.RED);
        this.update(reason);
        ConfigHandler.alterSetting(ModActionConfig.class, guild, map -> map.put(cas, this));
        OLDEST_CASE_MAP.putIfAbsent(guild, cas - 1);
        LAST_CASE.put(GuildUser.getGuildUser(guild, invoker), cas);
    }
    private void update(String reason){
        this.logMaker.getHeader().clear().append(getLogText(this.offender, this.invoker, reason)).getMaker().send();
        this.warningMaker.getHeader().clear().append(getWarningText(level, reason)).getMaker().send();
    }
    public enum ModActionLevel {
        WARN("Adds a strike to the user", new Color(0xA8CF00)),
        MUTE("Adds the configured muted role to user", new Color(0xFFF300)),
        KICK("Remove user from the guild", new Color(0xFF9600)),
        TEMP_BAN("Remove user from guild, unable to rejoin for a while", new Color(0xFF4700)),
        BAN("Permanently removes user from guild", new Color(0xB70000)),
        EXPUNGE("Eradicates all previous mentions of the user", new Color(0x890000)),;
        private Color color;
        private String description;
        ModActionLevel(String description, Color color) {
            this.color = color;
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }
    private static String getLogText(User offender, User invoker, String reason){
        return "Offender: " + offender.getNameAndDiscrim() + " (" + offender.getID() + ")\n" +
                "Reason: " + (reason == null ? "Not yet reported" : reason) + "\n" +
                "Reporter: " + invoker.getNameAndDiscrim();
    }
    private static String getWarningText(ModActionLevel level, String reason){
        return "You have been " + level.name() + (level.name().endsWith("E") ? "" : "E") + "D" + " for " + (reason == null ? "breaking server rules" : reason);
    }
}
