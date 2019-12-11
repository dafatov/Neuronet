package ru.demetrious.neuronet.neuron;

import ru.demetrious.algorithms.IAlgorithm;

public class InputNeuron extends Neuron {

    public InputNeuron(IAlgorithm iAlgorithm) {
        super(iAlgorithm, 0);
    }

    /**
     * The input neuron cannot calculate so we process the error
     */
    @Override
    public void calculate() {
        try {
            throw new Exception("The InputNeuron does not have calculate functionality");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99008);
        }
    }
}
