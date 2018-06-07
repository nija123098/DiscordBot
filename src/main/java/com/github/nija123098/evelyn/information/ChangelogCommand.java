package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Soarnir, nija123098
 * @since 1.0.0
 */
public class ChangelogCommand extends AbstractCommand {

    public ChangelogCommand() {
        super("changelog", ModuleLevel.INFO, "cl", null, "view the current changelog");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.getTitle().append("Changelog | " + Launcher.EVELYN_VERSION);
        try {
            Files.readAllLines(Paths.get(ConfigProvider.RESOURCE_FILES.changeLog())).forEach(s -> {
                if (s.equalsIgnoreCase("newpage")) maker.guaranteeNewListPage();
                else maker.getNewListPart().append(s);
            });
        } catch (IOException e) {
            Log.log(ConfigProvider.RESOURCE_FILES.changeLog() + " not found");
        }

    }
}