package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import static com.github.nija123098.evelyn.perms.BotRole.setRole;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class TrustCommand extends AbstractCommand {

    public TrustCommand() {
        super("trust", ModuleLevel.ADMINISTRATIVE, null, null, "trust a ");
    }

    @Command
    public void command(User setter, @Argument User user, Guild guild, MessageMaker maker) {
        if (BotRole.getSet(user, guild).contains(BotRole.GUILD_TRUSTEE)) {
            setRole(BotRole.GUILD_TRUSTEE, false, user, setter, guild);
            maker.getTitle().appendRaw("Guild Trustee Removed");
            maker.appendRaw(user.mention() + " is no longer trusted");
        } else {
            setRole(BotRole.GUILD_TRUSTEE, true, user, setter, guild);
            maker.getTitle().appendRaw("New Guild Trustee");
            maker.appendRaw(user.mention() + " is now trusted");
        }
    }
}