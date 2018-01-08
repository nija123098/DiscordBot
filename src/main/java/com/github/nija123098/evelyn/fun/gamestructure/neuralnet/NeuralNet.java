package com.github.nija123098.evelyn.fun.gamestructure.neuralnet;

import com.github.nija123098.evelyn.util.ArrayUtils;

import java.util.Random;

/**
 * An instance of this is a basic generational neural net.
 *
 * This is not written in an object oriented fashion
 * so XStream will properly serialize instances.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class NeuralNet {
    private int winCount = 0;// written like this for XStream so it actually functions when unloading from XML, also space.
    private double[][][] synapses;// level, first node, second node
    private int inputNodes, outNodes, hiddenLayers, hiddenWidth;
    private transient double[][] nodes;
    public NeuralNet(int inputNodes, int outNodes, int hiddenLayers, int hiddenWidth) {
        this.inputNodes = inputNodes;
        this.outNodes = outNodes;
        this.hiddenLayers = hiddenLayers;
        this.hiddenWidth = hiddenWidth;
        this.synapses = new double[this.hiddenLayers + 1][][];
        this.synapses[0] = new double[this.inputNodes][this.hiddenWidth];
        this.synapses[this.synapses.length - 1] = new double[this.hiddenWidth][this.outNodes];
        for (int i = 1; i < this.synapses.length - 1; i++) {
            this.synapses[i] = new double[this.hiddenWidth][this.hiddenWidth];
        }
        for (int i = 0; i < inputNodes; i++) {
            for (int j = 0; j < hiddenWidth; j++) {
                this.synapses[0][i][j] = 0;
            }
        }
        for (int i = 0; i < hiddenWidth; i++) {
            for (int j = 0; j < outNodes; j++) {
                this.synapses[this.synapses.length - 1][i][j] = 0;
            }
        }
        for (int i = 1; i < this.synapses.length - 1; i++) {
            for (int j = 0; j < hiddenWidth; j++) {
                for (int k = 0; k < hiddenWidth; k++) {
                    this.synapses[i][j][k] = 0;
                }
            }
        }
    }
    private NeuralNet(NeuralNet other){
        this.inputNodes = other.inputNodes;
        this.outNodes = other.outNodes;
        this.hiddenLayers = other.hiddenLayers;
        this.hiddenWidth = other.hiddenWidth;
        this.synapses = ArrayUtils.copy(other.synapses);
    }

    /**
     * Makes a deep copy of the neural net isntance.
     *
     * @return a deep copy of the neural net isntance.
     */
    public NeuralNet copy(){
        return new NeuralNet(this);
    }

    /**
     * Ensures that {@link NeuralNet#nodes} is initialized
     * which matches the structure of the {@link NeuralNet}.
     */
    private void ensureLayerNodes(){
        if (this.nodes != null) return;
        this.nodes = new double[this.hiddenLayers + 2][];
        this.nodes[0] = new double[this.inputNodes];
        this.nodes[this.nodes.length - 1] = new double[this.outNodes];
        for (int i = 1; i < this.nodes.length - 1; i++) {
            this.nodes[i] = new double[this.hiddenWidth];
        }
    }

    /**
     * Alters the {@link NeuralNet#synapses} of the network
     * by adding a Gaussian times half the ratio where the
     * multiplied by either 1 or -1.
     *
     * @param ratio the amount to vary alterations by.
     */
    public void alter(double ratio){
        Random random = new Random();
        for (int i = 0; i < this.synapses.length; i++) {
            for (int j = 0; j < this.synapses[i].length; j++) {
                for (int k = 0; k < this.synapses[i][j].length; k++) {
                    this.synapses[i][j][k] += random.nextGaussian() * (ratio / 2) * (random.nextInt(2) == 0 ? 1 : -1);
                }
            }
        }
    }

    /**
     * Computes the result of the neural net instance
     * when the input is set to the given values.
     *
     * @param input the values to set the input nodes to.
     * @return the value of the output nodes.
     */
    public double[] compute(double[] input){
        this.ensureLayerNodes();
        this.nodes[0] = input;
        for (int i = 0; i < this.inputNodes; i++) {
            double total = 0;
            for (int j = 0; j < this.hiddenWidth; j++) {
                total += this.synapses[0][i][j] * this.nodes[0][i];
            }
            this.nodes[1][i] = total / this.inputNodes;
        }
        for (int l = 1; l < this.hiddenLayers; l++) {
            for (int i = 0; i < this.hiddenWidth; i++) {
                double total = 0;
                for (int j = 0; j < this.hiddenWidth; j++) {
                    total += this.synapses[l][i][j] * this.nodes[l][j];
                }
                this.nodes[l + 1][i] = total / this.hiddenWidth;
            }
        }
        for (int i = 0; i < this.outNodes; i++) {
            double total = 0;
            for (int j = 0; j < this.hiddenWidth; j++) {
                total += this.synapses[this.synapses.length - 2][i][j] * this.nodes[this.nodes.length - 2][i];
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
