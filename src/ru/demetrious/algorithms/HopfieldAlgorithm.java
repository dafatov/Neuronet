package ru.demetrious.algorithms;

import ru.demetrious.Main;
import ru.demetrious.neuronet.INeuronet;
import ru.demetrious.neuronet.Neuronet;

import java.util.Arrays;

public class HopfieldAlgorithm implements IAlgorithm {
    final int STEP_COUNTER = 1000;
    private INeuronet neuronet;

    /**
     * Initialization of Hopfield neural network and his algorithm
     *
     * @param inputs Count of inputs
     */
    public HopfieldAlgorithm(int inputs) {
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
    public boolean learn(double[] input, double[] ideal) {
        if (neuronet.getOutput().getNeurons().length == ideal.length &&
                input == null) {
            for (int i = 0; i < ideal.length; i++) {
                for (int j = i + 1; j < ideal.length; j++) {
                    neuronet.getOutput().getNeurons()[i].getWeight()[j] += ideal[i] * ideal[j];
                    neuronet.getOutput().getNeurons()[j].getWeight()[i] = neuronet.getOutput().getNeurons()[i].getWeight()[j];
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
