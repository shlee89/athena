package athena.api.classification.DecisionTree;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class DecisionTreeModelSummary extends ClassificationModelSummary {

    DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm;
    DecisionTreeModel model;


    public DecisionTreeModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public DecisionTreeModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public DecisionTreeModelSummary(SparkContext sc) {
        super(sc);
    }

    public DecisionTreeDetectionAlgorithm getDecisionTreeDetectionAlgorithm() {
        return decisionTreeDetectionAlgorithm;
    }

    public void setDecisionTreeDetectionAlgorithm(DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm) {
        this.decisionTreeDetectionAlgorithm = decisionTreeDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Classification (Decision Tree)----------------");

        if (decisionTreeDetectionAlgorithm.getNumClasses() != -1) {
            System.out.println("numClasses = " + decisionTreeDetectionAlgorithm.getNumClasses());
        }

        System.out.println("Impurity = " + decisionTreeDetectionAlgorithm.getImpurity());
        System.out.println("maxDepth = " + decisionTreeDetectionAlgorithm.getMaxDepth());
        System.out.println("maxBins = " + decisionTreeDetectionAlgorithm.getMaxBins());
        System.out.println("categoricalFeatureInfo :" + decisionTreeDetectionAlgorithm.toString());


        System.out.println("");
    }
}
