package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeUserCommand extends AbstractCommand {
    public PurgeUserCommand() {
        super(PurgeCommand.class, "user", null, null, null, "Deletes a user's messages in the channel");
    }
    @Command
    public void command(@Argument User user, Channel channel, Guild guild, String options){// ensures guild context
        MessageDeleteService.delete(options.contains("all") ? channel.getMessages() : channel.getMessagesTo(GuildUserJoinTimeConfig.get(GuildUser.getGuildUser(guild, user))).stream().filter(message -> message.getAuthor().equals(user)).collect(Collectors.toList()));
    }
}
