package athena.api.clustering.gaussianMixture;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionModel;
import athena.api.DetectionStrategy;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.Summary;
import athena.api.clustering.ClusterModelSummary;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.onosproject.athena.database.FeatureConstraint;

/**
 * The detection model for K-Menas clustering algorithm. It contains actual detection model with Spark framework.
 * Created by seunghyeon on 4/7/16.
 */
public class GaussianMixtureDetectionModel implements DetectionModel {

    private static final long serialVersionUID = 6153128040759916473L;
//    KMeansModel kMeansModel;
    GaussianMixtureModel gaussianMixtureModel;
    FeatureConstraint featureConstraint;
    AthenaMLFeatureConfiguration athenaMLFeatureConfiguration;
    public Marking marking;
    public Indexing indexing;
    ClusterModelSummary clusterModelSummary;
    GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm;

    public FeatureConstraint getFeatureConstraint() {
        return featureConstraint;
    }

    public void setFeatureConstraint(FeatureConstraint featureConstraint) {
        this.featureConstraint = featureConstraint;
    }

    @Override
    public AthenaMLFeatureConfiguration getAthenaMLFeatureConfiguration() {
        return athenaMLFeatureConfiguration;
    }

    public void setAthenaMLFeatureConfiguration(AthenaMLFeatureConfiguration athenaMLFeatureConfiguration) {
        this.athenaMLFeatureConfiguration = athenaMLFeatureConfiguration;
    }

    public Marking getMarking() {
        return marking;
    }

    public void setMarking(Marking marking) {
        this.marking = marking;
    }

    public Indexing getIndexing() {
        return indexing;
    }

    public void setIndexing(Indexing indexing) {
        this.indexing = indexing;
    }

    public GaussianMixtureDetectionModel(GaussianMixtureModel gaussianMixtureModel,
                                         GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm,
                                         ClusterModelSummary clusterModelSummary) {
        this.gaussianMixtureModel = gaussianMixtureModel;
        this.clusterModelSummary = clusterModelSummary;
        this.gaussianMixtureDetectionAlgorithm = gaussianMixtureDetectionAlgorithm;
    }

    public GaussianMixtureDetectionModel(GaussianMixtureModel gaussianMixtureModel,
                                         GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm) {
        this.gaussianMixtureModel = gaussianMixtureModel;
        this.gaussianMixtureDetectionAlgorithm = gaussianMixtureDetectionAlgorithm;
    }

    public GaussianMixtureDetectionModel(GaussianMixtureModel gaussianMixtureModel) {
        this.gaussianMixtureModel = gaussianMixtureModel;
    }

    public GaussianMixtureDetectionModel() {
    }

    public void setkGaussianMixtureModel(GaussianMixtureModel gaussianMixtureModel) {
        this.gaussianMixtureModel = gaussianMixtureModel;
    }


    public void setClusterModelSummary(ClusterModelSummary clusterModelSummary) {
        this.clusterModelSummary = clusterModelSummary;
    }



    public void setGaussianMixtureDetectionAlgorithm(GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm) {
        this.gaussianMixtureDetectionAlgorithm = gaussianMixtureDetectionAlgorithm;
    }

    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLUSTERING;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.GMM;
    }

    @Override
    public Summary getSummary() {
        return clusterModelSummary;
    }

    @Override
    public Object getDetectionModel() {
        return gaussianMixtureModel;
    }

    @Override
    public DetectionAlgorithm getDetectionAlgorithm() {
        return gaussianMixtureDetectionAlgorithm;
    }
}
