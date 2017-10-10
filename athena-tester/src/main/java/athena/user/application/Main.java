package athena.user.application;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesDetectionAlgorithm;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesDetectionModel;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesValidationSummary;
import athena.api.ClassificationMarkingElement;
import athena.api.DetectionModel;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.DecisionTree.DecisionTreeDetectionAlgorithm;
import athena.api.classification.DecisionTree.DecisionTreeDetectionModel;
import athena.api.classification.DecisionTree.DecisionTreeValidationSummary;
import athena.api.classification.LogisticRegression.LogisticRegressionDetectionAlgorithm;
import athena.api.classification.LogisticRegression.LogisticRegressionDetectionModel;
import athena.api.classification.LogisticRegression.LogisticRegressionValidationSummary;
import athena.api.classification.NaiveBayes.NaiveBayesDetectionAlgorithm;
import athena.api.classification.NaiveBayes.NaiveBayesDetectionModel;
import athena.api.classification.NaiveBayes.NaiveBayesValidationSummary;
import athena.api.classification.RandomForest.RandomForestDetectionAlgorithm;
import athena.api.classification.RandomForest.RandomForestDetectionModel;
import athena.api.classification.RandomForest.RandomForestValidationSummary;
import athena.api.classification.SVM.SVMDetectionAlgorithm;
import athena.api.classification.SVM.SVMDetectionModel;
import athena.api.classification.SVM.SVMValidationSummary;
import athena.api.clustering.ClusterModelSummary;
import athena.api.clustering.gaussianMixture.GaussianMixtureDetectionAlgorithm;
import athena.api.clustering.gaussianMixture.GaussianMixtureDetectionModel;
import athena.api.clustering.gaussianMixture.GaussianMixtureValidationSummary;
import athena.api.clustering.kmeans.KMeansDetectionAlgorithm;
import athena.api.clustering.kmeans.KMeansDetectionModel;
import athena.api.clustering.kmeans.KmeansModelSummary;
import athena.api.clustering.kmeans.KmeansValidationSummary;
import athena.api.onlineMLEventListener;
import athena.api.regression.Lasso.LassoDetectionAlgorithm;
import athena.api.regression.Lasso.LassoDetectionModel;
import athena.api.regression.Lasso.LassoValidationSummary;
import athena.api.regression.LinearRegression.LinearRegressionDetectionAlgorithm;
import athena.api.regression.LinearRegression.LinearRegressionDetectionModel;
import athena.api.regression.LinearRegression.LinearRegressionValidationSummary;
import athena.api.regression.RidgeRegression.RidgeRegressionDetectionAlgorithm;
import athena.api.regression.RidgeRegression.RidgeRegressionDetectionModel;
import athena.api.regression.RidgeRegression.RidgeRegressionValidationSummary;
import athena.northbound.MachineLearningManager;
import athena.northbound.impl.EventDeliveryManagerImpl;
import athena.northbound.impl.FlowRuleManagerImpl;
import athena.northbound.impl.MachineLearningManagerImpl;
import athena.util.ControllerConnector;
import athena.util.DatabaseConnector;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.Accumulator;
import org.mapdb.Atomic;
import org.objenesis.strategy.SerializingInstantiatorStrategy;
import org.onosproject.athena.ExternalDataType;
import org.onosproject.athena.FlowRuleActionType;
import org.onosproject.athena.SerializerWrapper;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaField;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.AthenaValueGenerator;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.athena.database.impl.FeatureDatabaseMgmtManager;
import org.onosproject.core.ApplicationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.util.parsing.combinator.testing.Str;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * spark-submit --class "athena.user.application.SparkApplication" target/spark-application-1.4.0.jar
 * Created by seunghyeon on 1/13/16.
 */
