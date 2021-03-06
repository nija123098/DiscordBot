package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.util.ThreadHelper;
import sx.blah.discord.handle.audit.ActionType;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordUserLeave implements BotEvent {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "User-Leave-Thread"));
    private User responsible;
    private Boolean kicked, banned;
    private UserLeaveEvent event;
    public DiscordUserLeave(UserLeaveEvent event) {
        this.event = event;
        EXECUTOR_SERVICE.schedule(() -> {
            this.kicked = !Guild.getGuild(this.event.getGuild()).guild().getAuditLog(ActionType.MEMBER_KICK).getEntriesByTarget(event.getUser().getLongID()).isEmpty();
            this.banned = Guild.getGuild(this.event.getGuild()).getBannedUsers().contains(User.getUser(event.getUser()));
            if (wasForced()) {// todo add auditing to normal access
                this.responsible = User.getUser(this.event.getGuild().getAuditLog(this.wasKicked() ? ActionType.MEMBER_KICK : ActionType.MEMBER_BAN_ADD).getEntryByID(this.event.getUser().getLongID()).getResponsibleUser());
            }
        }, 1, TimeUnit.SECONDS);
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
    public User getUser() {
        return User.getUser(this.event.getUser());
    }
    public synchronized boolean wasKicked() {
        return kicked;
    }
    public synchronized boolean wasBanned() {
        return this.banned;
    }
    public synchronized User responsibleUser() {
        return this.responsible;
    }
    public boolean wasForced() {
        return this.wasBanned() || this.wasKicked();
    }
}
