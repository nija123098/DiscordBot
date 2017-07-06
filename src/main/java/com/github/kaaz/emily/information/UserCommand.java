package com.github.kaaz.emily.information;

import com.github.kaaz.emily.automoderation.GuildUserJoinTimeConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.command.configs.CommandsUsedCountConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 5/21/2017.
 */
public class UserCommand extends AbstractCommand {
    public UserCommand() {
        super("user", ModuleLevel.INFO, "whois", null, "Shows information about the user");
    }
    @Command
    public void command(@Argument(optional = true) User user, @Context(softFail = true) Guild guild, MessageMaker maker){
        maker.appendAlternate(false, "Querying for **", (guild == null ? user.getName() : user.getDisplayName(guild)) + "**\n")
                .withImage(user.getAvatarURL());
        addAtrib(maker, "bust_in_silhouette", "User", user.getNameAndDiscrim());
        addAtrib(maker, "id", "Discord id", user.getID());
        addAtrib(maker, "keyboard", "Commands used", ConfigHandler.getSetting(CommandsUsedCountConfig.class, user) + "");
        addAtrib(maker, "date", "Joined guild", Time.getAbbreviated(System.currentTimeMillis() - ConfigHandler.getSetting(GuildUserJoinTimeConfig.class, GuildUser.getGuildUser(guild, user))) + " ago");
        addAtrib(maker, "calendar_spiral", "Joined discord", Time.getAbbreviated(System.currentTimeMillis() - user.getJoinDate()) + " ago");
    }
    private static void addAtrib(MessageMaker maker, String icon, String info, String content){
        maker.appendAlternate(true, EmoticonHelper.getChars(icon), "  " + info, ": " + content + "\n");
    }
}
