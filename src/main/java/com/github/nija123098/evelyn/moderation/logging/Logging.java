package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.util.DiscordException;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */

public enum Logging {
    MOD_ACTION("Mod action"),
    KICK_ACTION("User kicked", MOD_ACTION),
    BAN_ACTION("User banned", MOD_ACTION),
    MUTE_ACTION("User muted",MOD_ACTION),
    WARN_ACTION("User warned", MOD_ACTION),
    EXPUNGE_ACTION("User expunged", MOD_ACTION),
    TEMPBAN_ACTION("User tempbanned", MOD_ACTION),
    ARCHIVE_ACTION("Channel archived", MOD_ACTION),
    PURGE_ACTION("Purge", MOD_ACTION),
    GUILD_ACTION("Guild action"),
    GUILD_JOIN("Guild joined", GUILD_ACTION),
    GUILD_LEAVE("Guild left", GUILD_ACTION),
    GUILD_OWNER_UPDATE("Guild owner changed", GUILD_ACTION, true),
    GUILD_NAME_UPDATE("Guild name changed", GUILD_ACTION, true),
    GUILD_ICON_UPDATE("Guild icon changed", GUILD_ACTION, true),
    GUILD_REGION_UPDATE("Guild region changed", GUILD_ACTION, true),
    GUILD_VERIFICATION_UPDATE("Guild verification level changed", GUILD_ACTION, true),
    GUILD_EMOJI_UPDATE("Emoji updated", GUILD_ACTION, true),
    GUILD_EMOJI_CREATION("Emoji created", GUILD_ACTION),
    GUILD_EMOJI_DELETION("Emoji deleted", GUILD_ACTION),
    GUILD_TRUST_ACTION("User trusted", GUILD_ACTION),
    GUILD_RESTRICTION_UPDATE("Restriction applied", GUILD_ACTION),
    CHANNEL_ACTION("Channel action"),
    CHANNEL_NAME_UPDATE("Channel name changed", CHANNEL_ACTION, true),
    CHANNEL_TOPIC_UPDATE("Channel topic changed", CHANNEL_ACTION, true),
    CHANNEL_NSFW_UPDATE("Channel NSFW status changed", CHANNEL_ACTION, true),
    CHANNEL_CREATION("Channel created", CHANNEL_ACTION),
    CHANNEL_DELETION("Channel deleted", CHANNEL_ACTION),
    MESSAGE_ACTION("Message action"),
    MESSAGE_EDITED("Message edited", MESSAGE_ACTION, true),
    MESSAGE_DELETED("Message deleted", MESSAGE_ACTION),
    ROLE_ACTION("Role action"),
    ROLE_NAME_UPDATE("Role name changed", ROLE_ACTION, true),
    ROLE_HOIST_UPDATE("Role hoist status", ROLE_ACTION, true),
    ROLE_MENTIONABLE_UPDATE("Role mentionable status", ROLE_ACTION, true),
    ROLE_COLOUR_UPDATE("Role colour changed", ROLE_ACTION, true),
    ROLE_ADMINISTRATOR_UPDATE("Role Administrator permission changed", ROLE_ACTION, true),
    ROLE_CREATION("Role created", ROLE_ACTION),
    ROLE_DELETION("Role deleted", ROLE_ACTION),
    USER_ACTION("User action"),
    USER_LEAVE("User left", USER_ACTION),
    USER_JOIN("User joined", USER_ACTION),
    USER_NICKNAME_UPDATE("User nickname changed", USER_ACTION, true),
    USER_ROLE_REMOVED("User role removed", USER_ACTION, true),
    USER_ROLE_ADDED("User role added", USER_ACTION, true),
    COMMAND_USED("Command used", USER_ACTION);

    private final String description;
    private Logging category;
    private boolean change;

    /**
     *
     * @param description Description
     */
    Logging(String description) {
        this.description = description;
    }

    /**
     *
     * @param description Description
     * @param category Category
     */
    Logging(String description, Logging category) {
        this.category = category;
        this.description = description;
    }

    /**
     *
     * @param description Description
     * @param category Category
     * @param change Change
     */
    Logging(String description, Logging category, Boolean change) {
        this.description = description;
        this.category = category;
        this.change = change;
    }

