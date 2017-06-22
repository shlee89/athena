package athena.util;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesDetectionModel;
import athena.api.DetectionModel;
import athena.api.classification.DecisionTree.DecisionTreeDetectionModel;
import athena.api.classification.LogisticRegression.LogisticRegressionDetectionModel;
import athena.api.classification.NaiveBayes.NaiveBayesDetectionModel;
import athena.api.classification.RandomForest.RandomForestDetectionModel;
import athena.api.classification.SVM.SVMDetectionModel;
import athena.api.clustering.gaussianMixture.GaussianMixtureDetectionModel;
import athena.api.clustering.kmeans.KMeansDetectionModel;
import athena.api.onlineMLEventListener;
import athena.api.regression.Lasso.LassoDetectionModel;
import athena.api.regression.LinearRegression.LinearRegressionDetectionModel;
import athena.api.regression.RidgeRegression.RidgeRegressionDetectionModel;
import athena.northbound.impl.EventDeliveryManagerImpl;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.AthenaFeatureField;


import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.regression.LassoModel;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.RidgeRegressionModel;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.core.ApplicationId;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This will be used in an online event delivery handler to test incoming features
 */
public class OnlineFeatureHandler {

    FeatureConstraint featureConstraint;

    static short QUERY_IDENTIFIER = 2;
    KMeansDetectionModel kMeansDetectionModel = null;

    DetectionModel detectionModel;
    KMeansModel kMeansModel = null;
    GaussianMixtureModel gaussianMixtureModel = null;
    DecisionTreeModel decisionTreeModel = null;
    NaiveBayesModel naiveBayesModel = null;
    RandomForestModel randomForestModel = null;
    GradientBoostedTreesModel gradientBoostedTreesModel = null;
    SVMModel svmModel = null;
    LogisticRegressionModel logisticRegressionModel = null;
    LinearRegressionModel linearRegressionModel = null;
    LassoModel lassoModel = null;
    RidgeRegressionModel ridgeRegressionModel = null;


    List<AthenaFeatureField> listOfTargetFeatures = null;
    Map<AthenaFeatureField, Integer> weight = null;
    int numberOfTargetValue = 0;
    Normalizer normalizer = new Normalizer();
    boolean isAbsolute = false;
    boolean isNormalization = false;

    EventDeliveryManagerImpl eventDeliveryManager;
    onlineMLEventListener onlineMLEventListener;

