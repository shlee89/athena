package org.onosproject.athena.database.impl;


import com.google.common.net.InetAddresses;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.BasicBSONList;
import org.onosproject.athena.database.AdvancedFeatureConstraintValue;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaField;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.athena.feature.FeatureIndex;
import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.AdvancedFeatureConstraintType;
import org.onosproject.athena.database.DatabaseType;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.feature.FeatureIndexField;
import org.slf4j.Logger;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Filters.or;
import static org.slf4j.LoggerFactory.getLogger;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Created by seunghyeon on 1/3/16.
 */
public class FeatureDatabaseMgmtManager implements Serializable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private static final int MONGO_PORT = 27017;


    private final Logger log = getLogger(getClass());

    //Database access
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    HashMap<String, MongoCollection> dbCollectionList;

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public void setMongoDatabase(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public HashMap<String, MongoCollection> getDbCollectionList() {
        return dbCollectionList;
    }

    public void setDbCollectionList(HashMap<String, MongoCollection> dbCollectionList) {
        this.dbCollectionList = dbCollectionList;
    }

    public FeatureDatabaseMgmtManager() {
    }

    AthenaIndexField athenaIndexField = new AthenaIndexField();
    AthenaFeatureField athenaFeatureField = new AthenaFeatureField();

    public boolean connectToDatabase(String ip) {
        try {
            mongoClient = new MongoClient(ip, MONGO_PORT);
            mongoDatabase = mongoClient.getDatabase("featureStore");
            dbCollectionList = getCollectionList(mongoDatabase);
            log.info("Connect to database successfully!!");

            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public boolean connectToDatabaseCluster(final List<String> seeds) {
        try {
            List<ServerAddress> seedList = new ArrayList<>();
            for (String ip : seeds) {
                seedList.add(new ServerAddress(ip, MONGO_PORT));
            }
            mongoClient = new MongoClient(seedList);
            mongoDatabase = mongoClient.getDatabase("featureStore");
            dbCollectionList = getCollectionList(mongoDatabase);
            log.info("Connect to database cluster successfully!!");

            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public Document extractIndexfromFeature(FeatureIndex fi) {
        Document doc = new Document();
        FeatureIndexField field;
        for (int i = 0; i < fi.getFeatureList().size(); i++) {
            field = fi.getFeatureList().get(i);

            switch (field) {
                case SWITCH_DATAPATH_ID:
                    //doc.append -> doc.put
                    doc.put(AthenaIndexField.SWITCH_DATAPATH_ID, fi.getSwitchDatapathId());
                    break;
                case SWITCH_PORT_ID:
                    doc.put(AthenaIndexField.SWITCH_PORT_ID, fi.getSwitchPortId());
                    break;
                case SWITCH_QUEUE_ID:
                    doc.put(AthenaIndexField.SWITCH_QUEUE_ID, fi.getSwitchQeueueId());
                    break;
                case SWITCH_TABLE_ID:
                    doc.put(AthenaIndexField.SWITCH_TABLE_ID, fi.getSwitchTableId());
                    break;
                case MATCH_IN_PORT:
                    doc.put(AthenaIndexField.MATCH_IN_PORT, fi.getMatchInPort());
                    break;
                case MATCH_IN_PHY_PORT:
                    doc.put(AthenaIndexField.MATCH_IN_PHY_PORT, fi.getMatchInPhyPort());
                    break;
                case MATCH_ETH_DST:
                    doc.put(AthenaIndexField.MATCH_ETH_DST, fi.getMatchEthDst());
                    break;
                case MATCH_ETH_SRC:
                    doc.put(AthenaIndexField.MATCH_ETH_SRC, fi.getMatchEthSrc());
                    break;
                case MATCH_ETH_TYPE:
                    doc.put(AthenaIndexField.MATCH_ETH_TYPE, fi.getMatchEthType());
                    break;
                case MATCH_VLAN_VID:
                    doc.put(AthenaIndexField.MATCH_VLAN_VID, fi.getMatchVlanVid());
                    break;
                case MATCH_VLAN_PCP:
                    doc.put(AthenaIndexField.MATCH_VLAN_PCP, fi.getMatchVlanPcp());
                    break;
                case MATCH_IP_DSCP:
                    doc.put(AthenaIndexField.MATCH_IP_DSCP, fi.getMatchIpDscp());
                    break;
                case MATCH_IP_ECN:
                    doc.put(AthenaIndexField.MATCH_IP_ECN, fi.getMatchIpEcn());
                    break;
                case MATCH_IP_PROTO:
                    doc.put(AthenaIndexField.MATCH_IP_PROTO, fi.getMatchIpProto());
                    break;
                case MATCH_IPV4_SRC:
                    doc.put(AthenaIndexField.MATCH_IPV4_SRC, fi.getMatchIpv4Src());
                    break;
                case MATCH_IPV4_SRC_MASK:
                    doc.put(AthenaIndexField.MATCH_IPV4_SRC_MASK, fi.getMatchIpv4SrcMask());
                    break;
                case MATCH_IPV4_DST:
                    doc.put(AthenaIndexField.MATCH_IPV4_DST, fi.getMatchIpv4Dst());
                    break;
                case MATCH_IPV4_DST_MASK:
                    doc.put(AthenaIndexField.MATCH_IPV4_DST_MASK, fi.getMatchIpv4DstMask());
                    break;
                case MATCH_TCP_SRC:
                    doc.put(AthenaIndexField.MATCH_TCP_SRC, fi.getMatchTcpSrc());
                    break;
                case MATCH_TCP_DST:
                    doc.put(AthenaIndexField.MATCH_TCP_DST, fi.getMatchTcpDst());
                    break;
                case MATCH_UDP_SRC:
                    doc.put(AthenaIndexField.MATCH_UDP_SRC, fi.getMatchUdpSrc());
                    break;
                case MATCH_UDP_DST:
                    doc.put(AthenaIndexField.MATCH_UDP_DST, fi.getMatchUdpDst());
                    break;
                case MATCH_MPLS_LABEL:
                    doc.put(AthenaIndexField.MATCH_MPLS_LABEL, fi.getMatchMplsLabel());
                    break;
                case MATCH_SCTP_SRC:
                    doc.put(AthenaIndexField.MATCH_SCTP_SRC, fi.getMatchSctpSrc());
                    break;
                case MATCH_SCTP_DST:
                    doc.put(AthenaIndexField.MATCH_SCTP_DST, fi.getMatchSctpDst());
                    break;
                case MATCH_ICMPv4_CODE:
                    doc.put(AthenaIndexField.MATCH_ICMPV4_CODE, fi.getMatchIcmpv4Code());
                    break;
                case MATCH_ICMPV4_TYPE:
                    doc.put(AthenaIndexField.MATCH_ICMPV4_TYPE, fi.getMatchIcmpv4Type());
                    break;
                case MATCH_IPV6_SRC:
                    doc.put(AthenaIndexField.MATCH_IPV6_SRC, ByteBuffer.wrap(fi.getMatchIpv6Src()).getInt());
                    break;
                case MATCH_IPV6_SRC_MASK:
                    doc.put(AthenaIndexField.MATCH_IPV6_SRC_MASK, fi.getMatchIpv6SrcMask());
                    break;
                case MATCH_IPV6_DST:
                    doc.put(AthenaIndexField.MATCH_IPV6_DST, ByteBuffer.wrap(fi.getMatchIpv6Dst()).getInt());
                    break;
                case MATCH_IPV6_DST_MASK:
                    doc.put(AthenaIndexField.MATCH_IPV6_DST_MASK, fi.getMatchIpv6DstMask());
                    break;
                case MATCH_ICMPV6_CODE:
                    doc.put(AthenaIndexField.MATCH_ICMPV6_CODE, fi.getMatchIcmpv6Code());
                    break;
                case MATCH_ICMPV6_TYPE:
                    doc.put(AthenaIndexField.MATCH_ICMPV6_TYPE, fi.getMatchIcmpv6Type());
                    break;
                case MATCH_IPV6_ND_SLL:
                    doc.put(AthenaIndexField.MATCH_IPV6_ND_SLL, fi.getMatchIpv6NdSll());
                    break;
                case MATCH_IPV6_ND_TARGET:
                    doc.put(AthenaIndexField.MATCH_IPV6_ND_TARGET,
                            ByteBuffer.wrap(fi.getMatchIpv6NdTarget()).getInt());
                    break;
                case MATCH_IPV6_ND_TLL:
                    doc.put(AthenaIndexField.MATCH_IPV6_ND_TLL, fi.getMatchIpv6NdTll());
                    break;
                case MATCH_IPV6_EXTHDR:
                    doc.put(AthenaIndexField.MATCH_IPV6_EXTHDR, fi);
                    break;
                default:
                    //log.warn("not supported type");
                    break;
            }
        }
        return doc;
    }


    public Document extractPayloadMatchfromFeature(FeatureIndex fi) {
        Document doc = new Document();
        FeatureIndexField field;
        for (int i = 0; i < fi.getFeatureList().size(); i++) {
            field = fi.getFeatureList().get(i);

            switch (field) {
                case MATCH_ETH_DST:
                    //doc.append -> doc.put
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_ETH_DST, fi.getMatchEthDst());
                    break;
                case MATCH_ETH_SRC:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_ETH_SRC, fi.getMatchEthSrc());
                    break;
                case MATCH_ETH_TYPE:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_ETH_TYPE, fi.getMatchEthType());
                    break;
                case MATCH_VLAN_VID:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_VLAN_VID, fi.getMatchVlanVid());
                    break;
                case MATCH_IP_DSCP:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IP_DSCP, fi.getMatchIpDscp());
                    break;
                case MATCH_IP_ECN:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IP_ECN, fi.getMatchIpEcn());
                    break;
                case MATCH_IP_PROTO:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IP_PROTO, fi.getMatchIpProto());
                    break;
                case MATCH_IPV4_SRC:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IPV4_SRC, fi.getMatchIpv4Src());
                    break;
                case MATCH_IPV4_DST:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IPV4_DST, fi.getMatchIpv4Dst());
                    break;
                case MATCH_TCP_SRC:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_TCP_SRC, fi.getMatchTcpSrc());
                    break;
                case MATCH_TCP_DST:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_TCP_DST, fi.getMatchTcpDst());
                    break;
                case MATCH_UDP_SRC:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_UDP_SRC, fi.getMatchUdpSrc());
                    break;
                case MATCH_UDP_DST:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_UDP_DST, fi.getMatchUdpDst());
                    break;
                case MATCH_ICMPv4_CODE:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_ICMPV4_CODE, fi.getMatchIcmpv4Code());
                    break;
                case MATCH_ICMPV4_TYPE:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_ICMPV4_TYPE, fi.getMatchIcmpv4Type());
                    break;
                case MATCH_IPV6_SRC:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IPV6_SRC,
                            ByteBuffer.wrap(fi.getMatchIpv6Src()).getInt());
                    break;
                case MATCH_IPV6_SRC_MASK:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IPV6_DST, fi.getMatchIpv6SrcMask());
                    break;
                case MATCH_IPV6_DST:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_IPV6_DST,
                            ByteBuffer.wrap(fi.getMatchIpv6Dst()).getInt());
                    break;
                case MATCH_ICMPV6_CODE:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_ICMPV6_CODE, fi.getMatchIcmpv6Code());
                    break;
                case MATCH_ICMPV6_TYPE:
                    doc.put(AthenaFeatureField.PACKET_IN_MATCH_ICMPV6_TYPE, fi.getMatchIcmpv6Type());
                    break;
                default:
                    //log.warn("not supported type");
                    break;
            }
        }
        return doc;
    }

    public HashMap<String, MongoCollection> getCollectionList(MongoDatabase mongoDatabase) {
        HashMap<String, MongoCollection> dbCollectionList = new HashMap<String, MongoCollection>();
        String[] tableNameList = {AthenaFeatureField.ERROR_MSG, AthenaFeatureField.FLOW_REMOVED,
                AthenaFeatureField.PACKET_IN,
                AthenaFeatureField.PORT_STATUS, AthenaFeatureField.FLOW_STATS, AthenaFeatureField.QUEUE_STATS,
                AthenaFeatureField.AGGREGATE_STATS, AthenaFeatureField.TABLE_STATS, AthenaFeatureField.PORT_STATS};

        for (String tableName : tableNameList) {
            MongoCollection dbCollection = mongoDatabase.getCollection(tableName);
            if (dbCollection == null) {
                mongoDatabase.createCollection(tableName);
                dbCollection = mongoDatabase.getCollection(tableName);
            }
            dbCollectionList.put(tableName, dbCollection);
        }

        return dbCollectionList;
    }

    public AthenaFeatures requestDataToMongoDB(AthenaFeatureRequester athenaFeatureRequester) {
        Iterable<Document> generatedFeatures =
                getDataFromRawDatabaseEntry(athenaFeatureRequester);

        AthenaFeatures athenaFeatures = new AthenaFeatures(new DatabaseType(DatabaseType.MONGO_DB),
                generatedFeatures);

        return athenaFeatures;
    }

    public void printAllDBFeatures(AthenaFeatureRequester athenaFeatureRequester, AthenaFeatures athenaFeatures) {
        if (athenaFeatures.getDatabaseType().getValue().startsWith(DatabaseType.MONGO_DB)) {
            String collectionName = getCollectionFromFeatures(athenaFeatureRequester.getFeatureConstraint());
            printAllMongoDBFeatures((Iterable<Document>) athenaFeatures.getFeatures(),
                    collectionName,
                    athenaFeatureRequester.getFeatureConstraint(),
                    athenaFeatureRequester.getDataRequestAdvancedObject());
        } else {
            log.warn("not supported DB type");
        }

    }

    public void printAllMongoDBFeatures(Iterable<Document> generatedFeatures,
                                        String table,
                                        FeatureConstraint featureConstraint,
                                        AdvancedFeatureConstraint advancedFeatureConstraint) {
        AthenaIndexField id = new AthenaIndexField();
        List<String> indexList = id.getListOfFeatures();
        List<String> featureList = athenaFeatureField.getListOfFeatureTable(table);
        List<String> affectedFeatures = affectedFeaturesFromRequest(featureConstraint);
        String index = null;

        boolean checkAggregated = exisitedAdvancedOption(AdvancedFeatureConstraintType.AGGREGATE,
                advancedFeatureConstraint);

        for (Document entry : generatedFeatures) {

            if (checkAggregated) {
                Document innerRow = (Document) entry.get("_id");

                // for usecase 2 -Jinwoo Kim
                String key = "timestamp";
                Date timestamp = (Date) innerRow.get(key);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                entry.remove(key);
                String result = sdf.format(timestamp);
                System.out.println("result : " + result);
                innerRow.append(key, sdf.format(timestamp));

                AdvancedFeatureConstraintValue value =
                        advancedFeatureConstraint
                                .getAdvancedOptions()
                                .get(AdvancedFeatureConstraintType.AGGREGATE);
                Iterator<String> params = value.getValue();
                while (params.hasNext()) {
                    String element = params.next();
                    System.out.print(FeatureDatabaseMgmtManager.ANSI_RED + "A:" +
                            element + ":" + innerRow.get(element) + ", " +
                            FeatureDatabaseMgmtManager.ANSI_RESET);
                }
                System.out.print(FeatureDatabaseMgmtManager.ANSI_GREEN +
                        "NumOfAddedElements" + ":" + entry.get("NumOfAddedElements") + ", " +
                        FeatureDatabaseMgmtManager.ANSI_RESET);
            } else {

                for (int j = 0; j < indexList.size(); j++) {

                    if (!entry.containsKey(indexList.get(j))) {
                        continue;
                    }

//                if (checkAggregated) {
//                    break;
//                }

                    //TODO to be fixed more intelligently (print for IP address)
                    if (indexList.get(j).endsWith("Mipv4Src") || indexList.get(j).endsWith("Mipv4Dst")) {
                        int ipAddress = ((Integer) entry.get(indexList.get(j))).intValue();
//                    byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
                        byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
                        InetAddress address = null;
                        try {
                            address = InetAddress.getByAddress(bytes);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                            log.warn("IP translating error!");
                            continue;
                        }

                        if (affectedFeatures.contains(indexList.get(j))) {
                            System.out.print(FeatureDatabaseMgmtManager.ANSI_RED +
                                    indexList.get(j) + ":" + address.toString() + ", " +
                                    FeatureDatabaseMgmtManager.ANSI_RESET);
                        } else {
                            System.out.print(FeatureDatabaseMgmtManager.ANSI_PURPLE +
                                    indexList.get(j) + ":" + address.toString() + ", " +
                                    FeatureDatabaseMgmtManager.ANSI_RESET);
                        }
                        continue;
                    }
                    if (affectedFeatures.contains(indexList.get(j))) {
                        System.out.print(FeatureDatabaseMgmtManager.ANSI_RED +
                                indexList.get(j) + ":" + entry.get(indexList.get(j)) + ", " +
                                FeatureDatabaseMgmtManager.ANSI_RESET);
                    } else {
                        System.out.print(FeatureDatabaseMgmtManager.ANSI_PURPLE +
                                indexList.get(j) + ":" + entry.get(indexList.get(j)) + ", " +
                                FeatureDatabaseMgmtManager.ANSI_RESET);
                    }
                }
                entry = (Document) entry.get(AthenaFeatureField.FEATURE);
            }

            for (int j = 0; j < featureList.size(); j++) {
                if (!entry.containsKey(featureList.get(j))) {
                    continue;
                }
                if (affectedFeatures.contains(featureList.get(j))) {
                    System.out.print(FeatureDatabaseMgmtManager.ANSI_RED +
                            featureList.get(j) + ":" + entry.get(featureList.get(j)) + ", " +
                            FeatureDatabaseMgmtManager.ANSI_RESET);
                } else {
                    System.out.print(FeatureDatabaseMgmtManager.ANSI_GREEN +
                            featureList.get(j) + ":" + entry.get(featureList.get(j)) + ", " +
                            FeatureDatabaseMgmtManager.ANSI_RESET);
                }
            }
            System.out.println("");
        }
    }


    //TODO there are lots of unneccesary task in this function. Please rewriting later
    public List<String> affectedFeaturesFromRequest(FeatureConstraint featureConstraint) {
        List<TargetAthenaValue> dataRequestObjectValueList = featureConstraint.getDataRequestObjectValueList();

        List<String> returnValue = new ArrayList<>();


        for (TargetAthenaValue obj : dataRequestObjectValueList) {
            FeatureConstraint dro = (FeatureConstraint) obj.getTargetAthenaValue();
            String featureName = getFeatureFromRequest(dro);
            returnValue.add(featureName);
        }
        return returnValue;
    }

    public boolean exisitedAdvancedOption(AdvancedFeatureConstraintType option,
                                          AdvancedFeatureConstraint object) {
        if (option == null || object == null) {
            return false;
        }
        for (int i = 0; i < object.getAdvancedOptions().size(); i++) {
            if (object.getAdvancedOptions().containsKey(option)) {
                return true;
            }
        }

        return false;
    }

    private int limitEntrySizeFromAdvancedOption(AdvancedFeatureConstraint object) {

        for (int i = 0; i < object.getAdvancedOptions().size(); i++) {
            if (object.getAdvancedOptions().containsKey(AdvancedFeatureConstraintType.LIMIT_FEATURE_COUNTS)) {
                AdvancedFeatureConstraintValue value =
                        object.getAdvancedOptions().get(AdvancedFeatureConstraintType.LIMIT_FEATURE_COUNTS);
                Iterator<String> params = value.getValue();

                if (params.hasNext()) {
                    return tryParseStringToInt(params.next());
                } else {
                    return 0;
                }
            }
        }

        return 0;
    }

    private Bson sortedTargetFromAdvancedOption(AdvancedFeatureConstraint object) {
        Document result = null;
        for (int i = 0; i < object.getAdvancedOptions().size(); i++) {
            if (object.getAdvancedOptions().containsKey(AdvancedFeatureConstraintType.SORTING)) {
                AdvancedFeatureConstraintValue value =
                        object.getAdvancedOptions().get(AdvancedFeatureConstraintType.SORTING);
                Iterator<String> params = value.getValue();

                // 1 : ascending, -1: descending

                if (params.hasNext()) {
//                    result.(new Document(params.next(), -1));
                    if (result == null) {
                        result = new Document("_id." + params.next(), 1);
                    } else {
                        result.append(params.next(), -1);
                    }
                } else {
                    log.warn("[sortedTargetFromAdvancedOption] No value for SORTING");
                    return null;
                }
            }
        }

        return result;
    }

    private Document aggregatedListFromAdvancedOption(AdvancedFeatureConstraint object) {
        Document aggregatedList = new Document();
        AthenaFeatureField fn = new AthenaFeatureField();
        AthenaIndexField in = new AthenaIndexField();

        for (int i = 0; i < object.getAdvancedOptions().size(); i++) {
            if (object.getAdvancedOptions().containsKey(AdvancedFeatureConstraintType.AGGREGATE)) {
                AdvancedFeatureConstraintValue value =
                        object.getAdvancedOptions().get(AdvancedFeatureConstraintType.AGGREGATE);
                Iterator<String> params = value.getValue();


                while (params.hasNext()) {
                    String element = params.next();

                    if (fn.isElements(element)) {
                        aggregatedList.append(element, "$" + AthenaFeatureField.FEATURE + "." + element);
                    } else {
                        aggregatedList.append(element, "$" + element);
                    }
                }
            }
        }


        return aggregatedList;
    }

    public Document generateGroupSumDocument(String collectionName) {
        //return document
        Document groupQuery = new Document();

        //temp for statistics!
        AthenaFeatureField fnt = new AthenaFeatureField();
        //TODO: temporary for usecase1 graph
        List<String> features = fnt.getListOfFeatureTable("FStatistics");

        for (int i = 0; i < features.size(); i++) {
            String featureTarget = features.get(i);
            //Sum case
            if (featureTarget.startsWith(AthenaFeatureField.FLOW_STATS_ACTION_DROP) ||
                    featureTarget.startsWith(AthenaFeatureField.FLOW_STATS_ACTION_OUTPUT) ||
                    featureTarget.startsWith(AthenaFeatureField.FLOW_STATS_ACTION_OUTPUT_PORT) ||
                    featureTarget.startsWith(AthenaFeatureField.FLOW_REMOVED_REASON) ||
                    featureTarget.startsWith(AthenaFeatureField.PACKET_IN_REASON) ||
                    featureTarget.startsWith(AthenaFeatureField.PACKET_IN_IN_PORT) ||
                    featureTarget.startsWith(AthenaFeatureField.PACKET_IN_IN_PHY_PORT) ||
                    featureTarget.startsWith(AthenaFeatureField.PORT_STATUS_REASON) ||
                    featureTarget.startsWith(AthenaFeatureField.PACKET_IN_MATCH)
                    ) {
                continue;
            }
            Bson sumEntryCalculate = new Document("$sum", "$" + AthenaFeatureField.FEATURE + "." + featureTarget);
            groupQuery.append(featureTarget, sumEntryCalculate);
        }
        //Total count information
        Bson sumEntryCalculate = new Document("$sum", 1);
        groupQuery.append("NumOfAddedElements", sumEntryCalculate);

        return groupQuery;

    }

    public Iterable<Document> getDataFromRawDatabaseEntry(AthenaFeatureRequester athenaFeatureRequester) {
        AdvancedFeatureConstraint advancedFeatureConstraint
                = athenaFeatureRequester.getDataRequestAdvancedObject();
        FeatureConstraint featureConstraint = athenaFeatureRequester.getFeatureConstraint();
        String collectionName = getCollectionFromFeatures(featureConstraint);
        //TODO; temporary for usecase 1
//        String collectionName = "usecase1";
//        MongoCollection dbCollection = mongoDatabase.getCollection(collectionName);
        MongoCollection dbCollection = getDbCollectionList().get(collectionName);
        Iterable<Document> result = null;

        //generate match!
        Bson matchQuery = generateBson(featureConstraint);
        BsonDocument matchQueryDocument =
                matchQuery.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());

        //advanced option params
        int limitEntrySize = 0;
        Bson sortedTarget = null;
        Document aggregatedList = null;
        Document groupQuery = null;

        //generate advanced query
        Boolean limitEntry = exisitedAdvancedOption(AdvancedFeatureConstraintType.LIMIT_FEATURE_COUNTS,
                advancedFeatureConstraint);
        if (limitEntry) {
            limitEntrySize = limitEntrySizeFromAdvancedOption(advancedFeatureConstraint);
        }

        Boolean sortingEntry = exisitedAdvancedOption(AdvancedFeatureConstraintType.SORTING,
                advancedFeatureConstraint);
