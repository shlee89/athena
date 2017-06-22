package athena.api.classification.LogisticRegression;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class LogisticRegressionValidationSummary extends ClassificationValidationSummary {

    LogisticRegressionDetectionAlgorithm logisticRegressionDetectionAlgorithm;

    public LogisticRegressionValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public LogisticRegressionValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public LogisticRegressionValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public LogisticRegressionDetectionAlgorithm getLogisticRegressionDetectionAlgorithm() {
        return logisticRegressionDetectionAlgorithm;
    }

    public void setLogisticRegressionDetectionAlgorithm(LogisticRegressionDetectionAlgorithm logisticRegressionDetectionAlgorithm) {
        this.logisticRegressionDetectionAlgorithm = logisticRegressionDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();

        System.out.println("------------------Classification (Logistic Regression)----------------");

        if (logisticRegressionDetectionAlgorithm.getNumClasses() != -1) {
            System.out.println("numClasses = " + logisticRegressionDetectionAlgorithm.getNumClasses());
        }


        System.out.println("");
        System.out.println("");
    }


}
