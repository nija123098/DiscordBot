package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ContextException;
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
    public void command(@Argument(optional = true) Guild guild, MessageMaker maker){
        if (guild == null) throw new ContextException("You have to be in a server to use that command!");
        maker.withAuthorIcon(guild.getIconURL())
                .getAuthorName().appendRaw(guild.getName()).getMaker()
                .withThumb(guild.getIconURL()).withColor(guild.getIconURL());
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
    }

    private static void withText(MessageMaker maker, String key, String value){
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
