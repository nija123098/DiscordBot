package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ArgumentException;

import java.lang.reflect.InvocationTargetException;

public class GameStartCommand extends AbstractCommand {
    public GameStartCommand() {
        super(GameCommand.class, "start", null, null, null, "Starts a game of the specified type");
    }
    @Command
    public static void command(@Argument Team red, @Argument Team blue, String name, Guild guild, MessageMaker maker){
        if (red.getUsers().equals(blue.getUsers())) throw new ArgumentException("You can't play against yourself, you can play against me though");
        Class<? extends AbstractGame> clazz = GameCommand.CLASS_MAP.get(name.toLowerCase());
        if (clazz == null) throw new ArgumentException("That is not a game supported by this command");
        try {
            GameHandler.register(guild, clazz.getConstructor(Team.class, Team.class).newInstance(red, blue));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            maker.append("There doesn't seem to be a game that supports teams called that!\n");
            GameCommand.command(maker);
        }
    }
}