public class Main implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = 6153228040759916473L;
    private static int k = 5;
    private static int iter = 20;

    private static String MONGO_DB_IP = System.getenv("MD1");
    private static String SPARK_MASTER = System.getenv("SP1");

    private static String ARTIFACT_ID = "athena-tester-1.6.0";
    private static String TRAIN_SET = "model";

    MachineLearningManager machineLearningManager = new MachineLearningManagerImpl();

    public static void main(String[] args) {
        Main main = new Main();
        try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        main.start();
    }

    public void start() {
//        saveMoelTestsComplete();
//        restoreAndValidateModelComplete();

//        serializationTest();
//        testOnlineDelivery();
//        restoreAndValidateModel();
//        saveMoelTests();
//        testkMenas();
//        tesetGaussian();
//        tesetDecisionTree();
//        testNaiveBayes();
//        tesetRandomForest();
//        testGBTs();
//        testSVMs();
//        testLR();
//        testLinearRegression();
//        evaluationDDoS();
////        evaluationDDoS_Model();
//        evaluationDDoS_Test();
//        copyDBElement(8, "127.0.0.1", "DDoSTrainSet", AthenaFeatureField.FLOW_STATS);
//        flowruleInstalltest();
//        onlineDetectionTest();

        Sample sample = new Sample();
//        sample.evaluationDDoS_Model();
        sample.testOnlineDelivery();
//        sample.requestFeatures();

//        evaluationDDoS_Model();
    }


    public void flowruleInstalltest() {
        TargetAthenaValue IP_SRC = new TargetAthenaValue(AthenaValueGenerator.parseIPv4ToAthenaValue("192.168.0.1"));
        TargetAthenaValue IP_DST = new TargetAthenaValue(AthenaValueGenerator.parseIPv4ToAthenaValue("143.248.3.2"));
        FlowRuleActionType actionType = FlowRuleActionType.BLOCK;
        ControllerConnector controllerConnector = new ControllerConnector();
        FlowRuleManagerImpl flowRuleManager = new FlowRuleManagerImpl(controllerConnector);
        String deviceUri = "of:000000001";

        flowRuleManager.issueFlowRule(null, IP_SRC, IP_DST, deviceUri, actionType);
    }

    public void onlineDetectionTest() {
        ControllerConnector controllerConnector = new ControllerConnector();

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        KMeansDetectionModel kMeansDetectionModel =
                (KMeansDetectionModel) machineLearningManager.loadDetectionModel("./AthenaModel.KMeansDetectionModel");

        InternalonlineMLEventListener internalonlineMLEventListener = new InternalonlineMLEventListener(controllerConnector);

        machineLearningManager.registerOnlineValidation(featureConstraint,
                null,
                kMeansDetectionModel,
                internalonlineMLEventListener,
                controllerConnector);

    }

    public void evaluationDDoS_Model() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);
//        databaseConnector.setDATABASE_IPs(MONGO_DB_IPs);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);

        machineLearningManager.setArtifactId(ARTIFACT_ID);
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_NE),
                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        //for train set
        featureConstraint.setLocation(TRAIN_SET);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.setNormalization(true);
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(8);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(10);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x000000ff, 0x00000065);
//        marking.setDstMaskMarking(0x000000ff, 0x000000d3);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));


        KMeansDetectionModel kMeansDetectionModel = (KMeansDetectionModel) machineLearningManager.generateAthenaDetectionModel(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionAlgorithm, indexing, marking);
        machineLearningManager.saveDetectionModel(kMeansDetectionModel, null);
        kMeansDetectionModel.getSummary().printSummary();
    }

    public void evaluationDDoS_Test() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);
//        databaseConnector.setDATABASE_IPs(MONGO_DB_IPs);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);

        machineLearningManager.setArtifactId(ARTIFACT_ID);
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_NE),
                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.setNormalization(true);
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(8);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(5);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x000000ff, 0x00000065);
//        marking.setDstMaskMarking(0x000000ff, 0x000000d3);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        KMeansDetectionModel kMeansDetectionModel =
                (KMeansDetectionModel) machineLearningManager.loadDetectionModel("./AthenaModel.KMeansDetectionModel");
        //Validationset
        featureConstraint.setLocation(TRAIN_SET);
        KmeansValidationSummary kmeansValidationSummary = (KmeansValidationSummary) machineLearningManager.validateAthenaFeatures(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionModel, indexing, marking);
        kmeansValidationSummary.printResults();

    }

    public void evaluationDDoS() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);
//        databaseConnector.setDATABASE_IPs(MONGO_DB_IPs);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);

        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_NE),
                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        //for train set
        featureConstraint.setLocation(TRAIN_SET);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.setNormalization(true);
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(8);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(5);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x000000ff, 0x00000065);
//        marking.setDstMaskMarking(0x000000ff, 0x000000d3);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));


        KMeansDetectionModel kMeansDetectionModel = (KMeansDetectionModel) machineLearningManager.generateAthenaDetectionModel(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionAlgorithm, indexing, marking);

        //Validationset
