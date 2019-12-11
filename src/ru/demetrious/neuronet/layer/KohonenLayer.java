package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.neuron.KohonenNeuron;

public class KohonenLayer extends OutputLayer {

    public KohonenLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    /**
     * Replaces the using neurons with KohonenNeuron.
     * Needs for another calculation logic
     *
     * @param previous Previous layer
     */
    @Override
    public void link(Layer previous) {
        this.setPrevious(previous);
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i] = new KohonenNeuron(getiAlgorithm(), previous.getNeurons().length);
        }
    }
}
