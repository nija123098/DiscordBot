package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.*;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.VerificationLevel;

import java.util.Collection;
import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class GuildLogConfig extends AbstractConfig<Channel, Guild> {
    public GuildLogConfig() {
        super("guild_edit_log", "Guild Edit Log", ConfigCategory.LOGGING, (Channel) null, "Logs guild changes");
    }

    @EventListener
    public void handle(DiscordGuildUpdate event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null) return;
        IGuild oldGuild = event.getOldGuild();
        IGuild newGuild = event.getNewGuild();
        if (!oldGuild.getName().equals(newGuild.getName())) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.GUILD_NAME_UPDATE.guildLog(maker, event.getGuild(), oldGuild.getName(), newGuild.getName());
        }
        if (!oldGuild.getIconURL().equals(newGuild.getIconURL())) {
            MessageMaker maker = new MessageMaker(channel);
            maker.withThumb(newGuild.getIconURL());
            Logging.GUILD_ICON_UPDATE.guildLog(maker, event.getGuild(), oldGuild.getIconURL(), newGuild.getIconURL());
        }
        if (!oldGuild.getOwner().equals(newGuild.getOwner())) {
            MessageMaker maker = new MessageMaker(channel);
            maker.withThumb(newGuild.getOwner().getAvatarURL());
            Logging.GUILD_OWNER_UPDATE.guildLog(maker, event.getGuild(), Guild.getGuild(oldGuild).getOwner().getNameAndDiscrim(), Guild.getGuild(newGuild).getOwner().getNameAndDiscrim());
        }
        if (!oldGuild.getRegion().equals(newGuild.getRegion())) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.GUILD_REGION_UPDATE.guildLog(maker, event.getGuild(), oldGuild.getRegion().getName(), newGuild.getRegion().getName());
        }
        if (!oldGuild.getVerificationLevel().name().equals(newGuild.getVerificationLevel().name())) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.GUILD_VERIFICATION_UPDATE.guildLog(maker, event.getGuild(), verificationText(oldGuild.getVerificationLevel()), verificationText(newGuild.getVerificationLevel()));
        }
    }

    @EventListener
    public void handle(DiscordEmojiUpdate event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel);
        Collection<IEmoji> oldEmoji = event.getOldEmoji();
        Collection<IEmoji> newEmoji = event.getNewEmoji();
        Collection<IEmoji> temp = event.getOldEmoji();
        oldEmoji.removeAll(newEmoji);
        newEmoji.removeAll(temp);
        if (newEmoji.size() == 0) {
            Logging.GUILD_EMOJI_DELETION.emojiLog(maker, ((List<IEmoji>) oldEmoji).get(0));
        } else if (oldEmoji.size() == 0) {
            Logging.GUILD_EMOJI_CREATION.emojiLog(maker, ((List<IEmoji>) newEmoji).get(0));
        }
    }

    private String verificationText(VerificationLevel verificationLevel) {
        String ret = "";
        switch (verificationLevel) {
            case NONE:
                ret = "NONE\n*Anyone can join*";
                break;
            case LOW:
                ret = "LOW\n*Verified email*";
                break;
            case MEDIUM:
                ret = "MEDIUM\n*Registered 5+ minutes*";
                break;
            case HIGH:
                ret = "(╯°□°）╯︵ ┻━┻\n*10 minute wait in server*";
                break;
            case EXTREME:
                ret = "┻━┻ ﾐヽ(ಠ益ಠ)ノ彡┻━┻\n*Verified phone*";
                break;
            case UNKNOWN:
                ret = "*this really should not happen*";
                break;
        }
        return ret;
    }
}
