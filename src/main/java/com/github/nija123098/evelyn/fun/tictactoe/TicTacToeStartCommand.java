package com.github.nija123098.evelyn.fun.tictactoe;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.fun.gamestructure.GameStartCommand;
import com.github.nija123098.evelyn.fun.gamestructure.Team;

public class TicTacToeStartCommand extends AbstractCommand {
    public TicTacToeStartCommand() {
        super(TicTacToeCommand.class, "start", null, null, null, "Starts a game of ttt");
    }
    @Command
    public void command(@Argument Team red, @Argument Team blue, Guild guild, MessageMaker maker){
        GameStartCommand.command(red, blue, "tictactoe", guild, maker);
    }
}
