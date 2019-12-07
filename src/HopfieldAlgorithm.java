import com.sun.istack.internal.Nullable;

import java.util.Arrays;

class HopfieldAlgorithm implements IAlgorithm {
    final int STEP_COUNTER = 1000;
    INeuronet neuronet;

    HopfieldAlgorithm(int inputs) {
        neuronet = new Neuronet(this, inputs, inputs);
    }

    @Override
    public StringBuilder launch(boolean learn) {
        StringBuilder stringBuilder = new StringBuilder();

        if (learn) {
            Main.Learning learning = new Main.Learning(this,
                    new double[][]{null, null, null}, Main.trainingSet, 1);
            learning.start();
            try {
                learning.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            neuronet.saveWeights(".\\weights\\HopfAlg.txt");
        }

        if (!learn) neuronet.loadWeights(".\\weights\\HopfAlg.txt");

        for (double[] trainingSetNumber : Main.trainingSet) {
            double[] result = step(trainingSetNumber);

            stringBuilder.append(Arrays.toString(trainingSetNumber)).append(":");
            stringBuilder.append(Arrays.toString(result)).append("\n");
        }
        double[] result = step(new double[]{1, -1, 1, -1});
        stringBuilder.append(Arrays.toString(new double[]{1, -1, 1, -1})).append(":");
        stringBuilder.append(Arrays.toString(result)).append("\n");

        return stringBuilder;
    }

    @Override
    public double[] step(double[] input) {
        double[] result;
        boolean equal;
        int counter = 0;
        do {
            result = IAlgorithm.super.step(input);

            equal = true;
            for (int i = 0; i < input.length; i++) {
                if (input[i] != result[i]) {
                    equal = false;
                    break;
                }
            }
            counter++;
            input = result;
        } while (!equal && counter < STEP_COUNTER);
        return result;
    }

    @Override
    public INeuronet[] getNeuronet() {
        return new INeuronet[]{neuronet};
    }

    @Override
    public boolean learn(@Nullable double[] input, double[] ideal) {
        if (neuronet.getOutput().neurons.length == ideal.length &&
                input == null) {
            for (int i = 0; i < ideal.length; i++) {
                for (int j = i + 1; j < ideal.length; j++) {
                    neuronet.getOutput().neurons[i].weight[j] += ideal[i] * ideal[j];
                    neuronet.getOutput().neurons[j].weight[i] = neuronet.getOutput().neurons[i].weight[j];
                }
            }
        } else try {
            throw new Exception("Invalid function input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1000225);
        }
        return false;
    }

    @Override
    public double activationFunction(double x) {
        return x < 0 ? -1 : 1;
    }
}