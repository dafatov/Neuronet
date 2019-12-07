import java.util.ArrayList;

class Neuronet implements INeuronet {
    private IAlgorithm iAlgorithm;
    private InputLayer input;
    private HiddenLayer[] hiddenLayers;
    private OutputLayer output;

    Neuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        this.iAlgorithm = iAlgorithm;
        init(inputs, biasInput, hiddenLayerStructs, outputs);
    }

    Neuronet(IAlgorithm iAlgorithm, int inputs, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        this(iAlgorithm, inputs, 0, hiddenLayerStructs, outputs);
    }

    Neuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, int outputs) {
        this.iAlgorithm = iAlgorithm;
        init(inputs, biasInput, outputs);
    }

    Neuronet(IAlgorithm iAlgorithm, int inputs, int outputs) {
        this(iAlgorithm, inputs, 0, outputs);
    }

    private void init(int inputs, double biasInput, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        if (hiddenLayerStructs == null || hiddenLayerStructs.length == 0) {
            init(inputs, biasInput, outputs);
            return;
        }

        input = new InputLayer(iAlgorithm, inputs, biasInput);

        initHiddenLayer(hiddenLayerStructs);

        initOutputLayer(outputs);

        input.link(hiddenLayers[0]);
        for (int i = 0; i < hiddenLayers.length; i++) {
            hiddenLayers[i].link(i == 0 ? input : hiddenLayers[i - 1], i == hiddenLayers.length - 1 ? output : hiddenLayers[i + 1]);
        }
        output.link(hiddenLayers[hiddenLayers.length - 1]);
    }

    void initHiddenLayer(HiddenLayerStruct[] hiddenLayerStructs) {
        hiddenLayers = new HiddenLayer[hiddenLayerStructs.length];
        for (int i = 0; i < hiddenLayers.length; i++) {
            hiddenLayers[i] = new HiddenLayer(iAlgorithm, hiddenLayerStructs[i].neurons, hiddenLayerStructs[i].biasHidden);
        }
    }

    void initOutputLayer(int outputs) {
        output = new OutputLayer(iAlgorithm, outputs);
    }

    private void init(int inputs, double biasInput, int outputs) {
        input = new InputLayer(iAlgorithm, inputs, biasInput);
        initOutputLayer(outputs);
        input.link(output);
        output.link(input);
    }

    @Override
    public String status() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(input.neurons.length).append(":");
        if (hiddenLayers != null) {
            for (int i = 0; i < hiddenLayers.length; i++) {
                stringBuilder.append(hiddenLayers[i].neurons.length).append(i == hiddenLayers.length - 1 ? ":" : ",");
            }
        }
        stringBuilder.append(output.neurons.length).append("\n");

        if (hiddenLayers != null) {
            for (HiddenLayer hiddenLayer : hiddenLayers) {
                for (int i = 0; i < hiddenLayer.neurons.length; i++) {
                    for (int j = 0; j < hiddenLayer.neurons[i].weight.length; j++) {
                        stringBuilder.append(hiddenLayer.neurons[i].weight[j]);
                        if (j != hiddenLayer.neurons[i].weight.length - 1) stringBuilder.append(",");
                    }
                    stringBuilder.append("/").append(hiddenLayer.neurons[i].biasWeight);
                    if (i != hiddenLayer.neurons.length - 1) stringBuilder.append(":");
                }
                stringBuilder.append("\n");
            }
        }

        for (int i = 0; i < output.neurons.length; i++) {
            for (int j = 0; j < output.neurons[i].weight.length; j++) {
                stringBuilder.append(output.neurons[i].weight[j]);
                if (j != output.neurons[i].weight.length - 1) stringBuilder.append(",");
            }
            stringBuilder.append("/").append(output.neurons[i].biasWeight);
            if (i != output.neurons.length - 1) stringBuilder.append(":");
        }
        return stringBuilder.toString();
    }

    @Override
    public void saveWeights(String path) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(status()).append("\n");
        stringBuilder.append(stringBuilder.toString().hashCode());

        if (FileManager.write(path, stringBuilder.toString(), false))
            System.out.println("Weights are saved to file " + path);
        else System.err.println("Weights are NOT saved to file");
    }

    @Override
    public void loadWeights(String path) {
        ArrayList<String> archive = FileManager.read(path);
        if (archive == null || archive.isEmpty()) {
            System.err.println("Weights are NOT found");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < archive.size() - 1; i++) {
            stringBuilder.append(archive.get(i)).append("\n");
        }
        if (stringBuilder.toString().hashCode() != Integer.parseInt(archive.get(archive.size() - 1))) {
            System.err.println("Weights are corrupted");
            return;
        }

        String[] parameters = archive.get(0).split(":");
        if (input.neurons.length != Integer.parseInt(parameters[0])) {
            System.err.println("The number of inputs does not match");
            return;
        }

        if (hiddenLayers != null) {
            String[] hiddenLayerParameters = parameters[1].split(",");
            if (hiddenLayers.length != hiddenLayerParameters.length) {
                System.err.println("The number of hidden layers does not match");
                return;
            }
            for (int i = 0; i < hiddenLayers.length; i++) {
                if (hiddenLayers[i].neurons.length != Integer.parseInt(hiddenLayerParameters[i])) {
                    System.err.println("The number of neurons on the hidden layer " + i + " does not match");
                    return;
                }
            }
        }

        if (output.neurons.length != Integer.parseInt(parameters[parameters.length - 1])) {
            System.err.println("The number of outputs does not match");
            return;
        }

        if (hiddenLayers != null) {
            for (int k = 0; k < archive.size() - 3; k++) {
                String[] hiddens = archive.get(k + 1).split(":");
                for (int i = 0; i < hiddenLayers[k].neurons.length; i++) {
                    String[] biasWeight = hiddens[i].split("/");
                    String[] weights = biasWeight[0].split(",");

                    for (int j = 0; j < hiddenLayers[k].neurons[i].weight.length; j++) {
                        hiddenLayers[k].neurons[i].weight[j] = Double.parseDouble(weights[j]);
                    }
                    hiddenLayers[k].neurons[i].biasWeight = Double.parseDouble(biasWeight[1]);
                }
            }
        }

        String[] outs = archive.get(archive.size() - 2).split(":");
        for (int i = 0; i < output.neurons.length; i++) {
            String[] biasWeight = outs[i].split("/");
            String[] weights = biasWeight[0].split(",");

            for (int j = 0; j < output.neurons[i].weight.length; j++) {
                output.neurons[i].weight[j] = Double.parseDouble(weights[j]);
            }
            output.neurons[i].biasWeight = Double.parseDouble(biasWeight[1]);
        }
        System.out.println("Weights are loaded from file " + path);
    }

    public IAlgorithm getiAlgorithm() {
        return iAlgorithm;
    }

    public InputLayer getInput() {
        return input;
    }

    @Override
    public HiddenLayer[] getHiddens() {
        return hiddenLayers;
    }

    public OutputLayer getOutput() {
        return output;
    }

    public void setOutput(OutputLayer output) {
        this.output = output;
    }

    @Override
    public double[] onAfterStep(double[] result) {
        return result;
    }

    public void setHiddenLayers(HiddenLayer[] hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    static class HiddenLayerStruct {
        int neurons;
        double biasHidden;

        HiddenLayerStruct(int neurons, double biasHidden) {
            this.neurons = neurons;
            this.biasHidden = biasHidden;
        }

        HiddenLayerStruct(int neurons) {
            this(neurons, 0);
        }
    }
}

class RBFNeuronet extends Neuronet {

    RBFNeuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        super(iAlgorithm, inputs, biasInput, hiddenLayerStructs, outputs);
    }

    RBFNeuronet(IAlgorithm iAlgorithm, int inputs, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        super(iAlgorithm, inputs, hiddenLayerStructs, outputs);
    }

    RBFNeuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, int outputs) {
        super(iAlgorithm, inputs, biasInput, outputs);
    }

    RBFNeuronet(IAlgorithm iAlgorithm, int inputs, int outputs) {
        super(iAlgorithm, inputs, outputs);
    }

    @Override
    void initHiddenLayer(HiddenLayerStruct[] hiddenLayerStructs) {
        setHiddenLayers(new RBFLayer[hiddenLayerStructs.length]);
        for (int i = 0; i < getHiddens().length; i++) {
            getHiddens()[i] = new RBFLayer(getiAlgorithm(), hiddenLayerStructs[i].neurons, hiddenLayerStructs[i].biasHidden);
        }
    }
}

class KahonenNeuronet extends Neuronet {
    KahonenNeuronet(IAlgorithm iAlgorithm, int inputs, int outputs) {
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
            getOutput().neurons[i].output = i == indexMin ? 1 : 0;
        }
        return result;
    }
}