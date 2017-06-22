package athena.api.classification.DecisionTree;

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
import org.onosproject.athena.database.FeatureConstraint;

/**
 * The detection model for K-Menas clustering algorithm. It contains actual detection model with Spark framework.
 * Created by seunghyeon on 4/7/16.
 */
public class DecisionTreeDetectionModel implements DetectionModel {

    private static final long serialVersionUID = 6123228040759916473L;

    DecisionTreeModel decisionTreeModel;
    FeatureConstraint featureConstraint;
    AthenaMLFeatureConfiguration athenaMLFeatureConfiguration;
    public Marking marking;
    public Indexing indexing;
    ClassificationModelSummary classificationModelSummary;
    DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm;

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



    public DecisionTreeDetectionModel() {
    }

    public void setDecisionTreeModel(DecisionTreeModel decisionTreeModel) {
        this.decisionTreeModel = decisionTreeModel;
    }


    public void setClassificationModelSummary(ClassificationModelSummary classificationModelSummary) {
        this.classificationModelSummary = classificationModelSummary;
    }



    public void setDecisionTreeDetectionAlgorithm(DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm) {
        this.decisionTreeDetectionAlgorithm = decisionTreeDetectionAlgorithm;
    }



    @Override
    public DetectionAlgorithmType getDetectionAlgorithmType() {
        return DetectionAlgorithmType.CLASSIFICATION;
    }

    @Override
    public DetectionStrategy getDetectionStrategy() {
        return DetectionStrategy.DT;
    }

    @Override
    public Summary getSummary() {
        return classificationModelSummary;
    }

    @Override
    public Object getDetectionModel() {
        return decisionTreeModel;
    }

    @Override
    public DetectionAlgorithm getDetectionAlgorithm() {
        return decisionTreeDetectionAlgorithm;
    }
}
