package athena.api.classification.SVM;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class SVMValidationSummary extends ClassificationValidationSummary {

    SVMDetectionAlgorithm svmDetectionAlgorithm;

    public SVMValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public SVMValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public SVMValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public SVMDetectionAlgorithm getSvmDetectionAlgorithm() {
        return svmDetectionAlgorithm;
    }

    public void setSvmDetectionAlgorithm(SVMDetectionAlgorithm svmDetectionAlgorithm) {
        this.svmDetectionAlgorithm = svmDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();

        System.out.println("------------------Classification (SVM-binary)----------------");

        System.out.println("numIterations = " + svmDetectionAlgorithm.getNumIterations());
        if (svmDetectionAlgorithm.getStepSize() != -1) {
            System.out.println("stepSize = " + svmDetectionAlgorithm.getStepSize());

        }
        if (svmDetectionAlgorithm.getRegParam() != -1) {

            System.out.println("regParam = " + svmDetectionAlgorithm.getRegParam());
        }
        if (svmDetectionAlgorithm.getMiniBatchFraction() != -1) {
            System.out.println("miniBatchFraction = " + svmDetectionAlgorithm.getMiniBatchFraction());
        }
        System.out.println("");
        System.out.println("");
    }


}
