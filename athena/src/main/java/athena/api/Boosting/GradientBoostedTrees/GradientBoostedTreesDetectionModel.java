package athena.api.Boosting.GradientBoostedTrees;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionModel;
import athena.api.DetectionStrategy;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.Summary;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.onosproject.athena.database.FeatureConstraint;

/**
 * The detection model for K-Menas clustering algorithm. It contains actual detection model with Spark framework.
 * Created by seunghyeon on 4/7/16.
 */
public class GradientBoostedTreesDetectionModel implements DetectionModel {

    private static final long serialVersionUID = 6153228040759956473L;
    GradientBoostedTreesModel gradientBoostedTreesModel;
    FeatureConstraint featureConstraint;
    AthenaMLFeatureConfiguration athenaMLFeatureConfiguration;
    public Marking marking;
    public Indexing indexing;
    ClassificationModelSummary classificationModelSummary;
    GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm;

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



    public GradientBoostedTreesDetectionModel() {
    }

    public void setGradientBoostedTreestModel(GradientBoostedTreesModel gradientBoostedTreesModel) {
        this.gradientBoostedTreesModel = gradientBoostedTreesModel;
    }


    public void setClassificationModelSummary(ClassificationModelSummary classificationModelSummary) {
        this.classificationModelSummary = classificationModelSummary;
    }



    public void setGradientBoostedTreesDetectionAlgorithm(GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm) {
        this.gradientBoostedTreesDetectionAlgorithm = gradientBoostedTreesDetectionAlgorithm;
    }



    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.BOOSTING;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.GBT;
    }

    @Override
    public Summary getSummary() {
        return classificationModelSummary;
    }

    @Override
    public Object getDetectionModel() {
        return gradientBoostedTreesModel;
    }

    @Override
    public DetectionAlgorithm getDetectionAlgorithm() {
        return gradientBoostedTreesDetectionAlgorithm;
    }
}
