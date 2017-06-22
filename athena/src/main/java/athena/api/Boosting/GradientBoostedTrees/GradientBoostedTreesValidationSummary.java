package athena.api.Boosting.GradientBoostedTrees;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class GradientBoostedTreesValidationSummary extends ClassificationValidationSummary {

    GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm;

    public GradientBoostedTreesValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public GradientBoostedTreesValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public GradientBoostedTreesValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public GradientBoostedTreesDetectionAlgorithm getGradientBoostedTreesDetectionAlgorithm() {
        return gradientBoostedTreesDetectionAlgorithm;
    }

    public void setGradientBoostedTreesDetectionAlgorithm(GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm) {
        this.gradientBoostedTreesDetectionAlgorithm = gradientBoostedTreesDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();

        System.out.println("------------------Boosting (GBTs)----------------");

        if (gradientBoostedTreesDetectionAlgorithm.getNumClasses() != -1) {
            System.out.println("numClasses = " + gradientBoostedTreesDetectionAlgorithm.getNumClasses());
        }

        System.out.println("iterations = " + gradientBoostedTreesDetectionAlgorithm.getIterations());
        System.out.println("maxDepth = " + gradientBoostedTreesDetectionAlgorithm.getMaxDepth());


        System.out.println("");
        System.out.println("");
    }


}
