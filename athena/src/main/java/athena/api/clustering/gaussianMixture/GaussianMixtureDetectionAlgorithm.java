package athena.api.clustering.gaussianMixture;

import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionStrategy;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;

import java.io.Serializable;

/**
 * Implementation for K-Means clustering algorithms
 * The options are described below:
 * data - training points stored as RDD[Vector]
 * k - number of clusters
 * maxIterations - max number of iterations
 * convergenceTol - the largest change in log-likelihood at which convergence is considered to have occurred.
 * initializationMode - the initial GMM starting point, bypassing the random initialization..
 * seed - random seed value for cluster initialization
 * Created by seunghyeon on 4/7/16.
 */
public class GaussianMixtureDetectionAlgorithm implements DetectionAlgorithm, Serializable {
    int k = -1;
    double convergenceTol = -1;
    int maxIterations = -1;
    long seed = -1;
    GaussianMixtureModel initializedModel = null;


    public GaussianMixtureDetectionAlgorithm() {
    }

    public GaussianMixtureDetectionAlgorithm(int k) {
        this.k = k;
    }

    public GaussianMixtureDetectionAlgorithm(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    public GaussianMixtureDetectionAlgorithm(int k, double convergenceTol, int maxIterations, long seed) {
        this.k = k;
        this.convergenceTol = convergenceTol;
        this.maxIterations = maxIterations;
        this.seed = seed;
    }

    public GaussianMixtureDetectionAlgorithm(int k, int maxIterations, long seed) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.seed = seed;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public double getConvergenceTol() {
        return convergenceTol;
    }

    public void setConvergenceTol(double convergenceTol) {
        this.convergenceTol = convergenceTol;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public GaussianMixtureModel getInitializedModel() {
        return initializedModel;
    }

    public void setInitializedModel(GaussianMixtureModel initializedModel) {
        this.initializedModel = initializedModel;
    }

    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLUSTERING;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.GMM;
    }
}
