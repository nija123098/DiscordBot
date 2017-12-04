package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.favor.configs.GuildUserReputationConfig;

import static com.github.nija123098.evelyn.command.ModuleLevel.ADMINISTRATIVE;
import static com.github.nija123098.evelyn.config.ConfigHandler.changeSetting;
import static com.github.nija123098.evelyn.config.GuildUser.getGuildUser;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ReputationCommand extends AbstractCommand {
    public ReputationCommand() {
        super("reputation", ADMINISTRATIVE, "rep", "+1", "Gives a user a reputation point");
    }

    @Command
    public void command(User invoker, Reaction reaction, Guild guild) {
        User user = reaction.getMessage().getAuthor();
        if (invoker.equals(user)) return;
        GuildUser guildUser = getGuildUser(guild, user);
        changeSetting(GuildUserReputationConfig.class, guildUser, integer -> ++integer);
    }

    @Override
    protected String getLocalUsages() {
        return "This is a reaction based command, react to a user's message with " + getChars("thumbsup", false) + " to activate it";
    }

    @Override
    public boolean useReactions() {
        return true;
    }
}
