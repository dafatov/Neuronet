package ru.demetrious.neuronet.neuron;

import ru.demetrious.algorithms.IAlgorithm;

public class Neuron {
    private IAlgorithm iAlgorithm;
    private double[] input;
    private double[] weight;
    private double biasInput;
    private double biasWeight;
    private double delta;
    private double output;

    public Neuron(IAlgorithm iAlgorithm, int inputs) {
        this.setiAlgorithm(iAlgorithm);
        init(inputs);
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;
    }

    public double[] getWeight() {
        return weight;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    public double getBiasInput() {
        return biasInput;
    }

    public void setBiasInput(double biasInput) {
        this.biasInput = biasInput;
    }

    public double getBiasWeight() {
        return biasWeight;
    }

    public void setBiasWeight(double biasWeight) {
        this.biasWeight = biasWeight;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    private void init(int inputs) {
        if (inputs > 0) {
            setInput(new double[inputs]);
            setWeight(new double[inputs]);
        }
    }

    /**
     * Calculate output of neuron
     */
    public void calculate() {
        setOutput(0);
        for (int i = 0; i < getInput().length; i++) {
            setOutput(getOutput() + getInput()[i] * getWeight()[i]);
        }
        setOutput(getOutput() + getBiasInput() * getBiasWeight());
        normalize();
    }

    /**
     * An activation function of the algorithm used is used
     */
    void normalize() {
        setOutput(getiAlgorithm().activationFunction(getOutput()));
    }

    public IAlgorithm getiAlgorithm() {
        return iAlgorithm;
    }

    public void setiAlgorithm(IAlgorithm iAlgorithm) {
        this.iAlgorithm = iAlgorithm;
    }
}

