package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.IAlgorithm;

public class HiddenLayer extends Layer {

    public HiddenLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    public HiddenLayer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        super(iAlgorithm, neurons, biasOutput);
    }
}
