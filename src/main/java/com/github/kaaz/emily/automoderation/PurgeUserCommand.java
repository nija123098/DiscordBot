package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeUserCommand extends AbstractCommand {
    public PurgeUserCommand() {
        super(PurgeCommand.class, "user", null, null, null, "Deletes a user's messages in the channel");
    }
    @Command
    public void command(@Argument User user, @Argument(optional = true) Integer integer, Channel channel, Guild guild, String options){// ensures guild context
        MessageDeleteService.delete(options.contains("all") ? channel.getMessages() : channel.getMessagesTo(GuildUserJoinTimeConfig.get(GuildUser.getGuildUser(guild, user))).stream().filter(message -> message.getAuthor().equals(user)).collect(Collectors.toList()));
    }
}
