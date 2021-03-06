package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.configs.CommandsUsedCountConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildLastCommandTimeConfig;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.handle.obj.VerificationLevel;

import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ServerCommand extends AbstractCommand {
    public ServerCommand() {
        super("server", ModuleLevel.INFO, "guild", null, "Displays information about the server");
    }
    @Command
    public void command(@Argument(optional = true) Guild guild, MessageMaker maker, User invoker, @Argument(optional = true, info = "detailed") String string) {
        String discord_white = ConfigProvider.URLS.discordWhitePng();
        if (guild == null) throw new ContextException("You have to be in a server to use that command!");
        if (guild.getIconURL().contains("null")) {
            maker.withAuthorIcon(discord_white).getAuthorName().appendRaw(guild.getName()).getMaker().withThumb(discord_white).withColor(discord_white);
        } else {
            maker.withAuthorIcon(guild.getIconURL()).getAuthorName().appendRaw(guild.getName()).getMaker().withThumb(guild.getIconURL()).withColor(guild.getIconURL());
        }
        String timeInGuild = Time.getAbbreviated(System.currentTimeMillis() - guild.getJoinTimeForUser(DiscordClient.getOurUser()));
        List<User> users = guild.getUsers();
        withText(maker, "Users", users.stream().filter(user -> user.getPresence().getStatus() != Presence.Status.OFFLINE).count() + " online\n" + (users.size() - users.stream().filter(User::isBot).count()) + " total");
        withText(maker, "Bots", users.stream().filter(User::isBot).count() + "");
        withText(maker, "Verification Level", verificationText(guild.guild().getVerificationLevel()));
        withText(maker, "Channels", guild.getChannels().size() + " text channels\n" + guild.getVoiceChannels().size() + " voice channels");
        withText(maker, "Guild Owner", guild.getOwner().getNameAndDiscrim());
        withText(maker, "Made", Time.getAbbreviated(System.currentTimeMillis() - guild.getCreationDate()) + " ago");
        withText(maker, "My prefix", ConfigHandler.getSetting(GuildPrefixConfig.class, guild));
        withText(maker, "Region", guild.getRegion().getName());
        withText(maker, "ID", guild.getID());
        if (BotRole.getSet(invoker, guild).contains(BotRole.BOT_ADMIN)) {
            if (string.equals("detailed")) {
                int cookies = guild.getUsers().stream().mapToInt(user -> ConfigHandler.getSetting(CurrentCurrencyConfig.class, user)).sum();
                int commands = guild.getUsers().stream().mapToInt(user -> ConfigHandler.getSetting(CommandsUsedCountConfig.class, user)).sum();
                withText(maker, "Time in guild", timeInGuild);
                withText(maker, "Total cookies", "" + cookies);
                withText(maker, "Commands used", "" + commands);
                withText(maker, "Last command used", Time.getDate(ConfigHandler.getSetting(GuildLastCommandTimeConfig.class, guild)));
            }
        }
    }

    private static void withText(MessageMaker maker, String key, String value) {
        maker.getNewFieldPart().withInline(true).getTitle().append(key).getFieldPart().getValue().append(value);
    }

    private String verificationText(VerificationLevel verificationLevel) {
        String ret = "";
        switch (verificationLevel) {
            case NONE:
                ret = "NONE\n*Anyone can join*";
                break;
            case LOW:
                ret = "LOW\n*Verified email*";
                break;
            case MEDIUM:
                ret = "MEDIUM\n*Registered 5+ minutes*";
                break;
            case HIGH:
                ret = "(╯°□°）╯︵ ┻━┻\n*10 minute wait in server*";
                break;
            case EXTREME:
                ret = "┻━┻ ﾐヽ(ಠ益ಠ)ノ彡┻━┻\n*Verified phone*";
                break;
            case UNKNOWN:
                ret = "*this really shouldn't happen*";
                break;
        }
        return ret;
    }
}
