package ru.demetrious.neuronet.neuron;

import ru.demetrious.algorithms.IAlgorithm;

public class BiasNeuron extends InputNeuron {

    public BiasNeuron(IAlgorithm iAlgorithm, double output) {
        super(iAlgorithm);
        init(output);
    }

    BiasNeuron(IAlgorithm iAlgorithm) {
        this(iAlgorithm, 0);
    }

    private void init(double output) {
        super.setOutput(output);
    }
}
