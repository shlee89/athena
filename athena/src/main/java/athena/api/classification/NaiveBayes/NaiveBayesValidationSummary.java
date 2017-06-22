package athena.api.classification.NaiveBayes;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class NaiveBayesValidationSummary extends ClassificationValidationSummary {

    NaiveBayesDetectionAlgorithm naiveBayesDetectionAlgorithm;

    public NaiveBayesValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public NaiveBayesValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public NaiveBayesValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public NaiveBayesDetectionAlgorithm getNaiveBayesDetectionAlgorithm() {
        return naiveBayesDetectionAlgorithm;
    }

    public void setNaiveBayesDetectionAlgorithm(NaiveBayesDetectionAlgorithm naiveBayesDetectionAlgorithm) {
        this.naiveBayesDetectionAlgorithm = naiveBayesDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();

        System.out.println("------------------Classification (NaiveBayes)----------------");
        System.out.println("Lambda = " + naiveBayesDetectionAlgorithm.getLambda());
        System.out.println("numClasses = " + naiveBayesDetectionAlgorithm.getNumClasses());


        System.out.println("");
        System.out.println("");
    }


}
