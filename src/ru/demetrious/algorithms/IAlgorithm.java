package ru.demetrious.algorithms;

import com.sun.istack.internal.NotNull;
import ru.demetrious.neuronet.INeuronet;

public interface IAlgorithm {
    StringBuilder launch(boolean learn);

    //double[] step(double[] input);

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

    default double[] step(double[] input) {
        return step(input, 0);
    }

    INeuronet[] getNeuronet();

    boolean learn(double[] input, @NotNull double[] ideal);

    double activationFunction(double x);
}
