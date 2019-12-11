package ru.demetrious;

import ru.demetrious.algorithms.*;
import ru.demetrious.neuronet.Neuronet;
import ru.demetrious.util.FileManager;
import ru.demetrious.util.GUI;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;

public class Main {
    //System.exit(1000328);
    public static final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static double[][] trainingSet;

    /**
     * The entry point to the program
     *
     * @param args Arguments are ignored
     */
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();

        /*Lab1*//*Реализация логической функции OR с ипользованием нейрона*/
        stringBuilder.append("\nLab1\n");
        SimplePerceptron simplePerceptronOR0 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
        stringBuilder.append(MessageFormat.format("0||0 = {0}", simplePerceptronOR0.calculate(0, 0))).append('\n');
        SimplePerceptron simplePerceptronOR1 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
        stringBuilder.append(MessageFormat.format("0||1 = {0}", simplePerceptronOR1.calculate(0, 1))).append('\n');
        SimplePerceptron simplePerceptronOR2 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
        stringBuilder.append(MessageFormat.format("1||0 = {0}", simplePerceptronOR2.calculate(1, 0))).append('\n');
        SimplePerceptron simplePerceptronOR3 = new SimplePerceptron(SimplePerceptron.LogicFunction.OR);
        stringBuilder.append(MessageFormat.format("1||1 = {0}", simplePerceptronOR3.calculate(1, 1))).append('\n');

        /*Lab2*//*Реализация логической функции NOT с ипользованием нейрона*/
        stringBuilder.append("\nLab2\n");
        SimplePerceptron simplePerceptronNOT0 = new SimplePerceptron(SimplePerceptron.LogicFunction.NOT);
        stringBuilder.append(MessageFormat.format("!0 = {0}", simplePerceptronNOT0.calculate(0))).append('\n');
        SimplePerceptron simplePerceptronNOT1 = new SimplePerceptron(SimplePerceptron.LogicFunction.NOT);
        stringBuilder.append(MessageFormat.format("!1 = {0}", simplePerceptronNOT1.calculate(1))).append('\n');

        /*Lab3*//*Распознавание цифр в формате изображения 5х7 с использованием адгоритма Хебба*/
        stringBuilder.append("\nLab3\n");
        trainingSetNumbersInit();
        HebbAlgorithm hebbAlgorithm = new HebbAlgorithm(Main.trainingSet[0].length, Main.trainingSet.length);
        stringBuilder.append(hebbAlgorithm.launch(false));

        /*Lab4*//*Распознавание букв латинского алфавита, в том же формате что и цифры; алгоритм Розенблата*/
        stringBuilder.append("\nLab4\n");
        trainingSetLettersInit();
        RosenblattAlgorithm rosenblattAlgorithm = new RosenblattAlgorithm(Main.trainingSet[0].length, 1, Main.trainingSet.length);
        stringBuilder.append(rosenblattAlgorithm.launch(false));

        /*Lab5*//*Применение обратного распространения ошибки в многослойной нейронной сети для решения
                    логической функции XOR*/
        stringBuilder.append("\nLab5\n");
        trainingSet = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        BackpropagationAlgorithm backpropagationAlgorithm = new BackpropagationAlgorithm(2, 1,
                new Neuronet.HiddenLayerStruct[]{new Neuronet.HiddenLayerStruct(100, -1),
                        new Neuronet.HiddenLayerStruct(100, 1)}, 1);
        stringBuilder.append(backpropagationAlgorithm.launch(false));

        /*Lab6*//*Наглядная демонстрация преимущества радиально-базисной функции над алгоритмом
                    обратного распространения на примере той же функции (XOR)*/
        stringBuilder.append("\nLab6\n");
        trainingSet = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        RBFAlgorithm rbfAlgorithm = new RBFAlgorithm(2, new Neuronet.HiddenLayerStruct(4), 1);
        stringBuilder.append(rbfAlgorithm.launch(false));

        /*Lab7*//*Простой пример кластеризации на основе принадлежности входа к правому или левому краю;
                    реализация выполена при помощи самоорганизующегося слоя Кохонена*/
        stringBuilder.append("\nLab7\n");
        trainingSet = new double[][]{{1, 1, 0, 0}, {0, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 1, 1}};
        KohonenAlgorithm kohonenAlgorithm = new KohonenAlgorithm(4, 2);
        stringBuilder.append(kohonenAlgorithm.launch(false));

        /*Lab8*//*Использование сетей встречного распространения для решения той же задачи,
                    уже в формате нечеткой принадлежности к одному из выходов в зависимости от входа*/
        stringBuilder.append("\nLab8\n");
        trainingSet = new double[][]{{1, 1, 0, 0}, {0, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 1, 1}};
        CounterAlgorithm counterAlgorithm = new CounterAlgorithm(4,
                new Neuronet.HiddenLayerStruct(9), 4);
        stringBuilder.append(counterAlgorithm.launch(false));

