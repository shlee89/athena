package athena.api.classification.RandomForest;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class RandomForestModelSummary extends ClassificationModelSummary {

    RandomForestDetectionAlgorithm randomForestDetectionAlgorithm;
    RandomForestModel model;


    public RandomForestModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public RandomForestModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public RandomForestModelSummary(SparkContext sc) {
        super(sc);
    }

    public RandomForestDetectionAlgorithm getRandomForestDetectionAlgorithm() {
        return randomForestDetectionAlgorithm;
    }

    public void setRandomForestDetectionAlgorithm(RandomForestDetectionAlgorithm randomForestDetectionAlgorithm) {
        this.randomForestDetectionAlgorithm = randomForestDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
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
    }
}
