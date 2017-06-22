package org.onosproject.athena.database.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AdvancedFeatureConstraintType;
import org.onosproject.athena.database.AdvancedFeatureConstraintValue;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.AthenaFeatureRequestrType;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.AthenaValueGenerator;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.FeatureDatabaseProvider;
import org.onosproject.athena.database.FeatureDatabaseProviderService;
import org.onosproject.athena.database.FeatureDatabaseService;
import org.onosproject.athena.database.OnlineEventTable;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.athena.feature.ErrorMessageFeature;
import org.onosproject.athena.feature.Feature;
import org.onosproject.athena.feature.FeatureCategory;
import org.onosproject.athena.feature.FeatureIndex;
import org.onosproject.athena.feature.FeatureType;
import org.onosproject.athena.feature.FlowRemovedFeature;
import org.onosproject.athena.feature.PacketInFeature;
import org.onosproject.athena.feature.PortStatisticsFeature;
import org.onosproject.athena.feature.PortStatusFeature;
import org.onosproject.athena.feature.QueueStatisticsFeature;
import org.onosproject.athena.feature.TableStatisticsFeature;
import org.onosproject.athena.feature.UnitAggregateStatistics;
import org.onosproject.athena.feature.UnitErrorMessageInformation;
import org.onosproject.athena.feature.UnitFlowRemovedInformation;
import org.onosproject.athena.feature.UnitFlowStatistics;
import org.onosproject.athena.feature.UnitPacketInInformation;
import org.onosproject.athena.feature.UnitPortStatistics;
import org.onosproject.athena.feature.UnitPortStatusInformation;
import org.onosproject.athena.feature.UnitQueueStatistics;
import org.onosproject.athena.feature.UnitTableStatistics;
import org.onosproject.core.ApplicationId;
import org.onosproject.athena.database.FeatureDatabaseProviderRegistry;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.athena.feature.AggregateStatisticsFeature;
import org.onosproject.athena.feature.FlowStatisticsFeature;
import org.onosproject.net.provider.AbstractProviderRegistry;
import org.onosproject.net.provider.AbstractProviderService;
import org.slf4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static org.slf4j.LoggerFactory.getLogger;


import org.bson.Document;

/**
 * Created by seunghyeon on 8/27/15.
 */
@Component(immediate = true)
@Service
public class FeatureDatabaseManager
        extends AbstractProviderRegistry<FeatureDatabaseProvider, FeatureDatabaseProviderService>
        implements FeatureDatabaseService, FeatureDatabaseProviderRegistry {

    private final Logger log = getLogger(getClass());
    protected Multimap<Integer, AthenaFeatureEventListener> databaseEventListener = ArrayListMultimap.create();

    private List<OnlineEventTable> onlineEventTableList = new ArrayList<>();
    private FeatureDatabaseMgmtManager featureDatabaseMgmtManager = new FeatureDatabaseMgmtManager();

    AthenaIndexField athenaIndexField = new AthenaIndexField();
    AthenaFeatureField athenaFeatureField = new AthenaFeatureField();


    //Performance tunning will be moved at the top of code/////////////////////////////////
    private final ExecutorService burstEventExecutor = Executors.newFixedThreadPool(1);

//            Executors.newFixedThreadPool(6, groupedThreads("onos/of", "event-framework-burst-%d", log));

    List<Document> activeGroup = new ArrayList<>();

    public class BatchFeatureHandler extends TimerTask {

        @Override
        public void run() {
            //handle packet_IN groups
            List<Document> stored;
            synchronized (activeGroup) {
                stored = activeGroup;
                activeGroup = new ArrayList<>();
                log.info("The data will be stored (DB bottleneck): " + stored.size());
            }

            if (stored.size() > 1) {
                burstEventExecutor.execute(new StoreFeatureHandler(athenaFeatureField.PACKET_IN, stored));
            }
        }
    }

    protected final class StoreFeatureHandler implements Runnable {

        protected List<Document> elements;
        String target;

        public StoreFeatureHandler(String target, List<Document> elements) {
            this.target = target;
            this.elements = elements;
        }

        @Override
        public void run() {
                featureDatabaseMgmtManager
                        .getDbCollectionList()
                        .get(target).insertMany(elements);
        }

    }

    int batchProecssTime = 20;

    void registerbatchHandler(int seconds) {
        BatchFeatureHandler batchFeatureHandler = new BatchFeatureHandler();

        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        timer.scheduleAtFixedRate(batchFeatureHandler, date.getTime(), 1000 * seconds);
    }
    ////////////////////////////////////////////////////////////////////////////////////


    @Activate
    public void activate() {
        log.info("started");
        //TODO: database IP address must be configured.
        if (System.getenv("MD1").isEmpty()) {
            System.err.println("Please set environment variables for DB cluster");
            System.exit(1);
        }
        featureDatabaseMgmtManager.connectToDatabase(System.getenv("MD1")); // config file

        //Register batch handler
        registerbatchHandler(batchProecssTime);
    }

    @Deactivate
    public void deactivate() {
        log.info("Stopped");
    }

    @Override
    public void debugCommand1(String param) {

        FeatureConstraint dataRequestobject =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint flowStatsDuration =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_N_SEC),
                        new TargetAthenaValue(1));
        FeatureConstraint flowstatsByteCount =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT),
                        new TargetAthenaValue(5));
        FeatureConstraint timestamp =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.TIMESTAMP),
                        new TargetAthenaValue(AthenaValueGenerator.parseDataToAthenaValue("2016-01-03-11:45")));
        dataRequestobject.appenValue(new TargetAthenaValue(flowStatsDuration));
        dataRequestobject.appenValue(new TargetAthenaValue(flowstatsByteCount));
