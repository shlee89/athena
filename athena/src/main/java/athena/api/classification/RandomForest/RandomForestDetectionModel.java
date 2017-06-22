package athena.api.classification.RandomForest;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.DetectionAlgorithm;
import athena.api.DetectionAlgorithmType;
import athena.api.DetectionModel;
import athena.api.DetectionStrategy;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.Summary;
import athena.api.classification.ClassificationModelSummary;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.onosproject.athena.database.FeatureConstraint;

/**
 * The detection model for K-Menas clustering algorithm. It contains actual detection model with Spark framework.
 * Created by seunghyeon on 4/7/16.
 */
public class RandomForestDetectionModel implements DetectionModel {

    private static final long serialVersionUID = 6153228040729916473L;
    RandomForestModel randomForestModel;
    FeatureConstraint featureConstraint;
    AthenaMLFeatureConfiguration athenaMLFeatureConfiguration;
    public Marking marking;
    public Indexing indexing;
    ClassificationModelSummary classificationModelSummary;
    RandomForestDetectionAlgorithm randomForestDetectionAlgorithm;

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



    public RandomForestDetectionModel() {
    }

    public void setRandomForestModel(RandomForestModel randomForestModel) {
        this.randomForestModel = randomForestModel;
    }


    public void setClassificationModelSummary(ClassificationModelSummary classificationModelSummary) {
        this.classificationModelSummary = classificationModelSummary;
    }



    public void setRandomForestDetectionAlgorithm(RandomForestDetectionAlgorithm randomForestDetectionAlgorithm) {
        this.randomForestDetectionAlgorithm = randomForestDetectionAlgorithm;
    }



    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLASSIFICATION;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.RF;
    }

    @Override
    public Summary getSummary() {
        return classificationModelSummary;
    }

    @Override
    public Object getDetectionModel() {
        return randomForestModel;
    }

    @Override
    public DetectionAlgorithm getDetectionAlgorithm() {
        return randomForestDetectionAlgorithm;
    }
}
