package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildPrefixConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class PrefixCommand extends AbstractCommand {
    public PrefixCommand() {
        super("prefix", ModuleLevel.ADMINISTRATIVE, null, null, "Shows or can change the prefix for this guild.");
    }
    @Command
    public void command(MessageMaker maker, Guild guild, User user, String s){
        if ((s != null && !s.isEmpty()) && BotRole.GUILD_TRUSTEE.hasRequiredRole(user, guild)) {
            ConfigHandler.setSetting(GuildPrefixConfig.class, guild, s);
        }
        maker.appendAlternate(false, "My prefix in this guild is `", ConfigHandler.getSetting(GuildPrefixConfig.class, guild), "`\n To change it use the config command");
    }
}
