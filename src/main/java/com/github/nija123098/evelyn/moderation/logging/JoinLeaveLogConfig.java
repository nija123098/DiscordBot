package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;

import java.awt.*;

public class JoinLeaveLogConfig extends AbstractConfig<Channel, Guild> {
    public JoinLeaveLogConfig() {
        super("current_money", "join_leave_log", ConfigCategory.LOGGING, (Channel) null, "The location to log users joining and leaving a server");
    }
    @EventListener
    public void handle(DiscordUserJoin join){
        Channel channel;
        if ((channel = this.getValue(join.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel).withColor(Color.MAGENTA).appendRaw(join.getUser().getNameAndDiscrim());
        maker.getTitle().appendRaw("User joined");
        maker.getFooter().appendRaw("ID: " + join.getUser().getID());
        maker.send();
    }
    @EventListener// log rewrite soon
    public void handle(DiscordUserLeave leave){
        Channel channel;
        if ((channel = this.getValue(leave.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel).withColor(Color.MAGENTA).appendRaw(leave.getUser().getNameAndDiscrim());
        maker.getTitle().appendRaw("User left");
        maker.getFooter().appendRaw("ID: " + leave.getUser().getID());
        maker.send();
    }
}
