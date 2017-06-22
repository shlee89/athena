package athena.api.clustering.kmeans;

import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionStrategy;

import java.io.Serializable;

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
public class KMeansDetectionAlgorithm implements DetectionAlgorithm, Serializable {
    int k = -1;
    int maxIterations = -1;
    int runs = -1;
    String initializationMode = null;
    long seed = -1;
    double epsilon = -1;


    public KMeansDetectionAlgorithm() {
    }

    public KMeansDetectionAlgorithm(int k) {
        this.k = k;
    }

    public KMeansDetectionAlgorithm(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    public KMeansDetectionAlgorithm(int k, int maxIterations, int runs) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.runs = runs;
    }

    public KMeansDetectionAlgorithm(int k, int maxIterations, int runs, String initializationMode) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.runs = runs;
        this.initializationMode = initializationMode;
    }

    public KMeansDetectionAlgorithm(int k, int maxIterations, int runs, String initializationMode, long seed) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.runs = runs;
        this.initializationMode = initializationMode;
        this.seed = seed;
    }

    public KMeansDetectionAlgorithm(int k, int maxIterations, int runs, String initializationMode, long seed, double epsilon) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.runs = runs;
        this.initializationMode = initializationMode;
        this.seed = seed;
        this.epsilon = epsilon;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public String getInitializationMode() {
        return initializationMode;
    }

    public void setInitializationMode(String initializationMode) {
        this.initializationMode = initializationMode;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLUSTERING;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.KMEANS;
    }
}
