package ru.demetrious.neuronet;

import ru.demetrious.neuronet.layer.HiddenLayer;
import ru.demetrious.neuronet.layer.InputLayer;
import ru.demetrious.neuronet.layer.OutputLayer;

public interface INeuronet {
    String status();

    void saveWeights(String path);

    void loadWeights(String path);

    InputLayer getInput();

    HiddenLayer[] getHiddens();

    OutputLayer getOutput();

    double[] onAfterStep(double[] result);
}
