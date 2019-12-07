import java.util.Arrays;

class SimplePerceptron implements IAlgorithm {
    private LogicFunction logicFunction;
    private INeuronet neuronet;

    SimplePerceptron(LogicFunction logicFunction) {
        init(logicFunction);
    }

    private void init(LogicFunction logicFunction) {
        this.logicFunction = logicFunction;
        switch (this.logicFunction) {
            case OR:
                neuronet = new Neuronet(this, 2, 1, 1);
                for (int i = 0; i < neuronet.getOutput().neurons.length; i++) {
                    Arrays.fill(neuronet.getOutput().neurons[i].weight, 1);
                    neuronet.getOutput().neurons[i].biasWeight = -0.5;
                }
                break;
            case NOT:
                neuronet = new Neuronet(this, 1, 1, 1);
                for (int i = 0; i < neuronet.getOutput().neurons.length; i++) {
                    Arrays.fill(neuronet.getOutput().neurons[i].weight, -1);
                    neuronet.getOutput().neurons[i].biasWeight = 0.5;
                }
                break;
            default:
                try {
                    throw new Exception("Wrong logical function in " + this.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(99101);
                }
        }
    }

    int calculate(int x1, int x2) {
        if (logicFunction.equals(LogicFunction.OR)) {
            if ((x1 == 0 || x1 == 1) && (x2 == 0 || x2 == 1)) {
                neuronet.getInput().neurons[0].output = x1;
                neuronet.getInput().neurons[1].output = x2;
                neuronet.getInput().next.calculate();
                return (int) neuronet.getOutput().neurons[0].output;
            } else try {
                throw new Exception("Wrong input data... Need logical values");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(99102);
            }
        } else try {
            throw new Exception("Wrong parameters for " + logicFunction.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99103);
        }
        return -1;
    }

    int calculate(int x) {
        if (logicFunction.equals(LogicFunction.NOT)) {
            if (x == 0 || x == 1) {
                neuronet.getInput().neurons[0].output = x;
                neuronet.getInput().next.calculate();
                return (int) neuronet.getOutput().neurons[0].output;
            } else try {
                throw new Exception("Wrong input data... Need logical values");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(99104);
            }
        } else try {
            throw new Exception("Wrong parameters for " + logicFunction.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99105);
        }
        return -1;
    }

    @Override
    public double activationFunction(double x) {
        return x > 0 ? 1. : 0.;
    }

    @Override
    public StringBuilder launch(boolean learn) {
        System.err.println("This method is not used");
        return null;
    }

    @Override
    public double[] step(double[] input) {
        System.err.println("This method is not used");
        return null;
    }

    @Override
    public INeuronet[] getNeuronet() {
        return new INeuronet[]{neuronet};
    }

    @Override
    public boolean learn(double[] input, double[] ideal) {
        System.err.println("This method is not used");
        return true;
    }

    enum LogicFunction {
        OR, NOT
    }
}