        /*Lab9*//*Нейросеть по Хопфилду позволяет восстанавливать образы из входный
                    до запомненных, таб в процессе обучения будет запомнено три образа*/
        stringBuilder.append("\nLab9\n");
        trainingSet = new double[][]{{-1, 1, -1, 1}, {1, -1, 1, 1}, {-1, 1, -1, -1}};
        HopfieldAlgorithm hopfieldAlgorithm = new HopfieldAlgorithm(trainingSet[0].length);
        stringBuilder.append(hopfieldAlgorithm.launch(false));

        /*Lab10 after Lab12*/

        /*Lab11*//*U - множество разных национальностей: русский(1), француз(2), алжирец(3), американец(4)
                    V - множество различной еды: борщ(1), лягушачьи лапки(2), онигири(3), бургер(4)
                    A = европеец = 0.7/1 + 1/2 + 0.2/3 + 0/4
                    B = калорийная = 0.8/1 + 0.2/2 + 0.6/3 + 1/4
                    C = горящая пища = 1/1 + 0.3/2 + 0/3 + 0.8/4*/
        stringBuilder.append("\nLab11\n");
        FuzzyLogic fuzzyLogic = new FuzzyLogic(new FuzzyLogic.FuzzySetStruct('A', .7, 1, .2, 0),
                new FuzzyLogic.FuzzySetStruct('B', .8, .2, .6, 1),
                new FuzzyLogic.FuzzySetStruct('C', 1, .3, 0, .8));
        stringBuilder.append(fuzzyLogic.launch());

        /*Lab12*//*Применение генетического алгоритма для решения Диофантовых уравнений
                    с любым количеством переменных и любой фукцией*/
        stringBuilder.append("\nLab12\n");
        stringBuilder.append("Function: ")
                .append("x * z + 99 * y + 13 * z")
                .append(" = ")
                .append("-13").append("\n");
        GeneticAlgorithm geneticAlgorithm =
                new GeneticAlgorithm(50, 3, -13) {
                    @Override
                    public int function(int... values) {
                        return values[0] * values[2] + 99 * values[1] + 13 * values[2];
                    }
                };
        stringBuilder.append(geneticAlgorithm.launch());

        /*Lab10*//*Использование библиотеки, реализующей сверточную нейронную сеть для обучения
                      нейронной сети пакету заранее заготовленных данных для обучения MNIST*/
        /*Run separately from the rest cause flood log*/
        stringBuilder.append("\nLab10\n");
        ConvolutionalNeuronet convolutionalNeuronet = new ConvolutionalNeuronet();
        convolutionalNeuronet.launch(false);

        System.out.println(stringBuilder.toString());
    }

    /**
     * Initialization training set of numbers in 5x7 format
     */
    private static void trainingSetNumbersInit() {
        Main.trainingSet = new double[10][35];
        for (int i = 0; i < 10; i++) {
            BufferedImage tmp = FileManager.readImage("D:\\YandexDisk\\MyProjects\\Idea\\Neuronet\\" +
                    "data\\numbers\\Data_" + i + ".png");
            for (int j = 0; j < tmp.getHeight() * tmp.getWidth(); j++) {
                Main.trainingSet[i][j] = tmp.getRGB(j % tmp.getWidth(), j / tmp.getWidth()) == -1 ? -1 : 1;
            }
        }
    }

    /**
     * Initialization training set of roman letters in 5x7 format
     */
    private static void trainingSetLettersInit() {
        Main.trainingSet = new double[26][35];
        for (int i = 0; i < 26; i++) {
            BufferedImage tmp = FileManager.readImage("D:\\YandexDisk\\MyProjects\\Idea\\Neuronet\\" +
                    "data\\letters\\Data_" + ALPHABET[i] + ".png");
            for (int j = 0; j < tmp.getHeight() * tmp.getWidth(); j++) {
                Main.trainingSet[i][j] = tmp.getRGB(j % tmp.getWidth(), j / tmp.getWidth()) == -1 ? -1 : 1;
            }
        }
    }

    /**
     * Used to determinate the maximum output in neuronets
     *
     * @param result Result on neural network outputs
     * @return Maximum output of neural network
     */
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

        /**
         * Сonstructor of the class responsible for training neural networks in a separate thread
         * and displays the learning process in the interface
         *
         * @param iAlgorithm    Used algorithm from the 'algorithm' package
         * @param trainingSet   The array of inputs training data
         * @param ideal         The ideal results of a neural network. May be null
         * @param trainingCount The count of training cycles.
         *                      If zero the output from cycle depends on the error
         */
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

        /**
         * Event called during the training every after each complete loop through the training sample
         */
        public void onEveryTraining() {
        }
    }
}