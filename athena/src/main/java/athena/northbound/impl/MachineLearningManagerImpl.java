package athena.northbound.impl;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesModelSummary;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesValidationSummary;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesDetectionAlgorithm;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesDetectionModel;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesDistJob;
import athena.api.DetectionAlgorithm;
import athena.api.DetectionModel;
import athena.api.DetectionStrategy;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.ValidationSummary;
import athena.api.classification.DecisionTree.DecisionTreeModelSummary;
import athena.api.classification.DecisionTree.DecisionTreeValidationSummary;
import athena.api.classification.DecisionTree.DecisionTreeDetectionAlgorithm;
import athena.api.classification.DecisionTree.DecisionTreeDetectionModel;
import athena.api.classification.DecisionTree.DecisionTreeDistJob;
import athena.api.regression.Lasso.LassoDetectionAlgorithm;
import athena.api.regression.Lasso.LassoDetectionModel;
import athena.api.regression.Lasso.LassoDistJob;
import athena.api.regression.Lasso.LassoModelSummary;
import athena.api.regression.Lasso.LassoValidationSummary;
import athena.api.regression.LinearRegression.LinearRegressionDetectionAlgorithm;
import athena.api.regression.LinearRegression.LinearRegressionDetectionModel;
import athena.api.regression.LinearRegression.LinearRegressionDistJob;
import athena.api.regression.LinearRegression.LinearRegressionModelSummary;
import athena.api.regression.LinearRegression.LinearRegressionValidationSummary;
import athena.api.classification.LogisticRegression.LogisticRegressionDetectionAlgorithm;
import athena.api.classification.LogisticRegression.LogisticRegressionDetectionModel;
import athena.api.classification.LogisticRegression.LogisticRegressionDistJob;
import athena.api.classification.LogisticRegression.LogisticRegressionModelSummary;
import athena.api.classification.LogisticRegression.LogisticRegressionValidationSummary;
import athena.api.classification.NaiveBayes.NaiveBayesModelSummary;
import athena.api.classification.NaiveBayes.NaiveBayesValidationSummary;
import athena.api.classification.NaiveBayes.NaiveBayesDetectionAlgorithm;
import athena.api.classification.NaiveBayes.NaiveBayesDetectionModel;
import athena.api.classification.NaiveBayes.NaiveBayesDistJob;
import athena.api.classification.RandomForest.RandomForestModelSummary;
import athena.api.classification.RandomForest.RandomForestValidationSummary;
import athena.api.classification.RandomForest.RandomForestDetectionAlgorithm;
import athena.api.classification.RandomForest.RandomForestDetectionModel;
import athena.api.classification.RandomForest.RandomForestDistJob;
import athena.api.regression.RidgeRegression.RidgeRegressionDetectionAlgorithm;
import athena.api.regression.RidgeRegression.RidgeRegressionDetectionModel;
import athena.api.regression.RidgeRegression.RidgeRegressionDistJob;
import athena.api.regression.RidgeRegression.RidgeRegressionModelSummary;
import athena.api.regression.RidgeRegression.RidgeRegressionValidationSummary;
import athena.api.classification.SVM.SVMModelSummary;
import athena.api.classification.SVM.SVMValidationSummary;
import athena.api.classification.SVM.SVMDetectionAlgorithm;
import athena.api.classification.SVM.SVMDetectionModel;
import athena.api.classification.SVM.SVMDistJob;
import athena.api.clustering.gaussianMixture.GaussianMixtureModelSummary;
import athena.api.clustering.gaussianMixture.GaussianMixtureValidationSummary;
import athena.api.clustering.gaussianMixture.GaussianMixtureDetectionAlgorithm;
import athena.api.clustering.gaussianMixture.GaussianMixtureDetectionModel;
import athena.api.clustering.gaussianMixture.GaussianMixtureDistJob;
import athena.api.clustering.kmeans.KMeansDetectionAlgorithm;
import athena.api.clustering.kmeans.KMeansDetectionModel;
import athena.api.clustering.kmeans.KMeansDistJob;
import athena.api.clustering.kmeans.KmeansModelSummary;
import athena.api.clustering.kmeans.KmeansValidationSummary;
import athena.api.onlineMLEventListener;
import athena.northbound.MachineLearningManager;
import athena.util.ComputingClusterConnector;
import athena.util.ControllerConnector;
import athena.util.DatabaseConnector;
import athena.util.ModelSerialization;
import athena.util.OnlineFeatureHandler;
import com.mongodb.MongoClient;
import com.mongodb.hadoop.MongoInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.regression.LassoModel;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.RidgeRegressionModel;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.bson.BSONObject;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaField;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.TargetAthenaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.ne;

/**
 * Created by seunghyeon on 4/3/16.
 */
public class MachineLearningManagerImpl implements MachineLearningManager, Serializable {
    private final Logger log = LoggerFactory.getLogger(getClass());
    List<String> registeredClasses = new ArrayList<>();
    ComputingClusterConnector computingClusterConnector;
    Configuration mongodbConfig;

    String mainClass;
    String computingClusterMasterIP;
    String artifactName;
    DatabaseConnector databaseConnector;
    ModelSerialization modelSerialization;

    JavaSparkContext sc = null;

    OnlineFeatureHandler onlineFeatureHandler = null;

    public MachineLearningManagerImpl() {
        this.modelSerialization = new ModelSerialization();
        if (System.getenv("SP1").isEmpty()) {
            System.err.println("Please set environment variables for computing cluster");
            System.exit(1);
        }
        this.computingClusterMasterIP = System.getenv("SP1") + ":7077";
    }


    @Override
    public void setArtifactId(String artifactId) {
        this.artifactName = artifactId;
    }

