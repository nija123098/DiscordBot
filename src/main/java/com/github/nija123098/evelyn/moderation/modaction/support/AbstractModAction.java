package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.moderation.logging.ModLogConfig;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class AbstractModAction {
    private static final Map<Guild, Integer> OLDEST_CASE_MAP = new ConcurrentHashMap<>();
    private static final Map<GuildUser, Integer> LAST_CASE = new ConcurrentHashMap<>();
    public static int lastCase(GuildUser user) {
        Integer i = LAST_CASE.get(user);
        if (i == null) throw new ArgumentException("There are no recent cases by you, please enter a case number");
        return i;
    }
    public static void updateCase(Guild guild, int cas, String reason) {
        AbstractModAction action = ConfigHandler.getSetting(ModActionConfig.class, guild).get(cas);
        if (action == null || OLDEST_CASE_MAP.getOrDefault(guild, Integer.MAX_VALUE) < cas) throw new ArgumentException("The entered case is too old to modify or doesn't exist (yet)");
        if (action.logMaker != null) action.update(reason);
        if (action.level == ModActionLevel.WARN) {
            GuildUser guildUser = GuildUser.getGuildUser(guild, action.offender);
            List<String> list = ConfigHandler.getSetting(WarningLogConfig.class, guildUser);
            if (list.get(list.size() - 1).equals("no reason given")) {
                list.add(list.size() - 1, reason);
                ConfigHandler.setSetting(WarningLogConfig.class, guildUser, list);
            }
        }
    }
    private MessageMaker logMaker, warningMaker;
    private ModActionLevel level;
    private User offender, invoker;
    public AbstractModAction(Guild guild, ModActionLevel level, User offender, User invoker, String reason) {
        this.level = level;
        this.offender = offender;
        this.invoker = invoker;
        Channel logChannel = ConfigHandler.getSetting(ModLogConfig.class, guild);
        int cas = CaseNumberConfig.incrament(guild);
        if (logChannel != null) {
            this.logMaker = new MessageMaker(logChannel)
                    .withColor(level.color)
                    .getAuthorName().append(level.name()).getMaker()
                    .withAuthorIcon(offender.getAvatarURL());
            this.logMaker.getNote().append("Case: " + cas).getMaker().withTimestamp(System.currentTimeMillis());
            this.logMaker.send();
            this.warningMaker = new MessageMaker(offender).withColor(Color.RED);
            this.update(reason);
        }
        ConfigHandler.alterSetting(ModActionConfig.class, guild, map -> map.put(cas, this));
        OLDEST_CASE_MAP.putIfAbsent(guild, cas - 1);
        LAST_CASE.put(GuildUser.getGuildUser(guild, invoker), cas);
    }
    private void update(String reason) {
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
    private static String getLogText(User offender, User invoker, String reason) {
        return "Offender: " + offender.getNameAndDiscrim() + " (" + offender.getID() + ")\n" +
                "Reason: " + (reason == null ? "Not yet reported" : reason) + "\n" +
                "Reporter: " + invoker.getNameAndDiscrim();
    }
    private static String getWarningText(ModActionLevel level, String reason) {
        return "You have been " + level.name() + (level.name().endsWith("E") ? "" : "E") + "D" + " for " + (reason == null ? "breaking server rules" : reason);
    }
}
