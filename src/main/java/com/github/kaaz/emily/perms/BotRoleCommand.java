package com.github.kaaz.emily.perms;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.FormatHelper;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class BotRoleCommand extends AbstractCommand {
    public BotRoleCommand() {
        super("botrole", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Gets information on the user's roles in relation to the bot");
    }
    @Command
    public void command(@Convert(optional = true) User user, Guild guild, MessageMaker maker){
        maker.getTitle().appendAlternate(true, FormatHelper.makePleural(user.getDisplayName(guild)) + " BotRoles");
        BotRole.getSet(user, guild).forEach(role -> maker.getNewFieldPart().getTitle().appendRaw(role.name()).getFieldPart().getValue().appendRaw(EmoticonHelper.getChars("white_check_mark")));
    }
}
