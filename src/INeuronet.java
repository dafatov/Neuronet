interface INeuronet {
    String status();

    void saveWeights(String path);

    void loadWeights(String path);

    InputLayer getInput();

    HiddenLayer[] getHiddens();

    OutputLayer getOutput();

    double[] onAfterStep(double[] result);
}
