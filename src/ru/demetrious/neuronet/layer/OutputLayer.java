package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.IAlgorithm;

public class OutputLayer extends Layer {

    public OutputLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    @Override
    void initBias(double biasOutput) {
    }

    @Override
    public void link(Layer previous, Layer next) {
        try {
            throw new Exception("The Neuronet.OutputLayer can not link next layer");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99307);
        }
    }

    public void link(Layer previous) {
        super.link(previous, null);
    }

    @Override
    public void adjustment() {
        try {
            throw new Exception("The Neuronet.OutputLayer can not adjustment without ideal");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99318);
        }
    }

    public void adjustment(double[] ideal) {
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i].setDelta(getNeurons()[i].getOutput() *
                    (1 - getNeurons()[i].getOutput()) * (ideal[i] - getNeurons()[i].getOutput()));
        }
        getPrevious().adjustment();
    }
}
