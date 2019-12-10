package ru.demetrious.neuronet;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.layer.KahonenLayer;

public class KahonenNeuronet extends Neuronet {
    public KahonenNeuronet(IAlgorithm iAlgorithm, int inputs, int outputs) {
        super(iAlgorithm, inputs, outputs);
    }

    @Override
    void initOutputLayer(int outputs) {
        setOutput(new KahonenLayer(getiAlgorithm(), outputs));
    }

    @Override
    public double[] onAfterStep(double[] result) {
        int indexMin = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] < result[indexMin]) {
                indexMin = i;
            }
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = i == indexMin ? 1 : 0;
            getOutput().getNeurons()[i].setOutput(i == indexMin ? 1 : 0);
        }
        return result;
    }
}