//        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.setLocation(TRAIN_SET);

        KmeansValidationSummary kmeansValidationSummary = (KmeansValidationSummary) machineLearningManager.validateAthenaFeatures(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionModel, indexing, marking);
        kmeansValidationSummary.printResults();
        kMeansDetectionModel.getSummary().printSummary();

    }


    public void serializationTest() {
        //port
        FeatureConstraint flow =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("1")));
        QueryIdentifier queryIdentifier = new QueryIdentifier((short) 1123);
        ExternalDataType externalDataType = new ExternalDataType(ExternalDataType.ONLINE_FEATURE_REQUEST_UNREGISTER);
        SerializerWrapper serializerWrapper = new SerializerWrapper();
        serializerWrapper.setExternalDataType(externalDataType);
        serializerWrapper.setFeatureConstraint(flow);
        //Serialize in here

        //Serialze in After
        Socket s = null;
        for (int i = 0; i < 100000; i++) {
            queryIdentifier = new QueryIdentifier((short) i);
            serializerWrapper.setQueryIdentifier(queryIdentifier);
            verifyElements(serializerWrapper);
            try {
                s = new Socket("127.0.0.1", 11231);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(serializerWrapper);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void verifyElements(SerializerWrapper serializerWrapper) {
        System.out.println("[Queryidentifier] " + serializerWrapper.getQueryIdentifier().getId());
        System.out.println("[ExternalDataType]" + serializerWrapper.getExternalDataType().getType());
        System.out.println("[FeatureConstraint][FeatureConstraintType]" +
                serializerWrapper.getFeatureConstraint().getFeatureConstraintType().name());
        System.out.println("[FeatureConstraint][FeatureConstraintOperatorType]" +
                serializerWrapper.getFeatureConstraint().getFeatureConstraintOperatorType().name());
        System.out.println("[FeatureConstraint][FeatureConstraintOperator]" +
                serializerWrapper.getFeatureConstraint().getFeatureConstraintOperator().getValue());
        System.out.println("[FeatureConstraint][AthenaFeatureField]" +
                serializerWrapper.getFeatureConstraint().getFeatureName().getValue());

    }

    public void restoreAndValidateModelComplete() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("SINGLE");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL, new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX, FeatureConstraintOperatorType.COMPARABLE, new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT), new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC), new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX, FeatureConstraintOperatorType.COMPARABLE, new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT), new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO), new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));
        athenaMLFeatureConfiguration.setNormalization(true);

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(k);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(3);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x0000000f, 0x6);
        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        KMeansDetectionModel kMeansDetectionModel =
                (KMeansDetectionModel) machineLearningManager.loadDetectionModel("./AthenaModel.KMeansDetectionModel");

        KmeansValidationSummary kmeansValidationSummary = (KmeansValidationSummary)
                machineLearningManager.validateAthenaFeatures(featureConstraint,
                        athenaMLFeatureConfiguration,
                        kMeansDetectionModel,
                        indexing, marking);
        kmeansValidationSummary.printResults();


    }

    public void restoreAndValidateModel() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("SINGLE");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL, new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX, FeatureConstraintOperatorType.COMPARABLE, new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT), new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC), new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX, FeatureConstraintOperatorType.COMPARABLE, new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT), new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO), new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));
        athenaMLFeatureConfiguration.setNormalization(true);

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(k);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(3);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x0000000f, 0x6);
        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));


        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new SerializingInstantiatorStrategy());
        kryo.register(KMeansDetectionModel.class);
        kryo.register(AthenaMLFeatureConfiguration.class);
        kryo.register(DetectionModel.class);
        kryo.register(FeatureConstraint.class);
        kryo.register(AthenaFeatureField.class);
        kryo.register(AthenaIndexField.class);
        kryo.register(AthenaField.class);
        kryo.register(FeatureConstraintOperator.class);
        kryo.register(FeatureConstraintOperatorType.class);
        kryo.register(FeatureConstraintType.class);
        kryo.register(AthenaMLFeatureConfiguration.class);
        kryo.register(Marking.class);
        kryo.register(FeatureDatabaseMgmtManager.class);
        kryo.register(Indexing.class);
        kryo.register(ClusterModelSummary.class);
        kryo.register(KMeansDetectionAlgorithm.class);
        kryo.register(KmeansModelSummary.class);
        kryo.register(Accumulator.class);
        kryo.register(ClusterModelSummary.UniqueFlowAccumulatorParam.class);
        kryo.register(ClusterModelSummary.LongAccumulatorParam.class);
        kryo.register(ClassificationMarkingElement.class);

        String serializedPath = "./tmp";

        KMeansDetectionModel kMeansDetectionModel = null;

        try {
            Input input = new Input(new FileInputStream(serializedPath));
            kMeansDetectionModel = (KMeansDetectionModel) kryo.readClassAndObject(input);
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        KmeansValidationSummary kmeansValidationSummary = (KmeansValidationSummary)
                machineLearningManager.validateAthenaFeatures(featureConstraint,
                        athenaMLFeatureConfiguration,
                        kMeansDetectionModel,
                        indexing, marking);
        kmeansValidationSummary.printResults();


    }


    public void saveMoelTestsComplete() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

