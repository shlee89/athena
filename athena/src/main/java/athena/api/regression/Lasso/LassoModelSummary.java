package athena.api.regression.Lasso;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.regression.LassoModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class LassoModelSummary extends ClassificationModelSummary {

    LassoDetectionAlgorithm lassoDetectionAlgorithm;
    LassoModel model;


    public LassoModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public LassoModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public LassoModelSummary(SparkContext sc) {
        super(sc);
    }

    public LassoDetectionAlgorithm getLassoDetectionAlgorithm() {
        return lassoDetectionAlgorithm;
    }

    public void setLassoDetectionAlgorithm(LassoDetectionAlgorithm lassoDetectionAlgorithm) {
        this.lassoDetectionAlgorithm = lassoDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Classification (Lasso Regression)----------------");

        System.out.println("numClasses = " + lassoDetectionAlgorithm.getNumClasses());

        System.out.println("NumIterations = " + lassoDetectionAlgorithm.getNumIterations());

        if (lassoDetectionAlgorithm.getStepSize() != -1) {
            System.out.println("StepSize = " + lassoDetectionAlgorithm.getStepSize());
        }

        if (lassoDetectionAlgorithm.getMiniBatchFraction() != -1) {
            System.out.println("MiniBatchFraction = " + lassoDetectionAlgorithm.getMiniBatchFraction());
        }
        if (lassoDetectionAlgorithm.getRegParam() != -1) {
            System.out.println("RegParam = " + lassoDetectionAlgorithm.getRegParam());
        }
    }
}
