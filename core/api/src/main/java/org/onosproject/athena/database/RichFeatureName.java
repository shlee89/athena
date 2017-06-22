package org.onosproject.athena.database;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by seunghyeon on 9/1/15.
 */
public class RichFeatureName {

    List<String> listOfFeatures = new ArrayList<>();


    private String value;

    public String getDescription() {
        return "Name of rich features";
    }

    public RichFeatureName() {
        listOfFeatures.add(FLOW_REMOVED_PACKET_PER_DURATION);
        listOfFeatures.add(FLOW_REMOVED_BYTE_PER_DURATION);
        listOfFeatures.add(TABLE_STATS_MATCHED_PER_LOOKUP);
        listOfFeatures.add(TABLE_STATS_ACTIVE_PER_MAX);
        listOfFeatures.add(TABLE_STATS_LOOKUP_PER_ACTIVE);
        listOfFeatures.add(TABLE_STATS_MATCHED_PER_ACTIVE);
        listOfFeatures.add(FLOW_STATS_PACKET_COUNT_VAR);
        listOfFeatures.add(FLOW_STATS_BYTE_COUNT_VAR);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_PACKET);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_PACKET_VAR);
        listOfFeatures.add(FLOW_STATS_PACKET_PER_DURATION);
        listOfFeatures.add(FLOW_STATS_PACKET_PER_DURATION_VAR);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_DURATION);
        listOfFeatures.add(FLOW_STATS_BYTE_PER_DURATION_VAR);
        listOfFeatures.add(PORT_STATS_TX_PACKETS_VAR);
        listOfFeatures.add(PORT_STATS_TX_PACKETS_VAR);
        listOfFeatures.add(PORT_STATS_RX_BYTES_VAR);
        listOfFeatures.add(PORT_STATS_TX_BYTES_VAR);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_VAR);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_VAR);
        listOfFeatures.add(PORT_STATS_RX_ERRORS_VAR);
        listOfFeatures.add(PORT_STATS_TX_ERRORS_VAR);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERR_VAR);
        listOfFeatures.add(PORT_STATS_TX_FRAME_ERR_VAR);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERR_VAR);
        listOfFeatures.add(PORT_STATS_TX_OVER_ERR_VAR);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERR_VAR);
        listOfFeatures.add(PORT_STATS_TX_CRC_ERR_VAR);
        listOfFeatures.add(PORT_STATS_RX_BYTES_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_BYTES_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_TX_BYTES_PER_PACKET);
        listOfFeatures.add(PORT_STATS_TX_BYTES_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_DROPPED_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_PER_PACKET);
        listOfFeatures.add(PORT_STATS_TX_DROPPED_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_ERROR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_ERROR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_TX_ERROR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_TX_ERROR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERROR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_FRAME_ERROR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERROR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_OVER_ERROR_PER_PACKET_VAR);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERROR_PER_PACKET);
        listOfFeatures.add(PORT_STATS_RX_CRC_ERROR_PER_PACKET_VAR);
    }

    public boolean isElements(String str) {

        for (int i = 0; i < listOfFeatures.size(); i++) {
            if (str.startsWith(listOfFeatures.get(i))) {
                return true;
            }
        }

        return false;
    }

    public RichFeatureName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getListOfFeatures() {
        return listOfFeatures;
    }

    //PACKET_COUNT / DURATION_N_SEC
    public static final String FLOW_REMOVED_PACKET_PER_DURATION = "FRpacketPerDuration";
    //BYTE_COUNT / DURATION_N_SEC
    public static final String FLOW_REMOVED_BYTE_PER_DURATION = "FRbytePerDuration";

    //MATCHED_COUNT / LOOKUP_COUNT
    public static final String TABLE_STATS_MATCHED_PER_LOOKUP = "TSSmatchedPerLookup";
    // ACTIVE_COUNT / MAX_ENTRIES
    public static final String TABLE_STATS_ACTIVE_PER_MAX = "TSSactivePerMax";
    // LOOKUP_COUNT / ACTIVE_COUNT
    public static final String TABLE_STATS_LOOKUP_PER_ACTIVE = "TSSlookupPerActive";
    // MATCHED_COUNT / ACTIVE_COUNT
    public static final String TABLE_STATS_MATCHED_PER_ACTIVE = "TSSmatchedPerActive";

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

    public static final String PORT_STATS_RX_PACKETS_VAR = "PSSrxPacketsVar";
    public static final String PORT_STATS_TX_PACKETS_VAR = "PSStxPacketsVar";
    public static final String PORT_STATS_RX_BYTES_VAR = "PSSrxBytesVar";
    public static final String PORT_STATS_TX_BYTES_VAR = "PSStxBytesVar";
    public static final String PORT_STATS_RX_DROPPED_VAR = "PSSrxDropeedVar";
    public static final String PORT_STATS_TX_DROPPED_VAR = "PSStxDropeedVar";
    public static final String PORT_STATS_RX_ERRORS_VAR = "PSSrxErrosVar";
    public static final String PORT_STATS_TX_ERRORS_VAR = "PSStxErrosVar";
    public static final String PORT_STATS_RX_FRAME_ERR_VAR = "PSSrxFrameErrVar";
    public static final String PORT_STATS_TX_FRAME_ERR_VAR = "PSStxFrameErrVar";
    public static final String PORT_STATS_RX_OVER_ERR_VAR = "PSSrxOverErrVar";
    public static final String PORT_STATS_TX_OVER_ERR_VAR = "PSStxOverErrVar";
    public static final String PORT_STATS_RX_CRC_ERR_VAR = "PSSrxCrcErrVar";
    public static final String PORT_STATS_TX_CRC_ERR_VAR = "PSStxCrcErrVar";
    //RX_BYTE / RX_PACKET
    public static final String PORT_STATS_RX_BYTES_PER_PACKET = "PSSrxBytePerPacket";
    //RX_BYTE_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_BYTES_PER_PACKET_VAR = "PSSrxBytePerPacketVar";
    //TX_BYTE / TX_PACKET
    public static final String PORT_STATS_TX_BYTES_PER_PACKET = "PSStxBytePerPacket";
    //TX_BYTE_VAR / TX_PACKET_VAR
    public static final String PORT_STATS_TX_BYTES_PER_PACKET_VAR = "PSStxBytePerPacketVar";
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
    public static final String PORT_STATS_RX_FRAME_ERROR_PER_PACKET = "PSSrxFrameErrorPerPacket";
    //RX_FRAME_ERROR_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_FRAME_ERROR_PER_PACKET_VAR = "PSSrxFrameErrorPerPacketVar";
    //RX_OVER_ERROR / RX_PACKET
    public static final String PORT_STATS_RX_OVER_ERROR_PER_PACKET = "PSSrxOverErrorPerPacket";
    //RX_OVER_ERROR_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_OVER_ERROR_PER_PACKET_VAR = "PSSrxOverErrorPerPacketVar";
    //RX_CRC_ERROR / RX_PACKET
    public static final String PORT_STATS_RX_CRC_ERROR_PER_PACKET = "PSSrxCrcErrorPerPacket";
  //RX_CRC_ERROR_VAR / RX_PACKET_VAR
    public static final String PORT_STATS_RX_CRC_ERROR_PER_PACKET_VAR = "PSSrxCrcErrorPerPacketVar";

}
