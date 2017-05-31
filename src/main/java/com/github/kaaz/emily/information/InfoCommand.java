package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class InfoCommand extends AbstractCommand {
    public InfoCommand() {
        super("info", ModuleLevel.INFO, "about, information", null, "Gives some information about me");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("What am I? ***I'm batman***\n" +
                "My purpose? ***What is your purpose in life?***\n" +
                "Who made me? ***Kaaz***\n" +
                "\n" +
                "***Type !help*** to see what I'll allow you to do. In total there are 84 commands I can perform.\n" +
                "\n" +
                "***For help*** about a specific command type !<command> help\n" +
                "An example: !help skip to see what you can do with the skip command.\n" +
                "\n" +
                "If you need assistance, want to share your thoughts or want to contribute feel free to join my ***!discord***");
    }
}
