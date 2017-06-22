package athena.api.classification.SVM;

import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionStrategy;
import org.apache.spark.mllib.linalg.Vector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for K-Means clustering algorithms
 * The options are described below:
 * data - training points stored as RDD[Vector]
 * k - number of clusters
 * maxIterations - max number of iterations
 * runs - number of parallel runs, defaults to 1. The best model is returned.
 * initializationMode - initialization model, either "random" or "k-means||" (default).
 * seed - random seed value for cluster initialization
 * epsion- Set the distance threshold within which we've consider centers to have converged.
 * Created by seunghyeon on 4/7/16.
 */
public class SVMDetectionAlgorithm implements DetectionAlgorithm, Serializable {

    int numIterations = 100;
    double stepSize = -1;
    double regParam = -1;
    double miniBatchFraction = -1;

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    public double getRegParam() {
        return regParam;
    }

    public void setRegParam(double regParam) {
        this.regParam = regParam;
    }

    public double getMiniBatchFraction() {
        return miniBatchFraction;
    }

    public void setMiniBatchFraction(double miniBatchFraction) {
        this.miniBatchFraction = miniBatchFraction;
    }

    public Vector getIntialWeight() {
        return intialWeight;
    }

    public void setIntialWeight(Vector intialWeight) {
        this.intialWeight = intialWeight;
    }

    Vector intialWeight = null;

    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLASSIFICATION;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.SVM;
    }
}
