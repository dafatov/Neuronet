package ru.demetrious;

import edu.hitsz.c102c.cnn.CNN;
import edu.hitsz.c102c.cnn.Layer;
import edu.hitsz.c102c.dataset.Dataset;

public class ConvolutionalNeuronet {
    private CNN cnn;

    public ConvolutionalNeuronet() {
        init();
    }

    /**
     * Initialization of convolutional neural network from lib
     */
    private void init() {
        CNN.LayerBuilder builder = new CNN.LayerBuilder();
        builder.addLayer(Layer.buildInputLayer(new Layer.Size(28, 28)));
        builder.addLayer(Layer.buildConvLayer(6, new Layer.Size(5, 5)));
        builder.addLayer(Layer.buildSampLayer(new Layer.Size(2, 2)));
        builder.addLayer(Layer.buildConvLayer(12, new Layer.Size(5, 5)));
        builder.addLayer(Layer.buildSampLayer(new Layer.Size(2, 2)));
        builder.addLayer(Layer.buildOutputLayer(10));
        cnn = new CNN(builder, 50);
    }

    /**
     * Execution of the specified task for the current algorithm
     * The source and target data are specified here
     */
    public void launch(boolean learn) {
        if (learn) {
            String fileName = "data/mnist/train.format";
            Dataset dataset = Dataset.load(fileName, ",", 784);
            cnn.train(dataset, 5);
            cnn.saveModel("weights/cnn.serializable");
        }

        if (!learn) {
            cnn = CNN.loadModel("weights/cnn.serializable");
        }

        Dataset testset = Dataset.load("data/mnist/test.format", ",", -1);
        assert testset != null;
        cnn.predict(testset, "data/mnist/test.predict");
    }
}
