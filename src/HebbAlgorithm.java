import java.util.Arrays;

class HebbAlgorithm implements IAlgorithm {
    INeuronet neuronet;

    HebbAlgorithm(int inputs, double biasOutput, int outputs) {
        init(inputs, biasOutput, outputs);
    }

    HebbAlgorithm(int inputs, int outputs) {
        this(inputs, 0, outputs);
    }

    private void init(int inputs, double biasOutput, int outputs) {
        neuronet = new Neuronet(this, inputs, biasOutput, outputs);
    }

    public StringBuilder launch(boolean learn) {
        StringBuilder stringBuilder = new StringBuilder();

        if (learn) {
            Main.Learning learning = new Main.Learning(this, Main.trainingSet,
                    new double[][]{{1, -1, -1, -1, -1, -1, -1, -1, -1, -1}, {-1, 1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, 1, -1, -1, -1, -1, -1, -1, -1}, {-1, -1, -1, 1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, 1, -1, -1, -1, -1, -1}, {-1, -1, -1, -1, -1, 1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, 1, -1, -1, -1}, {-1, -1, -1, -1, -1, -1, -1, 1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, 1, -1}, {-1, -1, -1, -1, -1, -1, -1, -1, -1, 1}}, 4);
            learning.start();
            try {
                learning.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            neuronet.saveWeights(".\\weights\\HebbAlg.txt");
        }

        if (!learn) neuronet.loadWeights(".\\weights\\HebbAlg.txt");

        for (double[] trainingSetNumber : Main.trainingSet) {
            double[] result = step(trainingSetNumber);
            int indexMax = Main.getMaxOutputIndex(result);

            stringBuilder.append(indexMax).append(" : ");
            stringBuilder.append(Arrays.toString(result)).append("\n");
        }
        return stringBuilder;
    }

    @Override
    public INeuronet[] getNeuronet() {
        return new INeuronet[]{neuronet};
    }

    public boolean learn(double[] input, double[] ideal) {
        if (neuronet.getInput().neurons.length == input.length &&
                neuronet.getOutput().neurons.length == ideal.length) {
            double[] result = step(input);
            for (int i = 0; i < neuronet.getOutput().neurons.length; i++) {
                if (result[i] != ideal[i]) {
                    for (int j = 0; j < neuronet.getOutput().neurons[i].weight.length; j++) {
                        neuronet.getOutput().neurons[i].weight[j] += input[j] * ideal[i];
                    }
                    neuronet.getOutput().neurons[i].biasWeight -= ideal[i];
                }
            }
        } else try {
            throw new Exception("Invalid function input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99411);
        }
        return false;
    }

    @Override
    public double activationFunction(double x) {
        return x > 0 ? 1. : -1.;
    }
}