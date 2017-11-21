package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.favor.configs.GuildUserReputationConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class ReputationCommand extends AbstractCommand {
    public ReputationCommand() {
        super("reputation", ModuleLevel.ADMINISTRATIVE, "rep", "+1", "Gives a user a reputation point");
    }
    @Command
    public void command(User invoker, Reaction reaction, Guild guild){
        User user = reaction.getMessage().getAuthor();
        if (invoker.equals(user)) return;
        GuildUser guildUser = GuildUser.getGuildUser(guild, user);
        ConfigHandler.changeSetting(GuildUserReputationConfig.class, guildUser, integer -> ++integer);
    }

    @Override
    protected String getLocalUsages() {
        return "This is a reaction based command, react to a user's message with " + EmoticonHelper.getChars("thumbsup", false) + " to activate it";
    }

    @Override
    public boolean useReactions() {
        return true;
    }
}
