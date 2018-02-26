package com.github.nija123098.evelyn.perms.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Set;

import static com.github.nija123098.evelyn.command.ModuleLevel.BOT_ADMINISTRATIVE;
import static com.github.nija123098.evelyn.perms.BotRole.getSet;
import static com.github.nija123098.evelyn.util.FormatHelper.makePlural;
import static com.github.nija123098.evelyn.util.GeneralEmotes.GREEN_TICK;
import static com.github.nija123098.evelyn.util.GeneralEmotes.RED_TICK;
import static java.util.stream.Stream.of;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class BotRoleCommand extends AbstractCommand {
    public BotRoleCommand() {
        super("botrole", BOT_ADMINISTRATIVE, null, null, "Gets information on the user's roles in relation to the bot");
    }

    @Command
    public static void command(@Argument(optional = true) User user, Guild guild, MessageMaker maker, @Context(softFail = true) Boolean trusted) {
        maker.getAuthorName().appendAlternate(true, makePlural(user.getDisplayName(guild)) + " BotRoles");
        maker.withAuthorIcon(user.getAvatarURL());
        Set<BotRole> botRoles = getSet(user, guild);
        if (trusted == null) {
            maker.getHeader().clear().appendRaw("\u200B\n");
        } else {
            maker.getHeader().clear().appendRaw(trusted ? user.getDisplayName(guild) + " now trusted" : user.getDisplayName(guild) + " no longer trusted");
        }
        of(BotRole.values()).forEach(role -> maker.getNewFieldPart().getTitle().appendRaw(role.name()).getFieldPart().getValue().appendRaw(botRoles.contains(role) ? GREEN_TICK : RED_TICK));
    }
}
