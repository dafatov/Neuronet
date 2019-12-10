package ru.demetrious;

import ru.demetrious.algorithms.GeneticAlgorithm;
import ru.demetrious.algorithms.IAlgorithm;
import ru.demetrious.util.FileManager;
import ru.demetrious.util.GUI;

import java.awt.image.BufferedImage;

public class Main {
    //System.exit(1000225);
    public static final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static double[][] trainingSet;

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
//
//        /*Lab1*/
//        stringBuilder.append("\nLab1\n");
//        SimplePerceptron simplePerceptronOR0 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
//        stringBuilder.append(MessageFormat.format("0||0 = {0}", simplePerceptronOR0.calculate(0, 0))).append('\n');
//        SimplePerceptron simplePerceptronOR1 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
//        stringBuilder.append(MessageFormat.format("0||1 = {0}", simplePerceptronOR1.calculate(0, 1))).append('\n');
//        SimplePerceptron simplePerceptronOR2 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
//        stringBuilder.append(MessageFormat.format("1||0 = {0}", simplePerceptronOR2.calculate(1, 0))).append('\n');
//        SimplePerceptron simplePerceptronOR3 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
//        stringBuilder.append(MessageFormat.format("1||1 = {0}", simplePerceptronOR3.calculate(1, 1))).append('\n');
//
//        /*Lab2*/
//        stringBuilder.append("\nLab2\n");
//        SimplePerceptron simplePerceptronNOT0 = new SimplePerceptron(SimplePerceptron.LogicFunction.NOT);
//        stringBuilder.append(MessageFormat.format("!0 = {0}", simplePerceptronNOT0.calculate(0))).append('\n');
//        SimplePerceptron simplePerceptronNOT1 = new SimplePerceptron(SimplePerceptron.LogicFunction.NOT);
//        stringBuilder.append(MessageFormat.format("!1 = {0}", simplePerceptronNOT1.calculate(1))).append('\n');
//
//        /*Lab3*/
//        stringBuilder.append("\nLab3\n");
//        trainingSetNumbersInit();
//        HebbAlgorithm hebbAlgorithm = new HebbAlgorithm(Main.trainingSet[0].length, Main.trainingSet.length);
//        stringBuilder.append(hebbAlgorithm.launch(false));
//
//        /*Lab4*/
//        stringBuilder.append("\nLab4\n");
//        trainingSetLettersInit();
//        RosenblattAlgorithm rosenblattAlgorithm = new RosenblattAlgorithm(Main.trainingSet[0].length, 1, Main.trainingSet.length);
//        stringBuilder.append(rosenblattAlgorithm.launch(false));
//
//        /*Lab5*/
//        stringBuilder.append("\nLab5\n");
//        trainingSet = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
//        BackpropagationAlgorithm backpropagationAlgorithm = new BackpropagationAlgorithm(2, 1,
//                new Neuronet.HiddenLayerStruct[]{new Neuronet.HiddenLayerStruct(100, -1),
//                        new Neuronet.HiddenLayerStruct(100, 1)}, 1);
//        stringBuilder.append(backpropagationAlgorithm.launch(false));
//
//        /*Lab6*/
//        stringBuilder.append("\nLab6\n");
//        trainingSet = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
//        RBFAlgorithm rbfAlgorithm = new RBFAlgorithm(2, new Neuronet.HiddenLayerStruct(4), 1);
//        stringBuilder.append(rbfAlgorithm.launch(false));
//
//        /*Lab7*/
//        stringBuilder.append("\nLab7\n");
//        trainingSet = new double[][]{{1, 1, 0, 0}, {0, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 1, 1}};
//        KahonenAlgorithm kahonenAlgorithm = new KahonenAlgorithm(4, 2);
//        stringBuilder.append(kahonenAlgorithm.launch(false));
//
//        /*Lab8*/
//        stringBuilder.append("\nLab8\n");
//        trainingSet = new double[][]{{1, 1, 0, 0}, {0, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 1, 1}};
//        CounterAlgorithm counterAlgorithm = new CounterAlgorithm(4,
//                new Neuronet.HiddenLayerStruct(3), 4);
//        stringBuilder.append(counterAlgorithm.launch(false));
//
//        /*Lab9*/
//        stringBuilder.append("\nLab9\n");
//        trainingSet = new double[][]{{-1, 1, -1, 1}, {1, -1, 1, 1}, {-1, 1, -1, -1}};
//        HopfieldAlgorithm hopfieldAlgorithm = new HopfieldAlgorithm(trainingSet[0].length);
//        stringBuilder.append(hopfieldAlgorithm.launch(false));

        /*Lab11*/
        stringBuilder.append("\nLab11\n");
        GeneticAlgorithm geneticAlgorithm =
                new GeneticAlgorithm(50, 3, -13) {
                    @Override
                    public int function(int... values) {
                        return values[0] * values[2] + 99 * values[1] + 13 * values[2];
                    }
                };
        stringBuilder.append(geneticAlgorithm.launch());

        System.out.println(stringBuilder.toString());
    }

    private static void trainingSetNumbersInit() {
        Main.trainingSet = new double[10][35];
        for (int i = 0; i < 10; i++) {
            BufferedImage tmp = FileManager.readImage("D:\\YandexDisk\\MyProjects\\Idea\\Neuronet\\" +
                    "data\\Data_" + i + ".png");
            for (int j = 0; j < tmp.getHeight() * tmp.getWidth(); j++) {
                Main.trainingSet[i][j] = tmp.getRGB(j % tmp.getWidth(), j / tmp.getWidth()) == -1 ? -1 : 1;
            }
        }
    }

    private static void trainingSetLettersInit() {
        Main.trainingSet = new double[26][35];
        for (int i = 0; i < 26; i++) {
            BufferedImage tmp = FileManager.readImage("D:\\YandexDisk\\MyProjects\\Idea\\Neuronet\\" +
                    "data\\Data_" + ALPHABET[i] + ".png");
            for (int j = 0; j < tmp.getHeight() * tmp.getWidth(); j++) {
                Main.trainingSet[i][j] = tmp.getRGB(j % tmp.getWidth(), j / tmp.getWidth()) == -1 ? -1 : 1;
            }
        }
    }

    public static int getMaxOutputIndex(double[] result) {
        double max = -Double.MAX_VALUE;
        int indexMax = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] > max) {
                max = result[i];
                indexMax = i;
            }
        }
        return indexMax;
    }

    public static class Learning extends Thread {
        private int trainingCount;
        private IAlgorithm iAlgorithm;
        private double[][] trainingSet;
        private double[][] ideal;
        private GUI gui;

        public Learning(IAlgorithm iAlgorithm, double[][] trainingSet, double[][] ideal, int trainingCount) {
            this.iAlgorithm = iAlgorithm;
            this.trainingSet = trainingSet;
            this.ideal = ideal;
            this.trainingCount = trainingCount;
            gui = new GUI(trainingCount * trainingSet.length);
        }

        @Override
        public void run() {
            boolean error = true;
            for (int i = 0; i < trainingCount || trainingCount == 0 && error; i++) {
                error = false;
                for (int j = 0; j < trainingSet.length; j++) {
                    if (iAlgorithm.learn(trainingSet[j], ideal[j])) {
                        error = true;
                    }
                    gui.setProgress(i * trainingSet.length + j + 1);
                }
                onEveryTraining();
            }
        }

        public void onEveryTraining() {
        }
    }
}