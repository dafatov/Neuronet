package ru.demetrious.algorithms;

import ru.demetrious.Main;
import ru.demetrious.neuronet.INeuronet;
import ru.demetrious.neuronet.KahonenNeuronet;
import ru.demetrious.neuronet.Neuronet;

import java.util.Arrays;

public class CounterAlgorithm implements IAlgorithm {
    double LEARN_SCALE_K = .6;
    double LEARN_SCALE_G = .1;
    private INeuronet neuronetKahonen, neuronetGrossberg;

    public CounterAlgorithm(int inputs, Neuronet.HiddenLayerStruct hiddenLayerStruct, int outputs) {
        neuronetKahonen = new KahonenNeuronet(this, inputs, hiddenLayerStruct.getNeurons());
        neuronetGrossberg = new Neuronet(this, hiddenLayerStruct.getNeurons(), outputs);
    }

    @Override
    public StringBuilder launch(boolean learn) {
        StringBuilder stringBuilder = new StringBuilder();

        if (learn) {
            initWeights(Main.trainingSet[0].length);
            Main.Learning learning = new Main.Learning(this,
                    Main.trainingSet,
                    new double[][]{{2, 1, 0, 0}, {0, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 1, 2}}, 0) {
                @Override
                public void onEveryTraining() {
                    LEARN_SCALE_K *= 0.5;
                    LEARN_SCALE_G *= 0.5;
                }
            };
            learning.start();
            try {
                learning.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            neuronetKahonen.saveWeights(".\\weights\\Counter0Alg.txt");
            neuronetGrossberg.saveWeights(".\\weights\\Counter1Alg.txt");
        }

        if (!learn) {
            neuronetKahonen.loadWeights(".\\weights\\Counter0Alg.txt");
            neuronetGrossberg.loadWeights(".\\weights\\Counter1Alg.txt");
        }

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
        return new INeuronet[]{neuronetKahonen, neuronetGrossberg};
    }

    @Override
    public boolean learn(double[] input, double[] ideal) {
        if (neuronetKahonen.getInput().getNeurons().length == input.length &&
                neuronetGrossberg.getOutput().getNeurons().length == ideal.length) {
            double[] inputNormalized = normalize(input.clone());

            double[] result = step(inputNormalized);
            System.out.println(Arrays.toString(result) + ":" + LEARN_SCALE_K + ":" + LEARN_SCALE_G);

            if (LEARN_SCALE_K > 0 || LEARN_SCALE_G > 0) {
                for (int i = 0; i < neuronetKahonen.getOutput().getNeurons().length; i++) {
                    if (neuronetKahonen.getOutput().getNeurons()[i].getOutput() == 1) {
                        for (int j = 0; j < neuronetKahonen.getOutput().getNeurons()[i].getWeight().length; j++) {
                            neuronetKahonen.getOutput().getNeurons()[i].getWeight()[j] += LEARN_SCALE_K *
                                    (inputNormalized[j] - neuronetKahonen.getOutput().getNeurons()[i].getWeight()[j]);
                        }
                        for (int j = 0; j < neuronetGrossberg.getOutput().getNeurons().length; j++) {
                            neuronetGrossberg.getOutput().getNeurons()[j].getWeight()[i] += LEARN_SCALE_G *
                                    (ideal[j] - neuronetGrossberg.getOutput().getNeurons()[j].getWeight()[i]);
                        }
                    }
                }
                return true;
            }
        } else try {
            throw new Exception("Invalid function input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99923);
        }
        return false;
    }

    @Override
    public double activationFunction(double x) {
        return x;
    }

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

    private void initWeights(int n) {
        for (int i = 0; i < neuronetKahonen.getOutput().getNeurons().length; i++) {
            Arrays.fill(neuronetKahonen.getOutput().getNeurons()[i].getWeight(), 1 / Math.sqrt(n));
        }
        for (int i = 0; i < neuronetGrossberg.getOutput().getNeurons().length; i++) {
            Arrays.fill(neuronetGrossberg.getOutput().getNeurons()[i].getWeight(), 1 / Math.sqrt(n));
        }
    }
}
