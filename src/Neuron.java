class Neuron {
    IAlgorithm iAlgorithm;
    double[] input;
    double[] weight;
    double biasInput;
    double biasWeight;
    double delta;
    double output;

    Neuron(IAlgorithm iAlgorithm, int inputs) {
        this.iAlgorithm = iAlgorithm;
        init(inputs);
    }

    private void init(int inputs) {
        if (inputs > 0) {
            input = new double[inputs];
            weight = new double[inputs];
        }
    }

    void calculate() {
        output = 0;
        for (int i = 0; i < input.length; i++) {
            output += input[i] * weight[i];
        }
        output += biasInput * biasWeight;
        normalize();
    }

    void normalize() {
        output = iAlgorithm.activationFunction(output);
    }
}

class InputNeuron extends Neuron {
    Void input, weight, biasInput, biasWeight;

    InputNeuron(IAlgorithm iAlgorithm) {
        super(iAlgorithm, 0);
    }

    @Override
    void calculate() {
        try {
            throw new Exception("The InputNeuron does not have calculate functionality");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99008);
        }
    }
}

class BiasNeuron extends InputNeuron {

    BiasNeuron(IAlgorithm iAlgorithm, double output) {
        super(iAlgorithm);
        init(output);
    }

    BiasNeuron(IAlgorithm iAlgorithm) {
        this(iAlgorithm, 0);
    }

    private void init(double output) {
        super.output = output;
    }
}

class RBFNeuron extends Neuron {
    double[] center;

    RBFNeuron(IAlgorithm iAlgorithm, int inputs) {
        super(iAlgorithm, inputs);
        init(inputs);
    }

    private void init(int inputs) {
        center = new double[inputs];
    }

    @Override
    void calculate() {
        output = 0;
        for (int i = 0; i < input.length; i++) {
            output += Math.pow(input[i] - center[i], 2);
        }
        output = Math.abs(output);
        normalize();
    }

    @Override
    void normalize() {
        output = Math.exp(-output / (2 * Math.pow(((RBFAlgorithm) iAlgorithm).WIDTH, 2)));
    }
}

class KahonenNeuron extends Neuron {
    KahonenNeuron(IAlgorithm iAlgorithm, int inputs) {
        super(iAlgorithm, inputs);
    }

    @Override
    void calculate() {
        output = 0;
        for (int i = 0; i < input.length; i++) {
            output += Math.pow(input[i] - weight[i], 2);
        }
        output = Math.sqrt(output);
        normalize();
    }
}