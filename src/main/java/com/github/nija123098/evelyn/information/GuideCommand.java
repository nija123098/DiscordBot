package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;
import sun.nio.cs.StandardCharsets;
import sun.text.normalizer.UnicodeSet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
            final String[] texty = {""};
            List<String> text = Files.readAllLines(Paths.get(BotConfig.CONTAINER_PATH, "guide.txt"));
            text.forEach(s -> {
                if (s.isEmpty()) {
                    texty[0] = texty[0] + "|";
                } else {
                    texty[0] = texty[0] + (s + "\n");
                }
            });
            return texty[0];
        } catch (IOException e) {
            return "can't find guide.txt /shrug";
        }
    }
}