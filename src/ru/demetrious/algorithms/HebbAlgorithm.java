package ru.demetrious.algorithms;

import ru.demetrious.Main;
import ru.demetrious.neuronet.INeuronet;
import ru.demetrious.neuronet.Neuronet;

import java.util.Arrays;

public class HebbAlgorithm implements IAlgorithm {
    private INeuronet neuronet;

    public HebbAlgorithm(int inputs, double biasOutput, int outputs) {
        init(inputs, biasOutput, outputs);
    }

    public HebbAlgorithm(int inputs, int outputs) {
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
        if (neuronet.getInput().getNeurons().length == input.length &&
                neuronet.getOutput().getNeurons().length == ideal.length) {
            double[] result = step(input);
            for (int i = 0; i < neuronet.getOutput().getNeurons().length; i++) {
                if (result[i] != ideal[i]) {
                    for (int j = 0; j < neuronet.getOutput().getNeurons()[i].getWeight().length; j++) {
                        neuronet.getOutput().getNeurons()[i].getWeight()[j] += input[j] * ideal[i];
                    }
                    neuronet.getOutput().getNeurons()[i].setBiasWeight(neuronet.getOutput().getNeurons()[i].getBiasWeight() - ideal[i]);
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