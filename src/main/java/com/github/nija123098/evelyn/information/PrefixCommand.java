package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class PrefixCommand extends AbstractCommand {
    public PrefixCommand() {
        super("prefix", ModuleLevel.ADMINISTRATIVE, null, null, "Shows or changes the prefix for this guild.");
    }
    @Command
    public void command(MessageMaker maker, Guild guild, User user, String s){
        if ((s != null && !s.isEmpty()) && BotRole.GUILD_TRUSTEE.hasRequiredRole(user, guild)) {
            ConfigHandler.setSetting(GuildPrefixConfig.class, guild, s);
        }
        maker.appendAlternate(false, "My prefix in this guild is `", ConfigHandler.getSetting(GuildPrefixConfig.class, guild), "`\n To change it use the config command");
    }
}
