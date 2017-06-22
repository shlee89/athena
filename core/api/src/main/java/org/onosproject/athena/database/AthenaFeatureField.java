package org.onosproject.athena.database;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by seunghyeon on 8/31/15.
 */
public class AthenaFeatureField implements AthenaField, Serializable {

    List<String> listOfFeatures = new ArrayList<>();
    HashMap<String, String> featureTypeOnDatabase = new HashMap<>();
    List<String> listOftables = new ArrayList<>();

    public String varintType = "varint";
    public String boolType = "bool";
    public String stringType = "string";
    public String bigintType = "bigint";
    public String doubleType = "double";
    public String timestampType = "timestamp";

    private String value;


    public List<String> getListOfFeatureTable(String table) {
        if (table.startsWith(AGGREGATE_STATS)) {
            return getListOfFAggregatedStatseatures();
        } else if (table.startsWith(PORT_STATS)) {
            return getListOfFPortStatseatures();
        } else if (table.startsWith(TABLE_STATS)) {
            return getListOfFTableStatseatures();
        } else if (table.startsWith(QUEUE_STATS)) {
            return getListOfFQueueStatseatures();
        } else if (table.startsWith(FLOW_STATS)) {
            return getListOfFFlowStatseatures();
        } else if (table.startsWith(PORT_STATUS)) {
            return getListOfFPortStatuseatures();
        } else if (table.startsWith(PACKET_IN)) {
            return getListOfFPacketIneatures();
        } else if (table.startsWith(FLOW_REMOVED)) {
            return getListOfFlowRemovedFeatures();
        } else if (table.startsWith(ERROR_MSG)) {
            return getListOfErrorMsgFeatures();
        } else {
            return null;
        }
    }

