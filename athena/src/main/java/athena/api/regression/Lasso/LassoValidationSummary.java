package athena.api.regression.Lasso;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import athena.api.regression.RegressionValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class LassoValidationSummary extends RegressionValidationSummary {

    LassoDetectionAlgorithm lassoDetectionAlgorithm;

    public LassoDetectionAlgorithm getLassoDetectionAlgorithm() {
        return lassoDetectionAlgorithm;
    }

    public void setLassoDetectionAlgorithm(LassoDetectionAlgorithm lassoDetectionAlgorithm) {
        this.lassoDetectionAlgorithm = lassoDetectionAlgorithm;
    }


    public void printResults() {
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


        super.printResults();

        System.out.println("");
        System.out.println("");
    }


}
