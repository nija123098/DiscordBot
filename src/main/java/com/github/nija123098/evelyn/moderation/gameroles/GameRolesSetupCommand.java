package com.github.nija123098.evelyn.moderation.gameroles;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GameRolesSetupCommand extends AbstractCommand {
    private static final Set<String> VERIFIED_GAMES = new HashSet<>();
    static {
        try{VERIFIED_GAMES.addAll(Files.readAllLines(Paths.get(ConfigProvider.RESOURCE_FILES.verifiedGames())));
        } catch (IOException e) {
            Log.log("Could not load verified games", e);
        }
    }
    public GameRolesSetupCommand() {
        super(GameRolesCommand.class, "setup", null, null, null, "Sets all roles that match in name with a verified name to be added to a user automatically when they are playing the game");
    }
    @Command
    public void command(Guild guild){
        guild.getRoles().stream().filter(role -> VERIFIED_GAMES.contains(role.getName())).forEach(role -> ConfigHandler.setSetting(GameRoleConfig.class, role, true));
    }
}