//        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL, new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

//        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
//                FeatureConstraintOperatorType.COMPARABLE,
//                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
//                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
//                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
//        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
//        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));
        athenaMLFeatureConfiguration.setNormalization(true);

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(k);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(3);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x0000000f, 0x6);
        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));


        KMeansDetectionModel kMeansDetectionModel = (KMeansDetectionModel)
                machineLearningManager.generateAthenaDetectionModel(featureConstraint,
                        athenaMLFeatureConfiguration,
                        kMeansDetectionAlgorithm,
                        indexing,
                        marking);
        kMeansDetectionModel.getSummary().printSummary();

        machineLearningManager.saveDetectionModel(kMeansDetectionModel, null);
    }

    public void saveMoelTests() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));
        athenaMLFeatureConfiguration.setNormalization(true);

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(k);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(3);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x0000000f, 0x6);
        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));


        KMeansDetectionModel kMeansDetectionModel = (KMeansDetectionModel)
                machineLearningManager.generateAthenaDetectionModel(featureConstraint,
                        athenaMLFeatureConfiguration,
                        kMeansDetectionAlgorithm,
                        indexing,
                        marking);
        kMeansDetectionModel.getSummary().printSummary();

        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new SerializingInstantiatorStrategy());
        kryo.register(KMeansDetectionModel.class);
        kryo.register(AthenaMLFeatureConfiguration.class);
        kryo.register(DetectionModel.class);
        kryo.register(FeatureConstraint.class);
        kryo.register(AthenaFeatureField.class);
        kryo.register(AthenaIndexField.class);
        kryo.register(AthenaField.class);
        kryo.register(FeatureConstraintOperator.class);
        kryo.register(FeatureConstraintOperatorType.class);
        kryo.register(FeatureConstraintType.class);
        kryo.register(AthenaMLFeatureConfiguration.class);
        kryo.register(Marking.class);
        kryo.register(FeatureDatabaseMgmtManager.class);
        kryo.register(Indexing.class);
        kryo.register(ClusterModelSummary.class);
        kryo.register(KMeansDetectionAlgorithm.class);
        kryo.register(KmeansModelSummary.class);
        kryo.register(Accumulator.class);
        kryo.register(ClusterModelSummary.UniqueFlowAccumulatorParam.class);
        kryo.register(ClusterModelSummary.LongAccumulatorParam.class);
        kryo.register(ClassificationMarkingElement.class);
//        kryo.register();
//        kryo.register();
//        kryo.register();

        String serializedPath = "./tmp";
        try {
            Output output = new Output(new FileOutputStream(serializedPath));
            kryo.writeClassAndObject(output, kMeansDetectionModel);

            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testLinearRegression() {
        int options = 2;

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP("192.168.1.121:27017");

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("192.168.1.124:7077");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        athenaMLFeatureConfiguration.setNormalization(true);
        Marking marking = new Marking();
        marking.setSrcLabeledMarking("malicious", 0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));


        if (options == 0) {
            //LinearRegression
            LinearRegressionDetectionAlgorithm algorithm = new LinearRegressionDetectionAlgorithm();
            algorithm.setNumIterations(100);
            algorithm.setStepSize(1);
            algorithm.setMiniBatchFraction(1);
            LinearRegressionDetectionModel model =
                    (LinearRegressionDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                            featureConstraint,
                            athenaMLFeatureConfiguration,
                            algorithm,
                            indexing,
                            marking);

            LinearRegressionValidationSummary summary =
                    (LinearRegressionValidationSummary) machineLearningManager.validateAthenaFeatures(
                            featureConstraint,
                            athenaMLFeatureConfiguration,
                            model,
                            indexing,
                            marking);

            summary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
            model.getSummary().printSummary();

        } else if (options == 1) {
            //LassoRegression
            LassoDetectionAlgorithm algorithm = new LassoDetectionAlgorithm();
            algorithm.setNumIterations(100);
            algorithm.setStepSize(1);
            algorithm.setMiniBatchFraction(1);

            LassoDetectionModel model =
                    (LassoDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                            featureConstraint,
                            athenaMLFeatureConfiguration,
                            algorithm,
                            indexing,
                            marking);

            LassoValidationSummary summary =
                    (LassoValidationSummary) machineLearningManager.validateAthenaFeatures(
                            featureConstraint,
                            athenaMLFeatureConfiguration,
                            model,
                            indexing,
                            marking);

            summary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
            model.getSummary().printSummary();
        } else {
            //RidgeRegression
            RidgeRegressionDetectionAlgorithm algorithm = new RidgeRegressionDetectionAlgorithm();
            algorithm.setNumIterations(100);
            algorithm.setStepSize(1);
            algorithm.setMiniBatchFraction(1);

            RidgeRegressionDetectionModel model =
                    (RidgeRegressionDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                            featureConstraint,
                            athenaMLFeatureConfiguration,
                            algorithm,
                            indexing,
                            marking);

            RidgeRegressionValidationSummary summary =
                    (RidgeRegressionValidationSummary) machineLearningManager.validateAthenaFeatures(
                            featureConstraint,
                            athenaMLFeatureConfiguration,
                            model,
                            indexing,
                            marking);

            summary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
            model.getSummary().printSummary();
        }


//        marking.setSrcMaskMarking(0x0000000f, 0x6);
//        marking.setDstMaskMarking(0x0000000f, 0x6);
    }


    public void testLR() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL, new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX, FeatureConstraintOperatorType.COMPARABLE, new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT), new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC), new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX, FeatureConstraintOperatorType.COMPARABLE, new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT), new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO), new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW), 1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        athenaMLFeatureConfiguration.setNormalization(true);
        Marking marking = new Marking();
        marking.setSrcLabeledMarking("malicious", 0x0000000f, 0x6);

        LogisticRegressionDetectionAlgorithm logisticRegressionDetectionAlgorithm = new LogisticRegressionDetectionAlgorithm();
        logisticRegressionDetectionAlgorithm.setNumClasses(marking.numberOfMarkingElements());


