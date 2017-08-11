package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class InfoCommand extends AbstractCommand {
    private static int totalCommands = -1;
    public InfoCommand() {
        super("info", ModuleLevel.INFO, "about, information", null, "Gives some information about me");
    }
    @Command
    public void command(MessageMaker maker){
        if (totalCommands == -1) totalCommands = (int) CommandHandler.getCommands().stream().filter(AbstractCommand::isHighCommand).filter(o -> !o.isTemplateCommand()).count();
        maker.append("What am I? ***I'm batman***\n" +
                "My purpose? ***What is your purpose in life?***\n" +
                "Who made me? ***Kaaz***\n" +
                "\n" +
                "***Type @Emily help*** to see what I'll allow you to do. In total there are " + totalCommands + " unique commands I can perform.\n" +
                "\n" +
                "***For help*** about a specific command type !help <command>\n" +
                "An example: `@Emily help skip` to see what you can do with the skip command.\n" +
                "\n" +
                "If you need assistance, want to share your thoughts or want to contribute feel free to join my ***!discord***");
    }
}
