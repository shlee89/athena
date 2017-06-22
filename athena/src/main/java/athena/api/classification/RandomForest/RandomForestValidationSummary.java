package athena.api.classification.RandomForest;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class RandomForestValidationSummary extends ClassificationValidationSummary {

    RandomForestDetectionAlgorithm randomForestDetectionAlgorithm;

    public RandomForestValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public RandomForestValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public RandomForestValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public RandomForestDetectionAlgorithm getRandomForestDetectionAlgorithm() {
        return randomForestDetectionAlgorithm;
    }

    public void setRandomForestDetectionAlgorithm(RandomForestDetectionAlgorithm randomForestDetectionAlgorithm) {
        this.randomForestDetectionAlgorithm = randomForestDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();

        System.out.println("------------------Classification (RandomForest)----------------");

        if (randomForestDetectionAlgorithm.getNumClasses() != -1) {
            System.out.println("numClasses = " + randomForestDetectionAlgorithm.getNumClasses());
        }

        System.out.println("numTrees = " + randomForestDetectionAlgorithm.getNumTrees());
        System.out.println("featureSubsetStrategy = " + randomForestDetectionAlgorithm.getFeatureSubsetStrategy());
        System.out.println("Impurity = " + randomForestDetectionAlgorithm.getImpurity());
        System.out.println("maxDepth = " + randomForestDetectionAlgorithm.getMaxDepth());
        System.out.println("maxBins = " + randomForestDetectionAlgorithm.getMaxBins());
        System.out.println("seed :" + randomForestDetectionAlgorithm.getSeed());

        System.out.println("");
        System.out.println("");
    }


}
