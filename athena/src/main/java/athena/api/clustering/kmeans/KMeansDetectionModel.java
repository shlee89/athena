package athena.api.clustering.kmeans;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionModel;
import athena.api.DetectionStrategy;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.ModelResult;
import athena.api.Summary;
import athena.api.clustering.ClusterModelSummary;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.onosproject.athena.database.FeatureConstraint;

import java.io.Serializable;

/**
 * The detection model for K-Menas clustering algorithm. It contains actual detection model with Spark framework.
 * Created by seunghyeon on 4/7/16.
 */
public class KMeansDetectionModel implements DetectionModel, Serializable {


    private static final long serialVersionUID = 6153228040759916433L;

    KMeansModel kMeansModel;
    FeatureConstraint featureConstraint;
    AthenaMLFeatureConfiguration athenaMLFeatureConfiguration;
    public Marking marking;
    public Indexing indexing;
    ClusterModelSummary clusterModelSummary;
    KMeansDetectionAlgorithm kMeansDetectionAlgorithm;

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


    public KMeansDetectionModel() {
    }

    public void setkMeansModel(KMeansModel kMeansModel) {
        this.kMeansModel = kMeansModel;
    }

    public void setClusterModelSummary(ClusterModelSummary clusterModelSummary) {
        this.clusterModelSummary = clusterModelSummary;
    }


    public void setkMeansDetectionAlgorithm(KMeansDetectionAlgorithm kMeansDetectionAlgorithm) {
        this.kMeansDetectionAlgorithm = kMeansDetectionAlgorithm;
    }

    public KMeansModel getkMeansModel() {
        return kMeansModel;
    }

    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLUSTERING;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.KMEANS;
    }

    @Override
    public Summary getSummary() {
        return clusterModelSummary;
    }

    @Override
    public Object getDetectionModel() {
        return kMeansModel;
    }

    @Override
    public DetectionAlgorithm getDetectionAlgorithm() {
        return kMeansDetectionAlgorithm;
    }
}