//        if (sortingEntry) {
//            sortedTarget = sortedTargetFromAdvancedOption(advancedFeatureConstraint);
//        }

        boolean aggregateEntry = exisitedAdvancedOption(AdvancedFeatureConstraintType.AGGREGATE,
                advancedFeatureConstraint);
        if (aggregateEntry) {
            aggregatedList = aggregatedListFromAdvancedOption(advancedFeatureConstraint);
        }

        //generate

        if (aggregateEntry) {
            //Aggregate optimize
            BasicBSONList pipeline = new BasicBSONList();
            //pipeline for match
            pipeline.add(new Document("$match", matchQueryDocument));


            groupQuery = generateGroupSumDocument(collectionName);


            //pipeline for group (add aggregated list)
//            groupQuery = new Document("_id", aggregatedList); //add aggregated list!
            groupQuery.append("_id", aggregatedList);

            pipeline.add(new Document("$group", groupQuery));

            //aggregate
            if (limitEntry) {
                //aggregate && limitEntry
                //for limit
                pipeline.add(new Document("$limit", limitEntrySize));
            }

            //sort
            if (sortingEntry) {
                sortedTarget = sortedTargetFromAdvancedOption(advancedFeatureConstraint);
                if (sortedTarget != null) {
                    pipeline.add(new Document("$sort", sortedTarget));
                }
                sortingEntry = false;
            }

            result = dbCollection.aggregate(pipeline);
        } else {
            //!aggregate
            if (limitEntry) {
                //!aggregate && limitEntry
                if (sortingEntry) {
                    result = dbCollection.find(matchQuery).sort(sortedTarget).limit(limitEntrySize);
                    //!aggregate && limitEntry && sortingEntry
                } else {
                    result = dbCollection.find(matchQuery).limit(limitEntrySize);
                    //!aggregate && limitEntry && !sortingEntry
                }
            } else {
                //!aggregate && !limitEntry
                if (sortingEntry) {
                    //!aggregate && !limitEntry && sortingEntry
                    result = dbCollection.find(matchQuery).sort(sortedTarget);
                } else {
                    //!aggregate && !limitEntry && !sortingEntry
                    result = dbCollection.find(matchQuery);
                }
            }
        }