    /**
     *
     * @param maker MessageMaker
     * @param channel Channel
     * @param user User
     * @param info String[]
     */
    public void commandLog(MessageMaker maker, Channel channel, User user, Message message, String...info) {
        maker.getTitle().appendRaw("Command Used");
        maker.getNewFieldPart().withBoth(info[0], message.getMentionCleanedContent()).withInline(false);
        maker.getNewFieldPart().withBoth("User", user.mention()).withInline(true);
        maker.getNewFieldPart().withBoth("Channel", channel.mention()).withInline(true);
        maker.getNote().appendRaw("User: " + user.getID() + " • Channel: " + channel.getID());
        maker.withTimestamp(System.currentTimeMillis()).send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param user User
     * @param info String[]
     */
    public void userJoinLeaveLog(MessageMaker maker, User user, String...info) {
        Log.log("no, over here");
        maker.getTitle().appendRaw(this.description);
        if (this.equals(Logging.USER_JOIN)) {
            maker.withColor(new Color(39, 209, 110));
        } else {
            maker.withColor(new Color(255, 0 ,0));
        }
        maker.getNewFieldPart().withBoth("User", user.getNameAndDiscrim());
        maker.getNewFieldPart().withBoth("ID", user.getID());
        maker.withTimestamp(System.currentTimeMillis());
        maker.send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param channel Channel
     * @param user User
     * @param messages Messages[]
     */
    public void messageLog(MessageMaker maker, Channel channel, User user, Message message, String...messages) {
        maker.getTitle().appendRaw(this.description);
        if (this.change) {
            maker.append("Message from ").appendRaw(user.getDisplayName(channel.getGuild())).appendRaw(" edited ").append("in ").appendRaw(channel.mention());
            maker.getNewFieldPart().withInline(false).withBoth("Previous Message", messages[0]);
            maker.getNewFieldPart().withInline(false).withBoth("Current Message", messages[1]);
        } else {
            maker.withColor(Color.GRAY).withAuthor(message.getAuthor());
            maker.appendRaw("Message from ").appendRaw(user.getDisplayName(channel.getGuild())).appendRaw(" deleted in ").appendRaw(channel.mention());
            maker.getNewFieldPart().withBoth("Message", message.getMentionCleanedContent());
        }
        maker.getNote().appendRaw("Message: " + message.getID() + " • User: " + user.getID());
        maker.withTimestamp(System.currentTimeMillis());
        Attachment attachment = message.getAttachments().stream().filter(att -> att.getUrl().endsWith("gif") || att.getUrl().endsWith("webv")).findFirst().orElse(null);
        if (attachment != null) maker.withImage(attachment.getUrl());
        maker.send();
    }

    /**
     *
     * @param channel Channel
     * @param info String[]
     */
    public void channelLog(MessageMaker maker, Channel channel, String...info) {
        maker.getTitle().appendRaw(this.description);
        if (this.change) {
            maker.getNewFieldPart().withBoth("Previous Value", info[0]);
            maker.getNewFieldPart().withBoth("Current Value", info[1]);
        }
        maker.getNewFieldPart().withBoth("Channel", channel.isDeleted() ? "#" + channel.getName() : channel.mention());
        maker.getNewFieldPart().withBoth("Category", channel.getCategory().getName());
        maker.withTimestamp(System.currentTimeMillis());
        maker.getNote().appendRaw("Channel: " + channel.getID());
        maker.send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param role Role
     * @param info String[]
     */
    public void roleLog(MessageMaker maker, Role role, String...info) {
        maker.getTitle().appendRaw(this.description);
        if (this.change) {
            maker.getNewFieldPart().withBoth("Previous Value", info[0]);
            maker.getNewFieldPart().withBoth("Current Value", info[1]);
            maker.getNewFieldPart().withInline(false).withBoth("Role", role.mention());
        } else {
            maker.appendRaw(FormatHelper.embedLink("`" + role.getName() + "`", ""));
        }
        maker.withTimestamp(System.currentTimeMillis());
        maker.getNote().appendRaw("Role: " + role.getID());
        maker.send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param role Role
     * @param user User
     * @param info String[]
     */
    public void userRoleLog(MessageMaker maker, Role role, User user, String...info) {
        maker.getTitle().appendRaw(this.description);
        maker.getNewFieldPart().withBoth("Role", FormatHelper.embedLink("`" + role.getName() + "`", ""));
        maker.getNewFieldPart().withBoth("User", user.getNameAndDiscrim());
        maker.withTimestamp(System.currentTimeMillis());
        maker.getNote().appendRaw("Role: " + role.getID() + " • User: " + user.getID());
        maker.send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param guild Guild
     * @param user User
     * @param info String[]
     */
    public void userLog(MessageMaker maker, Guild guild, User user, String...info) {
        maker.getTitle().appendRaw(this.description);
        maker.getNewFieldPart().withBoth("Previous Value", info[0]);
        maker.getNewFieldPart().withBoth("Current Value", info[1]);
        maker.getNewFieldPart().withInline(false).withBoth("User", user.mention());
        maker.withTimestamp(System.currentTimeMillis()).getNote().appendRaw("User: " + user.getID());
        maker.send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param emoji IEmoji
     * @param info String[]
     */
    public void emojiLog(MessageMaker maker, IEmoji emoji, String...info) {
        maker.getTitle().appendRaw(this.description);
        if (this.change) {
            maker.getNewFieldPart().withBoth("Previous Value", info[0]);
            maker.getNewFieldPart().withBoth("Current Value", info[1]);
        } else {
            maker.getNewFieldPart().withBoth("Emoji", ":" + emoji.getName() + ":");
        }
        maker.withTimestamp(System.currentTimeMillis()).getNote().appendRaw("Emoji: " + emoji.getStringID());
        maker.send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param guild Guild
     * @param info String[]
     */
    public void guildLog(MessageMaker maker, Guild guild, String...info) {
        maker.getTitle().appendRaw(this.description);
        if (this.change) {
            maker.getNewFieldPart().withBoth("Previous Value", info[0]);
            maker.getNewFieldPart().withBoth("Current Value", info[1]);
        } else if (this.equals(Logging.GUILD_TRUST_ACTION)){
            maker.getNewFieldPart().withBoth("User", info[0]);
            maker.getNote().appendRaw("User: " + info[1] + " • ");
        } else if (this.equals(Logging.GUILD_RESTRICTION_UPDATE)) {
            maker.getNewFieldPart().withBoth("Channel", info[0]);
            maker.getNote().appendRaw("Channel: " + info[1] + " • ");
        }
        maker.withTimestamp(System.currentTimeMillis()).getNote().appendRaw("Guild: " + guild.getID());
        maker.send();
    }

    /**
     *
     * @param maker MessageMaker
     * @param guild Guild
     * @param owner User
     * @param info String[]
     */
    public void botGuildLog(MessageMaker maker, Guild guild, User owner, String...info) {
        maker.getHeader().appendRaw("Owner: " + owner.getNameAndDiscrim() + " | " + owner.getID());
        maker.getAuthorName().appendRaw(guild.getName());
        maker.withThumb(guild.getIconURL().contains("null") ? ConfigProvider.URLS.discordWhitePng() : guild.getIconURL());
        if (this.equals(Logging.GUILD_LEAVE)) {
            maker.withAuthorIcon(ConfigProvider.URLS.redArrowPng()).withColor(new Color(255, 0 ,0));
            maker.appendRaw("\u200b                                           \u200b\nDate created: " + Time.getDate(guild.getCreationDate()) + "\nUsers: " + (guild.getUsers().stream().filter(user -> !user.isBot()).collect(Collectors.toList()).size() - 1) + "\nTime in guild: " + info[0] + "\nTotal guilds: " + DiscordClient.getGuilds().size());
        } else {
            maker.withAuthorIcon(ConfigProvider.URLS.greenArrowPng()).withColor(new Color(39, 209, 110));
            maker.appendRaw("\u200b                                           \u200b\nDate created: " + Time.getDate(guild.getCreationDate()) + "\nUsers: " + (guild.getUsers().stream().filter(user -> !user.isBot()).collect(Collectors.toList()).size() - 1) + "\nTotal guilds: " + DiscordClient.getGuilds().size());
        }
        maker.withTimestamp(System.currentTimeMillis());
        List<User> bots = guild.getUsers().stream().filter(User::isBot).collect(Collectors.toList());
        if (bots.size() > 0) {
            StringBuilder botList = new StringBuilder();
            for (User user : bots) {
                try {
                    if (!user.equals(DiscordClient.getOurUser())) {
                        botList.append("```\n" + user.getNameAndDiscrim()).append("\nSince: ").append(Time.getDate(guild.getJoinTimeForUser(user))).append("\n```");
                    }
                } catch (DiscordException ignored) {}
            }
            if (botList.toString().length() > 1000) {
                maker.getNewFieldPart().withBoth("Bots", "" + bots.size());
            } else {
                maker.getNewFieldPart().withBoth("Bots: " + bots.size(), botList.toString());
            }
        }
        maker.send();
    }
}