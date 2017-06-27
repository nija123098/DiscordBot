package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.perms.BotRole;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class ClassLocationCommand extends AbstractCommand {
    public ClassLocationCommand() {
        super("classlocation", BotRole.CONTRIBUTOR, ModuleLevel.DEVELOPMENT, null, null, "Shows the location of a class");
    }
    @Command
    public void command(MessageMaker maker, String s) throws ClassNotFoundException {
        maker.appendRaw(new Reflections(Reference.BASE_PACKAGE, new SubTypesScanner(false)).getSubTypesOf(Object.class).stream().filter(aClass -> aClass.getSimpleName().equals(s)).map(Class::getName).findAny().orElse("None found"));
    }
}
