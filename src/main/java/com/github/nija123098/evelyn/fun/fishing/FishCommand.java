package com.github.nija123098.evelyn.fun.fishing;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * Written by Soarnir 20/11/17
 */

public class FishCommand extends AbstractCommand {
    public FishCommand() {
        super("fish", null, null, null, "fish for shtuff");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.appendRaw("```" +
                "\u200b       ,-.\n" +
                "\u200b    O /   `.\n" +
                "\u200b   <\\/      `.\n" +
                "\u200b    |*        `.\n" +
                "\u200b   / \\          `.\n" +
                "\u200b  /  /            `>3s,\n" +
                "\u200b--------.+" +
                "```").mustEmbed();
    }
}
