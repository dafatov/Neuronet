import java.util.Arrays;

public class BackpropagationAlgorithm implements IAlgorithm {
    final double ALPHA = 0.5;
    final double ERROR = 0.01;
    INeuronet neuronet;

    BackpropagationAlgorithm(int inputs, double biasInput, Neuronet.HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        init(inputs, biasInput, hiddenLayerStructs, outputs);
    }

    BackpropagationAlgorithm(int inputs, Neuronet.HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        this(inputs, 0, hiddenLayerStructs, outputs);
    }

    BackpropagationAlgorithm(int inputs, double biasInput, int outputs) {
        neuronet = new Neuronet(this, inputs, biasInput, outputs);
    }

    BackpropagationAlgorithm(int inputs, int outputs) {
        this(inputs, 0, outputs);
    }

    private void init(int inputs, double biasInput, Neuronet.HiddenLayerStruct[] hiddenLayerStructs, int outputs) {
        neuronet = new Neuronet(this, inputs, biasInput, hiddenLayerStructs, outputs);
    }

    @Override
    public StringBuilder launch(boolean learn) {
        StringBuilder stringBuilder = new StringBuilder();

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
            neuronet.saveWeights(".\\weights\\BackAlg.txt");
        }

        if (!learn) neuronet.loadWeights(".\\weights\\BackAlg.txt");

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
            error *= 0.5;
            System.err.println(error);

            if (error > ERROR) neuronet.getOutput().adjustment(ideal);
            return error > ERROR;
        } else try {
            throw new Exception("Invalid function input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1000124);
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

    private double getRandomAboveZero() {
        double random = Math.random();
        return random > 0.5 ? random - 1 : random;
    }
}
