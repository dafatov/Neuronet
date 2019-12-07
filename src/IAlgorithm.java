import com.sun.istack.internal.NotNull;

interface IAlgorithm {
    StringBuilder launch(boolean learn);

    //double[] step(double[] input);

    default double[] step(double[] input, int index) {
        if (getNeuronet()[index].getInput().neurons.length == input.length) {
            for (int i = 0; i < getNeuronet()[index].getInput().neurons.length; i++) {
                getNeuronet()[index].getInput().neurons[i].output = input[i];
            }
            getNeuronet()[index].getInput().next.calculate();
            double[] result = new double[getNeuronet()[index].getOutput().neurons.length];
            for (int i = 0; i < getNeuronet()[index].getOutput().neurons.length; i++) {
                result[i] = getNeuronet()[index].getOutput().neurons[i].output;
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
