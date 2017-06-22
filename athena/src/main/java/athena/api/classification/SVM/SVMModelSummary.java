package athena.api.classification.SVM;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class SVMModelSummary extends ClassificationModelSummary {

    SVMDetectionAlgorithm SVMDetectionAlgorithm;
    SVMModel model;


    public SVMModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public SVMModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public SVMModelSummary(SparkContext sc) {
        super(sc);
    }

    public SVMDetectionAlgorithm getSVMDetectionAlgorithm() {
        return SVMDetectionAlgorithm;
    }

    public void setSVMDetectionAlgorithm(SVMDetectionAlgorithm SVMDetectionAlgorithm) {
        this.SVMDetectionAlgorithm = SVMDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Classification (SVM-binary)----------------");

        System.out.println("numIterations = " + SVMDetectionAlgorithm.getNumIterations());
        if (SVMDetectionAlgorithm.getStepSize() != -1) {
            System.out.println("stepSize = " + SVMDetectionAlgorithm.getStepSize());

        }
        if (SVMDetectionAlgorithm.getRegParam() != -1) {

            System.out.println("regParam = " + SVMDetectionAlgorithm.getRegParam());
        }
        if (SVMDetectionAlgorithm.getMiniBatchFraction() != -1) {
            System.out.println("miniBatchFraction = " + SVMDetectionAlgorithm.getMiniBatchFraction());
        }
        System.out.println("");
    }
}