//        dataRequestobject.appenValue(timestamp);

        //add options
        AdvancedFeatureConstraint advancedFeatureConstraint = new AdvancedFeatureConstraint();
        advancedFeatureConstraint.addAdvancedOptions(AdvancedFeatureConstraintType.LIMIT_FEATURE_COUNTS,
                new AdvancedFeatureConstraintValue("40"));
        advancedFeatureConstraint.addAdvancedOptions(AdvancedFeatureConstraintType.AGGREGATE,
                new AdvancedFeatureConstraintValue(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));

        AthenaFeatureRequester athenaFeatureRequester = new AthenaFeatureRequester(
                AthenaFeatureRequestrType.REQUEST_FEATURES,
                dataRequestobject, advancedFeatureConstraint, null);

        requestFeatures(null, athenaFeatureRequester);
    }


    @Override
    public void debugCommand2(String param) {

        FeatureConstraint dataRequestobject =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint flowStatsDuration =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_N_SEC),
                        new TargetAthenaValue(1));
        FeatureConstraint flowstatsByteCount =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT),
                        new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("5")));
        FeatureConstraint timestamp =
                new FeatureConstraint(FeatureConstraintType.INDEX,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaIndexField(AthenaIndexField.TIMESTAMP),
                        new TargetAthenaValue(AthenaValueGenerator.parseDataToAthenaValue("2016-01-03-11:45")));
        dataRequestobject.appenValue(new TargetAthenaValue(flowStatsDuration));
        dataRequestobject.appenValue(new TargetAthenaValue(flowstatsByteCount));
