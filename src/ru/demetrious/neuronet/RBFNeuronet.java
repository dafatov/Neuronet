package ru.demetrious.neuronet;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.layer.RBFLayer;

public class RBFNeuronet extends Neuronet {

    public RBFNeuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        super(iAlgorithm, inputs, biasInput, hiddenLayerStructs, outputs);
    }

    public RBFNeuronet(IAlgorithm iAlgorithm, int inputs, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        super(iAlgorithm, inputs, hiddenLayerStructs, outputs);
    }

    public RBFNeuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, int outputs) {
        super(iAlgorithm, inputs, biasInput, outputs);
    }

    public RBFNeuronet(IAlgorithm iAlgorithm, int inputs, int outputs) {
        super(iAlgorithm, inputs, outputs);
    }

    /**
     * Replaces the hidden layer with RBFLayer.
     * Needs for another calculation logic
     *
     * @param hiddenLayerStructs Structure of hiddenLayer
     */
    @Override
    void initHiddenLayer(HiddenLayerStruct[] hiddenLayerStructs) {
        setHiddenLayers(new RBFLayer[hiddenLayerStructs.length]);
        for (int i = 0; i < getHiddens().length; i++) {
            getHiddens()[i] = new RBFLayer(getiAlgorithm(), hiddenLayerStructs[i].getNeurons(), hiddenLayerStructs[i].getBiasHidden());
        }
    }
}
