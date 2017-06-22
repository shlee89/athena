package athena.api.classification.RandomForest;

import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionStrategy;

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
public class RandomForestDetectionAlgorithm implements DetectionAlgorithm, Serializable {
    public static String IMPURITY_GINI = "gini";
    public static String IMPURITY_ENTROPY = "entropy";

    public static String STRATEGY_AUTO = "auto";
    public static String STRATEGY_ALL = "all";
    public static String STRATEGY_SQRT = "sqrt";
    public static String STRATEGY_LOG = "log2";
    public static String STRATEGY_ONETHIRD = "onethird";

    int numClasses = -1;
    Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
    int numTrees = 3;
    String featureSubsetStrategy = STRATEGY_AUTO;
    String impurity = IMPURITY_GINI;
    Integer maxDepth = 5;
    Integer maxBins = 32;
    int seed = 12345;

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    public Map<Integer, Integer> getCategoricalFeaturesInfo() {
        return categoricalFeaturesInfo;
    }

    public void setCategoricalFeaturesInfo(Map<Integer, Integer> categoricalFeaturesInfo) {
        this.categoricalFeaturesInfo = categoricalFeaturesInfo;
    }

    public int getNumTrees() {
        return numTrees;
    }

    public void setNumTrees(int numTrees) {
        this.numTrees = numTrees;
    }

    public String getFeatureSubsetStrategy() {
        return featureSubsetStrategy;
    }

    public void setFeatureSubsetStrategy(String featureSubsetStrategy) {
        this.featureSubsetStrategy = featureSubsetStrategy;
    }

    public String getImpurity() {
        return impurity;
    }

    public void setImpurity(String impurity) {
        this.impurity = impurity;
    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Integer getMaxBins() {
        return maxBins;
    }

    public void setMaxBins(Integer maxBins) {
        this.maxBins = maxBins;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLASSIFICATION;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.RF;
    }
}
