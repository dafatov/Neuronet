package ru.demetrious.neuronet;

import ru.demetrious.neuronet.layer.HiddenLayer;
import ru.demetrious.neuronet.layer.InputLayer;
import ru.demetrious.neuronet.layer.OutputLayer;

public interface INeuronet {
    /**
     * Create a string contained all weights of neural network
     *
     * @return Return all weights of neural network in string format
     */
    String status();

    /**
     * Save weights of neural network into file
     *
     * @param path Path in string format
     */
    void saveWeights(String path);

    /**
     * Load saved weights into neural network
     * Structure of neural network must match
     *
     * @param path Path in string format
     */
    void loadWeights(String path);

    InputLayer getInput();

    HiddenLayer[] getHiddens();

    OutputLayer getOutput();

    /**
     * Event called after every step
     *
     * @param result Result of current step
     * @return Returns the modified result
     */
    double[] onAfterStep(double[] result);
}
