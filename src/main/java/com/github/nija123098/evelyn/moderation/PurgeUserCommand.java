package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import static com.github.nija123098.evelyn.config.GuildUser.getGuildUser;
import static com.github.nija123098.evelyn.moderation.GuildUserJoinTimeConfig.get;
import static com.github.nija123098.evelyn.moderation.MessageDeleteService.delete;
import static java.util.stream.Collectors.toList;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PurgeUserCommand extends AbstractCommand {
    public PurgeUserCommand() {
        super(PurgeCommand.class, "user", null, null, null, "Deletes a user's messages in the channel");
    }

    @Command
    public void command(@Argument User user, Channel channel, Guild guild, String options) {// ensures guild context
        delete(options.contains("all") ? channel.getMessages() : channel.getMessagesTo(get(getGuildUser(guild, user))).stream().filter(message -> message.getAuthor().equals(user)).collect(toList()));
    }
}
