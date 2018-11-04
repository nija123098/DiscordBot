package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordNicknameChange;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ModLogConfig extends AbstractConfig<Channel, Guild> {
    public ModLogConfig() {
        super("mod_log", "Mod Log", ConfigCategory.LOGGING, guild -> guild.getChannels().stream().filter(channel -> FormatHelper.filtering(channel.getName().toLowerCase(), Character::isLetter).contains("modlog")).filter(Channel::canPost).findFirst().orElse(null), "The channel log of moderator actions");
    }

    @EventListener
    public void handle(DiscordNicknameChange event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        MessageMaker maker = new MessageMaker(this.getValue(guild));
        Logging.USER_NICKNAME_UPDATE.userLog(maker, guild, user, event.getOldNickname(), event.getNewNickname());
    }
}
