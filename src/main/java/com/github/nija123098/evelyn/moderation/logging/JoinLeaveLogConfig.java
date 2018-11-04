package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.nija123098.evelyn.util.Log;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class JoinLeaveLogConfig extends AbstractConfig<Channel, Guild> {
    public JoinLeaveLogConfig() {
        super("join_leave_log", "Join Leave Log", ConfigCategory.LOGGING, (Channel) null, "The location to log users joining and leaving a server");
    }

    @EventListener
    public void handle(DiscordUserJoin join) {
        Channel channel;
        if ((channel = this.getValue(join.getGuild())) == null) return;
        User user = join.getUser();
        MessageMaker maker = new MessageMaker(channel);
        Logging.USER_JOIN.userJoinLeaveLog(maker, user);
    }
    @EventListener
    public void handle(DiscordUserLeave leave) {
        Log.log("over here");
        Channel channel;
        if ((channel = this.getValue(leave.getGuild())) == null) return;
        User user = leave.getUser();
        MessageMaker maker = new MessageMaker(channel).appendRaw(leave.getUser().getNameAndDiscrim());
        Logging.USER_LEAVE.userJoinLeaveLog(maker, user);
    }
}
