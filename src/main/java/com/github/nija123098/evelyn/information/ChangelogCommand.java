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
import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChangelogCommand extends AbstractCommand {

    public ChangelogCommand() {
        super("changelog", ModuleLevel.INFO, "cl", null, "view the current changelog");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.mustEmbed();
        String changeLog = reader();
        String[] things = changeLog.split("\\|");
        for (String thing : things) {
            if (thing.equals("%")) {
                maker.guaranteeNewListPage();
            } else {
                maker.getNewListPart().appendRaw(thing);
            }
        }
        maker.getTitle().append("Changelog | " + Launcher.EVELYN_VERSION);
    }

    public String reader() {
        try {
            StringBuilder texty = new StringBuilder();
            List<String> text = Files.readAllLines(Paths.get(ConfigProvider.RESOURCE_FILES.changeLog()));
            text.forEach(s -> {
                if (s.isEmpty()) {
                    texty.append("|");
                } else if (s.contains("newpage")) {
                    texty.append("|%|");
                }
                else texty.append(s + "\n");
            });
            return texty.toString();
        } catch (IOException e) {
            Log.log(ConfigProvider.RESOURCE_FILES.changeLog() + " not found");
            return "missing " + ConfigProvider.RESOURCE_FILES.changeLog();
        }
    }
}