package com.github.nija123098.evelyn.fun.gamestructure.neuralnet;

import com.github.nija123098.evelyn.util.ArrayUtils;

import java.util.Random;

public class NeuralNet {
    private int winCount = 0;// written like this for XStream so it actually functions when unloading from XML, also space.
    private double[][][] weights;// level, first node, second node
    private int inputNodes, outNodes, hiddenLayers, hiddenWidth;
    private transient double[][] nodes;
    public NeuralNet(int inputNodes, int outNodes, int hiddenLayers, int hiddenWidth) {
        this.inputNodes = inputNodes;
        this.outNodes = outNodes;
        this.hiddenLayers = hiddenLayers;
        this.hiddenWidth = hiddenWidth;
        this.weights = new double[this.hiddenLayers + 1][][];
        this.weights[0] = new double[this.inputNodes][this.hiddenWidth];
        this.weights[this.weights.length - 1] = new double[this.hiddenWidth][this.outNodes];
        for (int i = 1; i < this.weights.length - 1; i++) {
            this.weights[i] = new double[this.hiddenWidth][this.hiddenWidth];
        }
        for (int i = 0; i < inputNodes; i++) {
            for (int j = 0; j < hiddenWidth; j++) {
                this.weights[0][i][j] = 0;
            }
        }
        for (int i = 0; i < hiddenWidth; i++) {
            for (int j = 0; j < outNodes; j++) {
                this.weights[this.weights.length - 1][i][j] = 0;
            }
        }
        for (int i = 1; i < this.weights.length - 1; i++) {
            for (int j = 0; j < hiddenWidth; j++) {
                for (int k = 0; k < hiddenWidth; k++) {
                    this.weights[i][j][k] = 0;
                }
            }
        }
    }
    private NeuralNet(NeuralNet other){
        this.inputNodes = other.inputNodes;
        this.outNodes = other.outNodes;
        this.hiddenLayers = other.hiddenLayers;
        this.hiddenWidth = other.hiddenWidth;
        this.weights = ArrayUtils.copy(other.weights);
    }
    public NeuralNet copy(){
        return new NeuralNet(this);
    }
    private void ensureLayerNodes(){
        if (this.nodes != null) return;
        this.nodes = new double[this.hiddenLayers + 2][];
        this.nodes[0] = new double[this.inputNodes];
        this.nodes[this.nodes.length - 1] = new double[this.outNodes];
        for (int i = 1; i < this.nodes.length - 1; i++) {
            this.nodes[i] = new double[this.hiddenWidth];
        }
    }
    public void alter(double ratio){
        Random random = new Random();
        for (int i = 0; i < this.weights.length; i++) {
            for (int j = 0; j < this.weights[i].length; j++) {
                for (int k = 0; k < this.weights[i][j].length; k++) {
                    this.weights[i][j][k] += random.nextGaussian() * (ratio / 2) * (random.nextInt(2) == 0 ? 1 : -1);
                }
            }
        }
    }
    public double[] compute(double[] input){
        this.ensureLayerNodes();
        this.nodes[0] = input;
        for (int i = 0; i < this.inputNodes; i++) {
            double total = 0;
            for (int j = 0; j < this.hiddenWidth; j++) {
                total += this.weights[0][i][j] * this.nodes[0][i];
            }
            this.nodes[1][i] = total / this.inputNodes;
        }
        for (int l = 1; l < this.hiddenLayers; l++) {
            for (int i = 0; i < this.hiddenWidth; i++) {
                double total = 0;
                for (int j = 0; j < this.hiddenWidth; j++) {
                    total += this.weights[l][i][j] * this.nodes[l][j];
                }
                this.nodes[l + 1][i] = total / this.hiddenWidth;
            }
        }
        for (int i = 0; i < this.outNodes; i++) {
            double total = 0;
            for (int j = 0; j < this.hiddenWidth; j++) {
                total += this.weights[this.weights.length - 2][i][j] * this.nodes[this.nodes.length - 2][i];
            }
            this.nodes[this.nodes.length - 1][i] = total / this.outNodes;
        }
        return this.nodes[this.nodes.length - 1];
    }
    public int incrementWin(){
        return ++this.winCount;
    }
    public int getWinCount() {
        return this.winCount;
    }
}
