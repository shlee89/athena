package athena.api.classification.DecisionTree;

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
public class DecisionTreeDetectionAlgorithm implements DetectionAlgorithm, Serializable {
    public static String IMPURITY_GINI = "gini";
    public static String IMPURITY_ENTROPY = "entropy";

    int numClasses = -1;
    Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
    String impurity = "gini";
    Integer maxDepth = 5;
    Integer maxBins = 32;

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    public Map<Integer, Integer> getCategoricalFeaturesInfo() {
        return categoricalFeaturesInfo;
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

    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLASSIFICATION;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.DT;
    }
}
