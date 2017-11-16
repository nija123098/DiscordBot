package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.BotConfig.BotConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Written by Soarnir 12/9/17
 */

public class GuideCommand extends AbstractCommand {
    public GuideCommand() {
        super("guide", ModuleLevel.INFO, null, null, "A quickstart guide to Evelyn");
    }

    @Command
    public void command(MessageMaker maker) {
        int counter = 0;
        String GuideText = reader();
        String[] things = GuideText.split("\\|");
        for (String thing : things) {
            if (counter == 5) {
                maker.guaranteeNewListPage();
                counter = 0;
            }
            maker.getNewListPart().appendRaw(thing);
            counter++;
        }
        maker.getTitle().append("A beginner's guide to me");
    }

    public String reader() {
        try {
            StringBuilder texty = new StringBuilder();
            List<String> text = Files.readAllLines(Paths.get(BotConfig.GUIDE_NAME));
            text.forEach(s -> {
                if (s.isEmpty()) texty.append("|");
                else texty.append(s + "\n");
            });
            return texty.toString();
        } catch (IOException e) {
            Log.log("guide.txt not found, do you need a guide for this?");
            return "missing guide.txt, do you need a guide for this?";
        }
    }
}