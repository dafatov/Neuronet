package ru.demetrious.neuronet.neuron;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.algorithms.RBFAlgorithm;

public class RBFNeuron extends Neuron {
    private double[] center;

    public RBFNeuron(IAlgorithm iAlgorithm, int inputs) {
        super(iAlgorithm, inputs);
        init(inputs);
    }

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }

    /**
     * Initialization center array
     *
     * @param inputs Count of inputs
     */
    private void init(int inputs) {
        setCenter(new double[inputs]);
    }

    /**
     * Changed the way to count the output of a neuron
     */
    @Override
    public void calculate() {
        setOutput(0);
        for (int i = 0; i < getInput().length; i++) {
            setOutput(getOutput() + Math.pow(getInput()[i] - getCenter()[i], 2));
        }
        setOutput(Math.abs(getOutput()));
        normalize();
    }

    /**
     * The method of processing the result of counting has been changed
     */
    @Override
    void normalize() {
        setOutput(Math.exp(-getOutput() / (2 * Math.pow(((RBFAlgorithm) getiAlgorithm()).WIDTH, 2))));
    }
}