//        int i = 0;
//        for (Document row : result) {
//            System.out.println("[" + i + "]" + row.toString());
//            i++;
//        }
        return result;

        /*
        Temporary
        ArrayList<HashMap<String, Object>> generatedFeatures = new ArrayList<>();
        List<String> extractedEntries = getExtractedEntries(collectionName);
        HashMap<String, Object> generatedFeature;
        //extract data
        for (Document row : result) {
            generatedFeature = new HashMap<>();
            for (int i = 0; i < extractedEntries.size(); i++) {
                Object obj = null;
                obj = getFeatureFromRow(row, extractedEntries.get(i));
                if (obj != null) {
                    generatedFeature.put(extractedEntries.get(i), obj);
                }
            }
            generatedFeatures.add(generatedFeature);
        }
*/

            /*
            for (Document row : result) {
                generatedFeature = new HashMap<>();
                Document innerRow = (Document) row.get("_id");
                int sortedTargetCountValue = row.getInteger("count");
                for (i = 0; i < indexList.size(); i++) {
                    Object obj = null;
                    obj = getFeatureFromRow(innerRow, extractedEntries.get(i));
                    if (obj != null) {
                        generatedFeature.put(extractedEntries.get(i), obj);
                    }
                }
                generatedFeature.put(sortedTargetCount, sortedTargetCountValue);
                generatedFeatures.add(generatedFeature);
            }
            */