//        marking.setSrcMaskMarking(0x0000000f, 0x6);
//        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        LogisticRegressionDetectionModel model = (LogisticRegressionDetectionModel) machineLearningManager.generateAthenaDetectionModel(featureConstraint, athenaMLFeatureConfiguration, logisticRegressionDetectionAlgorithm, indexing, marking);

        LogisticRegressionValidationSummary summary = (LogisticRegressionValidationSummary) machineLearningManager.validateAthenaFeatures(featureConstraint, athenaMLFeatureConfiguration, model, indexing, marking);


        summary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
        model.getSummary().printSummary();

    }

    public void testSVMs() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP("192.168.1.121:27017");

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("192.168.1.124:7077");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        athenaMLFeatureConfiguration.setNormalization(true);
        Marking marking = new Marking();
        marking.setSrcLabeledMarking("malicious", 0x0000000f, 0x6);

        SVMDetectionAlgorithm svmDetectionAlgorithm = new SVMDetectionAlgorithm();
        svmDetectionAlgorithm.setNumIterations(100);


//        marking.setSrcMaskMarking(0x0000000f, 0x6);
//        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        SVMDetectionModel svmDetectionModel =
                (SVMDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        svmDetectionAlgorithm,
                        indexing,
                        marking);

        SVMValidationSummary svmValidationSummary =
                (SVMValidationSummary) machineLearningManager.validateAthenaFeatures(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        svmDetectionModel,
                        indexing,
                        marking);


        svmValidationSummary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
        svmDetectionModel.getSummary().printSummary();

    }

    public void testGBTs() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP("192.168.1.121:27017");

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("192.168.1.124:7077");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        athenaMLFeatureConfiguration.setNormalization(true);
        Marking marking = new Marking();
        marking.setSrcLabeledMarking("malicious", 0x0000000f, 0x6);


        GradientBoostedTreesDetectionAlgorithm gradientBoostedTreesDetectionAlgorithm = new GradientBoostedTreesDetectionAlgorithm();
        gradientBoostedTreesDetectionAlgorithm.setNumClasses(marking.numberOfMarkingElements());
        gradientBoostedTreesDetectionAlgorithm.setMaxDepth(5);
        gradientBoostedTreesDetectionAlgorithm.setIterations(3);
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        gradientBoostedTreesDetectionAlgorithm.setCategoricalFeaturesInfo(categoricalFeaturesInfo);

