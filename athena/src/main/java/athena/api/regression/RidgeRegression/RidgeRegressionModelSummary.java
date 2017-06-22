package athena.api.regression.RidgeRegression;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.regression.LinearRegressionModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class RidgeRegressionModelSummary extends ClassificationModelSummary {

    RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm;
    LinearRegressionModel model;


    public RidgeRegressionModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public RidgeRegressionModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public RidgeRegressionModelSummary(SparkContext sc) {
        super(sc);
    }

    public RidgeRegressionDetectionAlgorithm getRidgeRegressionDetectionAlgorithm() {
        return ridgeRegressionDetectionAlgorithm;
    }

    public void setRidgeRegressionDetectionAlgorithm(RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm) {
        this.ridgeRegressionDetectionAlgorithm = ridgeRegressionDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Classification (Ridge Regression)----------------");

        System.out.println("numClasses = " + ridgeRegressionDetectionAlgorithm.getNumClasses());

        System.out.println("NumIterations = " + ridgeRegressionDetectionAlgorithm.getNumIterations());

        if (ridgeRegressionDetectionAlgorithm.getStepSize() != -1) {
            System.out.println("StepSize = " + ridgeRegressionDetectionAlgorithm.getStepSize());
        }

        if (ridgeRegressionDetectionAlgorithm.getMiniBatchFraction() != -1) {
            System.out.println("MiniBatchFraction = " + ridgeRegressionDetectionAlgorithm.getMiniBatchFraction());
        }
        if (ridgeRegressionDetectionAlgorithm.getRegParam() != -1) {
            System.out.println("RegParam = " + ridgeRegressionDetectionAlgorithm.getRegParam());
        }
    }
}