    public OnlineFeatureHandler(FeatureConstraint featureConstraint,
                                DetectionModel detectionModel,
                                onlineMLEventListener onlineMLEventListener,
                                ControllerConnector controllerConnector) {
        this.featureConstraint = featureConstraint;
        this.detectionModel = detectionModel;
        setAthenaMLFeatureConfiguration(detectionModel.getAthenaMLFeatureConfiguration());

        if (detectionModel instanceof KMeansDetectionModel) {
            this.kMeansModel = (KMeansModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof GaussianMixtureDetectionModel) {
            this.gaussianMixtureModel = (GaussianMixtureModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof DecisionTreeDetectionModel) {
            this.decisionTreeModel = (DecisionTreeModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof NaiveBayesDetectionModel) {
            this.naiveBayesModel = (NaiveBayesModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof RandomForestDetectionModel) {
            this.randomForestModel = (RandomForestModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof GradientBoostedTreesDetectionModel) {
            this.gradientBoostedTreesModel = (GradientBoostedTreesModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof SVMDetectionModel) {
            this.svmModel = (SVMModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof LogisticRegressionDetectionModel) {
            this.logisticRegressionModel = (LogisticRegressionModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof LinearRegressionDetectionModel) {
            this.linearRegressionModel = (LinearRegressionModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof LassoDetectionModel) {
            this.lassoModel = (LassoModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof RidgeRegressionDetectionModel) {
            this.ridgeRegressionModel = (RidgeRegressionModel) detectionModel.getDetectionModel();
        } else {
            //not supported ML model
            System.out.println("Not supported model");
        }

        this.eventDeliveryManager = new EventDeliveryManagerImpl(controllerConnector, new InternalAthenaFeatureEventListener());
        this.eventDeliveryManager.registerOnlineAthenaFeature(null, new QueryIdentifier(QUERY_IDENTIFIER), featureConstraint);
        this.onlineMLEventListener = onlineMLEventListener;
        System.out.println("Install handler!");
    }

    public OnlineFeatureHandler(FeatureConstraint featureConstraint,
                                DetectionModel detectionModel,
                                onlineMLEventListener onlineMLEventListener) {
        this.detectionModel = detectionModel;
        setAthenaMLFeatureConfiguration(detectionModel.getAthenaMLFeatureConfiguration());

        if (detectionModel instanceof KMeansDetectionModel) {
            this.kMeansModel = (KMeansModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof GaussianMixtureDetectionModel) {
            this.gaussianMixtureModel = (GaussianMixtureModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof DecisionTreeDetectionModel) {
            this.decisionTreeModel = (DecisionTreeModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof NaiveBayesDetectionModel) {
            this.naiveBayesModel = (NaiveBayesModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof RandomForestDetectionModel) {
            this.randomForestModel = (RandomForestModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof GradientBoostedTreesDetectionModel) {
            this.gradientBoostedTreesModel = (GradientBoostedTreesModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof SVMDetectionModel) {
            this.svmModel = (SVMModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof LogisticRegressionDetectionModel) {
            this.logisticRegressionModel = (LogisticRegressionModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof LinearRegressionDetectionModel) {
            this.linearRegressionModel = (LinearRegressionModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof LassoDetectionModel) {
            this.lassoModel = (LassoModel) detectionModel.getDetectionModel();
        } else if (detectionModel instanceof RidgeRegressionDetectionModel) {
            this.ridgeRegressionModel = (RidgeRegressionModel) detectionModel.getDetectionModel();
        } else {
            //not supported ML model
            System.out.println("Not supported model");
        }
        this.onlineMLEventListener = onlineMLEventListener;

        this.eventDeliveryManager = new EventDeliveryManagerImpl(new InternalAthenaFeatureEventListener());



    }

    public void stop() {
        eventDeliveryManager.unRegisterOnlineAthenaFeature(null, new QueryIdentifier(QUERY_IDENTIFIER));
    }

    public void start() {
        this.eventDeliveryManager.registerOnlineAthenaFeature(null, new QueryIdentifier(QUERY_IDENTIFIER), featureConstraint);
    }

    public void setAthenaMLFeatureConfiguration(AthenaMLFeatureConfiguration athenaMLFeatureConfiguration) {
        this.weight = athenaMLFeatureConfiguration.getWeight();
        this.listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        this.numberOfTargetValue = this.listOfTargetFeatures.size();
        this.isAbsolute = athenaMLFeatureConfiguration.isAbsolute();
        this.isNormalization = athenaMLFeatureConfiguration.isNormalization();
    }

    Vector postProcessing(HashMap<String, Object> value) {
        org.apache.spark.mllib.linalg.Vector normedForVal;

        double[] values = new double[numberOfTargetValue];
        for (int j = 0; j < numberOfTargetValue; j++) {
            values[j] = 0;
            HashMap<String, Object> features = (HashMap<String, Object>) value.get(AthenaFeatureField.FEATURE);

            if (features.containsKey(listOfTargetFeatures.get(j).getValue())) {
                Object obj = features.get(listOfTargetFeatures.get(j).getValue());
                if (obj instanceof Long) {
                    values[j] = (Long) obj;
                } else if (obj instanceof Double) {
                    values[j] = (Double) obj;
                } else if (obj instanceof Boolean) {
                    values[j] = (Boolean) obj ? 1 : 0;
                } else {
                    return null;
                }

                //check weight
                if (weight.containsKey(listOfTargetFeatures.get(j))) {
                    values[j] *= weight.get(listOfTargetFeatures.get(j));
                }
                //check absolute
                if (isAbsolute) {
                    values[j] = Math.abs(values[j]);
                }
            }
        }


        if (isNormalization) {
            normedForVal = normalizer.transform(Vectors.dense(values));
        } else {
            normedForVal = Vectors.dense(values);
        }

        return normedForVal;
    }

    public double detectionWithPostProcessing(HashMap<String, Object> value) {
        return detection(postProcessing(value));
    }

    public double detection(Vector value) {

        if (detectionModel instanceof KMeansDetectionModel) {
//            int cluster = kMeansModel.predict(value);
//            System.out.println(value.toString() + "!!!!!!!!!!!!!!!!!" + cluster);
            return kMeansModel.predict(value);

        } else if (detectionModel instanceof GaussianMixtureDetectionModel) {
            return gaussianMixtureModel.predict(value);

        } else if (detectionModel instanceof DecisionTreeDetectionModel) {
            return decisionTreeModel.predict(value);

        } else if (detectionModel instanceof NaiveBayesDetectionModel) {
            return naiveBayesModel.predict(value);

        } else if (detectionModel instanceof RandomForestDetectionModel) {
            return randomForestModel.predict(value);

        } else if (detectionModel instanceof GradientBoostedTreesDetectionModel) {
            return gradientBoostedTreesModel.predict(value);

        } else if (detectionModel instanceof SVMDetectionModel) {
            return svmModel.predict(value);

        } else if (detectionModel instanceof LogisticRegressionDetectionModel) {
            return logisticRegressionModel.predict(value);

        } else if (detectionModel instanceof LinearRegressionDetectionModel) {
            return linearRegressionModel.predict(value);

        } else if (detectionModel instanceof LassoDetectionModel) {
            this.lassoModel = (LassoModel) detectionModel.getDetectionModel();

        } else if (detectionModel instanceof RidgeRegressionDetectionModel) {
            return ridgeRegressionModel.predict(value);

        } else {
            //not supported ML model
            System.out.println("Not supported model");
            return 0;
        }

        return 0;
    }

    public class InternalAthenaFeatureEventListener implements AthenaFeatureEventListener {

        @Override
        public void getRequestedFeatures(ApplicationId applicationId, AthenaFeatures athenaFeatures) {

        }

        @Override
        public void getFeatureEvent(ApplicationId applicationId, QueryIdentifier id, HashMap<String, Object> event) {

//            System.out.println("[In getFeatureEvent]" + event.toString());
            if (id.getId() == QUERY_IDENTIFIER) {
                onlineMLEventListener.getValidationResultOnlineResult(event, detectionWithPostProcessing(event));
            }
        }
    }

}
