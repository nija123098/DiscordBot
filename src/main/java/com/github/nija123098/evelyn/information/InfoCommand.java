package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

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
        maker.appendRaw("I am Evelyn, a music playing auto moderation bot!" +
                "\n" +
                "***Type @Evelyn help*** to see a list of commands. In total there are " + totalCommands + " unique commands I can perform.\n" +
                "\n" +
                "***For help*** about a command type !help <command>\n" +
                "An example: `@Evelyn help ping` to see what you can do with the ping command.\n" +
                "\n" +
                "If you need assistance, want to share your thoughts or want to contribute feel free to join my `!discord`");
    }
}