//        marking.setSrcMaskMarking(0x0000000f, 0x6);
//        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        GradientBoostedTreesDetectionModel gradientBoostedTreesDetectionModel =
                (GradientBoostedTreesDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        gradientBoostedTreesDetectionAlgorithm,
                        indexing,
                        marking);

        GradientBoostedTreesValidationSummary gradientBoostedTreesValidationSummary =
                (GradientBoostedTreesValidationSummary) machineLearningManager.validateAthenaFeatures(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        gradientBoostedTreesDetectionModel,
                        indexing,
                        marking);


        gradientBoostedTreesValidationSummary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
        gradientBoostedTreesDetectionModel.getSummary().printSummary();

    }

    public void tesetRandomForest() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP("192.168.1.121:27017");

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("192.168.1.124:7077");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        athenaMLFeatureConfiguration.setNormalization(true);
        Marking marking = new Marking();
        marking.setSrcLabeledMarking("malicious", 0x0000000f, 0x6);

        RandomForestDetectionAlgorithm randomForestDetectionAlgorithm = new RandomForestDetectionAlgorithm();
        randomForestDetectionAlgorithm.setNumClasses(marking.numberOfMarkingElements());
        randomForestDetectionAlgorithm.setNumClasses(2);
        randomForestDetectionAlgorithm.setFeatureSubsetStrategy(RandomForestDetectionAlgorithm.STRATEGY_AUTO);
        randomForestDetectionAlgorithm.setImpurity(RandomForestDetectionAlgorithm.IMPURITY_GINI);
        randomForestDetectionAlgorithm.setMaxDepth(5);
        randomForestDetectionAlgorithm.setMaxBins(32);
        randomForestDetectionAlgorithm.setSeed(12345);


//        marking.setSrcMaskMarking(0x0000000f, 0x6);
//        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        RandomForestDetectionModel randomForestDetectionModel =
                (RandomForestDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        randomForestDetectionAlgorithm,
                        indexing,
                        marking);

        RandomForestValidationSummary randomForestValidationSummary =
                (RandomForestValidationSummary) machineLearningManager.validateAthenaFeatures(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        randomForestDetectionModel,
                        indexing,
                        marking);


        randomForestValidationSummary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
        randomForestDetectionModel.getSummary().printSummary();

    }

    public void testNaiveBayes() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP("192.168.1.121:27017");

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("192.168.1.124:7077");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        athenaMLFeatureConfiguration.setNormalization(true);
        /////////////////////////////Naive bayes Must set this option!!!!!!!!!!!!!!!!!!!!!!!
        athenaMLFeatureConfiguration.setAbsolute(true);

        Marking marking = new Marking();
        marking.setSrcLabeledMarking("malicious", 0x0000000f, 0x6);

        NaiveBayesDetectionAlgorithm naiveBayesDetectionAlgorithm = new NaiveBayesDetectionAlgorithm();
        naiveBayesDetectionAlgorithm.setNumClasses(marking.numberOfMarkingElements());


//        marking.setSrcMaskMarking(0x0000000f, 0x6);
//        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        NaiveBayesDetectionModel naiveBayesDetectionModel =
                (NaiveBayesDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        naiveBayesDetectionAlgorithm,
                        indexing,
                        marking);

        NaiveBayesValidationSummary naiveBayesValidationSummary =
                (NaiveBayesValidationSummary) machineLearningManager.validateAthenaFeatures(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        naiveBayesDetectionModel,
                        indexing,
                        marking);


        naiveBayesValidationSummary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
        naiveBayesDetectionModel.getSummary().printSummary();

    }

    public void tesetDecisionTree() {

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));

        athenaMLFeatureConfiguration.setNormalization(true);
        Marking marking = new Marking();
        marking.setSrcLabeledMarking("malicious", 0x0000000f, 0x6);

        DecisionTreeDetectionAlgorithm decisionTreeDetectionAlgorithm = new DecisionTreeDetectionAlgorithm();
        decisionTreeDetectionAlgorithm.setNumClasses(marking.numberOfMarkingElements());


//        marking.setSrcMaskMarking(0x0000000f, 0x6);
//        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        DecisionTreeDetectionModel decisionTreeDetectionModel =
                (DecisionTreeDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        decisionTreeDetectionAlgorithm,
                        indexing,
                        marking);

        DecisionTreeValidationSummary decisionTreeValidationSummary =
                (DecisionTreeValidationSummary) machineLearningManager.validateAthenaFeatures(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        decisionTreeDetectionModel,
                        indexing,
                        marking);


        decisionTreeValidationSummary.printResults();
