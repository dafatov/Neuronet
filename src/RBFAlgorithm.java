import java.util.Arrays;

public class RBFAlgorithm implements IAlgorithm {
    final double WIDTH = 5;
    final double ERROR = 0.01;
    INeuronet neuronet;

    RBFAlgorithm(int inputs, double biasInput, Neuronet.HiddenLayerStruct hiddenLayerStruct, int outputs) {
        init(inputs, biasInput, hiddenLayerStruct, outputs);
    }

    RBFAlgorithm(int inputs, Neuronet.HiddenLayerStruct hiddenLayerStruct, int outputs) {
        this(inputs, 0, hiddenLayerStruct, outputs);
    }

    private void init(int inputs, double biasInput, Neuronet.HiddenLayerStruct hiddenLayerStruct, int outputs) {
        neuronet = new RBFNeuronet(this, inputs, biasInput, new Neuronet.HiddenLayerStruct[]{hiddenLayerStruct}, outputs);
    }

    @Override
    public StringBuilder launch(boolean learn) {
        StringBuilder stringBuilder = new StringBuilder();

        initCenters();
        if (learn) {
            initWeights();
            Main.Learning learning = new Main.Learning(this,
                    Main.trainingSet,
                    new double[][]{{-1}, {1}, {1}, {-1}}, 0);
            learning.start();
            try {
                learning.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            neuronet.saveWeights(".\\weights\\RBFAlg.txt");
        }

        if (!learn) neuronet.loadWeights(".\\weights\\RBFAlg.txt");

        for (double[] trainingSetNumber : Main.trainingSet) {
            double[] result = step(trainingSetNumber);

            stringBuilder.append(Arrays.toString(trainingSetNumber)).append(" : ");
            stringBuilder.append(Arrays.toString(result)).append("\n");
        }
        return stringBuilder;
    }

    @Override
    public INeuronet[] getNeuronet() {
        return new INeuronet[]{neuronet};
    }

    @Override
    public boolean learn(double[] input, double[] ideal) {
        if (neuronet.getInput().neurons.length == input.length &&
                neuronet.getOutput().neurons.length == ideal.length) {
            double[] result = step(input);
            double error = 0;
            for (int i = 0; i < ideal.length; i++) {
                error += Math.pow(ideal[i] - result[i], 2);
            }
            error /= result.length;
            System.err.println(error);

            if (error > ERROR) {
                for (int i = 0; i < neuronet.getOutput().neurons.length; i++) {
                    for (int j = 0; j < neuronet.getOutput().neurons[i].weight.length; j++) {
                        neuronet.getOutput().neurons[i].weight[j] += (ideal[i] - result[i]) * neuronet.getOutput().neurons[i].input[j];
                    }
                }
            }
            return error > ERROR;
        } else try {
            throw new Exception("Invalid function input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99721);
        }
        return false;
    }

    @Override
    public double activationFunction(double x) {
        return 2 / (1 + Math.exp(-x)) - 1;
    }

    private void initWeights() {
        for (int k = 0; k < neuronet.getHiddens().length; k++) {
            for (int i = 0; i < neuronet.getHiddens()[k].neurons.length; i++) {
                for (int j = 0; j < neuronet.getHiddens()[k].neurons[i].weight.length; j++) {
                    neuronet.getHiddens()[k].neurons[i].weight[j] = getRandomAboveZero();
                }
                neuronet.getHiddens()[k].neurons[i].biasWeight = getRandomAboveZero();
            }
        }
        for (int i = 0; i < neuronet.getOutput().neurons.length; i++) {
            for (int j = 0; j < neuronet.getOutput().neurons[i].weight.length; j++) {
                neuronet.getOutput().neurons[i].weight[j] = getRandomAboveZero();
            }
            neuronet.getOutput().neurons[i].biasWeight = getRandomAboveZero();
        }
    }

    private void initCenters() {
        for (int k = 0; k < neuronet.getHiddens().length; k++) {
            for (int i = 0; i < neuronet.getHiddens()[k].neurons.length; i++) {
                ((RBFNeuron) neuronet.getHiddens()[k].neurons[i]).center = Main.trainingSet[i].clone();
            }
        }
    }

    private double getRandomAboveZero() {
        double random = 10 * Math.random();
        return -5 + random;
    }
}
