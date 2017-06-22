package athena.user.application;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.clustering.kmeans.KMeansDetectionAlgorithm;
import athena.api.clustering.kmeans.KMeansDetectionModel;
import athena.northbound.DataQueryManager;
import athena.northbound.impl.DataQueryManagerImpl;
import athena.northbound.impl.EventDeliveryManagerImpl;
import athena.northbound.impl.MachineLearningManagerImpl;
import athena.util.DatabaseConnector;
import org.onosproject.athena.database.*;
import org.onosproject.athena.database.impl.FeatureDatabaseMgmtManager;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.DefaultApplicationId;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by jinwookim on 2017. 6. 7..
 */
public class Sample {

    public void requestFeatures() {

        // connect to DB cluster and initialize data query manager
        DatabaseConnector dc = new DatabaseConnector();
        dc.setDATABASE_IP(System.getenv("MD1"));
        DataQueryManager dqm = new DataQueryManagerImpl(dc);

        // make user-defined feature constraints
        FeatureConstraint and = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint flowStatsPacketCount = new FeatureConstraint(FeatureConstraintType.FEATURE,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("10")));

        FeatureConstraint flowStatsDuration =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC),
                        new TargetAthenaValue(1));

        and.appenValue(new TargetAthenaValue(flowStatsPacketCount));
        and.appenValue(new TargetAthenaValue(flowStatsDuration));

        AdvancedFeatureConstraint afc = new AdvancedFeatureConstraint();
        afc.addAdvancedOptions(AdvancedFeatureConstraintType.SORTING, new AdvancedFeatureConstraintValue(AthenaFeatureField.FLOW_STATS_DURATION_SEC));

        AthenaFeatures af = dqm.requestAthenaFeatures(and, afc);

        AthenaFeatureRequester afr = new AthenaFeatureRequester(AthenaFeatureRequestrType.REQUEST_FEATURES,
                and, afc);
        dqm.displayFeatures(afr, af);
    }

    public void generateModel() {

        int k = 5;
        int iter = 20;

        // Initialize DB and Computing cluster
        DatabaseConnector databaseConnector = new DatabaseConnector();
        MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
        machineLearningManager.setMainClass("athena.user.application.Main");
        machineLearningManager.setArtifactId("athena-tester-1.6.0");
        machineLearningManager.setDatabaseConnector(databaseConnector);

        // Set custom feature constraints
        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));
        FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                new TargetAthenaValue(AthenaValueGenerator.parseIPv4ToAthenaValue("10.0.0.1")));
        FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX,
                FeatureConstraintOperatorType.COMPARABLE,
                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("6")));

        featureConstraint.setLocation("model");
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint2));
        featureConstraint.appenValue(new TargetAthenaValue(featureConstraint3));

        // Set parameters for data pre-processing
        AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
        athenaMLFeatureConfiguration.setNormalization(true);
        athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO), 1000);

        // Specify which fields are used for ML features
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
        athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));

        // Set parameters for ML algorithms
        KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
        kMeansDetectionAlgorithm.setK(8);
        kMeansDetectionAlgorithm.setMaxIterations(iter);
        kMeansDetectionAlgorithm.setRuns(10);

        Marking marking = new Marking();
        marking.setSrcMaskMarking(0x000000ff, 0x00000065);

        Indexing indexing = new Indexing();
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
        indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));

        KMeansDetectionModel kMeansDetectionModel = (KMeansDetectionModel) machineLearningManager.generateAthenaDetectionModel(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionAlgorithm, indexing, marking);
        machineLearningManager.saveDetectionModel(kMeansDetectionModel, ".");
        kMeansDetectionModel.getSummary().printSummary();
    }

    public void testOnlineDelivery() {

        FeatureConstraint dataRequestobject =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint packetIn1 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_IN_PORT)
                        , new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("1")));
        FeatureConstraint packetIn2 =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC)
                        , new TargetAthenaValue(AthenaValueGenerator.parseIPv4ToAthenaValue("10.0.0.1")));

        FeatureConstraint featureConstraint =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
                        new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

//        FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintType.INDEX,
//                FeatureConstraintOperatorType.COMPARABLE,
//                new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
//                new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
//                new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("0")));

        dataRequestobject.appenValue(new TargetAthenaValue(packetIn1));
        dataRequestobject.appenValue(new TargetAthenaValue(packetIn2));

        System.out.println("Initialize EventDeliveryManager !!");
        EventDeliveryManagerImpl eventDeliveryManager = new EventDeliveryManagerImpl(new InternalAthenaFeatureEventListener());

        System.out.println("Register Online Athena Feature");
        eventDeliveryManager.registerOnlineAthenaFeature(new DefaultApplicationId(99, "online"), new QueryIdentifier((short) 1), packetIn2);
    }

    public class InternalAthenaFeatureEventListener implements AthenaFeatureEventListener {

        public InternalAthenaFeatureEventListener() {
        }

        @Override
        public void getRequestedFeatures(ApplicationId applicationId, AthenaFeatures athenaFeatures) {
        }

        @Override
        public void getFeatureEvent(ApplicationId applicationId, QueryIdentifier id, HashMap<String, Object> event) {
            System.out.println(event.toString());
        }
    }
}
