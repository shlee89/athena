package athena.api.classification.LogisticRegression;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class LogisticRegressionModelSummary extends ClassificationModelSummary {

    LogisticRegressionDetectionAlgorithm logisticRegressionDetectionAlgorithm;
    LogisticRegressionModel model;


    public LogisticRegressionModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public LogisticRegressionModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public LogisticRegressionModelSummary(SparkContext sc) {
        super(sc);
    }

    public LogisticRegressionDetectionAlgorithm getLogisticRegressionDetectionAlgorithm() {
        return logisticRegressionDetectionAlgorithm;
    }

    public void setLogisticRegressionDetectionAlgorithm(LogisticRegressionDetectionAlgorithm logisticRegressionDetectionAlgorithm) {
        this.logisticRegressionDetectionAlgorithm = logisticRegressionDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Classification (Logistic Regression)----------------");

        if (logisticRegressionDetectionAlgorithm.getNumClasses() != -1) {
            System.out.println("numClasses = " + logisticRegressionDetectionAlgorithm.getNumClasses());
        }
    }
}
