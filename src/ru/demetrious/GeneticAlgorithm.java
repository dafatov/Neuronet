package ru.demetrious;

import java.util.Random;

public class GeneticAlgorithm {
    final static long MAX_ITERATIONS = 100000;
    final static float MUTATION_CHANCE = 6F;

    private Chromosome[] population;
    private int unknowns;
    private int target;
    private long iterations;

    public GeneticAlgorithm(int maxPopulation, int unknowns, int target) {
        setPopulation(new Chromosome[maxPopulation]);
        this.setUnknowns(unknowns);
        this.setTarget(target);

        initPopulation();
    }

    private void initPopulation() {
        for (int i = 0; i < getPopulation().length; i++) {
            getPopulation()[i] = new Chromosome(this, getUnknowns());
        }
    }

    public StringBuilder launch() {
        StringBuilder stringBuilder = new StringBuilder();
        Chromosome chromosome = solve();

        if (chromosome != null) {
            stringBuilder.append("Solution is found: ").append(chromosome).append(" in ")
                    .append(iterations).append(" iterations").append("\n");
        } else {
            stringBuilder.append("No solution found in ").append(iterations).append(" iterations");
        }
        return stringBuilder;
    }

    public Chromosome solve() {
        Chromosome chromosome = null;

        iterations = 0;
        while (++iterations < MAX_ITERATIONS) {
            int solveIndex = checkSolve();

            if (solveIndex != -2) {
                chromosome = getPopulation()[solveIndex];
                break;
            }

            calculateProbability();

            int[][] pairs = getPairsForCrossover();

            setPopulation(makeCrossover(pairs));
        }
        return chromosome;
    }

    private Chromosome[] makeCrossover(int[][] pairs) {
        Chromosome[] nextGeneration = new Chromosome[getPopulation().length];

        for (int i = 0; i < getPopulation().length; i++) {
            Chromosome firstPair = getPopulation()[pairs[i][0]];
            Chromosome secondPair = getPopulation()[pairs[i][1]];

            Chromosome child = firstPair.getChildCrossing(secondPair);
            child.mutate();
            nextGeneration[i] = child;
        }
        return nextGeneration;
    }

    private int[][] getPairsForCrossover() {
        int[][] pairs = new int[getPopulation().length][2];
        for (int i = 0; i < getPopulation().length; i++) {
            float random = getRandomAlpha();
            int firstPair = getPair(random);
            int secondPair;
            do {
                random = getRandomAlpha();
                secondPair = getPair(random);
            } while (firstPair == secondPair);

            pairs[i][0] = firstPair;
            pairs[i][1] = secondPair;
        }
        return pairs;
    }

    private int getPair(float random) {
        int i;
        for (i = 0; i < getPopulation().length; i++) {
            if (random <= getPopulation()[i].getProbability()) {
                return i;
            }
        }
        return i - 1;
    }

    float getRandomAlpha() {
        return (float) (Math.random());
    }

    private void calculateProbability() {
        float fitnessSum = calculateFitnessSum();
        float last = 0F;
        for (Chromosome chromosome : getPopulation()) {
            float probability = last + (chromosome.getFitness() / fitnessSum);
            last = probability;
            chromosome.setProbability(probability);
        }
    }

    private float calculateFitnessSum() {
        float sum = 0F;
        for (Chromosome chromosome : getPopulation()) {
            sum += chromosome.getFitness();
        }
        return sum;
    }

    public int function(int... values) {
        return 0;
    }

    private int checkSolve() {
        for (int i = 0; i < getPopulation().length; i++) {
            float current = getPopulation()[i].calculateFitness();
            getPopulation()[i].setFitness(current);

            if (current == -1)
                return i;
        }
        return -2;
    }

    public Chromosome[] getPopulation() {
        return population;
    }

    public void setPopulation(Chromosome[] population) {
        this.population = population;
    }

    public int getUnknowns() {
        return unknowns;
    }

    public void setUnknowns(int unknowns) {
        this.unknowns = unknowns;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    private class Chromosome {
        private float probability;
        private GeneticAlgorithm geneticAlgorithm;
        private int[] genes;
        private float fitness;

        Chromosome(GeneticAlgorithm geneticAlgorithm, int unknowns) {
            this.setGeneticAlgorithm(geneticAlgorithm);
            setGenes(new int[unknowns]);
            init();
        }

        private void init() {
            for (int i = 0; i < getGenes().length; i++) {
                getGenes()[i] = getRandomGene();
            }
        }

        float calculateFitness() {
            int closeness = Math.abs(getGeneticAlgorithm().getTarget() - getGeneticAlgorithm().function(getGenes()));
            if (closeness != 0) {
                return 1 / (float) closeness;
            } else return -1;
        }

        private int getRandomGene() {
            return getRandomIntRange(-Math.abs(getGeneticAlgorithm().getTarget()), Math.abs(getGeneticAlgorithm().getTarget()));
        }

        private int getRandomIntRange(int start, int end) {
            Random random = new Random();
            return random.nextInt(end - start) + start;
        }

        Chromosome getChildCrossing(Chromosome pair) {
            Chromosome[] children = getChildrenCrossing(pair);
            return children[getRandomIntRange(0, 1)];
        }

        private Chromosome[] getChildrenCrossing(Chromosome pair) {
            int crossingLine = getRandomCrossingLine();
            Chromosome[] children = new Chromosome[2];
            children[0] = new Chromosome(getGeneticAlgorithm(), getGeneticAlgorithm().getUnknowns());
            children[1] = new Chromosome(getGeneticAlgorithm(), getGeneticAlgorithm().getUnknowns());

            for (int i = 0; i < getGeneticAlgorithm().getUnknowns(); i++) {
                if (i < crossingLine) {
                    children[0].getGenes()[i] = this.getGenes()[i];
                    children[1].getGenes()[i] = pair.getGenes()[i];
                } else {
                    children[0].getGenes()[i] = pair.getGenes()[i];
                    children[1].getGenes()[i] = this.getGenes()[i];
                }
            }
            return children;
        }

        private int getRandomCrossingLine() {
            return getRandomIntRange(0, getGeneticAlgorithm().getUnknowns() - 2);
        }

        void mutate() {
            for (int i = 0; i < getGeneticAlgorithm().getUnknowns(); i++) {
                float randomPercentage = getGeneticAlgorithm().getRandomAlpha();
                if (randomPercentage < GeneticAlgorithm.MUTATION_CHANCE) {
                    getGenes()[i] = getRandomGene();
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("[");
            for (int i = 0; i < getGenes().length; i++) {
                stringBuilder.append(getGenes()[i]).append(i == getGenes().length - 1 ? "]" : ", ");
            }
            return stringBuilder.toString();
        }

        public float getProbability() {
            return probability;
        }

        public void setProbability(float probability) {
            this.probability = probability;
        }

        public GeneticAlgorithm getGeneticAlgorithm() {
            return geneticAlgorithm;
        }

        public void setGeneticAlgorithm(GeneticAlgorithm geneticAlgorithm) {
            this.geneticAlgorithm = geneticAlgorithm;
        }

        public int[] getGenes() {
            return genes;
        }

        public void setGenes(int[] genes) {
            this.genes = genes;
        }

        public float getFitness() {
            return fitness;
        }

        public void setFitness(float fitness) {
            this.fitness = fitness;
        }
    }
}