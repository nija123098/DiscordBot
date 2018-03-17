package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.config.configs.guild.GuildIgnoreConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class StatsIgnoreCommand extends AbstractCommand {

    public StatsIgnoreCommand() {
        super(StatsCommand.class,"ignore", null, null, null, "toggles whether a guild should be ignored from stats count");
    }

    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Guild guild, MessageMaker maker) {
        maker.getTitle().appendRaw("Guild ignore list");
        if (guild != null) {
            ConfigHandler.alterSetting(GuildIgnoreConfig.class, GlobalConfigurable.GLOBAL, guilds -> {
                if (guilds.contains(guild)) {
                    guilds.remove(guild);
                    maker.appendRaw("Removed " + guild.getName() + " from the ignored list");
                } else {
                    guilds.add(guild);
                    maker.appendRaw("Added " + guild.getName() + " to the ignored list");
                }
            });
        } else {
            ConfigHandler.getSetting(GuildIgnoreConfig.class, GlobalConfigurable.GLOBAL).forEach(guild1 -> maker.getNewListPart().appendRaw(guild1.getName() + " | " + guild1.getID()));
        }
    }
}