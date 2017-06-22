package athena.api.clustering.gaussianMixture;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.clustering.ClusterValidationSummary;
import org.apache.spark.SparkContext;

/**
 * Created by seunghyeon on 5/1/16.
 */
public class GaussianMixtureValidationSummary extends ClusterValidationSummary {

    GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm;



    public GaussianMixtureValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        super(sc, numberOfCluster, indexing, marking);
    }

    public GaussianMixtureValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        super(sc, numberOfCluster, indexing);
    }

    public GaussianMixtureValidationSummary(SparkContext sc, int numberOfCluster) {
        super(sc, numberOfCluster);
    }

    public GaussianMixtureDetectionAlgorithm getGaussianMixtureDetectionAlgorithm() {
        return gaussianMixtureDetectionAlgorithm;
    }

    public void setGaussianMixtureDetectionAlgorithm(GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm) {
        this.gaussianMixtureDetectionAlgorithm = gaussianMixtureDetectionAlgorithm;
    }


    public void printResults() {
        super.printResults();
        System.out.println("------------------Cluster (Gaussian Mixture Validation)----------------");

        if (gaussianMixtureDetectionAlgorithm.getK() != -1) {
            System.out.println("k = " + gaussianMixtureDetectionAlgorithm.getK());
        }else {
            System.out.println("k = " + "2");
        }

        if (gaussianMixtureDetectionAlgorithm.getConvergenceTol() != -1) {
            System.out.println("convergenceTol = " + gaussianMixtureDetectionAlgorithm.getConvergenceTol());
        }else {
            System.out.println("convergenceTol = " +"0.01");
        }

        if (gaussianMixtureDetectionAlgorithm.getMaxIterations() != -1) {
            System.out.println("maxIterations = " + gaussianMixtureDetectionAlgorithm.getMaxIterations());
        }else {
            System.out.println("maxIterations = " +"100");
        }


        if (gaussianMixtureDetectionAlgorithm.getSeed() != -1) {
            System.out.println("Seed = " + gaussianMixtureDetectionAlgorithm.getSeed());
        }else {
            System.out.println("Seed = " + "random");
        }

        if (gaussianMixtureDetectionAlgorithm.getInitializedModel() != null) {
            System.out.println("model existed");
        }else {
            System.out.println("model non-exisited");
        }
        System.out.println("");
    }


}