    public AthenaFeatureField() {
        listOfFeatures.add(FEATURE);

        listOfFeatures.add(ERROR_MSG_ERRTYPE);
        featureTypeOnDatabase.put(ERROR_MSG_ERRTYPE, varintType);

        listOfFeatures.add(FLOW_REMOVED_REASON);
        featureTypeOnDatabase.put(FLOW_REMOVED_REASON, varintType);
        listOfFeatures.add(FLOW_REMOVED_DURATION_SECOND);
        featureTypeOnDatabase.put(FLOW_REMOVED_DURATION_SECOND, bigintType);
        listOfFeatures.add(FLOW_REMOVED_DURATION_N_SECOND);
        featureTypeOnDatabase.put(FLOW_REMOVED_DURATION_N_SECOND, bigintType);
        listOfFeatures.add(FLOW_REMOVED_IDLE_TIMEOUT);
        featureTypeOnDatabase.put(FLOW_REMOVED_IDLE_TIMEOUT, varintType);
        listOfFeatures.add(FLOW_REMOVED_HARD_TIMEOUT);
        featureTypeOnDatabase.put(FLOW_REMOVED_HARD_TIMEOUT, varintType);
        listOfFeatures.add(FLOW_REMOVED_PACKET_COUNT);
        featureTypeOnDatabase.put(FLOW_REMOVED_PACKET_COUNT, bigintType);
        listOfFeatures.add(FLOW_REMOVED_BYTE_COUNT);
        featureTypeOnDatabase.put(FLOW_REMOVED_BYTE_COUNT, bigintType);
        listOfFeatures.add(FLOW_REMOVED_PACKET_PER_DURATION);
        featureTypeOnDatabase.put(FLOW_REMOVED_PACKET_PER_DURATION, doubleType);
        listOfFeatures.add(FLOW_REMOVED_BYTE_PER_DURATION);
        featureTypeOnDatabase.put(FLOW_REMOVED_BYTE_PER_DURATION, doubleType);

        listOfFeatures.add(PACKET_IN_TOTAL_LEN);
        featureTypeOnDatabase.put(PACKET_IN_TOTAL_LEN, varintType);
        listOfFeatures.add(PACKET_IN_REASON);
        featureTypeOnDatabase.put(PACKET_IN_REASON, varintType);
        listOfFeatures.add(PACKET_IN_IN_PORT);
        featureTypeOnDatabase.put(PACKET_IN_IN_PORT, varintType);
        listOfFeatures.add(PACKET_IN_IN_PHY_PORT);
        featureTypeOnDatabase.put(PACKET_IN_IN_PHY_PORT, varintType);
        listOfFeatures.add(PACKET_IN_PAYLOAD_MATCH);
        featureTypeOnDatabase.put(PACKET_IN_PAYLOAD_MATCH, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_ETH_DST);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_ETH_DST, bigintType);
        listOfFeatures.add(PACKET_IN_MATCH_ETH_SRC);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_ETH_SRC, bigintType);
        listOfFeatures.add(PACKET_IN_MATCH_ETH_TYPE);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_ETH_TYPE, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_VLAN_VID);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_VLAN_VID, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_IPV4_SRC);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_IPV4_SRC, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_IPV4_DST);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_IPV4_DST, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_IP_DSCP);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_IP_DSCP, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_IP_ECN);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_IP_ECN, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_IP_PROTO);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_IP_PROTO, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_TCP_SRC);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_TCP_SRC, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_TCP_DST);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_TCP_DST, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_UDP_SRC);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_UDP_SRC, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_UDP_DST);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_UDP_DST, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV4_CODE);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_ICMPV4_CODE, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV4_TYPE);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_ICMPV4_TYPE, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_IPV6_SRC);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_IPV6_SRC, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_IPV6_DST);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_IPV6_DST, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV6_CODE);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_ICMPV6_CODE, varintType);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV6_TYPE);
        featureTypeOnDatabase.put(PACKET_IN_MATCH_ICMPV6_TYPE, varintType);

        listOfFeatures.add(PORT_STATUS_REASON);
        featureTypeOnDatabase.put(PORT_STATUS_REASON, varintType);

        listOfFeatures.add(FLOW_STATS_DURATION_SEC);
        featureTypeOnDatabase.put(FLOW_STATS_DURATION_SEC, bigintType);
        listOfFeatures.add(FLOW_STATS_DURATION_N_SEC);
        featureTypeOnDatabase.put(FLOW_STATS_DURATION_N_SEC, bigintType);
        listOfFeatures.add(FLOW_STATS_PRIORITY);
        featureTypeOnDatabase.put(FLOW_STATS_PRIORITY, varintType);
        listOfFeatures.add(FLOW_STATS_IDLE_TIMEOUT);
        featureTypeOnDatabase.put(FLOW_STATS_IDLE_TIMEOUT, varintType);
        listOfFeatures.add(FLOW_STATS_HARD_TIMEOUT);
        featureTypeOnDatabase.put(FLOW_STATS_HARD_TIMEOUT, varintType);
        listOfFeatures.add(FLOW_STATS_PACKET_COUNT);
        featureTypeOnDatabase.put(FLOW_STATS_PACKET_COUNT, bigintType);
        listOfFeatures.add(FLOW_STATS_BYTE_COUNT);
        featureTypeOnDatabase.put(FLOW_STATS_BYTE_COUNT, bigintType);
        listOfFeatures.add(FLOW_STATS_ACTION_OUTPUT);
        featureTypeOnDatabase.put(FLOW_STATS_ACTION_OUTPUT, boolType);
        listOfFeatures.add(FLOW_STATS_ACTION_OUTPUT_PORT);
        featureTypeOnDatabase.put(FLOW_STATS_ACTION_OUTPUT_PORT, varintType);
        listOfFeatures.add(FLOW_STATS_ACTION_DROP);
        featureTypeOnDatabase.put(FLOW_STATS_ACTION_DROP, boolType);
        listOfFeatures.add(FLOW_STATS_PACKET_COUNT_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_PACKET_COUNT_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_BYTE_COUNT_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_BYTE_COUNT_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_PACKET);
        featureTypeOnDatabase.put(FLOW_STATS_BYTE_PER_PACKET, doubleType);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_PACKET_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_BYTE_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_PACKET_PER_DURATION);
        featureTypeOnDatabase.put(FLOW_STATS_PACKET_PER_DURATION, doubleType);
        listOfFeatures.add(FLOW_STATS_PACKET_PER_DURATION_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_PACKET_PER_DURATION_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_DURATION);
        featureTypeOnDatabase.put(FLOW_STATS_BYTE_PER_DURATION, doubleType);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_DURATION_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_BYTE_PER_DURATION_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_TOTAL_FLOWS);
        featureTypeOnDatabase.put(FLOW_STATS_TOTAL_FLOWS, doubleType);
        listOfFeatures.add(FLOW_STATS_TOTAL_FLOWS_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_TOTAL_FLOWS_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_PAIR_FLOW);
        featureTypeOnDatabase.put(FLOW_STATS_PAIR_FLOW, boolType);
        listOfFeatures.add(FLOW_STATS_PAIR_FLOW_RATIO);
        featureTypeOnDatabase.put(FLOW_STATS_PAIR_FLOW_RATIO, doubleType);
        listOfFeatures.add(FLOW_STATS_PAIR_FLOW_RATIO_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_PAIR_FLOW_RATIO_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_TOTAL_SINGLE_FLOW);
        featureTypeOnDatabase.put(FLOW_STATS_TOTAL_SINGLE_FLOW, doubleType);
        listOfFeatures.add(FLOW_STATS_TOTAL_SINGLE_FLOW_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_TOTAL_SINGLE_FLOW_VAR, doubleType);
        listOfFeatures.add(FLOW_STATS_TOTAL_PAIR_FLOW);
        featureTypeOnDatabase.put(FLOW_STATS_TOTAL_PAIR_FLOW, doubleType);
        listOfFeatures.add(FLOW_STATS_TOTAL_PAIR_FLOW_VAR);
        featureTypeOnDatabase.put(FLOW_STATS_TOTAL_PAIR_FLOW_VAR, doubleType);


        listOfFeatures.add(QUEUE_STATS_TX_BYTES);
        featureTypeOnDatabase.put(QUEUE_STATS_TX_BYTES, bigintType);
        listOfFeatures.add(QUEUE_STATS_TX_PACKETS);
        featureTypeOnDatabase.put(QUEUE_STATS_TX_PACKETS, bigintType);
        listOfFeatures.add(QUEUE_STATS_TX_ERRORS);
        featureTypeOnDatabase.put(QUEUE_STATS_TX_ERRORS, bigintType);

        listOfFeatures.add(TABLE_STATS_MAX_ENTIRES);
        featureTypeOnDatabase.put(TABLE_STATS_MAX_ENTIRES, bigintType);
        listOfFeatures.add(TABLE_STATS_ACTIVE_COUNT);
        featureTypeOnDatabase.put(TABLE_STATS_ACTIVE_COUNT, bigintType);
        listOfFeatures.add(TABLE_STATS_LOOKUP_COUNT);
        featureTypeOnDatabase.put(TABLE_STATS_LOOKUP_COUNT, bigintType);
        listOfFeatures.add(TABLE_STATS_MATCHED_COUNT);
        featureTypeOnDatabase.put(TABLE_STATS_MATCHED_COUNT, bigintType);
        listOfFeatures.add(TABLE_STATS_MATCHED_PER_LOOKUP);
        featureTypeOnDatabase.put(TABLE_STATS_MATCHED_PER_LOOKUP, doubleType);
        listOfFeatures.add(TABLE_STATS_ACTIVE_PER_MAX);
        featureTypeOnDatabase.put(TABLE_STATS_ACTIVE_PER_MAX, doubleType);
        listOfFeatures.add(TABLE_STATS_LOOKUP_PER_ACTIVE);
        featureTypeOnDatabase.put(TABLE_STATS_LOOKUP_PER_ACTIVE, doubleType);
        listOfFeatures.add(TABLE_STATS_MATCHED_PER_ACTIVE);
        featureTypeOnDatabase.put(TABLE_STATS_MATCHED_PER_ACTIVE, doubleType);

        listOfFeatures.add(AGGREGATE_STATS_PACKET_COUNT);
        featureTypeOnDatabase.put(AGGREGATE_STATS_PACKET_COUNT, bigintType);
        listOfFeatures.add(AGGREGATE_STATS_BYTE_COUNT);
        featureTypeOnDatabase.put(AGGREGATE_STATS_BYTE_COUNT, bigintType);
        listOfFeatures.add(AGGREGATE_STATS_FLOW_COUNT);
        featureTypeOnDatabase.put(AGGREGATE_STATS_FLOW_COUNT, bigintType);
        featureNameRest();
        addtables();
    }

    public void featureNameRest() {
        listOfFeatures.add(PORT_STATS_RX_PACKETS);
        featureTypeOnDatabase.put(PORT_STATS_RX_PACKETS, bigintType);
        listOfFeatures.add(PORT_STATS_TX_PACKETS);
        featureTypeOnDatabase.put(PORT_STATS_TX_PACKETS, bigintType);
        listOfFeatures.add(PORT_STATS_RX_BYTES);
        featureTypeOnDatabase.put(PORT_STATS_RX_BYTES, bigintType);
        listOfFeatures.add(PORT_STATS_TX_BYTES);
        featureTypeOnDatabase.put(PORT_STATS_TX_BYTES, bigintType);
        listOfFeatures.add(PORT_STATS_RX_DROPPED);
        featureTypeOnDatabase.put(PORT_STATS_RX_DROPPED, bigintType);
        listOfFeatures.add(PORT_STATS_TX_DROPPED);
        featureTypeOnDatabase.put(PORT_STATS_TX_DROPPED, bigintType);
        listOfFeatures.add(PORT_STATS_RX_ERRORS);
        featureTypeOnDatabase.put(PORT_STATS_RX_ERRORS, bigintType);
        listOfFeatures.add(PORT_STATS_TX_ERRORS);
        featureTypeOnDatabase.put(PORT_STATS_TX_ERRORS, bigintType);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERROR);
        featureTypeOnDatabase.put(PORT_STATS_RX_FRAME_ERROR, bigintType);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERROR);
        featureTypeOnDatabase.put(PORT_STATS_RX_OVER_ERROR, bigintType);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERROR);
        featureTypeOnDatabase.put(PORT_STATS_RX_CRC_ERROR, bigintType);
        listOfFeatures.add(PORT_STATS_COLLISIONS);
        featureTypeOnDatabase.put(PORT_STATS_COLLISIONS, bigintType);
        listOfFeatures.add(PORT_STATS_TX_PACKETS_VAR);
        featureTypeOnDatabase.put(PORT_STATS_TX_PACKETS_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_BYTES_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_BYTES_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_TX_BYTES_VAR);
        featureTypeOnDatabase.put(PORT_STATS_TX_BYTES_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_DROPPED_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_VAR);
        featureTypeOnDatabase.put(PORT_STATS_TX_DROPPED_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_ERRORS_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_ERRORS_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_TX_ERRORS_VAR);
        featureTypeOnDatabase.put(PORT_STATS_TX_ERRORS_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERR_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_FRAME_ERR_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERR_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_OVER_ERR_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERR_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_CRC_ERR_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_BYTE_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_RX_BYTE_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_RX_BYTE_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_BYTE_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_TX_BYTE_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_TX_BYTE_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_TX_BYTE_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_TX_BYTE_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_RX_DROPPED_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_DROPPED_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_TX_DROPPED_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_TX_DROPPED_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_ERROR_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_RX_ERROR_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_RX_ERROR_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_ERROR_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_TX_ERROR_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_TX_ERROR_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_TX_ERROR_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_TX_ERROR_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERR_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_RX_FRAME_ERR_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERR_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_FRAME_ERR_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERR_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_RX_OVER_ERR_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERR_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_OVER_ERR_PER_PACKET_VAR, doubleType);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERR_PER_PACKET);
        featureTypeOnDatabase.put(PORT_STATS_RX_CRC_ERR_PER_PACKET, doubleType);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERR_PER_PACKET_VAR);
        featureTypeOnDatabase.put(PORT_STATS_RX_CRC_ERR_PER_PACKET_VAR, doubleType);
    }

    public void addtables() {
        listOftables.add(ERROR_MSG);
        listOftables.add(FLOW_REMOVED);
        listOftables.add(PACKET_IN);
        listOftables.add(PACKET_IN_MATCH);
        listOftables.add(PORT_STATUS);
        listOftables.add(FLOW_STATS);
        listOftables.add(QUEUE_STATS);
        listOftables.add(TABLE_STATS);
        listOftables.add(AGGREGATE_STATS);
        listOftables.add(PORT_STATS);
    }

    public boolean isElementofTables(String table) {
        return listOftables.contains(table);
    }


    public AthenaFeatureField(String value) {
        this.value = value;
    }

    @Override
    public String getTypeOnDatabase(String feature) {
        if (featureTypeOnDatabase.containsKey(feature)) {
            return featureTypeOnDatabase.get(feature);
        } else {
            return null;
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getDescription() {
        return "The name of Features on Database";
    }

    @Override
    public boolean isElements(String str) {

        for (int i = 0; i < listOfFeatures.size(); i++) {
            if (str.startsWith(listOfFeatures.get(i))) {
                return true;
            }
        }

        return false;
    }


    public List<String> getListOfFeatures() {
        return listOfFeatures;
    }

    public static final String FEATURE = "feature";

    public static final String ERROR_MSG = "EMessage";
    public static final String ERROR_MSG_ERRTYPE = "EMerrorType";

    public static final String FLOW_REMOVED = "FRemoved";
    public static final String FLOW_REMOVED_REASON = "FRreason";
    public static final String FLOW_REMOVED_DURATION_SECOND = "FRdurationSec";
    public static final String FLOW_REMOVED_DURATION_N_SECOND = "FRdurationNSec";
    public static final String FLOW_REMOVED_IDLE_TIMEOUT = "FRidleTimeout";
    public static final String FLOW_REMOVED_HARD_TIMEOUT = "FRhardTimeout";
    public static final String FLOW_REMOVED_PACKET_COUNT = "FRpacketCount";
    public static final String FLOW_REMOVED_BYTE_COUNT = "FRbyteCount";
    //rich
    //PACKET_COUNT / DURATION_N_SEC
    public static final String FLOW_REMOVED_PACKET_PER_DURATION = "FRpacketPerDuration";
    //BYTE_COUNT / DURATION_N_SEC
    public static final String FLOW_REMOVED_BYTE_PER_DURATION = "FRbytePerDuration";

    public static final String PACKET_IN = "Pin";
    public static final String PACKET_IN_TOTAL_LEN = "PItotalLen";
    public static final String PACKET_IN_REASON = "PIreason";
    /*
    todo to be removed
     */
    public static final String PACKET_IN_IN_PORT = "PIinPort";
    /*
    TODO to be removed
    */
    public static final String PACKET_IN_IN_PHY_PORT = "PIinPhyPort";
    public static final String PACKET_IN_PAYLOAD_MATCH = "PIinPayloadMatch";
    /**
     * Current available Match features are described in FreatureCollectorProvider.
     */
    public static final String PACKET_IN_MATCH = "PIMatch";
    public static final String PACKET_IN_MATCH_ETH_DST = "PIMethDst";
    public static final String PACKET_IN_MATCH_ETH_SRC = "PIMethSrc";
    public static final String PACKET_IN_MATCH_ETH_TYPE = "PIMethType";
    public static final String PACKET_IN_MATCH_VLAN_VID = "PIMvlanVid";
    public static final String PACKET_IN_MATCH_IPV4_SRC = "PIMipv4Src";
    public static final String PACKET_IN_MATCH_IPV4_DST = "PIMipv4Dst";
    public static final String PACKET_IN_MATCH_IP_DSCP = "PIMipDscp";
    public static final String PACKET_IN_MATCH_IP_ECN = "PIMipEcn"; //spelling miss - By Jinwoo
    public static final String PACKET_IN_MATCH_IP_PROTO = "PIMipProto";
    public static final String PACKET_IN_MATCH_TCP_SRC = "PIMtcpSrc";
    public static final String PACKET_IN_MATCH_TCP_DST = "PIMtcpDst";
    public static final String PACKET_IN_MATCH_UDP_SRC = "PIMudpSrc";
    public static final String PACKET_IN_MATCH_UDP_DST = "PIMudpDst";
    public static final String PACKET_IN_MATCH_ICMPV4_CODE = "PIMicmpv4Code";
    public static final String PACKET_IN_MATCH_ICMPV4_TYPE = "PIMicmpv4Type";
    public static final String PACKET_IN_MATCH_IPV6_SRC = "PIMipv6Src";
    public static final String PACKET_IN_MATCH_IPV6_DST = "PIMipv6Dst";
    public static final String PACKET_IN_MATCH_ICMPV6_CODE = "PIMicmpv6Code";
    public static final String PACKET_IN_MATCH_ICMPV6_TYPE = "PIMicmpv6Type";

    public static final String PORT_STATUS = "PStatus";
    public static final String PORT_STATUS_REASON = "PSreason";

    public static final String FLOW_STATS = "FStatistics";
    public static final String FLOW_STATS_DURATION_SEC = "FSSdurationSec";
    public static final String FLOW_STATS_DURATION_N_SEC = "FSSdurationNSec";
    public static final String FLOW_STATS_PRIORITY = "FSSpriority";
    public static final String FLOW_STATS_IDLE_TIMEOUT = "FSSidleTimeout";
    public static final String FLOW_STATS_HARD_TIMEOUT = "FSShardTimeout";
    public static final String FLOW_STATS_PACKET_COUNT = "FSSpacketCount";
    public static final String FLOW_STATS_BYTE_COUNT = "FSSbyteCount";
    public static final String FLOW_STATS_ACTION_OUTPUT = "FSSactionOutput";
    public static final String FLOW_STATS_ACTION_OUTPUT_PORT = "FSSactionOutputPort";
    public static final String FLOW_STATS_ACTION_DROP = "FSSActionDrop";

    //rich
    public static final String FLOW_STATS_PACKET_COUNT_VAR = "FSSpacketCountVar";
    public static final String FLOW_STATS_BYTE_COUNT_VAR = "FSSbyteCountVar";
    // BYTE_COUNT / PACKET_COUNT
    public static final String FLOW_STATS_BYTE_PER_PACKET = "FSSbytePerPacket";
    // BYTE_COUNT_VAR / PACKET_COUNT_VAR
    public static final String FLOW_STATS_BYTE_PER_PACKET_VAR = "FSSbytePerPacketVar";
    // PACKET_COUNT / DURATION_SEC + DURATION_N_SEC
    public static final String FLOW_STATS_PACKET_PER_DURATION = "FSSpacketPerDuration";
    // PACKET_COUNT_VAR / DURATION_DIFF
    public static final String FLOW_STATS_PACKET_PER_DURATION_VAR = "FSSpacketPerDurationVar";
    // BYTE_COUNT / DURATION_SEC + DURATION_N_SEC
    public static final String FLOW_STATS_BYTE_PER_DURATION = "FSSbytePerDuration";
    // BYTE_COUNT_VAR / DURATION_DIFF
    public static final String FLOW_STATS_BYTE_PER_DURATION_VAR = "FSSbytePerDurationVar";
    public static final String FLOW_STATS_TOTAL_FLOWS = "FSSTotalFlows";
    public static final String FLOW_STATS_TOTAL_FLOWS_VAR = "FSSTotalFlowsVar";
    public static final String FLOW_STATS_PAIR_FLOW = "FSSPairFlow";
    public static final String FLOW_STATS_PAIR_FLOW_RATIO = "FSSPairFlowRatio";
    public static final String FLOW_STATS_PAIR_FLOW_RATIO_VAR = "FSSPairFlowRatioVar";
    public static final String FLOW_STATS_TOTAL_SINGLE_FLOW = "FSSTotalSingleFlow";
    public static final String FLOW_STATS_TOTAL_SINGLE_FLOW_VAR = "FSSTotalSingleFlowVar";
    public static final String FLOW_STATS_TOTAL_PAIR_FLOW = "FSSTotalPairFlow";
    public static final String FLOW_STATS_TOTAL_PAIR_FLOW_VAR = "FSSTotalPairFlowVar";


    public static final String QUEUE_STATS = "Qstatistics";
    public static final String QUEUE_STATS_TX_BYTES = "QSStxBytes";
    public static final String QUEUE_STATS_TX_PACKETS = "QSStxPackets";
    public static final String QUEUE_STATS_TX_ERRORS = "QSStxErrors";

    public static final String TABLE_STATS = "TStatistics";
    public static final String TABLE_STATS_MAX_ENTIRES = "TSSmaxEntries";
    public static final String TABLE_STATS_ACTIVE_COUNT = "TSSactiveCount";
    public static final String TABLE_STATS_LOOKUP_COUNT = "TSSlookupCount";
    public static final String TABLE_STATS_MATCHED_COUNT = "TSSmatchedCount";
    //rich
    //MATCHED_COUNT / LOOKUP_COUNT
    public static final String TABLE_STATS_MATCHED_PER_LOOKUP = "TSSmatchedPerLookup";
    // ACTIVE_COUNT / MAX_ENTRIES
    public static final String TABLE_STATS_ACTIVE_PER_MAX = "TSSactivePerMax";
    // LOOKUP_COUNT / ACTIVE_COUNT
    public static final String TABLE_STATS_LOOKUP_PER_ACTIVE = "TSSlookupPerActive";
    // MATCHED_COUNT / ACTIVE_COUNT
    public static final String TABLE_STATS_MATCHED_PER_ACTIVE = "TSSmatchedPerActive";

    public static final String AGGREGATE_STATS = "AStatistics";
    public static final String AGGREGATE_STATS_PACKET_COUNT = "ASSpacketCount";
    public static final String AGGREGATE_STATS_BYTE_COUNT = "ASSbyteCount";
    public static final String AGGREGATE_STATS_FLOW_COUNT = "ASSflowCount";

    public static final String PORT_STATS = "PStatistics";
    public static final String PORT_STATS_RX_PACKETS = "PSSrxPackets";
    public static final String PORT_STATS_TX_PACKETS = "PSStxPackets";
    public static final String PORT_STATS_RX_BYTES = "PSSrxBytes";
    public static final String PORT_STATS_TX_BYTES = "PSStxBytes";
    public static final String PORT_STATS_RX_DROPPED = "PSSrxDropped";
    public static final String PORT_STATS_TX_DROPPED = "PSStxDropped";
    public static final String PORT_STATS_RX_ERRORS = "PSSrxErrors";
    public static final String PORT_STATS_TX_ERRORS = "PSStxErrors";
    public static final String PORT_STATS_RX_FRAME_ERROR = "PSSrxFrameError";
    public static final String PORT_STATS_RX_OVER_ERROR = "PSSrxOverError";
    public static final String PORT_STATS_RX_CRC_ERROR = "PSSrxCrcError";
    public static final String PORT_STATS_COLLISIONS = "PSScollisions";
    //rich
    public static final String PORT_STATS_RX_PACKETS_VAR = "PSSrxPacketsVar";
    public static final String PORT_STATS_TX_PACKETS_VAR = "PSStxPacketsVar";
    public static final String PORT_STATS_RX_BYTES_VAR = "PSSrxBytesVar";
    public static final String PORT_STATS_TX_BYTES_VAR = "PSStxBytesVar";
    public static final String PORT_STATS_RX_DROPPED_VAR = "PSSrxDropeedVar";
    public static final String PORT_STATS_TX_DROPPED_VAR = "PSStxDropeedVar";
    public static final String PORT_STATS_RX_ERRORS_VAR = "PSSrxErrosVar";
    public static final String PORT_STATS_TX_ERRORS_VAR = "PSStxErrosVar";
    public static final String PORT_STATS_RX_FRAME_ERR_VAR = "PSSrxFrameErrVar";
    public static final String PORT_STATS_RX_OVER_ERR_VAR = "PSSrxOverErrVar";
    public static final String PORT_STATS_RX_CRC_ERR_VAR = "PSSrxCrcErrVar";
    public static final String PORT_STATS_COLLISIONS_VAR = "PSScollisionsVar";
    //RX_BYTE / RX_PACKET
    public static final String PORT_STATS_RX_BYTE_PER_PACKET = "PSSrxBytePerPacket";
    //RX_BYTE_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_BYTE_PER_PACKET_VAR = "PSSrxBytePerPacketVar";
    //TX_BYTE / TX_PACKET
    public static final String PORT_STATS_TX_BYTE_PER_PACKET = "PSStxBytePerPacket";
    //TX_BYTE_VAR / TX_PACKET_VAR
    public static final String PORT_STATS_TX_BYTE_PER_PACKET_VAR = "PSStxBytePerPacketVar";
    //RX_DROPPED / RX_PACKET
    public static final String PORT_STATS_RX_DROPPED_PER_PACKET = "PSSrxDroppedPerPacket";
    //RX_DROPPED_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_DROPPED_PER_PACKET_VAR = "PSSrxDroppedPerPacketVar";
    //TX_DROPPED / TX_PACKET
    public static final String PORT_STATS_TX_DROPPED_PER_PACKET = "PSStxDroppedPerPacket";
    //TX_DROPPED_VAR / TX_PACKET_VAR
    public static final String PORT_STATS_TX_DROPPED_PER_PACKET_VAR = "PSStxDroppedPerPacketVar";
    //RX_ERRORS / RX_PACKET
    public static final String PORT_STATS_RX_ERROR_PER_PACKET = "PSSrxErrorPerPacket";
    //RX_ERRORS_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_ERROR_PER_PACKET_VAR = "PSSrxErrorPerPacketVar";
    //TX_ERRORS / TX_PACKET
    public static final String PORT_STATS_TX_ERROR_PER_PACKET = "PSStxErrorPerPacket";
    //TX_ERRORS_VAR / TX_PACKET_VAR
    public static final String PORT_STATS_TX_ERROR_PER_PACKET_VAR = "PSStxErrorPerPacketVar";
    //RX_FRAME_ERROR / RX_PACKET
    public static final String PORT_STATS_RX_FRAME_ERR_PER_PACKET = "PSSrxFrameErrPerPacket";
    //RX_FRAME_ERROR_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_FRAME_ERR_PER_PACKET_VAR = "PSSrxFrameErrPerPacketVar";
    //RX_OVER_ERROR / RX_PACKET
    public static final String PORT_STATS_RX_OVER_ERR_PER_PACKET = "PSSrxOverErrPerPacket";
    //RX_OVER_ERROR_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_OVER_ERR_PER_PACKET_VAR = "PSSrxOverErrPerPacketVar";
    //RX_CRC_ERROR / RX_PACKET
    public static final String PORT_STATS_RX_CRC_ERR_PER_PACKET = "PSSrxCrcErrPerPacket";
    //RX_CRC_ERROR_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_CRC_ERR_PER_PACKET_VAR = "PSSrxCrcErrPerPacketVar";


    public List<String> getListOfErrorMsgFeatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(ERROR_MSG_ERRTYPE);
        return listOfFeatures;
    }

    public List<String> getListOfFlowRemovedFeatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(FLOW_REMOVED_REASON);
        listOfFeatures.add(FLOW_REMOVED_DURATION_SECOND);
        listOfFeatures.add(FLOW_REMOVED_DURATION_N_SECOND);
        listOfFeatures.add(FLOW_REMOVED_IDLE_TIMEOUT);
        listOfFeatures.add(FLOW_REMOVED_HARD_TIMEOUT);
        listOfFeatures.add(FLOW_REMOVED_PACKET_COUNT);
        listOfFeatures.add(FLOW_REMOVED_BYTE_COUNT);
        listOfFeatures.add(FLOW_REMOVED_PACKET_PER_DURATION);
        listOfFeatures.add(FLOW_REMOVED_BYTE_PER_DURATION);
        return listOfFeatures;
    }

    public List<String> getListOfFPacketIneatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(PACKET_IN_TOTAL_LEN);
        listOfFeatures.add(PACKET_IN_REASON);
        listOfFeatures.add(PACKET_IN_IN_PORT);
        listOfFeatures.add(PACKET_IN_IN_PHY_PORT);
        listOfFeatures.add(PACKET_IN_PAYLOAD_MATCH);
        listOfFeatures.add(PACKET_IN_MATCH_ETH_DST);
        listOfFeatures.add(PACKET_IN_MATCH_ETH_SRC);
        listOfFeatures.add(PACKET_IN_MATCH_ETH_TYPE);
        listOfFeatures.add(PACKET_IN_MATCH_VLAN_VID);
        listOfFeatures.add(PACKET_IN_MATCH_IPV4_SRC);
        listOfFeatures.add(PACKET_IN_MATCH_IPV4_DST);
        listOfFeatures.add(PACKET_IN_MATCH_IP_DSCP);
        listOfFeatures.add(PACKET_IN_MATCH_IP_ECN);
        listOfFeatures.add(PACKET_IN_MATCH_IP_PROTO);
        listOfFeatures.add(PACKET_IN_MATCH_TCP_SRC);
        listOfFeatures.add(PACKET_IN_MATCH_TCP_DST);
        listOfFeatures.add(PACKET_IN_MATCH_UDP_SRC);
        listOfFeatures.add(PACKET_IN_MATCH_UDP_DST);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV4_CODE);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV4_TYPE);
        listOfFeatures.add(PACKET_IN_MATCH_IPV6_SRC);
        listOfFeatures.add(PACKET_IN_MATCH_IPV6_DST);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV6_CODE);
        listOfFeatures.add(PACKET_IN_MATCH_ICMPV6_TYPE);
        return listOfFeatures;
    }

    public List<String> getListOfFPortStatuseatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(PORT_STATUS_REASON);
        return listOfFeatures;
    }

    public List<String> getListOfFFlowStatseatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(FLOW_STATS_DURATION_SEC);
        listOfFeatures.add(FLOW_STATS_DURATION_N_SEC);
        listOfFeatures.add(FLOW_STATS_PRIORITY);
        listOfFeatures.add(FLOW_STATS_IDLE_TIMEOUT);
        listOfFeatures.add(FLOW_STATS_HARD_TIMEOUT);
        listOfFeatures.add(FLOW_STATS_PACKET_COUNT);
        listOfFeatures.add(FLOW_STATS_BYTE_COUNT);
        listOfFeatures.add(FLOW_STATS_ACTION_OUTPUT);
        listOfFeatures.add(FLOW_STATS_ACTION_OUTPUT_PORT);
        listOfFeatures.add(FLOW_STATS_ACTION_DROP);
        listOfFeatures.add(FLOW_STATS_PACKET_COUNT_VAR);
        listOfFeatures.add(FLOW_STATS_BYTE_COUNT_VAR);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_PACKET);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_PACKET_VAR);
        listOfFeatures.add(FLOW_STATS_PACKET_PER_DURATION);
        listOfFeatures.add(FLOW_STATS_PACKET_PER_DURATION_VAR);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_DURATION);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_DURATION_VAR);
        listOfFeatures.add(FLOW_STATS_TOTAL_FLOWS);
        listOfFeatures.add(FLOW_STATS_TOTAL_FLOWS_VAR);
        listOfFeatures.add(FLOW_STATS_PAIR_FLOW);
        listOfFeatures.add(FLOW_STATS_PAIR_FLOW_RATIO);
        listOfFeatures.add(FLOW_STATS_PAIR_FLOW_RATIO_VAR);
        listOfFeatures.add(FLOW_STATS_TOTAL_SINGLE_FLOW);
        listOfFeatures.add(FLOW_STATS_TOTAL_SINGLE_FLOW_VAR);
        listOfFeatures.add(FLOW_STATS_TOTAL_PAIR_FLOW);
        listOfFeatures.add(FLOW_STATS_TOTAL_PAIR_FLOW_VAR);
        return listOfFeatures;
    }

    public List<String> getListOfFQueueStatseatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(QUEUE_STATS_TX_BYTES);
        listOfFeatures.add(QUEUE_STATS_TX_PACKETS);
        listOfFeatures.add(QUEUE_STATS_TX_ERRORS);
        return listOfFeatures;
    }

    public List<String> getListOfFTableStatseatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(TABLE_STATS_MAX_ENTIRES);
        listOfFeatures.add(TABLE_STATS_ACTIVE_COUNT);
        listOfFeatures.add(TABLE_STATS_LOOKUP_COUNT);
        listOfFeatures.add(TABLE_STATS_MATCHED_COUNT);
        listOfFeatures.add(TABLE_STATS_MATCHED_PER_LOOKUP);
        listOfFeatures.add(TABLE_STATS_ACTIVE_PER_MAX);
        listOfFeatures.add(TABLE_STATS_LOOKUP_PER_ACTIVE);
        listOfFeatures.add(TABLE_STATS_MATCHED_PER_ACTIVE);
        return listOfFeatures;
    }

    public List<String> getListOfFPortStatseatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(PORT_STATS_RX_PACKETS);
        listOfFeatures.add(PORT_STATS_TX_PACKETS);
        listOfFeatures.add(PORT_STATS_RX_BYTES);
        listOfFeatures.add(PORT_STATS_TX_BYTES);
        listOfFeatures.add(PORT_STATS_RX_DROPPED);
        listOfFeatures.add(PORT_STATS_TX_DROPPED);
        listOfFeatures.add(PORT_STATS_RX_ERRORS);
        listOfFeatures.add(PORT_STATS_TX_ERRORS);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERROR);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERROR);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERROR);
        listOfFeatures.add(PORT_STATS_COLLISIONS);
        listOfFeatures.add(PORT_STATS_RX_PACKETS_VAR);
        listOfFeatures.add(PORT_STATS_TX_PACKETS_VAR);
        listOfFeatures.add(PORT_STATS_RX_BYTES_VAR);
        listOfFeatures.add(PORT_STATS_TX_BYTES_VAR);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_VAR);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_VAR);
        listOfFeatures.add(PORT_STATS_RX_ERRORS_VAR);
        listOfFeatures.add(PORT_STATS_TX_ERRORS_VAR);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERR_VAR);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERR_VAR);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERR_VAR);
        listOfFeatures.add(PORT_STATS_RX_BYTE_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_BYTE_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_TX_BYTE_PER_PACKET);
        listOfFeatures.add(PORT_STATS_TX_BYTE_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_PER_PACKET);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_ERROR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_ERROR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_TX_ERROR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_TX_ERROR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERR_PER_PACKET_VAR);
        return listOfFeatures;
    }

    public List<String> getListOfFAggregatedStatseatures() {
        List<String> listOfFeatures = new ArrayList<>();
        listOfFeatures.add(AGGREGATE_STATS_PACKET_COUNT);
        listOfFeatures.add(AGGREGATE_STATS_BYTE_COUNT);
        listOfFeatures.add(AGGREGATE_STATS_FLOW_COUNT);
        return listOfFeatures;
    }

}
