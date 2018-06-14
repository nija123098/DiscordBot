package com.github.nija123098.evelyn.perms.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.GeneralEmotes;

import java.util.Set;
import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class BotRoleCommand extends AbstractCommand {
    public BotRoleCommand() {
        super("botrole", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Gets information on the user's roles in relation to the bot");
    }

    @Command
    public static void command(@Argument(optional = true) User user, Guild guild, MessageMaker maker) {
        maker.getAuthorName().appendAlternate(true, FormatHelper.makePlural(user.getDisplayName(guild)) + " BotRoles");
        maker.withAuthorIcon(user.getAvatarURL());
        Set<BotRole> botRoles = BotRole.getSet(user, guild);
        maker.getHeader().clear().appendRaw("\u200B\n");
        Stream.of(BotRole.values()).forEach(role -> maker.getNewFieldPart().getTitle().appendRaw(role.name()).getFieldPart().getValue().appendRaw(botRoles.contains(role) ? GeneralEmotes.GREEN_TICK : GeneralEmotes.RED_TICK));
    }
}
