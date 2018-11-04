package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.moderation.logging.*;
import sx.blah.discord.util.MissingPermissionsException;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.EnumSet;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class SetupCommand extends AbstractCommand {

    public SetupCommand() {
        super("setup", ModuleLevel.ADMINISTRATIVE, null, null, "Setup bot channels automatically");
    }

    @Command
    public void command(MessageMaker maker, Guild guild, User user, Channel channel) {

        maker.getTitle().appendRaw("Setup");
        maker.withColor(new Color(175, 30,5));
        maker.appendRaw("3 channels will be created.\nbot_console\nbot_log\nmod_log\nThese will be setup to allow you access to them according to your highest role.\n");
        maker.appendRaw(channel.getCategory() == null ? "These channels will be created at the top of your discord server, use this command in a category to create them there" : "These channels will be created in this server category, to create them outside, use this command in a channel which is not in a category.");
        maker.withAutoSend(false);

        maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) { }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

        }));

        //if send
        maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) { }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

            MessageMaker maker2 = new MessageMaker(channel);
            String channelID, logChannelID, modLogChannelID;

            Role userRole, botRole = null, everyoneRole = guild.getEveryoneRole();
            userRole = user.getRolesForGuild(guild).get(0);
            if (userRole == null) {
                maker.appendRaw("You do not appear to have any roles, I need to be able to add one of your roles to the permissions of the channel.");
            }
            for (Role role : DiscordClient.getOurUser().getRolesForGuild(guild)) {
                if (role.getName().equals(DiscordClient.getOurUser().getName())) botRole = role;
            }
            EnumSet<DiscordPermission> fullPermissions = EnumSet.noneOf(DiscordPermission.class);
            EnumSet<DiscordPermission> emptyPermissions = EnumSet.noneOf(DiscordPermission.class);
            EnumSet<DiscordPermission> everyonePermissions = EnumSet.noneOf(DiscordPermission.class);

            fullPermissions.add(DiscordPermission.MANAGE_MESSAGES);
            fullPermissions.add(DiscordPermission.READ_MESSAGES);
            fullPermissions.add(DiscordPermission.SEND_MESSAGES);
            fullPermissions.add(DiscordPermission.READ_MESSAGE_HISTORY);
            fullPermissions.add(DiscordPermission.MANAGE_CHANNEL);
            fullPermissions.add(DiscordPermission.ADD_REACTIONS);
            fullPermissions.add(DiscordPermission.EMBED_LINKS);
            fullPermissions.add(DiscordPermission.USE_EXTERNAL_EMOJIS);

            everyonePermissions.add(DiscordPermission.READ_MESSAGES);

            if (channel.getCategory() != null) {
                try {
                    channelID = channel.getCategory().category().createChannel("bot_console").getStringID();
                    logChannelID = channel.getCategory().category().createChannel("bot_log").getStringID();
                    modLogChannelID = channel.getCategory().category().createChannel("mod_log").getStringID();
                    guild.getChannelByID(channelID).changeTopic("For configuring all bot settings");
                    guild.getChannelByID(logChannelID).changeTopic("For general logging");
                    guild.getChannelByID(modLogChannelID).changeTopic("For logging moderation actions");
                    guild.getChannelByID(channelID).overrideRolePermissions(botRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(channelID).overrideRolePermissions(userRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(channelID).overrideRolePermissions(everyoneRole, emptyPermissions, everyonePermissions);
                    guild.getChannelByID(logChannelID).overrideRolePermissions(botRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(logChannelID).overrideRolePermissions(userRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(logChannelID).overrideRolePermissions(everyoneRole, emptyPermissions, everyonePermissions);
                    guild.getChannelByID(modLogChannelID).overrideRolePermissions(botRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(modLogChannelID).overrideRolePermissions(userRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(modLogChannelID).overrideRolePermissions(everyoneRole, emptyPermissions, everyonePermissions);

                    ConfigHandler.setSetting(ModLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(BotLogConfig.class, guild, guild.getChannelByID(logChannelID));
                    ConfigHandler.setSetting(MessageDeleteLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(MessageEditLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(JoinLeaveLogConfig.class, guild, guild.getChannelByID(logChannelID));
                    ConfigHandler.setSetting(GuildLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(ChannelLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(RoleLogConfig.class, guild,guild.getChannelByID(modLogChannelID));
                    //ConfigHandler.setSetting(ServerLogConfig.class, guild, guild.getChannelByID(logChannelID));

                    maker2.appendRaw("it would appear that the channels were created successfully, well done");
                } catch (MissingPermissionsException | PermissionsException e) {
                    maker2.appendRaw("I could not create the channels, ensure I have the `MANAGE CHANNELS` permission active in my role");
                }
            } else {
                try {
                    channelID = guild.createChannel("bot_console").getID();
                    logChannelID = guild.createChannel("bot_log").getID();
                    modLogChannelID = guild.createChannel("mod_log").getID();
                    guild.getChannelByID(channelID).changeTopic("For configuring all bot settings");
                    guild.getChannelByID(logChannelID).changeTopic("For general logging");
                    guild.getChannelByID(modLogChannelID).changeTopic("For logging moderation actions");
                    guild.getChannelByID(channelID).overrideRolePermissions(botRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(channelID).overrideRolePermissions(userRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(channelID).overrideRolePermissions(guild.getEveryoneRole(), emptyPermissions, everyonePermissions);
                    guild.getChannelByID(logChannelID).overrideRolePermissions(botRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(logChannelID).overrideRolePermissions(userRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(logChannelID).overrideRolePermissions(guild.getEveryoneRole(), emptyPermissions, everyonePermissions);
                    guild.getChannelByID(modLogChannelID).overrideRolePermissions(botRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(modLogChannelID).overrideRolePermissions(userRole, fullPermissions, emptyPermissions);
                    guild.getChannelByID(modLogChannelID).overrideRolePermissions(guild.getEveryoneRole(), emptyPermissions, everyonePermissions);

                    ConfigHandler.setSetting(ModLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(BotLogConfig.class, guild, guild.getChannelByID(logChannelID));
                    ConfigHandler.setSetting(MessageDeleteLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(MessageEditLogConfig.class, guild, guild.getChannelByID(modLogChannelID));
                    ConfigHandler.setSetting(JoinLeaveLogConfig.class, guild, guild.getChannelByID(logChannelID));
                    //ConfigHandler.setSetting(ServerLogConfig.class, guild, guild.getChannelByID(logChannelID));

                    maker2.appendRaw("it would appear that the channels were created successfully, well done");
                } catch (MissingPermissionsException | PermissionsException e) {
                    maker2.appendRaw("I could not create the channels, ensure I have the `MANAGE CHANNELS` permission active in my role");
                }
            }
            maker.sentMessage().delete();
            maker2.withoutReactionBehavior("green_tick");
            maker2.forceCompile().send();
        }));

        maker.forceCompile().send();
    }
}