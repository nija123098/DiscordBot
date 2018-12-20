package com.github.nija123098.evelyn.information.descriptions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DescriptionCommand extends AbstractCommand {
    public DescriptionCommand() {
        super("description", ModuleLevel.INFO, null, null, "Shows the information a user set for his or her self");
    }
    @Command
    public void command(@Argument User user, @Context(softFail = true) Guild guild, MessageMaker maker) {
        String info;
        if (guild == null || (info = ConfigHandler.getSetting(GuildUserDescriptionConfig.class, GuildUser.getGuildUser(guild, user))) == null) {
            info = ConfigHandler.getSetting(UserDescriptionConfig.class, user);
        }
        maker.append(info).withAuthorIcon(user.getAvatarURL()).getTitle().appendRaw(user.getDisplayName(guild));
    }
}
