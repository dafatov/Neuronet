import java.util.Arrays;

public class RosenblattAlgorithm implements IAlgorithm {
    private final double ALPHA = 0.5;

    private INeuronet neuronet;

    RosenblattAlgorithm(int inputs, int outputs) {
        this(inputs, 0, outputs);
    }

    RosenblattAlgorithm(int inputs, int biasInput, int outputs) {
        init(inputs, biasInput, outputs);
    }

    private void init(int inputs, double biasInput, int outputs) {
        neuronet = new Neuronet(this, inputs, biasInput, outputs);
    }

    public INeuronet[] getNeuronet() {
        return new INeuronet[]{neuronet};
    }

    @Override
    public StringBuilder launch(boolean learn) {
        StringBuilder stringBuilder = new StringBuilder();

        if (learn) {
            //initWeights();
            Main.Learning learning = new Main.Learning(this, Main.trainingSet,
                    new double[][]{{1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1}}, 0);
            learning.start();
            try {
                learning.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            neuronet.saveWeights(".\\weights\\RoseAlg.txt");
        }

        if (!learn) neuronet.loadWeights(".\\weights\\RoseAlg.txt");

        for (double[] trainingSetLetter : Main.trainingSet) {
            double[] result = step(trainingSetLetter);
            int indexMax = Main.getMaxOutputIndex(result);

            stringBuilder.append(indexMax == -1 ? "-" : Main.ALPHABET[indexMax]).append(" : ");
            stringBuilder.append(Arrays.toString(result)).append("\n");
        }
        return stringBuilder;
    }

    @Override
    public boolean learn(double[] input, double[] ideal) {
        if (neuronet.getInput().neurons.length == input.length &&
                neuronet.getOutput().neurons.length == ideal.length) {
            double[] result = step(input);
            boolean error = false;

            for (int i = 0; i < neuronet.getOutput().neurons.length; i++) {
                if (result[i] != ideal[i]) {
                    for (int j = 0; j < neuronet.getOutput().neurons[i].weight.length; j++) {
                        neuronet.getOutput().neurons[i].weight[j] +=
                                ALPHA * neuronet.getOutput().neurons[i].input[j] * (ideal[i] - result[i]);
                    }
                    neuronet.getOutput().neurons[i].biasWeight +=
                            ALPHA * neuronet.getOutput().neurons[i].biasInput * (ideal[i] - result[i]);
                    error = true;
                }
            }
            return error;
        } else try {
            throw new Exception("Invalid function input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99517);
        }
        return false;
    }

    @Override
    public double activationFunction(double x) {
        return x > 0 ? 1 : -1;
    }
}