//        dataRequestobject.appenValue(timestamp);
        AthenaFeatureRequester athenaFeatureRequester = new AthenaFeatureRequester(
                AthenaFeatureRequestrType.REQUEST_FEATURES,
                dataRequestobject, null, null);

        requestFeatures(null, athenaFeatureRequester);
    }

    @Override
    public void debugCommand3(String param) {
        Date date = new Date();
        String ip = "192.168.0.1";
        Integer addr;


        addr = AthenaValueGenerator.parseIPv4ToAthenaValue("10.0.0.1");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        String dateInString = "2016-01-01 19:02:59";

        try {
            date = simpleDateFormat.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("value 2 {}:", featureDatabaseMgmtManager.getValueFromrequestedObject(date));

        log.info("Verify: {}", simpleDateFormat.format(date));

    }

    @Override
    protected FeatureDatabaseProviderService createProviderService(FeatureDatabaseProvider provider) {
        return new InternalFeatureDatabaseProviderService(provider);
    }

    @Override
    public void addDatabaseEventListener(int prioirity, AthenaFeatureEventListener listener) {
        databaseEventListener.put(prioirity, listener);
    }

    @Override
    public void removeDatabaseEventListener(AthenaFeatureEventListener listener) {
        databaseEventListener.values().remove(listener);
    }

    @Override
    public boolean registerOnlineFeature(ApplicationId applicationId, QueryIdentifier identifier,
                                         AthenaFeatureRequester athenaFeatureRequester) {

        if (applicationId == null) {
            log.warn("applicationId could not be null");
            return false;
        }

        if (identifier == null) {
            log.warn("identifier could not be null");
            return false;
        }

        if (athenaFeatureRequester == null) {
            log.warn("athenaFeatureRequester could not be null");
            return false;
        }

        if (athenaFeatureRequester.getAthenaFeatureRequestrType() !=
                AthenaFeatureRequestrType.REGISTER_ONLINE_HANDLER) {
            log.warn("dataRequesterType must be REGISTER_ONLINE_HANDLER");
            return false;
        }

        OnlineEventTable onlineEventTable = new OnlineEventTable(applicationId, identifier, athenaFeatureRequester);
        if (!onlineEventTableList.contains(onlineEventTable)) {
            onlineEventTableList.add(onlineEventTable);
        }


        return true;
    }

    @Override
    public boolean unRegisterOnlineFeature(ApplicationId applicationId, QueryIdentifier identifier) {
        OnlineEventTable onlineEventTable;

        for (int i = 0; i < onlineEventTableList.size(); i++) {
            onlineEventTable = onlineEventTableList.get(i);

            if (onlineEventTable.getApplicationId() == applicationId
                    && onlineEventTable.getQueryIdentifier() == identifier) {
                onlineEventTableList.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object kyroSerializerInitialize() {
        return null;
    }


    @Override
    public void requestFeatures(ApplicationId applicationId, AthenaFeatureRequester athenaFeatureRequester) {
        AthenaFeatures athenaFeatures = featureDatabaseMgmtManager.requestDataToMongoDB(athenaFeatureRequester);

        featureDatabaseMgmtManager.printAllDBFeatures(athenaFeatureRequester, athenaFeatures);

        for (AthenaFeatureEventListener d : databaseEventListener.values()) {
            //Online handler
            d.getRequestedFeatures(applicationId, athenaFeatures);
        }
    }


    //not move
    public void checkOnlineEventSatisfy(Document onlineFeature) {
        OnlineEventTable onlineEventTable;
        FeatureConstraint featureConstraint;
        AthenaFeatureRequester athenaFeatureRequester;
        Document innerFeature = null;
        //Document innerFeature = (Document) onlineFeature.get(AthenaFeatureField.FEATURE);

        for (int i = 0; i < onlineEventTableList.size(); i++) {
            onlineEventTable = onlineEventTableList.get(i);

//            // added PIN_PAYLOAD_MATCH -- Jinwoo Kim, 2016/08/12
            featureConstraint = onlineEventTable.getAthenaFeatureRequester().getFeatureConstraint();
//
//            if (featureConstraint.getFeatureConstraintType() == FeatureConstraintType.FEATURE) {
//                innerFeature = (Document) onlineFeature.get(AthenaFeatureField.FEATURE);
//            } else if (featureConstraint.getFeatureConstraintType() ==
// FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH) {
//                innerFeature = (Document) onlineFeature.get(AthenaFeatureField.PACKET_IN_PAYLOAD_MATCH);
//            }

            athenaFeatureRequester = onlineEventTable.getAthenaFeatureRequester();

            if (featureDatabaseMgmtManager.isSatisfyOnlineEvent(onlineFeature,
                    null, featureConstraint)) {

                //TODO deliver selective event to users
                // Extract values
                HashMap<String, Object> elements =
                        featureDatabaseMgmtManager.deriveUserDefinedFeatures(onlineFeature, athenaFeatureRequester);

                // notify all (To be optimized later)
                for (AthenaFeatureEventListener d : databaseEventListener.values()) {
                    d.getFeatureEvent(onlineEventTable.getApplicationId(), onlineEventTable.getQueryIdentifier(),
                            elements);
                }

            }
        }
    }


    public void checkOnlineEventSatisfyList(List<Document> onlineFeatureList) {

        for (int i = 0; i < onlineFeatureList.size(); i++) {
            checkOnlineEventSatisfy(onlineFeatureList.get(i));
        }

    }


    private class InternalFeatureDatabaseProviderService extends AbstractProviderService<FeatureDatabaseProvider>
            implements FeatureDatabaseProviderService {

        InternalFeatureDatabaseProviderService(FeatureDatabaseProvider provider) {
            super(provider);
        }

        @Override
        public void featureHandler(FeatureType featureType, FeatureCategory featureCategory, Feature feature) {

            //Document -> Document
            Document storedFeature = null;
            List<Document> listOfStoredFeatures = null;

            // Store features to persistent storage
            switch (featureCategory) {
                case ASYNCHRONOUS_ERROR_MSG:
                    storedFeature = storeErrorMsg(featureType, featureCategory, (ErrorMessageFeature) feature);
                    checkOnlineEventSatisfy(storedFeature);
                    break;

                case ASYNCHRONOUS_FLOW_REMOVED:
                    storedFeature = storeFlowRemoved(featureType, featureCategory, (FlowRemovedFeature) feature);
                    checkOnlineEventSatisfy(storedFeature);
                    break;

                case ASYNCHRONOUS_PACKET_IN:
                    storedFeature = storePacketIn(featureType, featureCategory, (PacketInFeature) feature);
                    checkOnlineEventSatisfy(storedFeature);
                    break;

                case ASYNCHRONOUS_PORT_STATUS:
                    storedFeature = storePortStatus(featureType, featureCategory, (PortStatusFeature) feature);
                    checkOnlineEventSatisfy(storedFeature);
                    break;

                case SYNCHRONOUS_FLOW_STATISTICS:
                    listOfStoredFeatures = storeFlowStats(featureType, featureCategory,
                            (FlowStatisticsFeature) feature);
                    checkOnlineEventSatisfyList(listOfStoredFeatures);
                    break;

                case SYNCHRONOUS_QUEUE_STATISTICS:
                    listOfStoredFeatures = storeQueueStats(featureType, featureCategory,
                            (QueueStatisticsFeature) feature);
                    checkOnlineEventSatisfyList(listOfStoredFeatures);
                    break;

                case SYNCHRONOUS_TABLE_STATISTICS:
                    listOfStoredFeatures = storeTableStats(featureType, featureCategory,
                            (TableStatisticsFeature) feature);
                    checkOnlineEventSatisfyList(listOfStoredFeatures);
                    break;

                case SYNCHRONOUS_AGGREGATE_STATISTICS:
                    listOfStoredFeatures = storeAggregateStats(featureType, featureCategory,
                            (AggregateStatisticsFeature) feature);
                    checkOnlineEventSatisfyList(listOfStoredFeatures);
                    break;

                case SYNCHRONOUS_PORT_STATISTICS:
                    listOfStoredFeatures = storePortStats(featureType, featureCategory,
                            (PortStatisticsFeature) feature);
                    checkOnlineEventSatisfyList(listOfStoredFeatures);
                    break;

                default:
                    break;
            }
        }

    }

    public Document storeErrorMsg(FeatureType featureType, FeatureCategory featureCategory,
                                  ErrorMessageFeature feature) {
        Document unitInsertedDoc = null;
        try {
            FeatureIndex fi = feature.getFeatureindex();
            UnitErrorMessageInformation uemi = feature.getUnitErrorMessageInformation();

            if (fi == null || uemi == null) {
                return null;
            }
            unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

            unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, uemi.getTimestamp())
                    .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                    .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());

            // Document -> Document
            Document unitInsertedfeature = new Document()
                    .append(AthenaFeatureField.ERROR_MSG_ERRTYPE, uemi.getErrType());

            unitInsertedfeature.append(AthenaFeatureField.FEATURE, unitInsertedfeature);
            featureDatabaseMgmtManager
                    .getDbCollectionList()
                    .get(athenaFeatureField.ERROR_MSG).insertOne(unitInsertedDoc);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed :" + e.getMessage().toString());
            return null;
        }
        return unitInsertedDoc;
    }

    private Document storeFlowRemoved(FeatureType featureType, FeatureCategory featureCategory,
                                      FlowRemovedFeature feature) {
        Document unitInsertedDoc = null;
        try {
            FeatureIndex fi = feature.getFeatureindex();
            UnitFlowRemovedInformation ufri = feature.getUnitFlowRemovedInformation();

            if (fi == null || ufri == null) {
                return null;
            }
            unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

            unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, ufri.getTimestamp())
                    .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                    .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());

            // JSONObject -> Document
            Document unitInsertedfeature = new Document()
                    .append(AthenaFeatureField.FLOW_REMOVED_REASON, ufri.getReason())
                    .append(AthenaFeatureField.FLOW_REMOVED_DURATION_SECOND, ufri.getDurationSec())
                    .append(AthenaFeatureField.FLOW_REMOVED_DURATION_N_SECOND, ufri.getDurationNsec())
                    .append(AthenaFeatureField.FLOW_REMOVED_IDLE_TIMEOUT, ufri.getIdleTimeout())
                    .append(AthenaFeatureField.FLOW_REMOVED_HARD_TIMEOUT, ufri.getHardTimeout())
                    .append(AthenaFeatureField.FLOW_REMOVED_PACKET_COUNT, ufri.getPacketCount())
                    .append(AthenaFeatureField.FLOW_REMOVED_BYTE_COUNT, ufri.getByteCount())
                    .append(AthenaFeatureField.FLOW_REMOVED_PACKET_PER_DURATION, ufri.getPacketPerDuration())
                    .append(AthenaFeatureField.FLOW_REMOVED_BYTE_PER_DURATION, ufri.getBytePerDuration());

            unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature);
            featureDatabaseMgmtManager
                    .getDbCollectionList()
                    .get(athenaFeatureField.FLOW_REMOVED).insertOne(unitInsertedDoc);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed (FlowRemoved):" + e.getMessage().toString());
        }

        return unitInsertedDoc;
    }


    private Document storePacketIn(FeatureType featureType, FeatureCategory featureCategory,
                                   PacketInFeature feature) {
        Document unitInsertedDoc = null;

        try {
            FeatureIndex fi = feature.getFeatureindex();
            UnitPacketInInformation upii = feature.getUnitPacketInInformation();
            if (fi == null || upii == null) {
                return null;
            }
            unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

            unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, upii.getTimestamp())
                    .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                    .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal())
                    .append("nanotime", System.nanoTime());

            Document payloadMatch = featureDatabaseMgmtManager.extractIndexfromFeature(upii.getPayloadMatch());

            if (payloadMatch == null) {
                return null;
            }

            // JSONObject -> Document
            Document unitInsertedfeature = new Document()
                    .append(AthenaFeatureField.PACKET_IN_TOTAL_LEN, upii.getTotalLen())
                    .append(AthenaFeatureField.PACKET_IN_REASON, upii.getReason());

            unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature)
                    .append(AthenaFeatureField.PACKET_IN_PAYLOAD_MATCH, payloadMatch);

            synchronized (activeGroup) {
                activeGroup.add(unitInsertedDoc);
            }
            //log.info("insert successful : {}", featureCategory.toString());

        } catch (Exception e) {
            log.warn("DB Insertion failed (PacketIn):" + e.getMessage().toString());
        }
        return unitInsertedDoc;
    }

    private Document storePortStatus(FeatureType featureType, FeatureCategory featureCategory,
                                     PortStatusFeature feature) {
        Document unitInsertedDoc = null;
        try {
            FeatureIndex fi = feature.getFeatureindex();
            UnitPortStatusInformation upsi = feature.getUnitPortStatusInformation();

            if (fi == null || upsi == null) {
                return null;
            }
            unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

            unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, upsi.getTimestamp())
                    .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                    .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());

            // JSONObject -> Document
            Document unitInsertedfeature = new Document()
                    .append(AthenaFeatureField.PORT_STATUS_REASON, upsi.getReason());

            unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature);
            featureDatabaseMgmtManager
                    .getDbCollectionList()
                    .get(athenaFeatureField.PORT_STATUS).insertOne(unitInsertedDoc);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed (PortStatus):" + e.getMessage().toString());
        }
        return unitInsertedDoc;
    }

    private List<Document> storeFlowStats(FeatureType featureType, FeatureCategory featureCategory,
                                          FlowStatisticsFeature feature) {
        List<Document> insertedDocument = null;
        try {
            insertedDocument = new ArrayList<>();
            int i = 0;
            while (true) {
                FeatureIndex fi = feature.getFeatureIndex(i);
                UnitFlowStatistics ufs = feature.getUnitFlowStatistics(i);

                if (fi == null || ufs == null) {
                    break;
                }

                // JSONObject -> Document
                Document unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

                unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, ufs.getTimestamp())
                        .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                        //TODO: need to be fixed later
                        .append(AthenaIndexField.APP_APPLICATION_ID, ufs.getApplicationId().id())
                        .append(AthenaIndexField.APP_APPLICATION_NAME, ufs.getApplicationId().name())
                        .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());
                // JSONObject -> Document
                Document unitInsertedfeature = new Document()
                        .append(AthenaFeatureField.FLOW_STATS_DURATION_SEC, ufs.getDurationSec())
                        .append(AthenaFeatureField.FLOW_STATS_DURATION_N_SEC, ufs.getDurationNsec())
                        .append(AthenaFeatureField.FLOW_STATS_PRIORITY, ufs.getPriority())
                        .append(AthenaFeatureField.FLOW_STATS_IDLE_TIMEOUT, ufs.getIdleTimeout())
                        .append(AthenaFeatureField.FLOW_STATS_HARD_TIMEOUT, ufs.getHardTimeout())
                        .append(AthenaFeatureField.FLOW_STATS_PACKET_COUNT, ufs.getPacketCount())
                        .append(AthenaFeatureField.FLOW_STATS_BYTE_COUNT, ufs.getByteCount())
                        .append(AthenaFeatureField.FLOW_STATS_ACTION_OUTPUT, ufs.getActionOutput())
                        .append(AthenaFeatureField.FLOW_STATS_ACTION_OUTPUT_PORT, ufs.getActionOutputPort())
                        .append(AthenaFeatureField.FLOW_STATS_ACTION_DROP, ufs.getActionDrop())
                        // add rich features
                        .append(AthenaFeatureField.FLOW_STATS_PACKET_COUNT_VAR, ufs.getPacketCountVar())
                        .append(AthenaFeatureField.FLOW_STATS_BYTE_COUNT_VAR, ufs.getByteCountVar())
                        .append(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET, ufs.getBytePerPacket())
                        .append(AthenaFeatureField.FLOW_STATS_BYTE_PER_PACKET_VAR, ufs.getBytePerPacketVar())
                        .append(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION, ufs.getPacketPerDuration())
                        .append(AthenaFeatureField.FLOW_STATS_PACKET_PER_DURATION_VAR, ufs.getPacketPerDurationVar())
                        .append(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION, ufs.getBytePerDuration())
                        .append(AthenaFeatureField.FLOW_STATS_BYTE_PER_DURATION_VAR, ufs.getBytePerDurationVar())
                        // add pairflow
                        .append(AthenaFeatureField.FLOW_STATS_PAIR_FLOW, ufs.getPairFlow())
                        .append(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO, ufs.getPairFlowRatio())
                        .append(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO_VAR, ufs.getPairFlowRatioVar())
                        .append(AthenaFeatureField.FLOW_STATS_TOTAL_FLOWS, ufs.getTotalFlows())
                        .append(AthenaFeatureField.FLOW_STATS_TOTAL_FLOWS_VAR, ufs.getTotalFlowsVar())
                        .append(AthenaFeatureField.FLOW_STATS_TOTAL_SINGLE_FLOW, ufs.getTotalSingleFlow())
                        .append(AthenaFeatureField.FLOW_STATS_TOTAL_SINGLE_FLOW_VAR, ufs.getTotalSingleFlowVar())
                        .append(AthenaFeatureField.FLOW_STATS_TOTAL_PAIR_FLOW, ufs.getTotalPairFlow())
                        .append(AthenaFeatureField.FLOW_STATS_TOTAL_PAIR_FLOW_VAR, ufs.getTotalPairFlowVar());

                unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature);
                insertedDocument.add(unitInsertedDoc);
                i++;
            }

            featureDatabaseMgmtManager
                    .getDbCollectionList()
                    .get(athenaFeatureField.FLOW_STATS).insertMany(insertedDocument);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed (FlowStats):" + e.getMessage().toString());
        }
        return insertedDocument;
    }

    private List<Document> storeQueueStats(FeatureType featureType, FeatureCategory featureCategory,
                                           QueueStatisticsFeature feature) {
        List<Document> insertedDocument = null;
        try {
            insertedDocument = new ArrayList<>();
            int i = 0;
            while (true) {
                FeatureIndex fi = feature.getFeatureIndex(i);
                UnitQueueStatistics uqs = feature.getUnitQueueStatistics(i);

                if (fi == null || uqs == null) {
                    break;
                }
                // JSONObject -> Document
                Document unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

                unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, uqs.getTimestamp())
                        .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                        .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());

                // JSONObject -> Document
                Document unitInsertedfeature = new Document()
                        .append(AthenaFeatureField.QUEUE_STATS_TX_BYTES, uqs.getTxBytes())
                        .append(AthenaFeatureField.QUEUE_STATS_TX_PACKETS, uqs.getTxPackets())
                        .append(AthenaFeatureField.QUEUE_STATS_TX_ERRORS, uqs.getTxErrors());

                unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature);
                insertedDocument.add(unitInsertedDoc);
                i++;
            }

            if (insertedDocument.size() == 0) {
                return null;
            }

            featureDatabaseMgmtManager
                    .getDbCollectionList()
                    .get(athenaFeatureField.QUEUE_STATS).insertMany(insertedDocument);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed :" + e.getMessage().toString());
        }

        return insertedDocument;
    }

    private List<Document> storeTableStats(FeatureType featureType, FeatureCategory featureCategory,
                                           TableStatisticsFeature feature) {
        List<Document> insertedDocument = null;
        try {
            insertedDocument = new ArrayList<>();
            int i = 0;
            while (true) {
                FeatureIndex fi = feature.getFeatureIndex(i);
                UnitTableStatistics uts = feature.getUnitTableStatistics(i);

                if (fi == null || uts == null) {
                    break;
                }
                // JSONObject -> Document
                Document unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

                unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, uts.getTimestamp())
                        .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                        .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());

                // JSONObject -> Document
                Document unitInsertedfeature = new Document()
                        .append(AthenaFeatureField.TABLE_STATS_MAX_ENTIRES, uts.getMaxEntries())
                        .append(AthenaFeatureField.TABLE_STATS_ACTIVE_COUNT, uts.getActiveCount())
                        .append(AthenaFeatureField.TABLE_STATS_LOOKUP_COUNT, uts.getLookupCount())
                        .append(AthenaFeatureField.TABLE_STATS_MATCHED_COUNT, uts.getMatchedCount())
                        // add rich features
                        .append(AthenaFeatureField.TABLE_STATS_MATCHED_PER_LOOKUP, uts.getMatchedPerLookup())
                        .append(AthenaFeatureField.TABLE_STATS_ACTIVE_PER_MAX, uts.getActivePerMax())
                        .append(AthenaFeatureField.TABLE_STATS_LOOKUP_PER_ACTIVE, uts.getLookupPerActive())
                        .append(AthenaFeatureField.TABLE_STATS_MATCHED_PER_ACTIVE, uts.getMatchedPerActive());

                unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature);
                insertedDocument.add(unitInsertedDoc);
                i++;
            }

            featureDatabaseMgmtManager
                    .getDbCollectionList()
                    .get(athenaFeatureField.TABLE_STATS).insertMany(insertedDocument);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed :" + e.getMessage().toString());
        }
        return insertedDocument;
    }

    private List<Document> storeAggregateStats(FeatureType featureType, FeatureCategory featureCategory,
                                               AggregateStatisticsFeature feature) {
        List<Document> insertedDocument = null;
        try {
            insertedDocument = new ArrayList<>();
            int i = 0;
            while (true) {
                FeatureIndex fi = feature.getFeatureIndex(i);
                UnitAggregateStatistics uas = feature.getUnitAggregateStatistics(i);

                if (fi == null || uas == null) {
                    break;
                }

                // JSONObject -> Document
                Document unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

                unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, uas.getTimestamp())
                        .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                        .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());

                // JSONObject -> Document
                Document unitInsertedfeature = new Document()
                        .append(AthenaFeatureField.AGGREGATE_STATS_PACKET_COUNT, uas.getPacketCount())
                        .append(AthenaFeatureField.AGGREGATE_STATS_BYTE_COUNT, uas.getByteCount())
                        .append(AthenaFeatureField.AGGREGATE_STATS_FLOW_COUNT, uas.getFlowCount());

                unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature);
                insertedDocument.add(unitInsertedDoc);
                i++;
            }

            featureDatabaseMgmtManager
                    .getDbCollectionList()
                    .get(athenaFeatureField.AGGREGATE_STATS).insertMany(insertedDocument);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed :" + e.getMessage().toString());
        }
        return insertedDocument;
    }

    private List<Document> storePortStats(FeatureType featureType, FeatureCategory featureCategory,
                                          PortStatisticsFeature feature) {
        List<Document> insertedDocument = null;
        try {
            insertedDocument = new ArrayList<>();
            int i = 0;
            while (true) {
                FeatureIndex fi = feature.getFeatureIndex(i);
                UnitPortStatistics ups = feature.getUnitPortStatistics(i);

                if (fi == null || ups == null) {
                    break;
                }

                // JSONObject -> Document
                Document unitInsertedDoc = featureDatabaseMgmtManager.extractIndexfromFeature(fi);

                unitInsertedDoc.append(AthenaIndexField.TIMESTAMP, ups.getTimestamp())
                        .append(AthenaIndexField.FEATURE_TYPE, featureType.ordinal())
                        .append(AthenaIndexField.FEATURE_CATEGORY, featureCategory.ordinal());

                // JSONObject -> Document
                Document unitInsertedfeature = new Document()
                        .append(AthenaFeatureField.PORT_STATS_RX_PACKETS, ups.getRxPackets())
                        .append(AthenaFeatureField.PORT_STATS_TX_PACKETS, ups.getTxPackets())
                        .append(AthenaFeatureField.PORT_STATS_RX_BYTES, ups.getRxBytes())
                        .append(AthenaFeatureField.PORT_STATS_TX_BYTES, ups.getTxBytes())
                        .append(AthenaFeatureField.PORT_STATS_RX_DROPPED, ups.getRxDropped())
                        .append(AthenaFeatureField.PORT_STATS_TX_DROPPED, ups.getTxDropped())
                        .append(AthenaFeatureField.PORT_STATS_RX_ERRORS, ups.getRxErrors())
                        .append(AthenaFeatureField.PORT_STATS_TX_ERRORS, ups.getTxErrors())
                        .append(AthenaFeatureField.PORT_STATS_RX_FRAME_ERROR, ups.getRxFrameErr())
                        .append(AthenaFeatureField.PORT_STATS_RX_OVER_ERROR, ups.getRxOverErr())
                        .append(AthenaFeatureField.PORT_STATS_RX_CRC_ERROR, ups.getRxCrcErr())
                        .append(AthenaFeatureField.PORT_STATS_COLLISIONS, ups.getCollisions())
                        // add rich features
                        .append(AthenaFeatureField.PORT_STATS_RX_PACKETS_VAR, ups.getRxPacketsVar())
                        .append(AthenaFeatureField.PORT_STATS_TX_PACKETS_VAR, ups.getTxPacketsVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_BYTES_VAR, ups.getRxBytesVar())
                        .append(AthenaFeatureField.PORT_STATS_TX_BYTES_VAR, ups.getTxBytesVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_DROPPED_VAR, ups.getRxDroppedVar())
                        .append(AthenaFeatureField.PORT_STATS_TX_DROPPED_VAR, ups.getTxDroppedVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_ERRORS_VAR, ups.getRxErrorsVar())
                        .append(AthenaFeatureField.PORT_STATS_TX_ERRORS_VAR, ups.getTxErrorsVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_FRAME_ERR_VAR, ups.getRxFrameErrVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_OVER_ERR_VAR, ups.getRxOverErrVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_CRC_ERR_VAR, ups.getRxCrcErrVar())
                        .append(AthenaFeatureField.PORT_STATS_COLLISIONS_VAR, ups.getCollisionsVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_BYTE_PER_PACKET, ups.getRxBytePerPacket())
                        .append(AthenaFeatureField.PORT_STATS_RX_BYTE_PER_PACKET_VAR, ups.getRxBytePerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_TX_BYTE_PER_PACKET, ups.getTxBytePerPacket())
                        .append(AthenaFeatureField.PORT_STATS_TX_BYTE_PER_PACKET_VAR, ups.getTxBytePerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_DROPPED_PER_PACKET, ups.getRxDroppedPerPacket())
                        .append(AthenaFeatureField.PORT_STATS_RX_DROPPED_PER_PACKET_VAR,
                                ups.getRxDroppedPerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_TX_DROPPED_PER_PACKET, ups.getTxDroppedPerPacket())
                        .append(AthenaFeatureField.PORT_STATS_TX_DROPPED_PER_PACKET_VAR,
                                ups.getTxDroppedPerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_ERROR_PER_PACKET, ups.getRxErrorPerPacket())
                        .append(AthenaFeatureField.PORT_STATS_RX_ERROR_PER_PACKET_VAR, ups.getRxErrorPerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_TX_ERROR_PER_PACKET, ups.getTxErrorPerPacket())
                        .append(AthenaFeatureField.PORT_STATS_TX_ERROR_PER_PACKET_VAR, ups.getTxErrorPerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_FRAME_ERR_PER_PACKET, ups.getRxFrameErrPerPacket())
                        .append(AthenaFeatureField.PORT_STATS_RX_FRAME_ERR_PER_PACKET_VAR,
                                ups.getRxFrameErrPerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_OVER_ERR_PER_PACKET, ups.getRxOverErrPerPacket())
                        .append(AthenaFeatureField.PORT_STATS_RX_OVER_ERR_PER_PACKET_VAR,
                                ups.getRxOverErrPerPacketVar())
                        .append(AthenaFeatureField.PORT_STATS_RX_CRC_ERR_PER_PACKET, ups.getRxCrcErrPerPacket())
                        .append(AthenaFeatureField.PORT_STATS_RX_CRC_ERR_PER_PACKET_VAR,
                                ups.getRxCrcErrPerPacketVar());
                unitInsertedDoc.append(AthenaFeatureField.FEATURE, unitInsertedfeature);

                insertedDocument.add(unitInsertedDoc);
                i++;
            }

            featureDatabaseMgmtManager.
                    getDbCollectionList().
                    get(athenaFeatureField.PORT_STATS).insertMany(insertedDocument);

            //log.info("insert successful : {}", featureCategory.toString());
        } catch (Exception e) {
            log.warn("DB Insertion failed :" + e.getMessage().toString());
        }
        return insertedDocument;
    }

    class IntegerComparator implements Comparator<Map<String, Object>> {
        private final String key;
        private final String type;

        public IntegerComparator(String key, String type) {
            this.key = key;
            this.type = type;
        }

        public int compare(Map<String, Object> first,
                           Map<String, Object> second) {
            // TODO: Null checking, both for maps and values
            Integer firstValue;
            Integer secondValue;

            firstValue = (Integer) first.get(key);
            secondValue = (Integer) second.get(key);

            return secondValue.compareTo(firstValue);
        }
    }

    class MapComparator implements Comparator<Map<String, Object>> {
        private final String key;
        private final String type;

        public MapComparator(String key, String type) {
            this.key = key;
            this.type = type;
        }

        public int compare(Map<String, Object> first,
                           Map<String, Object> second) {
            // TODO: Null checking, both for maps and values


            Object firstValue;
            Object secondValue;

            firstValue = first.get(key);
            secondValue = second.get(key);
            if (type.startsWith(athenaFeatureField.varintType)) {
                return ((Integer) secondValue).compareTo((Integer) firstValue);
            } else if (type.startsWith(athenaFeatureField.bigintType)) {
                return ((Long) secondValue).compareTo((Long) firstValue);
            } else if (type.startsWith(athenaFeatureField.doubleType)) {
                return ((Double) secondValue).compareTo((Double) firstValue);
            } else if (type.startsWith(athenaFeatureField.timestampType)) {
                return ((Date) secondValue).compareTo((Date) firstValue);
            } else {
                log.warn("[getFeatreFromRowInternal] Not supported type :{}", type);
                return 0;
            }

        }
    }
}
