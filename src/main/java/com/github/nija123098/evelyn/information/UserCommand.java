package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.command.configs.CommandsUsedCountConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.moderation.GuildUserJoinTimeConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;
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
    public void command(@Argument(optional = true) User user, @Context(softFail = true) Guild guild, MessageMaker maker, User invoker){
        maker.withThumb(user.getAvatarURL()).withColor(user);
        maker.getTitle().appendRaw(user.getName());
        maker.withColor(user.getAvatarURL());
        maker.getNewFieldPart().withInline(false).withBoth("\u200b", user.getPermissionsForGuild(guild).contains(DiscordPermission.ADMINISTRATOR) ? ("\n" + EmoticonHelper.getChars("oncoming_police_car", false) + " Administrator\n") : "\n\u200b");
		if (guild != null) {
			GuildUser guildUser = GuildUser.getGuildUser(guild, user);
			maker.getNewFieldPart().withInline(true).withBoth(EmoticonHelper.getChars("hash", false) + " User number", " " + (guildUser.getJoinPosition() + 1));
		}
		maker.getNewFieldPart().withInline(true).withBoth(EmoticonHelper.getChars("id", false) + " User ID", " " + user.getID());
		maker.getNewFieldPart().withInline(true).withBoth(EmoticonHelper.getChars("keyboard", false) + " Commands used", " " + ConfigHandler.getSetting(CommandsUsedCountConfig.class, user));
		maker.getNewFieldPart().withInline(true).withBoth((guild == null ? EmoticonHelper.getChars("cookie", false) : (ConfigHandler.getSetting(CurrencySymbolConfig.class, guild))) + " Currency", " " + ConfigHandler.getSetting(CurrentCurrencyConfig.class, user));
		if (guild != null) {
			GuildUser guildUser = GuildUser.getGuildUser(guild, user);
			maker.getNewFieldPart().withInline(true).withBoth(EmoticonHelper.getChars( "date", false) +  " Joined server", " " + Time.getAbbreviated(System.currentTimeMillis() - GuildUserJoinTimeConfig.get(guildUser)) + " ago");
		}
		maker.getNewFieldPart().withInline(true).withBoth(EmoticonHelper.getChars( "calendar_spiral", false) + " Joined discord", " " + Time.getAbbreviated(System.currentTimeMillis() - user.getJoinDate()) + " ago");
		if (invoker.getPermissionsForGuild(guild).contains(DiscordPermission.ADMINISTRATOR) || invoker.getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_ROLES) || invoker.getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_SERVER)) {
			maker.getNewFieldPart().withInline(false).withBoth("Key permissions", FormatHelper.makeUserPermissionsTable(user, guild, true));
			maker.getNote().appendRaw("use `" + ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "user permissions` to see a detailed view of user permissions");
		}
    }
}
