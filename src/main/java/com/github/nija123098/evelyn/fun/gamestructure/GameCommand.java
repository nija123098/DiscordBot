package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Reference;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class GameCommand extends AbstractCommand {
    static final Map<String, Class<? extends AbstractGame>> CLASS_MAP = new HashMap<>();
    static {
        new Reflections(Reference.BASE_PACKAGE).getSubTypesOf(AbstractGame.class).forEach(clazz -> CLASS_MAP.put(clazz.getSimpleName().toLowerCase(), clazz));
    }
    public GameCommand() {
        super("game", ModuleLevel.FUN, null, null, "Lists the games avalable which support team decisions");
    }
    @Command
    public static void command(MessageMaker maker){
        maker.getTitle().append("Games with team support");
        CLASS_MAP.values().stream().filter(clazz -> !Modifier.isAbstract(clazz.getModifiers())).forEach(s -> maker.getNewListPart().appendRaw(s.getSimpleName()));
    }
}
