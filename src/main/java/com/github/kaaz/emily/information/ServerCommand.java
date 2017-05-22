package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildPrefixConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Presence;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.Time;

import java.util.List;

/**
 * Made by nija123098 on 5/21/2017.
 */
public class ServerCommand extends AbstractCommand {
    public ServerCommand() {
        super("server", ModuleLevel.INFO, "guild", null, "Displays information about the server");
    }
    @Command
    public void command(Guild guild, MessageMaker maker){
        maker.withAuthorIcon(guild.getIconURL())
                .getAuthorName().appendRaw(guild.getName()).getMaker()
                .withThumb(guild.getIconURL());
        List<User> users = guild.getUsers();
        addAtrib(maker, "Members", users.stream().filter(user -> user.getPresence().getStatus() != Presence.Status.OFFLINE).count() + " online\n" + users.size() + " total");
        addAtrib(maker, "Channels", guild.getChannels().size() + " text channels\n" + guild.getVoiceChannels().size() + " voice channels");
        addAtrib(maker, "Guild Owner", guild.getOwner().getNameAndDiscrim());
        addAtrib(maker, "Made", Time.getAbbreviated(guild.getCreationDate()) + " ago");
        addAtrib(maker, "My prefix", ConfigHandler.getSetting(GuildPrefixConfig.class, guild));
        addAtrib(maker, "ID", guild.getID());
    }
    private static void addAtrib(MessageMaker maker, String name, String content){
        maker.getNewFieldPart().withBoth(name, content);
    }
}
