package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class GlobalNicknameChangeCommand extends AbstractCommand {
    public GlobalNicknameChangeCommand() {
        super("globalnick", BotRole.BOT_ADMIN, ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Changes the global nickname");
    }
    @Command
    public void command(String arg, MessageMaker maker){
        String global = ConfigHandler.getSetting(GlobalNicknameConfig.class, GlobalConfigurable.GLOBAL);
        DiscordClient.getGuilds().stream().filter(guild -> {
            String display = DiscordClient.getOurUser().getDisplayName(guild);
            return display.equals(DiscordClient.getOurUser().getName()) || display.equals(global);
        }).forEach(guild -> guild.setUserNickname(DiscordClient.getOurUser(), arg));
    }
}