//        decisionTreeDetectionModel.getClassificationModelSummary().printSummary();
        decisionTreeDetectionModel.getSummary().printSummary();

    }

    public void tesetGaussian() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP("192.168.1.121:27017");

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP("192.168.1.124:7077");
        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));
        athenaMLFeatureConfiguration.setNormalization(true);

        GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm = new GaussianMixtureDetectionAlgorithm();

        gaussianMixtureDetectionAlgorithm.setK(k);
        gaussianMixtureDetectionAlgorithm.setMaxIterations(iter);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x0000000f, 0x6);
        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        GaussianMixtureDetectionModel gaussianMixtureDetectionModel = (GaussianMixtureDetectionModel) machineLearningManager.generateAthenaDetectionModel(
                featureConstraint,
                athenaMLFeatureConfiguration,
                gaussianMixtureDetectionAlgorithm,
                indexing,
                marking);


        GaussianMixtureValidationSummary kmeansClusterValidationSummary =
                (GaussianMixtureValidationSummary) machineLearningManager.validateAthenaFeatures(
                        featureConstraint,
                        athenaMLFeatureConfiguration,
                        gaussianMixtureDetectionModel,
                        indexing,
                        marking);
        kmeansClusterValidationSummary.printResults();
        gaussianMixtureDetectionModel.getSummary().printSummary();

    }

    public void testkMenas() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setDATABASE_IP(MONGO_DB_IP);

        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setComputingClusterMasterIP(SPARK_MASTER);

        machineLearningManager.setArtifactId("athena-spark-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));


        featureConstraint.setLocation(AthenaFeatureField.FLOW_STATS);
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.setNormalization(true);
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW),
                1000);
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION));
        athenaMLFeatureConfiguration.setNormalization(true);

        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(k);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(3);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x0000000f, 0x6);
        marking.setDstMaskMarking(0x0000000f, 0x6);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));


        KMeansDetectionModel kMeansDetectionModel = (KMeansDetectionModel) machineLearningManager.generateAthenaDetectionModel(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionAlgorithm, indexing, marking);


        KmeansValidationSummary kmeansValidationSummary = (KmeansValidationSummary) machineLearningManager.validateAthenaFeatures(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionModel, indexing, marking);
        kmeansValidationSummary.printResults();
        kMeansDetectionModel.getSummary().printSummary();

    }

    public void testOnlineDelivery() {
        //port
//        FeatureConstraint flow =
//                new FeatureConstraint(FeatureConstraintType.FEATURE,
//                        FeatureConstraintOperatorType.COMPARABLE,
//                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
//                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT)
//                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("1")));
        FeatureConstraint dataRequestobject =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));
        FeatureConstraint packetIn1 =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaFeatureField(AthenaFeatureField.PACKET_IN_REASON)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("1")));
        FeatureConstraint packetIn2 =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaFeatureField(AthenaFeatureField.PACKET_IN_TOTAL_LEN)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("98")));
        FeatureConstraint packetIn3 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_IN_PORT)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("1")));
        FeatureConstraint packetIn4 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_ETH_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("1")));
        FeatureConstraint packetIn5 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_ETH_DST)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("2")));
        FeatureConstraint packetIn6 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_ETH_TYPE)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("2048")));
        FeatureConstraint packetIn7 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("167772161")));
        FeatureConstraint packetIn8 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("167772162")));
        FeatureConstraint packetIn9 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("1")));
        FeatureConstraint packetIn10 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_ICMPV4_TYPE)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("8")));
        FeatureConstraint packetIn11 =
                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_ICMPV4_CODE)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn1));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn2));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn3));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn4));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn5));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn6));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn7));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn8));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn9));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn10));

//        FeatureConstraint packetIn =
//                new FeatureConstraint(FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH,
//                        FeatureConstraintOperatorType.COMPARABLE,
//                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
//                        new AthenaFeatureField(AthenaIndexField.MATCH_IPV4_SRC)
//                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("167772162")));

        System.out.println("Initialize EventDeliveryManager !!");
        EventDeliveryManagerImpl eventDeliveryManager = new EventDeliveryManagerImpl(new InternalAthenaFeatureEventListener());

        System.out.println("Register Online Athena Feature");
