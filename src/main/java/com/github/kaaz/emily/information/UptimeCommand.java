package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Presence;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.information.configs.UptimeConfig;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class UptimeCommand extends AbstractCommand {
    public UptimeCommand() {
        super("uptime", ModuleLevel.INFO, "ut", null, "Show the uptime of a user or the bot itself");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) User user, Guild guild, MessageMaker maker){
        if (user == null) user = DiscordClient.getOurUser();
        maker.appendAlternate(true, user.getDisplayName(guild), " has been " + (user.getPresence().getStatus() != Presence.Status.OFFLINE ? "on" : "off") + "line for ", Time.getAbbreviated(System.currentTimeMillis() - ConfigHandler.getSetting(UptimeConfig.class, user)));
    }
}
