package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.neuron.KahonenNeuron;

public class KahonenLayer extends OutputLayer {

    public KahonenLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    @Override
    public void link(Layer previous) {
        this.setPrevious(previous);
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i] = new KahonenNeuron(getiAlgorithm(), previous.getNeurons().length);
        }
    }
}
