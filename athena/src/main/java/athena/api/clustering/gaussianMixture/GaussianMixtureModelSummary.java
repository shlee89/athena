package athena.api.clustering.gaussianMixture;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.clustering.ClusterModelSummary;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class GaussianMixtureModelSummary extends ClusterModelSummary {

    GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm;
    GaussianMixtureModel gaussianMixtureModel = null;

    public GaussianMixtureModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        super(sc, indexing, marking);
    }

    public GaussianMixtureModelSummary(SparkContext sc, Indexing indexing) {
        super(sc, indexing);
    }

    public GaussianMixtureModelSummary(SparkContext sc) {
        super(sc);
    }

    public GaussianMixtureModel getGaussianMixtureModel() {
        return gaussianMixtureModel;
    }

    public void setGaussianMixtureModel(GaussianMixtureModel gaussianMixtureModel) {
        this.gaussianMixtureModel = gaussianMixtureModel;
    }

    public GaussianMixtureDetectionAlgorithm getGaussianMixtureDetectionAlgorithm() {
        return gaussianMixtureDetectionAlgorithm;
    }

    public void setGaussianMixtureDetectionAlgorithm(GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm) {
        this.gaussianMixtureDetectionAlgorithm = gaussianMixtureDetectionAlgorithm;
    }

    public void printSummary() {
        super.printSummary();
        System.out.println("------------------Cluster (Gaussian Mixture Model)----------------");

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

        System.out.println("------------------Cluster (Gaussian Mixture Model parameters)----------------");
        for(int j=0; j<gaussianMixtureModel.k(); j++) {
            System.out.printf("weight=%f\nmu=%s\nsigma=\n%s\n",
                    gaussianMixtureModel.weights()[j], gaussianMixtureModel.gaussians()[j].mu(), gaussianMixtureModel.gaussians()[j].sigma());
        }
    }
}
