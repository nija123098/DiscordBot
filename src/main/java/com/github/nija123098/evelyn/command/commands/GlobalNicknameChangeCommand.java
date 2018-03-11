package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.configs.GlobalNicknameConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GlobalNicknameChangeCommand extends AbstractCommand {
    public GlobalNicknameChangeCommand() {
        super("globalnick", BotRole.BOT_ADMIN, ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Changes the global nickname");
    }
    @Command
    public void command(String arg) {
        ConfigHandler.changeSetting(GlobalNicknameConfig.class, GlobalConfigurable.GLOBAL, global -> {
            DiscordClient.getGuilds().stream().filter(guild -> {
                String display = DiscordClient.getOurUser().getDisplayName(guild);
                return display.equals(DiscordClient.getOurUser().getName()) || display.equals(global);
            }).forEach(guild -> guild.setUserNickname(DiscordClient.getOurUser(), arg));
            return arg;
        });
    }
}
