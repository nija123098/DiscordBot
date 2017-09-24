package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.Time;

import java.util.List;

/**
 * Made by nija123098 on 5/21/2017.
 */
public class ServerCommand extends AbstractCommand {
    public ServerCommand() {
        super("server", ModuleLevel.INFO, "guild", null, "Displays information about the server");
    }
    @Command
    public void command(@Argument(optional = true) Guild guild, MessageMaker maker){
        maker.withAuthorIcon(guild.getIconURL())
                .getAuthorName().appendRaw(guild.getName()).getMaker()
                .withThumb(guild.getIconURL()).withColor(guild.getIconURL());
        List<User> users = guild.getUsers();
        addAtrib(maker, "Members", users.stream().filter(user -> user.getPresence().getStatus() != Presence.Status.OFFLINE).count() + " online\n" + users.size() + " total");
        addAtrib(maker, "Bots", users.stream().filter(User::isBot).count() + "");
        addAtrib(maker, "Channels", guild.getChannels().size() + " text channels\n" + guild.getVoiceChannels().size() + " voice channels");
        addAtrib(maker, "Guild Owner", guild.getOwner().getNameAndDiscrim());
        addAtrib(maker, "Made", Time.getAbbreviated(System.currentTimeMillis() - guild.getCreationDate()) + " ago");
        addAtrib(maker, "My prefix", ConfigHandler.getSetting(GuildPrefixConfig.class, guild));
        addAtrib(maker, "ID", guild.getID());
    }
    private static void addAtrib(MessageMaker maker, String name, String content){
        MessageMaker.FieldPart part = maker.getNewFieldPart();
        part.getTitle().append(name);
        part.getValue().appendRaw(content);
    }
}
