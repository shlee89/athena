package athena.api.clustering.kmeans;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.clustering.ClusterModelSummary;
import org.apache.spark.SparkContext;

import java.io.Serializable;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class KmeansModelSummary extends ClusterModelSummary implements Serializable {

    KMeansDetectionAlgorithm kMeansDetectionAlgorithm;
    double WSSSE = 0;

    public KmeansModelSummary() {
    }

    public void setWSSSE(double WSSSE) {
        this.WSSSE = WSSSE;
    }

    public KmeansModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public KmeansModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public KmeansModelSummary(SparkContext sc) {
        super(sc);
    }

    public KMeansDetectionAlgorithm getkMeansDetectionAlgorithm() {
        return kMeansDetectionAlgorithm;
    }

    public void setkMeansDetectionAlgorithm(KMeansDetectionAlgorithm kMeansDetectionAlgorithm) {
        this.kMeansDetectionAlgorithm = kMeansDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
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

        System.out.println("WSSSE = " + WSSSE);

        System.out.println("");
    }
}
