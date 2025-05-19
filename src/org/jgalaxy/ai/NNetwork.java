package org.jgalaxy.ai;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NNetwork {

  static public NNetwork of( File pConfigTraining ) throws IOException {
    Map<String, String> config = new HashMap<>();
    Pair<double[][],double[][]> trainingData = readTrainingData( pConfigTraining.getAbsolutePath(), config);
    int inputSize = Integer.parseInt(config.getOrDefault("inputSize", "8"));
    int hiddenSize = Integer.parseInt(config.getOrDefault("hiddenSize", "8"));
    int outputSize = Integer.parseInt(config.getOrDefault("outputSize", "1"));
    int epochs = Integer.parseInt(config.getOrDefault("epochs", "1000"));
    double learningRate = Double.parseDouble(config.getOrDefault("learningRate", "0.1"));
    NNetwork nn = new NNetwork(inputSize, hiddenSize, outputSize, learningRate);
    nn.train(trainingData.getKey(), trainingData.getValue(), epochs);
    return nn;
  }

  private final int inputSize;
  private final int hiddenSize;
  private final int outputSize;

  private final double[][] weightsInputHidden;
  private final double[][] weightsHiddenOutput;

  private final double[] hiddenLayer;
  private final double[] outputLayer;

  private       double learningRate = 0.01;
  private final Random random = new Random();

  public NNetwork(int inputSize, int hiddenSize, int outputSize, double learningRate) {
    this.inputSize = inputSize;
    this.hiddenSize = hiddenSize;
    this.outputSize = outputSize;
    this.learningRate = learningRate;

    weightsInputHidden = new double[inputSize][hiddenSize];
    weightsHiddenOutput = new double[hiddenSize][outputSize];

    hiddenLayer = new double[hiddenSize];
    outputLayer = new double[outputSize];

    initializeWeights();
  }

  private void initializeWeights() {
    for (int i = 0; i < inputSize; i++) {
      for (int j = 0; j < hiddenSize; j++) {
        weightsInputHidden[i][j] = (random.nextDouble() - 0.5) * 2;
      }
    }
    for (int i = 0; i < hiddenSize; i++) {
      for (int j = 0; j < outputSize; j++) {
        weightsHiddenOutput[i][j] = (random.nextDouble() - 0.5) * 2;
      }
    }
  }

  public double[] forward(double[] input) {
    // Hidden layer
    for (int i = 0; i < hiddenSize; i++) {
      hiddenLayer[i] = 0;
      for (int j = 0; j < inputSize; j++) {
        hiddenLayer[i] += input[j] * weightsInputHidden[j][i];
      }
      hiddenLayer[i] = sigmoid(hiddenLayer[i]);
    }

    // Output layer
    for (int i = 0; i < outputSize; i++) {
      outputLayer[i] = 0;
      for (int j = 0; j < hiddenSize; j++) {
        outputLayer[i] += hiddenLayer[j] * weightsHiddenOutput[j][i];
      }
      outputLayer[i] = sigmoid(outputLayer[i]);
    }

    return outputLayer.clone();
  }

  public void train(double[][] trainingData, double[][] expectedOutput, int epochs) {
    for (int epoch = 0; epoch < epochs; epoch++) {
      double totalError = 0;

      for (int sample = 0; sample < trainingData.length; sample++) {
        double[] input = trainingData[sample];
        double[] expected = expectedOutput[sample];

        double[] output = forward(input);

        double[] outputDelta = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
          double error = expected[i] - output[i];
          outputDelta[i] = error * sigmoidDerivative(output[i]);
          totalError += error * error;
        }

        double[] hiddenDelta = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
          double sum = 0;
          for (int j = 0; j < outputSize; j++) {
            sum += outputDelta[j] * weightsHiddenOutput[i][j];
          }
          hiddenDelta[i] = sum * sigmoidDerivative(hiddenLayer[i]);
        }

        // Update weights hidden → output
        for (int i = 0; i < hiddenSize; i++) {
          for (int j = 0; j < outputSize; j++) {
            weightsHiddenOutput[i][j] += learningRate * outputDelta[j] * hiddenLayer[i];
          }
        }

        // Update weights input → hidden
        for (int i = 0; i < inputSize; i++) {
          for (int j = 0; j < hiddenSize; j++) {
            weightsInputHidden[i][j] += learningRate * hiddenDelta[j] * input[i];
          }
        }
      }

//      if (epoch % 100 == 0) {
//        System.out.printf("Epoch %d - Total Error: %.6f%n", epoch, totalError);
//      }
    }
  }

  private double sigmoid(double x) {
    return 1.0 / (1.0 + Math.exp(-x));
  }

  private double sigmoidDerivative(double x) {
    return x * (1 - x);
  }

  public int classify(double[] input) {
    double[] output = forward(input);
    int maxIndex = 0;
    for (int i = 1; i < output.length; i++) {
      if (output[i] > output[maxIndex]) {
        maxIndex = i;
      }
    }
    return maxIndex;
  }

  static public Pair<double[][],double[][]> readTrainingData(String fileName, Map<String, String> config) throws IOException {
    List<double[]> dataList = new ArrayList<>();
    List<double[]> output = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) continue;  // Skip comments & empty lines

        if (line.contains("=")) {
          // Read network configuration
          String[] parts = line.split("=");
          config.put(parts[0].trim(), parts[1].trim());
        } else {
          // Read training data
          int inputSize = Integer.parseInt(config.get("inputSize").trim());
          int outputSize = Integer.parseInt(config.get("outputSize").trim());
          String[] values = line.split(",");
          double[] inputs = new double[inputSize];
          double[] outputs = new double[outputSize];

          for (int i = 0; i < inputSize; i++) {
            inputs[i] = Double.parseDouble(values[i].trim());
          }
          dataList.add(inputs);
          for (int i = inputSize; i < (inputSize+outputSize); i++) {
            outputs[i-inputSize] = Double.parseDouble(values[i].trim());
          }
          output.add(outputs);
        }
      }
    }
    return new Pair(dataList.toArray(new double[0][]),output.toArray(new double[0][]));
  }


}
