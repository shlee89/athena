package athena.api.classification.NaiveBayes;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class NaiveBayesModelSummary extends ClassificationModelSummary {

    NaiveBayesDetectionAlgorithm naiveBayesDetectionAlgorithm;


    public NaiveBayesModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public NaiveBayesModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public NaiveBayesModelSummary(SparkContext sc) {
        super(sc);
    }

    public NaiveBayesDetectionAlgorithm getNaiveBayesDetectionAlgorithm() {
        return naiveBayesDetectionAlgorithm;
    }

    public void setNaiveBayesDetectionAlgorithm(NaiveBayesDetectionAlgorithm naiveBayesDetectionAlgorithm) {
        this.naiveBayesDetectionAlgorithm = naiveBayesDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Classification (NaiveBayes)----------------");
        System.out.println("Lambda = " + naiveBayesDetectionAlgorithm.getLambda());
        System.out.println("numClasses = " + naiveBayesDetectionAlgorithm.getNumClasses());

        System.out.println("");
    }
}
