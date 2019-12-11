package ru.demetrious.neuronet;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.layer.KohonenLayer;

public class KohonenNeuronet extends Neuronet {
    public KohonenNeuronet(IAlgorithm iAlgorithm, int inputs, int outputs) {
        super(iAlgorithm, inputs, outputs);
    }

    /**
     * Replaces the output layer with KohonenLayer.
     * Needs for another calculation logic
     *
     * @param outputs Count of outputs
     */
    @Override
    void initOutputLayer(int outputs) {
        setOutput(new KohonenLayer(getiAlgorithm(), outputs));
    }

    /**
     * Modified result after every step
     *
     * @param result Result of current step
     * @return Modified result
     */
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
