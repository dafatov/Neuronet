package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.neuron.RBFNeuron;

public class RBFLayer extends HiddenLayer {

    public RBFLayer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        super(iAlgorithm, neurons, biasOutput);
    }

    public RBFLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    /**
     * Replaces the using neurons with RBFNeuron.
     * Needs for another calculation logic
     *
     * @param previous Previous layer
     * @param next     Next layer
     */
    @Override
    public void link(Layer previous, Layer next) {
        this.setPrevious(previous);
        this.setNext(next);
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i] = new RBFNeuron(getiAlgorithm(), previous.getNeurons().length);
        }
    }
}
