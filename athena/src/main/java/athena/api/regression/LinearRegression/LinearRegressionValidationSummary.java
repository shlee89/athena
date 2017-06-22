package athena.api.regression.LinearRegression;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import athena.api.regression.RegressionValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class LinearRegressionValidationSummary extends RegressionValidationSummary {

    LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm;

    public LinearRegressionDetectionAlgorithm getLinearRegressionDetectionAlgorithm() {
        return linearRegressionDetectionAlgorithm;
    }

    public void setLinearRegressionDetectionAlgorithm(LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm) {
        this.linearRegressionDetectionAlgorithm = linearRegressionDetectionAlgorithm;
    }


    public void printResults() {
        System.out.println("------------------Classification (Linear Regression)----------------");

        System.out.println("numClasses = " + linearRegressionDetectionAlgorithm.getNumClasses());

        System.out.println("NumIterations = " + linearRegressionDetectionAlgorithm.getNumIterations());

        if (linearRegressionDetectionAlgorithm.getStepSize() != -1) {
            System.out.println("StepSize = " + linearRegressionDetectionAlgorithm.getStepSize());
        }

        if (linearRegressionDetectionAlgorithm.getMiniBatchFraction() != -1) {
            System.out.println("MiniBatchFraction = " + linearRegressionDetectionAlgorithm.getMiniBatchFraction());
        }

        super.printResults();

        System.out.println("");
        System.out.println("");
    }


}
