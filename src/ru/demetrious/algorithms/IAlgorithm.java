package ru.demetrious.algorithms;

import ru.demetrious.neuronet.INeuronet;

public interface IAlgorithm {
    /**
     * Execution of the specified task for the current algorithm
     * The source and target data are specified here
     *
     * @param learn Boolean parameter. If true neural network starts learning
     *              and then calculates the results for each input data.
     *              If false neural network calculates the result for each input data
     *              using saved weights it received during training
     * @return StringBuilder containing sources and results of the algorithm
     */
    StringBuilder launch(boolean learn);

    /**
     * Sets the corresponding input value for each input neuron
     * and call calculate on the next layer
     * then read the results on output layer and return them
     *
     * @param input Input data
     * @param index the index of the neural network used in algorithms
     *              with more than one neural network.
     *              The next neural network will receive the input
     *              output of the current neural network
     * @return Output of current neural network with these input data
     */
    default double[] step(double[] input, int index) {
        if (getNeuronet()[index].getInput().getNeurons().length == input.length) {
            for (int i = 0; i < getNeuronet()[index].getInput().getNeurons().length; i++) {
                getNeuronet()[index].getInput().getNeurons()[i].setOutput(input[i]);
            }
            getNeuronet()[index].getInput().getNext().calculate();
            double[] result = new double[getNeuronet()[index].getOutput().getNeurons().length];
            for (int i = 0; i < getNeuronet()[index].getOutput().getNeurons().length; i++) {
                result[i] = getNeuronet()[index].getOutput().getNeurons()[i].getOutput();
            }
            result = getNeuronet()[index].onAfterStep(result);
            if (++index < getNeuronet().length) {
                result = step(result, index);
            }
            return result;
        } else try {
            throw new Exception("Invalid input");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99619);
        }
        return null;
    }

    /**
     * Call method step with index 0. Used when algorithm using only one
     * neural network
     *
     * @param input Input data
     * @return Output of current neural network with these input data
     */
    default double[] step(double[] input) {
        return step(input, 0);
    }

    /**
     * Get all using neural networks in algorithm
     *
     * @return Return Neural networks array
     */
    INeuronet[] getNeuronet();

    /**
     * Learn the neural network by input data and ideal result.
     * Ideal resul may be null.
     *
     * @param input Input data
     * @param ideal Ideal result data
     * @return Returns whether the real output value matches the ideal
     */
    boolean learn(double[] input, double[] ideal);

    /**
     * Activation function, which is calculated for the output of each neuron
     *
     * @param x Input
     * @return Output
     */
    double activationFunction(double x);
}