//        eventDeliveryManager.registerOnlineAthenaFeature(null, new QueryIdentifier((short) 1), packetIn);
        eventDeliveryManager.registerOnlineAthenaFeature(null, new QueryIdentifier((short) 1), dataRequestobject);
    }

    public class InternalAthenaFeatureEventListener implements AthenaFeatureEventListener {

        private long count = 0;
        private float sum = 0;
        FileWriter fw;

        public InternalAthenaFeatureEventListener() {
        }

        @Override
        public void getRequestedFeatures(ApplicationId applicationId, AthenaFeatures athenaFeatures) {

        }

        @Override
        public void getFeatureEvent(ApplicationId applicationId, QueryIdentifier id, HashMap<String, Object> event) {

            //Effectiveness measurement
            try {
                //---- for nano seconds measurement
                try {
                    fw = new FileWriter("result/eval2/2_10.csv", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long startTime = (long) event.get("nanotime");
                long endTime = System.nanoTime();
                long elapsedTime = endTime - startTime;

                System.out.println(event.toString());
                sum += elapsedTime;
                count++;
                System.out.println("[" + count + "]" + "diffTime : " + elapsedTime + " ns");
                if (count <= 10000) {
                    fw.write(elapsedTime + ",");
                }
                if (count % 1000 == 0) {
                    System.out.println("[Average Delivery Time for 1000 entries : " + sum / 10.0f);
                    sum = 0;

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class InternalonlineMLEventListener implements onlineMLEventListener {
        Map<String, String> set = new HashMap<>();
        ControllerConnector controllerConnector = null;
        InetAddress localIP = null;

        List<List<Integer>> clusterHistory = new ArrayList<>();

        public InternalonlineMLEventListener() {
            generateMapping();
        }

        public InternalonlineMLEventListener(ControllerConnector controllerConnector) {
            this.controllerConnector = controllerConnector;
            generateMapping();
            getLocalIP();
            initHistory();
        }

        public void initHistory() {

            for (int i = 0; i < 25; i++) {
                List<Integer> host = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    Integer val = 0;
                    host.add(j, val);
                }
                clusterHistory.add(i, host);
            }
        }

        public void getLocalIP() {

            NetworkInterface ni = null;
            try {
                ni = NetworkInterface.getByName("eth0");
            } catch (SocketException e) {
                e.printStackTrace();
            }
            Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();

            while (inetAddresses.hasMoreElements())

            {
                InetAddress ia = inetAddresses.nextElement();
                if (!ia.isLinkLocalAddress()) {
                    localIP = ia;
                    break;
                }
            }
        }


        public void generateMapping() {
            for (int i = 0; i < 25; i++) {
                String src = "10.0.0.";
                String deviceId = "of:00000000000000";

                if (i < 16) {
                    String value = String.format("%x", i);
                    src = src + String.valueOf(i);
                    deviceId = deviceId + "0" + value;
                } else {
                    String value = String.format("%x", i);
                    src = src + String.valueOf(i);
                    deviceId = deviceId + value;
                }
                set.put(src, deviceId);
                System.out.println(src + "-" + deviceId);
            }
        }

        public void issueBlock(String sipAddr, String dipAddr) {
            TargetAthenaValue IP_SRC = new TargetAthenaValue(AthenaValueGenerator.parseIPv4ToAthenaValue(sipAddr));
            TargetAthenaValue IP_DST = new TargetAthenaValue(AthenaValueGenerator.parseIPv4ToAthenaValue(dipAddr));
            FlowRuleActionType actionType = FlowRuleActionType.BLOCK;
            FlowRuleManagerImpl flowRuleManager = new FlowRuleManagerImpl(controllerConnector);
            String deviceUri = set.get(sipAddr);

            flowRuleManager.issueFlowRule(null, IP_SRC, IP_DST, deviceUri, actionType);
        }

        @Override
        public void getValidationResultOnlineResult(HashMap<String, Object> event, double result) {
            Integer sip = (Integer) event.get(AthenaIndexField.MATCH_IPV4_SRC);
            Integer dip = (Integer) event.get(AthenaIndexField.MATCH_IPV4_DST);
            byte[] sbytes = BigInteger.valueOf(sip).toByteArray();
            byte[] dbytes = BigInteger.valueOf(dip).toByteArray();
            InetAddress saddress = null;
            InetAddress daddress = null;
            try {
                saddress = InetAddress.getByAddress(sbytes);
                daddress = InetAddress.getByAddress(dbytes);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            String[] array;
            array = saddress.getHostAddress().split("\\.");

            int endHost = Integer.parseInt(array[3]);
            List<Integer> hostValue = clusterHistory.get(endHost);
            Integer hostTargetValue = hostValue.get((int) result);
            hostTargetValue += 1;
            hostValue.set((int) result, hostTargetValue);
            clusterHistory.set(endHost, hostValue);

            String outputs = "[Instance in " + localIP.getHostAddress() + "]" + "sIP=" + saddress.getHostAddress() + ";dIP=" + daddress.getCanonicalHostName() + ";loc=" + set.get(saddress.getHostAddress()) + ";"
                    + "detection results = " + result;

            System.out.print("host." + endHost + "=>");
            for (int i = 0; i < 10; i++) {
                System.out.print(i + "(" + hostValue.get(i) + ")" + " ");
                if (i == 0 || i == 1 || i == 3) {
                    if (hostValue.get(i) > 100) {
                        System.out.println("Host " +saddress.getHostAddress() + " has been blocked (Malicious host!)");
                        issueBlock(saddress.getHostAddress(), daddress.getHostAddress());
                    }
                }
            }
            System.out.print("\n");

            log.info(outputs);
            System.out.println(outputs);
            //Detection results in here
        }
    }
}
