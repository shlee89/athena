package athena.api.classification.DecisionTree;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class DecisionTreeValidationSummary extends ClassificationValidationSummary {

    DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm;

    public DecisionTreeValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public DecisionTreeValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public DecisionTreeValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public DecisionTreeDetectionAlgorithm getDecisionTreeDetectionAlgorithm() {
        return decisionTreeDetectionAlgorithm;
    }

    public void setDecisionTreeDetectionAlgorithm(DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm) {
        this.decisionTreeDetectionAlgorithm = decisionTreeDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();

        System.out.println("------------------Classification (Decision Tree)----------------");

        if (decisionTreeDetectionAlgorithm.getNumClasses() != -1) {
            System.out.println("numClasses = " + decisionTreeDetectionAlgorithm.getNumClasses());
        }

        System.out.println("Impurity = " + decisionTreeDetectionAlgorithm.getImpurity());
        System.out.println("maxDepth = " + decisionTreeDetectionAlgorithm.getMaxDepth());
        System.out.println("maxBins = " + decisionTreeDetectionAlgorithm.getMaxBins());

        System.out.println("");
        System.out.println("");
    }


}
