package athena.api.clustering.kmeans;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.clustering.ClusterValidationSummary;
import org.apache.spark.SparkContext;

import java.io.Serializable;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class KmeansValidationSummary extends ClusterValidationSummary implements Serializable {

    KMeansDetectionAlgorithm kMeansDetectionAlgorithm;

    public KmeansValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public KmeansValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public KmeansValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public KMeansDetectionAlgorithm getkMeansDetectionAlgorithm() {
        return kMeansDetectionAlgorithm;
    }

    public void setkMeansDetectionAlgorithm(KMeansDetectionAlgorithm kMeansDetectionAlgorithm) {
        this.kMeansDetectionAlgorithm = kMeansDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();
        System.out.println("------------------Cluster (K-means)----------------");

        if (kMeansDetectionAlgorithm.getK() != -1) {
            System.out.println("k = " + kMeansDetectionAlgorithm.getK());
        }else {
            System.out.println("k = " + "2");
        }

        if (kMeansDetectionAlgorithm.getMaxIterations() != -1) {
            System.out.println("MaxIterations = " + kMeansDetectionAlgorithm.getMaxIterations());
        }else {
            System.out.println("MaxIterations = " +"20");
        }

        if (kMeansDetectionAlgorithm.getRuns() != -1) {
            System.out.println("Runs = " + kMeansDetectionAlgorithm.getRuns());
        }else {
            System.out.println("Runs = " +"1");
        }

        if (kMeansDetectionAlgorithm.getInitializationMode() != null) {
            System.out.println("InitializationMode = " + kMeansDetectionAlgorithm.getInitializationMode());
        }else {
            System.out.println("InitializationMode = " +"k-means||");
        }

        if (kMeansDetectionAlgorithm.getSeed() != -1) {
            System.out.println("Seed = " + kMeansDetectionAlgorithm.getSeed());
        }else {
            System.out.println("Seed = " + "random");
        }

        if (kMeansDetectionAlgorithm.getEpsilon() != -1) {
            System.out.println("Epsilon = " + kMeansDetectionAlgorithm.getEpsilon());
        }else {
            System.out.println("Epsilon = " + "1e-4");
        }
        System.out.println("");
    }


}
