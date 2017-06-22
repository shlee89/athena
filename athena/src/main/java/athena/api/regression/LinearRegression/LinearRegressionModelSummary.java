package athena.api.regression.LinearRegression;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.regression.LinearRegressionModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class LinearRegressionModelSummary extends ClassificationModelSummary {

    LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm;
    LinearRegressionModel model;


    public LinearRegressionModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public LinearRegressionModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public LinearRegressionModelSummary(SparkContext sc) {
        super(sc);
    }

    public LinearRegressionDetectionAlgorithm getLinearRegressionDetectionAlgorithm() {
        return linearRegressionDetectionAlgorithm;
    }

    public void setLinearRegressionDetectionAlgorithm(LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm) {
        this.linearRegressionDetectionAlgorithm = linearRegressionDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Classification (Linear Regression)----------------");

        System.out.println("numClasses = " + linearRegressionDetectionAlgorithm.getNumClasses());

        System.out.println("NumIterations = " + linearRegressionDetectionAlgorithm.getNumIterations());

        if (linearRegressionDetectionAlgorithm.getStepSize() != -1) {
            System.out.println("StepSize = " + linearRegressionDetectionAlgorithm.getStepSize());
        }

        if (linearRegressionDetectionAlgorithm.getMiniBatchFraction() != -1) {
            System.out.println("MiniBatchFraction = " + linearRegressionDetectionAlgorithm.getMiniBatchFraction());
        }
    }
}
