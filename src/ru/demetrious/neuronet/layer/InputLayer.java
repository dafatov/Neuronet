package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.neuron.InputNeuron;

public class InputLayer extends Layer {

    public InputLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    public InputLayer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        super(iAlgorithm, neurons, biasOutput);
    }

    /**
     * The input layer cannot calculate so we process the error
     */
    @Override
    public void calculate() {
        try {
            throw new Exception("The Neuronet.InputLayer does not have calculate functionality");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99305);
        }
    }

    /**
     * The input layer cannot adjustment so we ignored
     */
    @Override
    public void adjustment() {
    }

    /**
     * The input layer don't have previous layer so we process the error
     */
    @Override
    public void link(Layer previous, Layer next) {
        try {
            throw new Exception("The Neuronet.InputLayer can not link previous layer");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99306);
        }
    }

    /**
     * Link input layer to next
     *
     * @param next Next layer
     */
    public void link(Layer next) {
        super.setNext(next);
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i] = new InputNeuron(getiAlgorithm());
        }
    }
}
