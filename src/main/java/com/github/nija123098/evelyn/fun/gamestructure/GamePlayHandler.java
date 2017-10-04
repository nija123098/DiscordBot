package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.config.TypeChanger;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.fun.gamestructure.neuralnet.NeuralNet;
import com.github.nija123098.evelyn.fun.gamestructure.neuralnet.AbstractNeuralNetGame;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.Rand;
import com.google.common.base.Joiner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GamePlayHandler {
    private static final double ALTER_PERCENT = .005D;
    private static final Map<String, NeuralNet> LOADED_NEURAL_NETS = new HashMap<>();
    private static final Map<AbstractGame, NeuralNet> GAME_NET_MAP = new HashMap<>();
    static {
        new File(BotConfig.NEURAL_NET_FOLDER).mkdirs();
    }
    static void decideGame(AbstractGame game){
        Team team = game.getTeam(DiscordClient.getOurUser());
        if (game instanceof AbstractNeuralNetGame) {
            AbstractNeuralNetGame nGame = (AbstractNeuralNetGame) game;
            NeuralNet neuralNet = GAME_NET_MAP.get(game);
            if (neuralNet == null) {
                NeuralNet newNet = LOADED_NEURAL_NETS.computeIfAbsent(getNeuralNetName(nGame), s -> loadNet(nGame)).copy();
                newNet.alter(ALTER_PERCENT);
                neuralNet = newNet;
                GAME_NET_MAP.put(game, neuralNet);
            }
            nGame.chose(DiscordClient.getOurUser(), nGame.getDecision(neuralNet.compute(nGame.inputNodes())).getName());
        }else{// random
            List<GameChoice> list = game.getChoices().stream().filter(gameChoice -> gameChoice.mayChose(team)).collect(Collectors.toList());
            if (list.isEmpty()) throw new GameResultException(game, null);// this should not happen
            game.chose(DiscordClient.getOurUser(), Rand.getRand(list, false).getName());
        }
    }
    static void reportWin(AbstractNeuralNetGame game){
        NeuralNet net = GAME_NET_MAP.get(game);
        String gameNetName = getNeuralNetName(game);
        if (net.incrementWin() > LOADED_NEURAL_NETS.get(gameNetName).getWinCount()) {
            LOADED_NEURAL_NETS.put(gameNetName, net);
            try{Files.write(getNeuralNetPath(game), Collections.singletonList(TypeChanger.getXStream().toXML(net)));
            } catch (IOException e) {
                Log.log("Exception saving game neural net", e);
            }
        }
    }
    private static NeuralNet loadNet(AbstractNeuralNetGame game){
        Path path = getNeuralNetPath(game);
        NeuralNet neuralNet;
        if (!path.toFile().exists()){
            neuralNet = new NeuralNet(game.getInputWidth(), game.getOutputWidth(), game.getHiddenLayerCount(), game.getHiddenLayerWidth());
        } else {// I'm tempted to inline this
            neuralNet = (NeuralNet) TypeChanger.getXStream().fromXML(path.toFile());
        }
        return neuralNet;
    }
    private static String getNeuralNetName(AbstractNeuralNetGame game){
        return game.getName() + "-" + Joiner.on("-").join(new Integer[]{game.getInputWidth(), game.getOutputWidth(), game.getHiddenLayerWidth(), game.getHiddenLayerCount()}) + ".txt";
    }
    private static Path getNeuralNetPath(AbstractNeuralNetGame game){
        return Paths.get(BotConfig.NEURAL_NET_FOLDER, getNeuralNetName(game));
    }
}
