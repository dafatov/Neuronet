abstract class Layer {
    IAlgorithm iAlgorithm;
    Layer previous;
    Layer next;
    Neuron[] neurons;
    BiasNeuron biasNeuron;

    Layer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        this.iAlgorithm = iAlgorithm;
        init(neurons);
        initBias(biasOutput);
    }

    Layer(IAlgorithm iAlgorithm, int neurons) {
        this(iAlgorithm, neurons, 0);
    }

    void initBias(double biasOutput) {
        biasNeuron = new BiasNeuron(iAlgorithm, biasOutput);
    }

    void init(int neurons) {
        this.neurons = new Neuron[neurons];
    }

    void link(Layer previous, Layer next) {
        this.previous = previous;
        this.next = next;
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(iAlgorithm, previous.neurons.length);
        }
    }

    void calculate() {
        for (Neuron neuron : neurons) {
            for (int j = 0; j < previous.neurons.length; j++) {
                neuron.input[j] = previous.neurons[j].output;
            }
            neuron.biasInput = previous.biasNeuron.output;
            neuron.calculate();
        }
        if (next != null) {
            next.calculate();
        }
    }

    void adjustment() {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].delta = 0;
            for (int j = 0; j < next.neurons.length; j++) {
                neurons[i].delta += next.neurons[j].weight[i] * next.neurons[j].delta;
            }
            neurons[i].delta *= neurons[i].output * (1 - neurons[i].output);
        }
        for (int i = 0; i < next.neurons.length; i++) {
            for (int j = 0; j < neurons.length; j++) {
                next.neurons[i].weight[j] += ((BackpropagationAlgorithm) iAlgorithm).ALPHA * next.neurons[i].delta * neurons[j].output;
            }
            next.neurons[i].biasWeight += ((BackpropagationAlgorithm) iAlgorithm).ALPHA * next.neurons[i].delta * biasNeuron.output;
        }
        previous.adjustment();
    }
}

class InputLayer extends Layer {
    Void previous;

    InputLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    InputLayer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        super(iAlgorithm, neurons, biasOutput);
    }

    @Override
    void calculate() {
        try {
            throw new Exception("The InputLayer does not have calculate functionality");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99305);
        }
    }

    @Override
    void adjustment() {
    }

    @Override
    void link(Layer previous, Layer next) {
        try {
            throw new Exception("The InputLayer can not link previous layer");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99306);
        }
    }

    void link(Layer next) {
        super.next = next;
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new InputNeuron(iAlgorithm);
        }
    }
}

class HiddenLayer extends Layer {
    HiddenLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    HiddenLayer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        super(iAlgorithm, neurons, biasOutput);
    }
}

class OutputLayer extends Layer {
    Void next;

    OutputLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    @Override
    void initBias(double biasOutput) {
    }

    @Override
    void link(Layer previous, Layer next) {
        try {
            throw new Exception("The OutputLayer can not link next layer");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99307);
        }
    }

    void link(Layer previous) {
        super.link(previous, null);
    }

    @Override
    void adjustment() {
        try {
            throw new Exception("The OutputLayer can not adjustment without ideal");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99318);
        }
    }

    void adjustment(double[] ideal) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].delta = neurons[i].output * (1 - neurons[i].output) * (ideal[i] - neurons[i].output);
        }
        previous.adjustment();
    }
}

class RBFLayer extends HiddenLayer {

    RBFLayer(IAlgorithm iAlgorithm, int neurons, double biasOutput) {
        super(iAlgorithm, neurons, biasOutput);
    }

    RBFLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    @Override
    void link(Layer previous, Layer next) {
        this.previous = previous;
        this.next = next;
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new RBFNeuron(iAlgorithm, previous.neurons.length);
        }
    }
}

class KahonenLayer extends OutputLayer {

    KahonenLayer(IAlgorithm iAlgorithm, int neurons) {
        super(iAlgorithm, neurons);
    }

    @Override
    void link(Layer previous) {
        this.previous = previous;
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new KahonenNeuron(iAlgorithm, previous.neurons.length);
        }
    }
}