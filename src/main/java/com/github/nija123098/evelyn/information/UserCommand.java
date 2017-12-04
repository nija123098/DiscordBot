package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.command.configs.CommandsUsedCountConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.moderation.GuildUserJoinTimeConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Time;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class UserCommand extends AbstractCommand {
    public UserCommand() {
        super("user", ModuleLevel.INFO, "whois, profile", null, "Shows information about the user");
    }
    @Command
    public void command(@Argument(optional = true) User user, @Context(softFail = true) Guild guild, MessageMaker maker){
        maker.appendAlternate(false, "Querying for **", (guild == null ? user.getName() : user.getDisplayName(guild)) + "**\n\n").withThumb(user.getAvatarURL()).withColor(user);
        addAtrib(maker, "bust_in_silhouette", "User", user.getNameAndDiscrim());
        addAtrib(maker, "keyboard", "Commands used", ConfigHandler.getSetting(CommandsUsedCountConfig.class, user));
        addAtrib(maker, (guild == null ? "cookie" : EmoticonHelper.getName(ConfigHandler.getSetting(CurrencySymbolConfig.class, guild))), "Currency", ConfigHandler.getSetting(CurrentCurrencyConfig.class, user));
        if (guild != null) {
            GuildUser guildUser = GuildUser.getGuildUser(guild, user);
            addAtrib(maker, "hash", "User Number", guildUser.getJoinPosition() + 1);
            addAtrib(maker, "date", "Joined server", Time.getAbbreviated(System.currentTimeMillis() - GuildUserJoinTimeConfig.get(guildUser)) + " ago");
        }
        addAtrib(maker, "calendar_spiral", "Joined Discord", Time.getAbbreviated(System.currentTimeMillis() - user.getJoinDate()) + " ago");
        maker.getNote().append("ID: " + user.getID());
    }
    private static void addAtrib(MessageMaker maker, String icon, String info, Object o){
        maker.appendAlternate(true, EmoticonHelper.getChars(icon, false), "  " + info, ": " + String.valueOf(o) + "\n");
    }
}
