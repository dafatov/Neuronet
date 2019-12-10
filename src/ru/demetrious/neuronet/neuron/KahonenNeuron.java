package ru.demetrious.neuronet.neuron;

import ru.demetrious.algorithms.IAlgorithm;

public class KahonenNeuron extends Neuron {
    public KahonenNeuron(IAlgorithm iAlgorithm, int inputs) {
        super(iAlgorithm, inputs);
    }

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
