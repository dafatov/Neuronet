package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.IAlgorithm;

public class OutputLayer extends Layer {

    public OutputLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    /**
     * The output layer cannot have bias neuron so we process the error
     */
    @Override
    void initBias(double biasOutput) {
    }

    /**
     * The input layer don't have next layer so we process the error
     *
     * @param previous Previous layer
     * @param next     Next layer
     */
    @Override
    public void link(Layer previous, Layer next) {
        try {
            throw new Exception("The Neuronet.OutputLayer can not link next layer");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99307);
        }
    }

    /**
     * Link output layer to previous
     *
     * @param previous Previous layer
     */
    public void link(Layer previous) {
        super.link(previous, null);
    }

    /**
     * The input layer cannot adjustment without ideal result
     * so we process the error
     */
    @Override
    public void adjustment() {
        try {
            throw new Exception("The Neuronet.OutputLayer can not adjustment without ideal");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99318);
        }
    }

    /**
     * Calculates delta for each neuron on this layer
     * and calls this method on previous layer
     *
     * @param ideal Ideal result
     */
    public void adjustment(double[] ideal) {
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i].setDelta(getNeurons()[i].getOutput() *
                    (1 - getNeurons()[i].getOutput()) * (ideal[i] - getNeurons()[i].getOutput()));
        }
        getPrevious().adjustment();
    }
}
