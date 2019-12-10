package ru.demetrious.neuronet;

import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.layer.HiddenLayer;
import ru.demetrious.neuronet.layer.InputLayer;
import ru.demetrious.neuronet.layer.OutputLayer;
import ru.demetrious.util.FileManager;

import java.util.ArrayList;

public class Neuronet implements INeuronet {
    private IAlgorithm iAlgorithm;
    private InputLayer input;
    private HiddenLayer[] hiddenLayers;
    private OutputLayer output;

    public Neuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        this.setiAlgorithm(iAlgorithm);
        init(inputs, biasInput, hiddenLayerStructs, outputs);
    }

    public Neuronet(IAlgorithm iAlgorithm, int inputs, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        this(iAlgorithm, inputs, 0, hiddenLayerStructs, outputs);
    }

    public Neuronet(IAlgorithm iAlgorithm, int inputs, double biasInput, int outputs) {
        this.setiAlgorithm(iAlgorithm);
        init(inputs, biasInput, outputs);
    }

    public Neuronet(IAlgorithm iAlgorithm, int inputs, int outputs) {
        this(iAlgorithm, inputs, 0, outputs);
    }

    private void init(int inputs, double biasInput, HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        if (hiddenLayerStructs == null || hiddenLayerStructs.length == 0) {
            init(inputs, biasInput, outputs);
            return;
        }

        initInputLayer(inputs, biasInput);

        initHiddenLayer(hiddenLayerStructs);

        initOutputLayer(outputs);

        getInput().link(getHiddenLayers()[0]);
        for (int i = 0; i < getHiddenLayers().length; i++) {
            getHiddenLayers()[i].link(i == 0 ? getInput() : getHiddenLayers()[i - 1], i == getHiddenLayers().length - 1 ? getOutput() : getHiddenLayers()[i + 1]);
        }
        getOutput().link(getHiddenLayers()[getHiddenLayers().length - 1]);
    }

    void initInputLayer(int inputs, double biasInput) {
        setInput(new InputLayer(getiAlgorithm(), inputs, biasInput));
    }

    void initHiddenLayer(HiddenLayerStruct[] hiddenLayerStructs) {
        setHiddenLayers(new HiddenLayer[hiddenLayerStructs.length]);
        for (int i = 0; i < getHiddenLayers().length; i++) {
            getHiddenLayers()[i] = new HiddenLayer(getiAlgorithm(), hiddenLayerStructs[i].getNeurons(), hiddenLayerStructs[i].getBiasHidden());
        }
    }

    void initOutputLayer(int outputs) {
        setOutput(new OutputLayer(getiAlgorithm(), outputs));
    }

    private void init(int inputs, double biasInput, int outputs) {
        initInputLayer(inputs, biasInput);
        initOutputLayer(outputs);
        getInput().link(getOutput());
        getOutput().link(getInput());
    }

    @Override
    public String status() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getInput().getNeurons().length).append(":");
        if (getHiddenLayers() != null) {
            for (int i = 0; i < getHiddenLayers().length; i++) {
                stringBuilder.append(getHiddenLayers()[i].getNeurons().length).append(i == getHiddenLayers().length - 1 ? ":" : ",");
            }
        }
        stringBuilder.append(getOutput().getNeurons().length).append("\n");

        if (getHiddenLayers() != null) {
            for (HiddenLayer hiddenLayer : getHiddenLayers()) {
                for (int i = 0; i < hiddenLayer.getNeurons().length; i++) {
                    for (int j = 0; j < hiddenLayer.getNeurons()[i].getWeight().length; j++) {
                        stringBuilder.append(hiddenLayer.getNeurons()[i].getWeight()[j]);
                        if (j != hiddenLayer.getNeurons()[i].getWeight().length - 1) stringBuilder.append(",");
                    }
                    stringBuilder.append("/").append(hiddenLayer.getNeurons()[i].getBiasWeight());
                    if (i != hiddenLayer.getNeurons().length - 1) stringBuilder.append(":");
                }
                stringBuilder.append("\n");
            }
        }

        for (int i = 0; i < getOutput().getNeurons().length; i++) {
            for (int j = 0; j < getOutput().getNeurons()[i].getWeight().length; j++) {
                stringBuilder.append(getOutput().getNeurons()[i].getWeight()[j]);
                if (j != getOutput().getNeurons()[i].getWeight().length - 1) stringBuilder.append(",");
            }
            stringBuilder.append("/").append(getOutput().getNeurons()[i].getBiasWeight());
            if (i != getOutput().getNeurons().length - 1) stringBuilder.append(":");
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
        if (getInput().getNeurons().length != Integer.parseInt(parameters[0])) {
            System.err.println("The number of inputs does not match");
            return;
        }

        if (getHiddenLayers() != null) {
            String[] hiddenLayerParameters = parameters[1].split(",");
            if (getHiddenLayers().length != hiddenLayerParameters.length) {
                System.err.println("The number of hidden layers does not match");
                return;
            }
            for (int i = 0; i < getHiddenLayers().length; i++) {
                if (getHiddenLayers()[i].getNeurons().length != Integer.parseInt(hiddenLayerParameters[i])) {
                    System.err.println("The number of neurons on the hidden layer " + i + " does not match");
                    return;
                }
            }
        }

        if (getOutput().getNeurons().length != Integer.parseInt(parameters[parameters.length - 1])) {
            System.err.println("The number of outputs does not match");
            return;
        }

        if (getHiddenLayers() != null) {
            for (int k = 0; k < archive.size() - 3; k++) {
                String[] hiddens = archive.get(k + 1).split(":");
                for (int i = 0; i < getHiddenLayers()[k].getNeurons().length; i++) {
                    String[] biasWeight = hiddens[i].split("/");
                    String[] weights = biasWeight[0].split(",");

                    for (int j = 0; j < getHiddenLayers()[k].getNeurons()[i].getWeight().length; j++) {
                        getHiddenLayers()[k].getNeurons()[i].getWeight()[j] = Double.parseDouble(weights[j]);
                    }
                    getHiddenLayers()[k].getNeurons()[i].setBiasWeight(Double.parseDouble(biasWeight[1]));
                }
            }
        }

        String[] outs = archive.get(archive.size() - 2).split(":");
        for (int i = 0; i < getOutput().getNeurons().length; i++) {
            String[] biasWeight = outs[i].split("/");
            String[] weights = biasWeight[0].split(",");

            for (int j = 0; j < getOutput().getNeurons()[i].getWeight().length; j++) {
                getOutput().getNeurons()[i].getWeight()[j] = Double.parseDouble(weights[j]);
            }
            getOutput().getNeurons()[i].setBiasWeight(Double.parseDouble(biasWeight[1]));
        }
        System.out.println("Weights are loaded from file " + path);
    }

    public IAlgorithm getiAlgorithm() {
        return iAlgorithm;
    }

    public void setiAlgorithm(IAlgorithm iAlgorithm) {
        this.iAlgorithm = iAlgorithm;
    }

    public InputLayer getInput() {
        return input;
    }

    public void setInput(InputLayer input) {
        this.input = input;
    }

    @Override
    public HiddenLayer[] getHiddens() {
        return getHiddenLayers();
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

    public HiddenLayer[] getHiddenLayers() {
        return hiddenLayers;
    }

    public void setHiddenLayers(HiddenLayer[] hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    public static class HiddenLayerStruct {
        private int neurons;
        private double biasHidden;

        public HiddenLayerStruct(int neurons, double biasHidden) {
            this.setNeurons(neurons);
            this.setBiasHidden(biasHidden);
        }

        public HiddenLayerStruct(int neurons) {
            this(neurons, 0);
        }

        public int getNeurons() {
            return neurons;
        }

        public void setNeurons(int neurons) {
            this.neurons = neurons;
        }

        public double getBiasHidden() {
            return biasHidden;
        }

        public void setBiasHidden(double biasHidden) {
            this.biasHidden = biasHidden;
        }
    }
}