//        }

//        return generatedFeatures;
    }


    public Object getFeatureFromRow(Document row, String feature) {
        String type = athenaIndexField.getTypeOnDatabase(feature);

        if (type != null) {
            return getFeatreFromRowInternal(row, feature, type);
        }

        type = athenaFeatureField.getTypeOnDatabase(feature);

        if (type != null) {
            return getFeatreFromRowInternal((Document) row.get(AthenaFeatureField.FEATURE), feature, type);
        }

        log.warn("getFeatureFromRow -> Unsupported type : {}", feature);
        return null;
    }

    public Object getFeatreFromRowInternal(Document row, String feature, String type) {

        if (type.startsWith(athenaFeatureField.varintType)) {
            return row.getInteger(feature);
        } else if (type.startsWith(athenaFeatureField.bigintType)) {
            Long l = row.getLong(feature);
            return l;
        } else if (type.startsWith(athenaFeatureField.doubleType)) {
            Double d = row.getDouble(feature);
            return d;
        } else if (type.startsWith(athenaFeatureField.timestampType)) {
            return row.getDate(feature);
        } else {
            log.warn("[getFeatreFromRowInternal] Not supported type :{}", type);
            return null;
        }

    }


    public List<String> getExtractedEntries(String table) {
        AthenaIndexField id = new AthenaIndexField();
        List<String> completedEntries = id.getListOfFeatures();
        List<String> feature;
        int i;

        //add entries
        if (table.startsWith(AthenaFeatureField.ERROR_MSG)) {
            feature = athenaFeatureField.getListOfErrorMsgFeatures();
        } else if (table.startsWith(AthenaFeatureField.FLOW_REMOVED)) {
            feature = athenaFeatureField.getListOfFlowRemovedFeatures();
        } else if (table.startsWith(AthenaFeatureField.PACKET_IN)) {
            feature = athenaFeatureField.getListOfFPacketIneatures();
        } else if (table.startsWith(AthenaFeatureField.PORT_STATUS)) {
            feature = athenaFeatureField.getListOfFPortStatuseatures();
        } else if (table.startsWith(AthenaFeatureField.FLOW_STATS)) {
            feature = athenaFeatureField.getListOfFFlowStatseatures();
        } else if (table.startsWith(AthenaFeatureField.QUEUE_STATS)) {
            feature = athenaFeatureField.getListOfFQueueStatseatures();
        } else if (table.startsWith(AthenaFeatureField.TABLE_STATS)) {
            feature = athenaFeatureField.getListOfFTableStatseatures();
        } else if (table.startsWith(AthenaFeatureField.AGGREGATE_STATS)) {
            feature = athenaFeatureField.getListOfFAggregatedStatseatures();
        } else if (table.startsWith(AthenaFeatureField.PORT_STATS)) {
            feature = athenaFeatureField.getListOfFPortStatseatures();
        } else {
            //log.warn("Not supported table name. ");
            return null;
        }
        for (i = 0; i < feature.size(); i++) {
            completedEntries.add(feature.get(i));
        }

        return completedEntries;
    }

    public Integer tryParseStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            log.warn("Input error please check limitSize {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Converting User-defined constaints to Bson constraints.
     *
     * @param featureConstraint
     * @return
     */
    public Bson generateBson(FeatureConstraint featureConstraint) {
        List<TargetAthenaValue> dataRequestObjectValueList = featureConstraint.getDataRequestObjectValueList();
        FeatureConstraintOperatorType featureConstraintOperatorType =
                featureConstraint.getFeatureConstraintOperatorType();
        Bson query = null;

        if (!(dataRequestObjectValueList.size() > 0)) {
            return null;
        }

        if (featureConstraintOperatorType == FeatureConstraintOperatorType.COMPARABLE) {
            query = getBsonFromRequestOperatorComparison(featureConstraint);

        } else if (featureConstraintOperatorType == FeatureConstraintOperatorType.LOGICAL) {
            query = getBsonFromRequestOperatorLogical(featureConstraint);

        } else {
            log.warn("not supported FeatureConstraintOperatorType");
        }

        return query;

    }

    public String getCollectionFromFeatures(FeatureConstraint featureConstraint) {
        List<TargetAthenaValue> dataRequestObjectValueList = featureConstraint.getDataRequestObjectValueList();
        FeatureConstraintOperatorType featureConstraintOperatorType =
                featureConstraint.getFeatureConstraintOperatorType();
        boolean flag = false;
        int i = 0;
        String table = null;

        List<String> listOfFeatures = new ArrayList<>();

        if (!(dataRequestObjectValueList.size() > 0)) {
            return null;
        }

        if (featureConstraintOperatorType == FeatureConstraintOperatorType.COMPARABLE) {
            listOfFeatures.add(getFeatureFromRequest(featureConstraint));

        } else if (featureConstraintOperatorType == FeatureConstraintOperatorType.LOGICAL) {
            listOfFeatures = getFeatureFromRequestLogical(featureConstraint);

        } else {
            log.warn("not supported FeatureConstraintOperatorType");
        }

        for (i = 0; i < listOfFeatures.size(); i++) {
            if (flag) {
                if (!table.equals(getCollectionRaw(listOfFeatures.get(i)))) {
                    return null;
                }
            } else {
                table = getCollectionRaw(listOfFeatures.get(i));
                if (table != null) {
                    flag = true;
                    return table;
                }
            }
        }
        return table;
    }


    public String getCollectionRaw(String feature) {
        if (feature.startsWith("E")) {
            return AthenaFeatureField.ERROR_MSG;
        } else if (feature.startsWith("FR")) {
            return AthenaFeatureField.FLOW_REMOVED;
        } else if (feature.startsWith("PI")) {
            return AthenaFeatureField.PACKET_IN;
        } else if (feature.startsWith("PIM")) {
            return AthenaFeatureField.PACKET_IN;
        } else if (feature.startsWith("PSS")) {
            return AthenaFeatureField.PORT_STATS;
        } else if (feature.startsWith("PS")) {
            return AthenaFeatureField.PORT_STATUS;
        } else if (feature.startsWith("FS")) {
            return AthenaFeatureField.FLOW_STATS;
        } else if (feature.startsWith("QS")) {
            return AthenaFeatureField.QUEUE_STATS;
        } else if (feature.startsWith("TS")) {
            return AthenaFeatureField.TABLE_STATS;
        } else if (feature.startsWith("AS")) {
            return AthenaFeatureField.AGGREGATE_STATS;
        } else {
            return null;
        }
    }


    public String getFeatureFromRequest(FeatureConstraint featureConstraint) {
        FeatureConstraintType featureConstraintType =
                featureConstraint.getFeatureConstraintType();
        AthenaField name = featureConstraint.getFeatureName();

        if (featureConstraintType == FeatureConstraintType.FEATURE) {
            return name.getValue();
        } else if (featureConstraintType == FeatureConstraintType.INDEX) {
            return name.getValue();
        } else {
            //log.warn("not supported type :{}", featureConstraintType.toString());
            return null;
        }
    }

    public List<String> getFeatureFromRequestLogical(FeatureConstraint featureConstraint) {
        List<String> featureList = new ArrayList<>();
        List<TargetAthenaValue> valueList = featureConstraint.getDataRequestObjectValueList();

        String query = null;

        for (int i = 0; i < valueList.size(); i++) {
            if (!(valueList.get(i).getTargetAthenaValue() instanceof FeatureConstraint)) {
                log.warn("Rqeust type must be DataRequestOBject");
                return null;
            }


            // TODO: need to be validated

            FeatureConstraintOperatorType featureConstraintOperatorType = ((FeatureConstraint)
                    valueList.get(i).getTargetAthenaValue()).getFeatureConstraintOperatorType();
            if (featureConstraintOperatorType == FeatureConstraintOperatorType.COMPARABLE) {
                featureList.add(getFeatureFromRequest((FeatureConstraint) valueList.get(i).getTargetAthenaValue()));

            } else if (featureConstraintOperatorType == FeatureConstraintOperatorType.LOGICAL) {
                List<String> listOfFeatures = getFeatureFromRequestLogical(featureConstraint);
                featureList.addAll(listOfFeatures);
            }
//            featureList.add(getFeatureFromRequest((FeatureConstraint) valueList.get(i)));
        }

        return featureList;
    }


    /**
     * It supports features as INDEX and FEATURE!
     *
     * @param featureConstraint
     * @return
     */
    public Bson getBsonFromRequestOperatorComparison(FeatureConstraint featureConstraint) {

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
                    //comparator = "$eq";
                    break;
                case FeatureConstraintOperator.COMPARISON_GT:
                    obj = gt(target, value.get(i).getTargetAthenaValue());
                    //comparator = "$gt";
                    break;
                case FeatureConstraintOperator.COMPARISON_GTE:
                    obj = gte(target, value.get(i).getTargetAthenaValue());
                    //comparator = "$gte";
                    break;
                case FeatureConstraintOperator.COMPARISON_LT:
                    obj = lt(target, value.get(i).getTargetAthenaValue());
                    //comparator = "$lt";
                    break;
                case FeatureConstraintOperator.COMPARISON_LTE:
                    obj = lte(target, value.get(i).getTargetAthenaValue());
                    //comparator = "$lte";
                    break;
                case FeatureConstraintOperator.COMPARISON_NE:
                    obj = ne(target, value.get(i).getTargetAthenaValue());
                    //comparator = "$ne";
                    break;
                default:
                    log.warn("not supported comparsion type");
                    return null;
            }
        }
        return obj;
    }


    public Bson getBsonFromRequestOperatorLogical(FeatureConstraint featureConstraint) {
        List<Bson> innerObjectList = new ArrayList<>();
        Iterable<Bson> iterableFilters;
        FeatureConstraintOperator featureConstraintOperator = featureConstraint.getFeatureConstraintOperator();
        List<TargetAthenaValue> valueList = featureConstraint.getDataRequestObjectValueList();

        Bson query;

        for (int i = 0; i < valueList.size(); i++) {
            if (!(valueList.get(i).getTargetAthenaValue() instanceof FeatureConstraint)) {
                log.warn("Rqeust type must be DataRequestOBject");
                return null;
            }

            innerObjectList.add(generateBson((FeatureConstraint) valueList.get(i).getTargetAthenaValue()));
        }
        iterableFilters = innerObjectList;

        switch (featureConstraintOperator.getValue()) {
            case FeatureConstraintOperator.LOGICAL_AND:
                query = and(iterableFilters);
                break;
            case FeatureConstraintOperator.LOGICAL_OR:
                query = or(iterableFilters);
                break;
            case FeatureConstraintOperator.LOGICAL_IN:
//                query = in(iterableFilters);
            default:
                log.warn("not supported logical type");
                return null;
        }

        return query;
    }


    public String getValueFromrequestedObject(Object obj) {
        if (obj instanceof BigInteger) {
            return obj.toString();
        } else if (obj instanceof Integer) {
            return obj.toString();
        } else if (obj instanceof Long) {
            return obj.toString();

        } else if (obj instanceof Date) {
            Long tmp = ((Date) obj).getTime();
            return tmp.toString();

        } else if (obj instanceof InetAddress) {
            Integer addr = InetAddresses.coerceToInteger((InetAddress) obj);
            return addr.toString();
        } else {
            log.warn("input value has something problems");
            return null;
        }
    }


    public HashMap<String, Object> deriveUserDefinedFeatures(Document onlineFeature,
                                                             AthenaFeatureRequester athenaFeatureRequester) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> feature = new HashMap<>();

        /*
        if (athenaFeatureRequester.getListOfFeatures() == null) {
            //TODO return all of online features to user!
            return null;
        }
*/
        boolean check = false;
        Iterator it = onlineFeature.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            if (((String) pair.getKey()).endsWith(AthenaFeatureField.FEATURE)) {
                Iterator itI = ((Document) pair.getValue()).entrySet().iterator();
                while (itI.hasNext()) {
                    Map.Entry pairI = (Map.Entry) itI.next();
                    feature.put((String) pairI.getKey(), pairI.getValue());
                }
                data.put(AthenaFeatureField.FEATURE, feature);
            } else {
                data.put((String) pair.getKey(), pair.getValue());
            }
            /*
            if (((String) pair.getKey()).endsWith(AthenaFeatureField.FEATURE)) {
                Iterator itI = ((Document) pair.getValue()).entrySet().iterator();
                while (itI.hasNext()) {
                    Map.Entry pairI = (Map.Entry) itI.next();
                    data.put((String) pairI.getKey(), pairI.getValue());
                }
            } else {
                data.put((String) pair.getKey(), pair.getValue());
            }*/

        }

        return data;
    }


    public boolean isSatisfyOnlineEvent(Document onlineFeature, Document innerFeature,
                                        FeatureConstraint featureConstraint) {
        // FeatureConstraint featureConstraint =
        // onlineEventTable.getAthenaFeatureRequester().getFeatureConstraint();
        List<TargetAthenaValue> dataRequestObjectValueList = featureConstraint.getDataRequestObjectValueList();

        FeatureConstraintOperatorType featureConstraintOperatorType =
                featureConstraint.getFeatureConstraintOperatorType();

        if (!(dataRequestObjectValueList.size() > 0)) {
            return false;
        }

        if (featureConstraintOperatorType == FeatureConstraintOperatorType.COMPARABLE) {
            // added PIN_PAYLOAD_MATCH -- Jinwoo Kim, 2016/08/12
            if (featureConstraint.getFeatureConstraintType() == FeatureConstraintType.FEATURE) {
                innerFeature = (Document) onlineFeature.get(AthenaFeatureField.FEATURE);
            } else if (featureConstraint.getFeatureConstraintType() == FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH) {
                innerFeature = (Document) onlineFeature.get(AthenaFeatureField.PACKET_IN_PAYLOAD_MATCH);
            }
            return getSatisficationFromRequestOperatorComparison(onlineFeature, innerFeature, featureConstraint);

        } else if (featureConstraintOperatorType == FeatureConstraintOperatorType.LOGICAL) {
            return getSatisficationFromRequestOperatorLogical(onlineFeature, innerFeature, featureConstraint);

        } else {
            log.warn("not supported FeatureConstraintOperatorType");
        }

        return true;
    }

    public boolean getSatisficationFromRequestOperatorLogical(Document onlineFeature, Document innerFeature,
                                                              FeatureConstraint featureConstraint) {
        List<Boolean> innerSatisficationList = new ArrayList<>();
        FeatureConstraintOperator featureConstraintOperator = featureConstraint.getFeatureConstraintOperator();
        List<TargetAthenaValue> valueList = featureConstraint.getDataRequestObjectValueList();

        for (int i = 0; i < valueList.size(); i++) {
            if (!(valueList.get(i).getTargetAthenaValue() instanceof FeatureConstraint)) {
                log.warn("Rqeust type must be DataRequestOBject");
                return false;
            }
            innerSatisficationList
                    .add(isSatisfyOnlineEvent(onlineFeature, innerFeature,
                            (FeatureConstraint) valueList.get(i).getTargetAthenaValue()));
        }

        return checkLogicalComparisionFromResults(innerSatisficationList, featureConstraintOperator);
    }

    public boolean checkLogicalComparisionFromResults(List<Boolean> satisficationList,
                                                      FeatureConstraintOperator featureConstraintOperator) {
        int numOfTrue = 0;

        for (int i = 0; i < satisficationList.size(); i++) {
            if (satisficationList.get(i)) {
                numOfTrue++;
            }
        }

        switch (featureConstraintOperator.getValue()) {
            case FeatureConstraintOperator.LOGICAL_AND:
                return numOfTrue == satisficationList.size();

            case FeatureConstraintOperator.LOGICAL_OR:
                return numOfTrue > 0;

            case FeatureConstraintOperator.LOGICAL_IN:
            default:
                log.warn("not supported logical type");
                return false;
        }
    }

    public boolean getSatisficationFromRequestOperatorComparison(Document onlineFeature, Document innerFeature,
                                                                 FeatureConstraint featureConstraint) {

        FeatureConstraintType featureConstraintType =
                featureConstraint.getFeatureConstraintType();
        FeatureConstraintOperator featureConstraintOperator = featureConstraint.getFeatureConstraintOperator();
        AthenaField name = featureConstraint.getFeatureName();
        List<TargetAthenaValue> value = featureConstraint.getDataRequestObjectValueList();

        String target = null;
        Object targetValue = null;
        if (featureConstraintType == FeatureConstraintType.FEATURE ||
                featureConstraintType == FeatureConstraintType.PACKET_IN_PAYLOAD_MATCH) {
            target = name.getValue();
            targetValue = innerFeature.get(target);
        } else if (featureConstraintType == FeatureConstraintType.INDEX) {
            target = name.getValue();
            targetValue = onlineFeature.get(target);
        } else {
            log.warn("not supported type :{}", featureConstraintType.toString());
            return false;
        }

        if (targetValue == null) {
            return false;
        }


        if (!(value.size() > 0)) {
            log.warn("list size is not bigger than 0 :{}", value.size());
            return false;
        }

        for (int i = 0; i < value.size(); i++) {
            boolean result = comparisonBetweenObjectWithOperator(targetValue,
                    value.get(i).getTargetAthenaValue(), featureConstraintOperator);

            if (!result) {
                return false;
            }
        }
        return true;
    }

    // always convert to Long value
    public boolean comparisonBetweenObjectWithOperator(Object obj1, Object obj2,
                                                       FeatureConstraintOperator featureConstraintOperator) {
        Long longObj1 = convertObjectValueToLong(obj1);
        Long longObj2 = convertObjectValueToLong(obj2);

        if (longObj1 == null || longObj2 == null) {
            log.warn("comparsionBetwenObjectWithOperator: obj1 and obj2 MUST NOT BE null. Cannot compare!");
            return false;
        }
        return comparisonBetweenObjectWithOperatorLong(longObj1, longObj2, featureConstraintOperator);
    }

    public Long convertObjectValueToLong(Object obj) {
        Long returnValue = null;
        if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Integer) {
            returnValue = new Long(((Integer) obj).longValue());
            return returnValue;
        } else if (obj instanceof Short) { //Short, Byte buf fix -- 2016/08/13, Jinwoo Kim
            returnValue = new Long(((Short) obj).longValue());
            return returnValue;
        } else if (obj instanceof Byte) {
            returnValue = new Long(((Byte) obj).longValue());
            return returnValue;
        }
        log.warn("convertObjectValueToLong: obj MUST be Long or Integer");
        return null;
    }

    public boolean comparisonBetweenObjectWithOperatorLong(Long obj1, Long obj2,
                                                           FeatureConstraintOperator featureConstraintOperator) {
        switch (featureConstraintOperator.getValue()) {
            case FeatureConstraintOperator.COMPARISON_EQ:
                if (obj1.longValue() == obj2.longValue()) {
                    return true;
                }
                break;
            case FeatureConstraintOperator.COMPARISON_GT:
                if (obj1.longValue() > obj2.longValue()) {
                    return true;
                }
                break;
            case FeatureConstraintOperator.COMPARISON_GTE:
                if (obj1.longValue() >= obj2.longValue()) {
                    return true;
                }
                break;
            case FeatureConstraintOperator.COMPARISON_LT:
                if (obj1.longValue() < obj2.longValue()) {
                    return true;
                }
                break;
            case FeatureConstraintOperator.COMPARISON_LTE:
                if (obj1.longValue() <= obj2.longValue()) {
                    return true;
                }
                break;
            case FeatureConstraintOperator.COMPARISON_NE:
                if (obj1.longValue() != obj2.longValue()) {
                    return true;
                }
                break;
            default:
                log.warn("not supported comparsion type");
                return false;
        }
        return false;
    }


}
