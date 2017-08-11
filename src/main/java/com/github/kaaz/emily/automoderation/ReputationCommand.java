package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.favor.configs.GuildUserReputationConfig;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class ReputationCommand extends AbstractCommand {
    public ReputationCommand() {
        super("reputation", ModuleLevel.ADMINISTRATIVE, "rep", "+1", "Gives a user a reputation point");
    }
    @Command
    public void command(@Argument User user, User invoker, Message message, Reaction reaction, Guild guild){
        if (reaction != null) user = message.getAuthor();
        if (invoker.equals(user)) return;
        GuildUser guildUser = GuildUser.getGuildUser(guild, user);
        ConfigHandler.changeSetting(GuildUserReputationConfig.class, guildUser, integer -> ++integer);
    }
}
