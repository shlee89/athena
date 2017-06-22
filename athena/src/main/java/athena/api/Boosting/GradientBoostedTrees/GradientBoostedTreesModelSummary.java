package athena.api.Boosting.GradientBoostedTrees;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.tree.GradientBoostedTrees;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class GradientBoostedTreesModelSummary extends ClassificationModelSummary {

    GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm;
    GradientBoostedTreesModel model;


    public GradientBoostedTreesModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public GradientBoostedTreesModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public GradientBoostedTreesModelSummary(SparkContext sc) {
        super(sc);
    }

    public GradientBoostedTreesDetectionAlgorithm getGradientBoostedTreesDetectionAlgorithm() {
        return gradientBoostedTreesDetectionAlgorithm;
    }

    public void setGradientBoostedTreesDetectionAlgorithm(GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm) {
        this.gradientBoostedTreesDetectionAlgorithm = gradientBoostedTreesDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Boosting (GBTs)----------------");

        if (gradientBoostedTreesDetectionAlgorithm.getNumClasses() != -1) {
            System.out.println("numClasses = " + gradientBoostedTreesDetectionAlgorithm.getNumClasses());
        }

        System.out.println("iterations = " + gradientBoostedTreesDetectionAlgorithm.getIterations());
        System.out.println("maxDepth = " + gradientBoostedTreesDetectionAlgorithm.getMaxDepth());

        System.out.println("");
    }
}
