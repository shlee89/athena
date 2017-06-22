package athena.api.regression.RidgeRegression;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import athena.api.regression.RegressionValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class RidgeRegressionValidationSummary extends RegressionValidationSummary {

    RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm;

    public RidgeRegressionDetectionAlgorithm getRidgeRegressionDetectionAlgorithm() {
        return ridgeRegressionDetectionAlgorithm;
    }

    public void setRidgeRegressionDetectionAlgorithm(RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm) {
        this.ridgeRegressionDetectionAlgorithm = ridgeRegressionDetectionAlgorithm;
    }


    public void printResults() {
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

        super.printResults();

        System.out.println("");
        System.out.println("");
    }


}
