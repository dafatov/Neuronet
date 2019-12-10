package ru.demetrious.neuronet.layer;

import ru.demetrious.algorithms.BackpropagationAlgorithm;
import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.neuronet.neuron.BiasNeuron;
import ru.demetrious.neuronet.neuron.Neuron;

public abstract class Layer {
    private IAlgorithm iAlgorithm;
    private Layer previous;
    private Layer next;
    private Neuron[] neurons;
    private BiasNeuron biasNeuron;

    Layer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        this.setiAlgorithm(iAlgorithm);
        init(neurons);
        initBias(biasOutput);
    }

    Layer(IAlgorithm iAlgorithm, int neurons) {
        this(iAlgorithm, neurons, 0);
    }

    void initBias(double biasOutput) {
        setBiasNeuron(new BiasNeuron(getiAlgorithm(), biasOutput));
    }

    void init(int neurons) {
        this.setNeurons(new Neuron[neurons]);
    }

    public void link(Layer previous, Layer next) {
        this.setPrevious(previous);
        this.setNext(next);
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i] = new Neuron(getiAlgorithm(), previous.getNeurons().length);
        }
    }

    public void calculate() {
        for (Neuron neuron : getNeurons()) {
            for (int j = 0; j < getPrevious().getNeurons().length; j++) {
                neuron.getInput()[j] = getPrevious().getNeurons()[j].getOutput();
            }
            neuron.setBiasInput(getPrevious().getBiasNeuron().getOutput());
            neuron.calculate();
        }
        if (getNext() != null) {
            getNext().calculate();
        }
    }

    public void adjustment() {
        for (int i = 0; i < getNeurons().length; i++) {
            getNeurons()[i].setDelta(0);
            for (int j = 0; j < getNext().getNeurons().length; j++) {
                getNeurons()[i].setDelta(getNeurons()[i].getDelta() + getNext().getNeurons()[j].getWeight()[i] * getNext().getNeurons()[j].getDelta());
            }
            getNeurons()[i].setDelta(getNeurons()[i].getDelta() * (getNeurons()[i].getOutput() * (1 - getNeurons()[i].getOutput())));
        }
        for (int i = 0; i < getNext().getNeurons().length; i++) {
            for (int j = 0; j < getNeurons().length; j++) {
                getNext().getNeurons()[i].getWeight()[j] += ((BackpropagationAlgorithm) getiAlgorithm()).ALPHA *
                        getNext().getNeurons()[i].getDelta() * getNeurons()[j].getOutput();
            }
            getNext().getNeurons()[i].setBiasWeight(getNext().getNeurons()[i].getBiasWeight() +
                    ((BackpropagationAlgorithm) getiAlgorithm()).ALPHA * getNext().getNeurons()[i].getDelta() * getBiasNeuron().getOutput());
        }
        getPrevious().adjustment();
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void setNeurons(Neuron[] neurons) {
        this.neurons = neurons;
    }

    public Layer getPrevious() {
        return previous;
    }

    public void setPrevious(Layer previous) {
        this.previous = previous;
    }

    public Layer getNext() {
        return next;
    }

    public void setNext(Layer next) {
        this.next = next;
    }

    public BiasNeuron getBiasNeuron() {
        return biasNeuron;
    }

    public void setBiasNeuron(BiasNeuron biasNeuron) {
        this.biasNeuron = biasNeuron;
    }

    public IAlgorithm getiAlgorithm() {
        return iAlgorithm;
    }

    public void setiAlgorithm(IAlgorithm iAlgorithm) {
        this.iAlgorithm = iAlgorithm;
    }
}

