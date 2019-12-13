package ru.demetrious.algorithms;

import ru.demetrious.Main;
import ru.demetrious.neuronet.INeuronet;
import ru.demetrious.neuronet.KohonenNeuronet;

import java.util.Arrays;

public class KohonenAlgorithm implements IAlgorithm {
    double LEARN_SCALE = .6;
    private INeuronet neuronet;

    /**
     * Constructor of Kohonen algorithm
     *
     * @param inputs  Count of inputs
     * @param outputs Count of outputs
     */
    public KohonenAlgorithm(int inputs, int outputs) {
        neuronet = new KohonenNeuronet(this, inputs, outputs);
    }

    @Override
    public StringBuilder launch(boolean learn) {
        StringBuilder stringBuilder = new StringBuilder();

        if (learn) {
            initWeights(Main.trainingSet[0].length);
            Main.Learning learning = new Main.Learning(this,
                    Main.trainingSet, new double[][]{null, null, null, null}, 0) {
                @Override
                public void onEveryTraining() {
                    LEARN_SCALE *= 0.5;
                }
            };
            learning.start();
            try {
                learning.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            neuronet.saveWeights(".\\weights\\KahoAlg.txt");
        }

        if (!learn) neuronet.loadWeights(".\\weights\\KahoAlg.txt");

        for (double[] trainingSetNumber : Main.trainingSet) {
            double[] result = step(trainingSetNumber);

            stringBuilder.append(Arrays.toString(trainingSetNumber)).append(" : ");
            stringBuilder.append(Arrays.toString(result)).append("\n");
        }

        return stringBuilder;
    }

    @Override
    public double[] step(double[] input) {
        return IAlgorithm.super.step(input);
    }

    @Override
    public INeuronet[] getNeuronet() {
        return new INeuronet[]{neuronet};
    }

    @Override
    public boolean learn(double[] input, double[] ideal) {
        if (neuronet.getInput().getNeurons().length == input.length &&
                ideal == null) {
            double[] inputNormalized = normalize(input.clone());

            double[] result = step(inputNormalized);
            System.out.println(Arrays.toString(result) + ":" + LEARN_SCALE);

            if (LEARN_SCALE > 0) {
                for (int i = 0; i < result.length; i++) {
                    if (result[i] == 1) {
                        for (int j = 0; j < neuronet.getOutput().getNeurons()[i].getWeight().length; j++) {
                            neuronet.getOutput().getNeurons()[i].getWeight()[j] += LEARN_SCALE *
                                    (inputNormalized[j] - neuronet.getOutput().getNeurons()[i].getWeight()[j]);
                        }
                    }
                }
                return true;
            }
        } else try {
            throw new Exception("Invalid function input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99822);
        }
        return false;
    }

    /**
     * Normalizes input data to values from zero to one
     *
     * @param input Input data
     * @return Return input modified
     */
    private double[] normalize(double[] input) {
        double summX = 0;
        for (double v : input) {
            summX += v;
        }
        for (int i = 0; i < input.length; i++) {
            input[i] /= summX;
        }
        return input;
    }

    @Override
    public double activationFunction(double x) {
        return x;
    }

    /**
     * Initialization of weights for each link between neurons,
     * depending on the n
     *
     * @param n Parameter n in 1/sqrt(n)
     */
    private void initWeights(int n) {
        for (int i = 0; i < neuronet.getOutput().getNeurons().length; i++) {
            Arrays.fill(neuronet.getOutput().getNeurons()[i].getWeight(), 1 / Math.sqrt(n));
        }
    }
}
