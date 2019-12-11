package ru.demetrious.neuronet.neuron;

import ru.demetrious.algorithms.IAlgorithm;

public class KohonenNeuron extends Neuron {
    public KohonenNeuron(IAlgorithm iAlgorithm, int inputs) {
        super(iAlgorithm, inputs);
    }

    /**
     * Changed the way to count the output of a neuron
     */
    @Override
    public void calculate() {
        setOutput(0);
        for (int i = 0; i < getInput().length; i++) {
            setOutput(getOutput() + Math.pow(getInput()[i] - getWeight()[i], 2));
        }
        setOutput(Math.sqrt(getOutput()));
        normalize();
    }
}