    @Override
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public void setComputingClusterMasterIP(String computingClusterMasterIP) {
        this.computingClusterMasterIP = computingClusterMasterIP;
    }

    @Override
    public void setDatabaseConnector(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void registerOnlineValidation(FeatureConstraint featureConstraint,
                                         AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                         DetectionModel detectionModel,
                                         onlineMLEventListener listener,
                                         ControllerConnector controllerConnector) {
        if (controllerConnector == null){
            onlineFeatureHandler = new OnlineFeatureHandler(featureConstraint,
                    detectionModel,
                    listener);
        }else {
            onlineFeatureHandler = new OnlineFeatureHandler(featureConstraint,
                    detectionModel,
                    listener,
                    controllerConnector);
        }

        if (athenaMLFeatureConfiguration != null){
            onlineFeatureHandler.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        }
    }

    @Override
    public void unRegisterOnlineValidation(onlineMLEventListener listener) {
        onlineFeatureHandler.stop();
    }

    @Override
    public ValidationSummary validateAthenaFeatures(FeatureConstraint featureConstraint,
                                                    AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                    DetectionModel detectionModel,
                                                    Indexing indexing,
                                                    Marking marking) {
        ValidationSummary validationSummary;

        if (detectionModel instanceof KMeansDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateKMeansAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (KMeansDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof GaussianMixtureDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateGaussianMixtureAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (GaussianMixtureDetectionModel) detectionModel,
                    indexing,
                    marking);

            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof DecisionTreeDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateDecisionTreeAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (DecisionTreeDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof NaiveBayesDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateNaiveBayesAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (NaiveBayesDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof RandomForestDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateRandomForestAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (RandomForestDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof GradientBoostedTreesDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateGradientBoostedTreesAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (GradientBoostedTreesDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof SVMDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateSVMAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (SVMDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof LogisticRegressionDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateLogisticRegressionAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (LogisticRegressionDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof LinearRegressionDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateLinearRegressionAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (LinearRegressionDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof LassoDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateLassoAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (LassoDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else if (detectionModel instanceof RidgeRegressionDetectionModel) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            validationSummary = validateRidgeRegressionAthenaFeatures(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    (RidgeRegressionDetectionModel) detectionModel,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return validationSummary;
        } else {
            log.warn("Not supported DetectionStrategy -return null");
            return null;
        }

    }

    public LassoValidationSummary validateLassoAthenaFeatures(JavaSparkContext sc,
                                                              FeatureConstraint featureConstraint,
                                                              AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                              LassoDetectionModel lassoDetectionModel,
                                                              Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        LassoDetectionAlgorithm lassoDetectionAlgorithm =
                (LassoDetectionAlgorithm) lassoDetectionModel.getDetectionAlgorithm();

        LassoValidationSummary lassoValidationSummary = new LassoValidationSummary();
        lassoValidationSummary.setLassoDetectionAlgorithm(lassoDetectionAlgorithm);
        LassoDistJob lassoDistJob = new LassoDistJob();

        lassoDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                lassoDetectionModel,
                lassoValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;
        lassoValidationSummary.setValidationTime(time);

        return lassoValidationSummary;
    }

    public RidgeRegressionValidationSummary validateRidgeRegressionAthenaFeatures(JavaSparkContext sc,
                                                                                  FeatureConstraint featureConstraint,
                                                                                  AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                  RidgeRegressionDetectionModel ridgeRegressionDetectionModel,
                                                                                  Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm =
                (RidgeRegressionDetectionAlgorithm) ridgeRegressionDetectionModel.getDetectionAlgorithm();

        RidgeRegressionValidationSummary ridgeRegressionValidationSummary = new RidgeRegressionValidationSummary();
        ridgeRegressionValidationSummary.setRidgeRegressionDetectionAlgorithm(ridgeRegressionDetectionAlgorithm);
        RidgeRegressionDistJob ridgeRegressionDistJob = new RidgeRegressionDistJob();

        ridgeRegressionDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                ridgeRegressionDetectionModel,
                ridgeRegressionValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;
        ridgeRegressionValidationSummary.setValidationTime(time);
        return ridgeRegressionValidationSummary;
    }

    public LinearRegressionValidationSummary validateLinearRegressionAthenaFeatures(JavaSparkContext sc,
                                                                                    FeatureConstraint featureConstraint,
                                                                                    AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                    LinearRegressionDetectionModel linearRegressionDetectionModel,
                                                                                    Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm =
                (LinearRegressionDetectionAlgorithm) linearRegressionDetectionModel.getDetectionAlgorithm();

        LinearRegressionValidationSummary linearRegressionValidationSummary =
                new LinearRegressionValidationSummary();
        linearRegressionValidationSummary.setLinearRegressionDetectionAlgorithm(linearRegressionDetectionAlgorithm);

        LinearRegressionDistJob linearRegressionDistJob = new LinearRegressionDistJob();

        linearRegressionDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                linearRegressionDetectionModel,
                linearRegressionValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;
        linearRegressionValidationSummary.setValidationTime(time);


        return linearRegressionValidationSummary;
    }

    public LogisticRegressionValidationSummary validateLogisticRegressionAthenaFeatures(JavaSparkContext sc,
                                                                                        FeatureConstraint featureConstraint,
                                                                                        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                        LogisticRegressionDetectionModel logisticRegressionDetectionModel,
                                                                                        Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        LogisticRegressionDetectionAlgorithm logisticRegressionDetectionAlgorithm =
                (LogisticRegressionDetectionAlgorithm) logisticRegressionDetectionModel.getDetectionAlgorithm();

        LogisticRegressionValidationSummary logisticRegressionValidationSummary =
                new LogisticRegressionValidationSummary(sc.sc(), logisticRegressionDetectionAlgorithm.getNumClasses(), indexing, marking);

        LogisticRegressionDistJob logisticRegressionDistJob = new LogisticRegressionDistJob();

        logisticRegressionDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                logisticRegressionDetectionModel,
                logisticRegressionValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;

        logisticRegressionValidationSummary.setTotalValidationTime(time);
        return logisticRegressionValidationSummary;
    }

    public SVMValidationSummary validateSVMAthenaFeatures(JavaSparkContext sc,
                                                          FeatureConstraint featureConstraint,
                                                          AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                          SVMDetectionModel svmDetectionModel,
                                                          Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        SVMDetectionAlgorithm svmDetectionAlgorithm =
                (SVMDetectionAlgorithm) svmDetectionModel.getDetectionAlgorithm();

        SVMValidationSummary svmValidationSummary =
                new SVMValidationSummary(sc.sc(),
                        2, indexing, marking);

        SVMDistJob svmDistJob = new SVMDistJob();

        svmDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                svmDetectionModel,
                svmValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;

        svmValidationSummary.setTotalValidationTime(time);
        return svmValidationSummary;
    }

    public GradientBoostedTreesValidationSummary validateGradientBoostedTreesAthenaFeatures(JavaSparkContext sc,
                                                                                            FeatureConstraint featureConstraint,
                                                                                            AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                            GradientBoostedTreesDetectionModel gradientBoostedTreesDetectionModel,
                                                                                            Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm =
                (GradientBoostedTreesDetectionAlgorithm) gradientBoostedTreesDetectionModel.getDetectionAlgorithm();

        GradientBoostedTreesValidationSummary gradientBoostedTreesValidationSummary =
                new GradientBoostedTreesValidationSummary(sc.sc(),
                        gradientBoostedTreesDetectionAlgorithm.getNumClasses(), indexing, marking);

        GradientBoostedTreesDistJob gradientBoostedTreesDistJob = new GradientBoostedTreesDistJob();

        gradientBoostedTreesDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                gradientBoostedTreesDetectionModel,
                gradientBoostedTreesValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;

        gradientBoostedTreesValidationSummary.setTotalValidationTime(time);
        return gradientBoostedTreesValidationSummary;
    }

    public RandomForestValidationSummary validateRandomForestAthenaFeatures(JavaSparkContext sc,
                                                                            FeatureConstraint featureConstraint,
                                                                            AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                            RandomForestDetectionModel randomForestDetectionModel,
                                                                            Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        RandomForestDetectionAlgorithm randomForestDetectionAlgorithm = (RandomForestDetectionAlgorithm) randomForestDetectionModel.getDetectionAlgorithm();

        RandomForestValidationSummary randomForestValidationSummary =
                new RandomForestValidationSummary(sc.sc(), randomForestDetectionAlgorithm.getNumClasses(), indexing, marking);

        RandomForestDistJob randomForestDistJob = new RandomForestDistJob();

        randomForestDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                randomForestDetectionModel,
                randomForestValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;

        randomForestValidationSummary.setTotalValidationTime(time);
        return randomForestValidationSummary;
    }

    public NaiveBayesValidationSummary validateNaiveBayesAthenaFeatures(JavaSparkContext sc,
                                                                        FeatureConstraint featureConstraint,
                                                                        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                        NaiveBayesDetectionModel naiveBayesDetectionModel,
                                                                        Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        NaiveBayesDetectionAlgorithm naiveBayesDetectionAlgorithm = (NaiveBayesDetectionAlgorithm) naiveBayesDetectionModel.getDetectionAlgorithm();

        NaiveBayesValidationSummary naiveBayesValidationSummary =
                new NaiveBayesValidationSummary(sc.sc(), naiveBayesDetectionAlgorithm.getNumClasses(), indexing, marking);

        NaiveBayesDistJob naiveBayesDistJob = new NaiveBayesDistJob();


        naiveBayesDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                naiveBayesDetectionModel,
                naiveBayesValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;

        naiveBayesValidationSummary.setTotalValidationTime(time);
        return naiveBayesValidationSummary;
    }

    public DecisionTreeValidationSummary validateDecisionTreeAthenaFeatures(JavaSparkContext sc,
                                                                            FeatureConstraint featureConstraint,
                                                                            AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                            DecisionTreeDetectionModel decisionTreeDetectionModel,
                                                                            Indexing indexing, Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm = (DecisionTreeDetectionAlgorithm) decisionTreeDetectionModel.getDetectionAlgorithm();

        DecisionTreeValidationSummary decisionTreeValidationSummary =
                new DecisionTreeValidationSummary(sc.sc(), decisionTreeDetectionAlgorithm.getNumClasses(), indexing, marking);

        DecisionTreeDistJob decisionTreeDistJob = new DecisionTreeDistJob();

        decisionTreeDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                decisionTreeDetectionModel,
                decisionTreeValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;

        decisionTreeValidationSummary.setTotalValidationTime(time);
        return decisionTreeValidationSummary;
    }

    public GaussianMixtureValidationSummary validateGaussianMixtureAthenaFeatures(JavaSparkContext sc,
                                                                                  FeatureConstraint featureConstraint,
                                                                                  AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                  GaussianMixtureDetectionModel gaussianMixtureDetectionModel,
                                                                                  Indexing indexing,
                                                                                  Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm = (GaussianMixtureDetectionAlgorithm) gaussianMixtureDetectionModel.getDetectionAlgorithm();
        GaussianMixtureValidationSummary gaussianMixtureValidationSummary =
                new GaussianMixtureValidationSummary(sc.sc(), gaussianMixtureDetectionAlgorithm.getK(), indexing, marking);


        GaussianMixtureDistJob gaussianMixtureDistJob = new GaussianMixtureDistJob();

        gaussianMixtureDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                gaussianMixtureDetectionModel,
                gaussianMixtureValidationSummary);


        long end = System.nanoTime(); // <-- start
        long time = end - start;

        gaussianMixtureValidationSummary.setTotalValidationTime(time);
        return gaussianMixtureValidationSummary;
    }

    public KmeansValidationSummary validateKMeansAthenaFeatures(JavaSparkContext sc,
                                                                FeatureConstraint featureConstraint,
                                                                AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                KMeansDetectionModel kMeansDetectionModel,
                                                                Indexing indexing,
                                                                Marking marking) {
        long start = System.nanoTime(); // <-- start

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = (KMeansDetectionAlgorithm) kMeansDetectionModel.getDetectionAlgorithm();

        KmeansValidationSummary kmeansValidationSummary =
                new KmeansValidationSummary(sc.sc(), kMeansDetectionAlgorithm.getK(), indexing, marking);

        KMeansDistJob KMeansDistJob = new KMeansDistJob();

        KMeansDistJob.validate(mongoRDD,
                athenaMLFeatureConfiguration,
                kMeansDetectionModel,
                kmeansValidationSummary);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        kmeansValidationSummary.setTotalValidationTime(time);
        return kmeansValidationSummary;
    }


    public JavaSparkContext SCGenerator(FeatureConstraint featureConstraint) {
        mongodbConfig = new Configuration();
        String IP = "";
        if (databaseConnector.getDATABASE_IP() != null) {
            IP = databaseConnector.getDATABASE_IP();
        } else {
            List<String> DATABASE_IPs = databaseConnector.getDATABASE_IPs();
            for (String ip : DATABASE_IPs) {
                IP = IP + ip +",";
            }
            IP = IP.substring(0, IP.length()-1);
        }

        mongodbConfig.set("mongo.input.uri",
                "mongodb://" +
                        IP +
                        "/featureStore." +
                        featureConstraint.getLocation());

        String query = generatequery(featureConstraint);

        mongodbConfig.set("mongo.input.query", query);
        computingClusterConnector = new ComputingClusterConnector();
        computingClusterConnector.setMasterIP(computingClusterMasterIP);
        registeredClasses.add(mainClass);

        registeredClasses.add("org.apache.spark.mllib.clustering.VectorWithNorm");
        registeredClasses.add("org.apache.spark.mllib.linalg.DenseVector");
        computingClusterConnector.setRegisteredClasses(registeredClasses);
        computingClusterConnector.setArtifactName(artifactName);

        return computingClusterConnector.generateConnector();
    }

    @Override
    public DetectionModel generateAthenaDetectionModel(FeatureConstraint featureConstraint,
                                                       AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                       DetectionAlgorithm detectionAlgorithm,
                                                       Indexing indexing,
                                                       Marking marking) {
        DetectionModel detectionModel;

        if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.KMEANS) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateKMeansAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.GMM) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateGaussianMixtureAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.DT) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateDecisionTreeAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.NV) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateNaiveBayesAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.RF) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateRandomForestAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.GBT) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateGradientBoostedTreesAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.SVM) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateSVMAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.LR) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateLogisticRegressionAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.Linear) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateLinearRegressionAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.Lasso) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateLassoAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else if (detectionAlgorithm.getDetectionStrategy() == DetectionStrategy.Ridge) {
            JavaSparkContext sc = SCGenerator(featureConstraint);
            detectionModel = generateRidgeRegressionAthenaDetectionModel(sc,
                    featureConstraint,
                    athenaMLFeatureConfiguration,
                    detectionAlgorithm,
                    indexing,
                    marking);
            computingClusterConnector.destroySC();
            return detectionModel;
        } else {
            log.warn("Not supported DetectionStrategy -return null");
            return null;
        }
    }

    public LassoDetectionModel generateLassoAthenaDetectionModel(JavaSparkContext sc,
                                                                 FeatureConstraint featureConstraint,
                                                                 AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                 DetectionAlgorithm detectionAlgorithm,
                                                                 Indexing indexing,
                                                                 Marking marking) {
        LassoModelSummary lassoModelSummary = new LassoModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        LassoDetectionAlgorithm lassoDetectionAlgorithm = (LassoDetectionAlgorithm) detectionAlgorithm;

        LassoDetectionModel lassoDetectionModel = new LassoDetectionModel();

        lassoDetectionModel.setLassoDetectionAlgorithm(lassoDetectionAlgorithm);
        lassoModelSummary.setLassoDetectionAlgorithm(lassoDetectionAlgorithm);
        lassoDetectionModel.setFeatureConstraint(featureConstraint);
        lassoDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        lassoDetectionModel.setIndexing(indexing);
        lassoDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        LassoDistJob lassoDistJob = new LassoDistJob();

        LassoModel lassoModel = lassoDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, lassoDetectionAlgorithm, marking, lassoModelSummary);


        lassoDetectionModel.setModel(lassoModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        lassoModelSummary.setTotalLearningTime(time);
        lassoDetectionModel.setClassificationModelSummary(lassoModelSummary);

        return lassoDetectionModel;
    }

    public RidgeRegressionDetectionModel generateRidgeRegressionAthenaDetectionModel(JavaSparkContext sc,
                                                                                     FeatureConstraint featureConstraint,
                                                                                     AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                     DetectionAlgorithm detectionAlgorithm,
                                                                                     Indexing indexing,
                                                                                     Marking marking) {
        RidgeRegressionModelSummary ridgeRegressionModelSummary = new RidgeRegressionModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm = (RidgeRegressionDetectionAlgorithm) detectionAlgorithm;

        RidgeRegressionDetectionModel ridgeRegressionDetectionModel = new RidgeRegressionDetectionModel();

        ridgeRegressionDetectionModel.setRidgeRegressionDetectionAlgorithm(ridgeRegressionDetectionAlgorithm);
        ridgeRegressionModelSummary.setRidgeRegressionDetectionAlgorithm(ridgeRegressionDetectionAlgorithm);
        ridgeRegressionDetectionModel.setFeatureConstraint(featureConstraint);
        ridgeRegressionDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        ridgeRegressionDetectionModel.setIndexing(indexing);
        ridgeRegressionDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        RidgeRegressionDistJob ridgeRegressionDistJob = new RidgeRegressionDistJob();

        RidgeRegressionModel ridgeRegressionModel = ridgeRegressionDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, ridgeRegressionDetectionAlgorithm, marking, ridgeRegressionModelSummary);


        ridgeRegressionDetectionModel.setModel(ridgeRegressionModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        ridgeRegressionModelSummary.setTotalLearningTime(time);
        ridgeRegressionDetectionModel.setClassificationModelSummary(ridgeRegressionModelSummary);

        return ridgeRegressionDetectionModel;
    }

    public LinearRegressionDetectionModel generateLinearRegressionAthenaDetectionModel(JavaSparkContext sc,
                                                                                       FeatureConstraint featureConstraint,
                                                                                       AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                       DetectionAlgorithm detectionAlgorithm,
                                                                                       Indexing indexing,
                                                                                       Marking marking) {
        LinearRegressionModelSummary linearRegressionModelSummary = new LinearRegressionModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm = (LinearRegressionDetectionAlgorithm) detectionAlgorithm;

        LinearRegressionDetectionModel linearRegressionDetectionModel = new LinearRegressionDetectionModel();

        linearRegressionDetectionModel.setLinearRegressionDetectionAlgorithm(linearRegressionDetectionAlgorithm);
        linearRegressionModelSummary.setLinearRegressionDetectionAlgorithm(linearRegressionDetectionAlgorithm);
        linearRegressionDetectionModel.setFeatureConstraint(featureConstraint);
        linearRegressionDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        linearRegressionDetectionModel.setIndexing(indexing);
        linearRegressionDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        LinearRegressionDistJob linearRegressionDistJob = new LinearRegressionDistJob();

        LinearRegressionModel linearRegressionModel = linearRegressionDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, linearRegressionDetectionAlgorithm, marking, linearRegressionModelSummary);


        linearRegressionDetectionModel.setModel(linearRegressionModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        linearRegressionModelSummary.setTotalLearningTime(time);
        linearRegressionDetectionModel.setClassificationModelSummary(linearRegressionModelSummary);

        return linearRegressionDetectionModel;
    }

    public LogisticRegressionDetectionModel generateLogisticRegressionAthenaDetectionModel(JavaSparkContext sc,
                                                                                           FeatureConstraint featureConstraint,
                                                                                           AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                           DetectionAlgorithm detectionAlgorithm,
                                                                                           Indexing indexing,
                                                                                           Marking marking) {
        LogisticRegressionModelSummary logisticRegressionModelSummary = new LogisticRegressionModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        LogisticRegressionDetectionAlgorithm logisticRegressionDetectionAlgorithm = (LogisticRegressionDetectionAlgorithm) detectionAlgorithm;

        LogisticRegressionDetectionModel logisticRegressionDetectionModel = new LogisticRegressionDetectionModel();

        logisticRegressionDetectionModel.setLogisticRegressionDetectionAlgorithm(logisticRegressionDetectionAlgorithm);
        logisticRegressionModelSummary.setLogisticRegressionDetectionAlgorithm(logisticRegressionDetectionAlgorithm);
        logisticRegressionDetectionModel.setFeatureConstraint(featureConstraint);
        logisticRegressionDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        logisticRegressionDetectionModel.setIndexing(indexing);
        logisticRegressionDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        LogisticRegressionDistJob logisticRegressionDistJob = new LogisticRegressionDistJob();

        LogisticRegressionModel logisticRegressionModel = logisticRegressionDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, logisticRegressionDetectionAlgorithm, marking, logisticRegressionModelSummary);


        logisticRegressionDetectionModel.setModel(logisticRegressionModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        logisticRegressionModelSummary.setTotalLearningTime(time);
        logisticRegressionDetectionModel.setClassificationModelSummary(logisticRegressionModelSummary);

        return logisticRegressionDetectionModel;
    }

    public SVMDetectionModel generateSVMAthenaDetectionModel(JavaSparkContext sc,
                                                             FeatureConstraint featureConstraint,
                                                             AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                             DetectionAlgorithm detectionAlgorithm,
                                                             Indexing indexing,
                                                             Marking marking) {
        SVMModelSummary svmModelSummary = new SVMModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        SVMDetectionAlgorithm svmDetectionAlgorithm = (SVMDetectionAlgorithm) detectionAlgorithm;

        SVMDetectionModel svmDetectionModel = new SVMDetectionModel();

        svmDetectionModel.setSVMDetectionAlgorithm(svmDetectionAlgorithm);
        svmModelSummary.setSVMDetectionAlgorithm(svmDetectionAlgorithm);
        svmDetectionModel.setFeatureConstraint(featureConstraint);
        svmDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        svmDetectionModel.setIndexing(indexing);
        svmDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        SVMDistJob svmDistJob = new SVMDistJob();

        SVMModel svmModel = svmDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, svmDetectionAlgorithm, marking, svmModelSummary);


        svmDetectionModel.setSVMModel(svmModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        svmModelSummary.setTotalLearningTime(time);
        svmDetectionModel.setClassificationModelSummary(svmModelSummary);

        return svmDetectionModel;
    }

    public GradientBoostedTreesDetectionModel generateGradientBoostedTreesAthenaDetectionModel(JavaSparkContext sc,
                                                                                               FeatureConstraint featureConstraint,
                                                                                               AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                               DetectionAlgorithm detectionAlgorithm,
                                                                                               Indexing indexing,
                                                                                               Marking marking) {
        GradientBoostedTreesModelSummary gradientBoostedTreesModelSummary = new GradientBoostedTreesModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm = (GradientBoostedTreesDetectionAlgorithm) detectionAlgorithm;

        GradientBoostedTreesDetectionModel gradientBoostedTreesDetectionModel = new GradientBoostedTreesDetectionModel();

        gradientBoostedTreesDetectionModel.setGradientBoostedTreesDetectionAlgorithm(gradientBoostedTreesDetectionAlgorithm);
        gradientBoostedTreesModelSummary.setGradientBoostedTreesDetectionAlgorithm(gradientBoostedTreesDetectionAlgorithm);
        gradientBoostedTreesDetectionModel.setFeatureConstraint(featureConstraint);
        gradientBoostedTreesDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        gradientBoostedTreesDetectionModel.setIndexing(indexing);
        gradientBoostedTreesDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        GradientBoostedTreesDistJob gradientBoostedTreesDistJob = new GradientBoostedTreesDistJob();

        GradientBoostedTreesModel decisionTreeModel = gradientBoostedTreesDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, gradientBoostedTreesDetectionAlgorithm, marking, gradientBoostedTreesModelSummary);


        gradientBoostedTreesDetectionModel.setGradientBoostedTreestModel(decisionTreeModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        gradientBoostedTreesModelSummary.setTotalLearningTime(time);
        gradientBoostedTreesDetectionModel.setClassificationModelSummary(gradientBoostedTreesModelSummary);

        return gradientBoostedTreesDetectionModel;
    }

    public RandomForestDetectionModel generateRandomForestAthenaDetectionModel(JavaSparkContext sc,
                                                                               FeatureConstraint featureConstraint,
                                                                               AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                               DetectionAlgorithm detectionAlgorithm,
                                                                               Indexing indexing,
                                                                               Marking marking) {
        RandomForestModelSummary randomForestModelSummary = new RandomForestModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        RandomForestDetectionAlgorithm randomForestDetectionAlgorithm = (RandomForestDetectionAlgorithm) detectionAlgorithm;

        RandomForestDetectionModel randomForestDetectionModel = new RandomForestDetectionModel();

        randomForestDetectionModel.setRandomForestDetectionAlgorithm(randomForestDetectionAlgorithm);
        randomForestModelSummary.setRandomForestDetectionAlgorithm(randomForestDetectionAlgorithm);
        randomForestDetectionModel.setFeatureConstraint(featureConstraint);
        randomForestDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        randomForestDetectionModel.setIndexing(indexing);
        randomForestDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        RandomForestDistJob randomForestDistJob = new RandomForestDistJob();

        RandomForestModel decisionTreeModel = randomForestDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, randomForestDetectionAlgorithm, marking, randomForestModelSummary);


        randomForestDetectionModel.setRandomForestModel(decisionTreeModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        randomForestModelSummary.setTotalLearningTime(time);
        randomForestDetectionModel.setClassificationModelSummary(randomForestModelSummary);

        return randomForestDetectionModel;
    }

    public NaiveBayesDetectionModel generateNaiveBayesAthenaDetectionModel(JavaSparkContext sc,
                                                                           FeatureConstraint featureConstraint,
                                                                           AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                           DetectionAlgorithm detectionAlgorithm,
                                                                           Indexing indexing,
                                                                           Marking marking) {

        NaiveBayesModelSummary naiveBayesModelSummary = new NaiveBayesModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        NaiveBayesDetectionAlgorithm naiveBayesDetectionAlgorithm = (NaiveBayesDetectionAlgorithm) detectionAlgorithm;

        NaiveBayesDetectionModel naiveBayesDetectionModel = new NaiveBayesDetectionModel();

        naiveBayesDetectionModel.setNaiveBayesDetectionAlgorithm(naiveBayesDetectionAlgorithm);
        naiveBayesModelSummary.setNaiveBayesDetectionAlgorithm(naiveBayesDetectionAlgorithm);
        naiveBayesDetectionModel.setFeatureConstraint(featureConstraint);
        naiveBayesDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        naiveBayesDetectionModel.setIndexing(indexing);
        naiveBayesDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        NaiveBayesDistJob naiveBayesDistJob = new NaiveBayesDistJob();

        NaiveBayesModel naiveBayesModel = naiveBayesDistJob.generateModelWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, naiveBayesDetectionAlgorithm, marking, naiveBayesModelSummary);

        naiveBayesDetectionModel.setNaiveBayesModel(naiveBayesModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        naiveBayesModelSummary.setTotalLearningTime(time);
        naiveBayesDetectionModel.setClassificationModelSummary(naiveBayesModelSummary);

        return naiveBayesDetectionModel;
    }

    public DecisionTreeDetectionModel generateDecisionTreeAthenaDetectionModel(JavaSparkContext sc,
                                                                               FeatureConstraint featureConstraint,
                                                                               AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                               DetectionAlgorithm detectionAlgorithm,
                                                                               Indexing indexing,
                                                                               Marking marking) {
        DecisionTreeModelSummary decisionTreeModelSummary = new DecisionTreeModelSummary(
                sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm = (DecisionTreeDetectionAlgorithm) detectionAlgorithm;

        DecisionTreeDetectionModel decisionTreeDetectionModel = new DecisionTreeDetectionModel();

        decisionTreeDetectionModel.setDecisionTreeDetectionAlgorithm(decisionTreeDetectionAlgorithm);
        decisionTreeModelSummary.setDecisionTreeDetectionAlgorithm(decisionTreeDetectionAlgorithm);
        decisionTreeDetectionModel.setFeatureConstraint(featureConstraint);
        decisionTreeDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        decisionTreeDetectionModel.setIndexing(indexing);
        decisionTreeDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        DecisionTreeDistJob decisionTreeDistJob = new DecisionTreeDistJob();

        DecisionTreeModel decisionTreeModel = decisionTreeDistJob.generateDecisionTreeWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, decisionTreeDetectionAlgorithm, marking, decisionTreeModelSummary);


        decisionTreeDetectionModel.setDecisionTreeModel(decisionTreeModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        decisionTreeModelSummary.setTotalLearningTime(time);
        decisionTreeDetectionModel.setClassificationModelSummary(decisionTreeModelSummary);

        return decisionTreeDetectionModel;
    }

    public GaussianMixtureDetectionModel generateGaussianMixtureAthenaDetectionModel(JavaSparkContext sc,
                                                                                     FeatureConstraint featureConstraint,
                                                                                     AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                                     DetectionAlgorithm detectionAlgorithm,
                                                                                     Indexing indexing,
                                                                                     Marking marking) {
        GaussianMixtureModelSummary gaussianMixtureModelSummary = new GaussianMixtureModelSummary(
                sc.sc(), indexing, marking);
        long start = System.nanoTime(); // <-- start

        GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm = (GaussianMixtureDetectionAlgorithm) detectionAlgorithm;

        GaussianMixtureDetectionModel gaussianMixtureDetectionModel = new GaussianMixtureDetectionModel();
        gaussianMixtureDetectionModel.setGaussianMixtureDetectionAlgorithm(gaussianMixtureDetectionAlgorithm);
        gaussianMixtureModelSummary.setGaussianMixtureDetectionAlgorithm(gaussianMixtureDetectionAlgorithm);
        gaussianMixtureDetectionModel.setFeatureConstraint(featureConstraint);
        gaussianMixtureDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        gaussianMixtureDetectionModel.setIndexing(indexing);
        gaussianMixtureDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        GaussianMixtureDistJob gaussianMixtureDistJob = new GaussianMixtureDistJob();

        GaussianMixtureModel gaussianMixtureModel = gaussianMixtureDistJob.generateGaussianMixtureWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, gaussianMixtureDetectionAlgorithm, gaussianMixtureModelSummary);


        gaussianMixtureDetectionModel.setkGaussianMixtureModel(gaussianMixtureModel);

        long end = System.nanoTime(); // <-- start
        long time = end - start;
        gaussianMixtureModelSummary.setTotalLearningTime(time);
        gaussianMixtureDetectionModel.setClusterModelSummary(gaussianMixtureModelSummary);
        gaussianMixtureModelSummary.setGaussianMixtureModel(gaussianMixtureModel);
        return gaussianMixtureDetectionModel;
    }

    public KMeansDetectionModel generateKMeansAthenaDetectionModel(JavaSparkContext sc,
                                                                   FeatureConstraint featureConstraint,
                                                                   AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                   DetectionAlgorithm detectionAlgorithm,
                                                                   Indexing indexing,
                                                                   Marking marking) {
        KmeansModelSummary kmeansModelSummary = new KmeansModelSummary(sc.sc(), indexing, marking);

        long start = System.nanoTime(); // <-- start

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = (KMeansDetectionAlgorithm) detectionAlgorithm;
        KMeansDetectionModel kMeansDetectionModel = new KMeansDetectionModel();
        kMeansDetectionModel.setkMeansDetectionAlgorithm(kMeansDetectionAlgorithm);
        kmeansModelSummary.setkMeansDetectionAlgorithm(kMeansDetectionAlgorithm);
        kMeansDetectionModel.setFeatureConstraint(featureConstraint);
        kMeansDetectionModel.setAthenaMLFeatureConfiguration(athenaMLFeatureConfiguration);
        kMeansDetectionModel.setIndexing(indexing);
        kMeansDetectionModel.setMarking(marking);

        JavaPairRDD<Object, BSONObject> mongoRDD;
        mongoRDD = sc.newAPIHadoopRDD(
                mongodbConfig,            // Configuration
                MongoInputFormat.class,   // InputFormat: read from a live cluster.
                Object.class,             // Key class
                BSONObject.class          // Value class
        );

        KMeansDistJob KMeansDistJob = new KMeansDistJob();


        KMeansModel kMeansModel = KMeansDistJob.generateKmeansWithPreprocessing(mongoRDD,
                athenaMLFeatureConfiguration, kMeansDetectionAlgorithm, kmeansModelSummary);


        kMeansDetectionModel.setkMeansModel(kMeansModel);
        long end = System.nanoTime(); // <-- start
        long time = end - start;
        kmeansModelSummary.setTotalLearningTime(time);
        kMeansDetectionModel.setClusterModelSummary(kmeansModelSummary);
        return kMeansDetectionModel;
    }


    public Configuration testMongoDBConfiguration(FeatureConstraint featureConstraint) {
        mongodbConfig = new Configuration();
        mongodbConfig.set("mongo.input.uri",
                "mongodb://" +
                        databaseConnector.getDATABASE_IP() +
                        "/featureStore." +
                        featureConstraint.getLocation());

        String query = generatequery(featureConstraint);

        mongodbConfig.set("mongo.input.query", query);

        return mongodbConfig;
    }

    public JavaSparkContext testJavaSparkContext() {
        computingClusterConnector = new ComputingClusterConnector();
        computingClusterConnector.setMasterIP(computingClusterMasterIP);
        registeredClasses.add(mainClass);
        registeredClasses.add("org.apache.spark.mllib.clustering.VectorWithNorm");
        registeredClasses.add("org.apache.spark.mllib.linalg.DenseVector");
        computingClusterConnector.setRegisteredClasses(registeredClasses);
        computingClusterConnector.setArtifactName(artifactName);
        return computingClusterConnector.generateConnector();
    }

    public JavaSparkContext getSC() {
        return this.sc;
    }

    @Override
    public void saveDetectionModel(DetectionModel detectionModel, String path) {
        modelSerialization.saveModelToFile(detectionModel, path);
    }

    @Override
    public DetectionModel loadDetectionModel(String path) {
        return modelSerialization.loadModelFromFile(path);
    }




    public String generatequery(FeatureConstraint featureConstraint) {
        List<TargetAthenaValue> dataRequestObjectValueList = featureConstraint.getDataRequestObjectValueList();

        FeatureConstraintOperatorType featureConstraintOperatorType =
                featureConstraint.getFeatureConstraintOperatorType();
        String query = null;


        if (!(dataRequestObjectValueList.size() > 0)) {
            return null;
        }

        if (featureConstraintOperatorType == FeatureConstraintOperatorType.COMPARABLE) {
            query = getqueryFromRequestOperatorComparison(featureConstraint);

        } else if (featureConstraintOperatorType == FeatureConstraintOperatorType.LOGICAL) {
            query = getqueryFromRequestOperatorLogical(featureConstraint);

        } else {
            log.warn("not supported FeatureConstraintOperatorType");
        }

        return query;

    }

    public String getqueryFromRequestOperatorComparison(FeatureConstraint featureConstraint) {

        FeatureConstraintType featureConstraintType =
                featureConstraint.getFeatureConstraintType();
        FeatureConstraintOperator featureConstraintOperator = featureConstraint.getFeatureConstraintOperator();
        AthenaField name = featureConstraint.getFeatureName();
        List<TargetAthenaValue> value = featureConstraint.getDataRequestObjectValueList();

        Bson obj = null;

        String target = null;
        if (featureConstraintType == FeatureConstraintType.FEATURE) {
            target = AthenaFeatureField.FEATURE + "." + name.getValue();
        } else if (featureConstraintType == FeatureConstraintType.INDEX) {
            target = name.getValue();

        } else {
            log.warn("not supported type :{}", featureConstraintType.toString());
            return null;
        }


        if (!(value.size() > 0)) {
            log.warn("list size is not bigger than 0 :{}", value.size());
            return null;
        }

        for (int i = 0; i < value.size(); i++) {
            switch (featureConstraintOperator.getValue()) {
                case FeatureConstraintOperator.COMPARISON_EQ:
                    obj = eq(target, value.get(i).getTargetAthenaValue());
                    break;
                case FeatureConstraintOperator.COMPARISON_GT:
                    obj = gt(target, value.get(i).getTargetAthenaValue());
                    break;
                case FeatureConstraintOperator.COMPARISON_GTE:
                    obj = gte(target, value.get(i).getTargetAthenaValue());
                    break;
                case FeatureConstraintOperator.COMPARISON_LT:
                    obj = lt(target, value.get(i).getTargetAthenaValue());
                    break;
                case FeatureConstraintOperator.COMPARISON_LTE:
                    obj = lte(target, value.get(i).getTargetAthenaValue());
                    break;
                case FeatureConstraintOperator.COMPARISON_NE:
                    obj = ne(target, value.get(i).getTargetAthenaValue());
                    break;
                default:
                    log.warn("not supported comparsion type");
                    return null;
            }
        }

        BsonDocument matchQueryDocument =
                obj.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());

        String modifiedQuery = matchQueryDocument.toString();

        if (Objects.equals(featureConstraintOperator.getValue(), FeatureConstraintOperator.COMPARISON_EQ)) {
            String[] splited = modifiedQuery.split(":");
            modifiedQuery = splited[0] + ": { \"$eq\" :" + splited[1] + ":" + splited[2] + " }";
        }


        return modifiedQuery;
    }

    public String getqueryFromRequestOperatorLogical(FeatureConstraint featureConstraint) {
        List<String> innerObjectList = new ArrayList<>();
        FeatureConstraintOperator featureConstraintOperator = featureConstraint.getFeatureConstraintOperator();
        List<TargetAthenaValue> valueList = featureConstraint.getDataRequestObjectValueList();

        String query = "{ \"$operator\" : [";

        for (int i = 0; i < valueList.size(); i++) {
            if (!(valueList.get(i).getTargetAthenaValue() instanceof FeatureConstraint)) {
                log.warn("Rqeust type must be DataRequestOBject");
                return null;
            }
            if (i != 0) {
                query = query + ", ";
            }
            query = query + generatequery((FeatureConstraint) valueList.get(i).getTargetAthenaValue());
        }

        switch (featureConstraintOperator.getValue()) {
            case FeatureConstraintOperator.LOGICAL_AND:
                query = query.replaceAll("operator", "and");
                break;
            case FeatureConstraintOperator.LOGICAL_OR:
                query = query.replaceAll("operator", "or");
                break;
            case FeatureConstraintOperator.LOGICAL_IN:
            default:
                log.warn("not supported logical type");
                return null;
        }

        query = query + "] }";
        System.out.println("comparable:" + query);
        return query;
    }

}
