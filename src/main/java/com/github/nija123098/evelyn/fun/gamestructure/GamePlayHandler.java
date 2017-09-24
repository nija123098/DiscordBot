package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.util.Rand;

import java.util.List;
import java.util.stream.Collectors;

public class GamePlayHandler {
    public static void decideGame(AbstractGame game){
        Team team = game.getTeam(DiscordClient.getOurUser());
        List<GameChoice> list = game.getChoices().stream().filter(gameChoice -> gameChoice.mayChose(team)).collect(Collectors.toList());
        if (list.isEmpty()) throw new GameResultException(game, null);// this should not happen
        game.chose(DiscordClient.getOurUser(), Rand.getRand(list, false).getName());
    }
}
