package org.onosproject.athena.database;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by seunghyeon on 10/13/15.
 */
public class RichFeatureCalculator {

    //PACKET_COUNT / DURATION_N_SEC
    private List<String> flowRemovedPacketPerDurationList = new ArrayList<>();
    //BYTE_COUNT / DURATION_N_SEC
    private List<String> flowRemovedBytePerDurationList = new ArrayList<>();


    //MATCHED_COUNT / LOOKUP_COUNT
    private List<String> tableStatsMatchedPerLookupList = new ArrayList<>();
    // ACTIVE_COUNT / MAX_ENTRIES
    private List<String> tableStatsActivePerMaxList = new ArrayList<>();
    // LOOKUP_COUNT / ACTIVE_COUNT
    private List<String> tableStatsLookupPerActiveList = new ArrayList<>();
    // MATCHED_COUNT / ACTIVE_COUNT
    private List<String> tableStatsMatchedPerActiveList = new ArrayList<>();

    // BYTE_COUNT / PACKET_COUNT
    private List<String> flowStatsBytePerPacetList = new ArrayList<>();
    // PACKET_COUNT / DURATION_N_SEC
    private List<String> flowStatsPacketPerDurationList = new ArrayList<>();
    // BYTE_COUNT / DURATION_N_SEC
    private List<String> flowStatsBytePerDurationList = new ArrayList<>();

    //RX_BYTE / RX_PACKET
    private List<String> portStatsRxbytesPerPacketList = new ArrayList<>();
    //TX_BYTE / TX_PACKET
    private List<String> portStatsTxbytesPerPacketList = new ArrayList<>();
    //RX_DROPPED / RX_PACKET
    private List<String> portStatsRxdroppedPerPacketList = new ArrayList<>();
    //TX_DROPPED / TX_PACKET
    private List<String> portStatsTxdroppedPerPacketList = new ArrayList<>();
    //RX_ERRORs / RX_PACKET
    private List<String> portStatsRxerrorsPerPacketList = new ArrayList<>();
    //TX_ERRORS / TX_PACKET
    private List<String> portStatsTxerrorPerPacketList = new ArrayList<>();
    //RX_FRAME_ERROR / RX_PACKET
    private List<String> portStatsRxframeerrorPerPacketList = new ArrayList<>();
    //RX_OVER_ERROR / RX_PACKET
    private List<String> portStatsRxovererrorPerPacket = new ArrayList<>();
    //RX_CRC_ERROR / RX_PACKET
    private List<String> portStatsRxcrcerrorPerPacketList = new ArrayList<>();

    private Long noValue = (long) -1;

    public RichFeatureCalculator() {
        this.setCalculationInformationToList("");
    }

    public RichFeatureCalculator(String prefix) {
        this.setCalculationInformationToList(prefix + ".");
    }

    public Long calculateReceivedValue(Object op1, Object op2, String operator) {


        Long operand1 = convertToLong(op1);
        Long operand2 = convertToLong(op2);
        if (operand1 == null || operand2 == null) {
            return noValue;
        }

        Long result;

        switch (operator) {
            case "add":
                result = new Long(operand1.longValue() + operand2.longValue());
                return result;
            case "mul":
                result = new Long(operand1.longValue() * operand2.longValue());
                return result;
            case "div":
                if (operand2.longValue() == 0) {
                    return noValue;
                }
                result = new Long(operand1.longValue() / operand2.longValue());
                return result;
            default:
                return null;
        }
    }

    public Long getNoValue() {
        return noValue;
    }

    private Long convertToLong(Object obj) {

        if (obj == null) {
            return null;
        }

        Long operand = null;
        if (obj instanceof Long) {
            operand = (Long) obj;
        } else if (obj instanceof Integer) {
            operand = new Long(((Integer) obj).intValue());
        } else {
            operand = null;
        }
        return operand;
    }


    public List<String> getRichFeatureList(String feature) {
//        public List<String> getRichFeatureList(String prefix, String feature) {
//        String[] splitedFeature = feature.split(".");
//        if (!splitedFeature[0].startsWith(prefix)) {
//            return null;
//        }
//        String richFeature = splitedFeature[1];

        switch (feature) {
            case RichFeatureName.FLOW_REMOVED_BYTE_PER_DURATION:
                return flowRemovedBytePerDurationList;
            case RichFeatureName.FLOW_REMOVED_PACKET_PER_DURATION:
                return flowRemovedPacketPerDurationList;
            case RichFeatureName.TABLE_STATS_MATCHED_PER_LOOKUP:
                return tableStatsMatchedPerLookupList;
            case RichFeatureName.TABLE_STATS_ACTIVE_PER_MAX:
                return tableStatsActivePerMaxList;
            case RichFeatureName.TABLE_STATS_LOOKUP_PER_ACTIVE:
                return tableStatsLookupPerActiveList;
            case RichFeatureName.TABLE_STATS_MATCHED_PER_ACTIVE:
                return tableStatsMatchedPerActiveList;
            case RichFeatureName.FLOW_STATS_BYTE_PER_PACKET:
                return flowStatsBytePerPacetList;
            case RichFeatureName.FLOW_STATS_PACKET_PER_DURATION:
                return flowStatsPacketPerDurationList;
            case RichFeatureName.FLOW_STATS_BYTE_PER_DURATION:
                return flowStatsBytePerDurationList;
            case RichFeatureName.PORT_STATS_RX_BYTES_PER_PACKET:
                return portStatsRxbytesPerPacketList;
            case RichFeatureName.PORT_STATS_TX_BYTES_PER_PACKET:
                return portStatsTxbytesPerPacketList;
            case RichFeatureName.PORT_STATS_TX_DROPPED_PER_PACKET:
                return portStatsTxdroppedPerPacketList;
            case RichFeatureName.PORT_STATS_RX_DROPPED_PER_PACKET:
                return portStatsRxdroppedPerPacketList;
            case RichFeatureName.PORT_STATS_RX_ERROR_PER_PACKET:
                return portStatsRxerrorsPerPacketList;
            case RichFeatureName.PORT_STATS_RX_FRAME_ERROR_PER_PACKET:
                return portStatsRxframeerrorPerPacketList;
            case RichFeatureName.PORT_STATS_RX_OVER_ERROR_PER_PACKET:
                return portStatsRxovererrorPerPacket;
            case RichFeatureName.PORT_STATS_RX_CRC_ERROR_PER_PACKET:
                return portStatsRxcrcerrorPerPacketList;
            default:
                return null;
        }

    }

    private void setCalculationInformationToList(String prefix) {
        //rich features for flow removed
        flowRemovedPacketPerDurationList.add(prefix + AthenaFeatureField.FLOW_REMOVED_PACKET_COUNT);
        flowRemovedPacketPerDurationList.add("div");
        flowRemovedPacketPerDurationList.add(prefix + AthenaFeatureField.FLOW_REMOVED_DURATION_N_SECOND);

        flowRemovedBytePerDurationList.add(prefix + AthenaFeatureField.FLOW_REMOVED_BYTE_COUNT);
        flowRemovedBytePerDurationList.add("div");
        flowRemovedBytePerDurationList.add(prefix + AthenaFeatureField.FLOW_REMOVED_DURATION_N_SECOND);

        //rich features for table stats
        tableStatsMatchedPerLookupList.add(prefix + AthenaFeatureField.TABLE_STATS_MATCHED_COUNT);
        tableStatsMatchedPerLookupList.add("div");
        tableStatsMatchedPerLookupList.add(prefix + AthenaFeatureField.TABLE_STATS_LOOKUP_COUNT);

        tableStatsActivePerMaxList.add(prefix + AthenaFeatureField.TABLE_STATS_ACTIVE_COUNT);
        tableStatsActivePerMaxList.add("div");
        tableStatsActivePerMaxList.add(prefix + AthenaFeatureField.TABLE_STATS_MAX_ENTIRES);

        tableStatsLookupPerActiveList.add(prefix + AthenaFeatureField.TABLE_STATS_LOOKUP_COUNT);
        tableStatsLookupPerActiveList.add("div");
        tableStatsLookupPerActiveList.add(prefix + AthenaFeatureField.TABLE_STATS_ACTIVE_COUNT);

        tableStatsMatchedPerActiveList.add(prefix + AthenaFeatureField.TABLE_STATS_MATCHED_COUNT);
        tableStatsMatchedPerActiveList.add("div");
        tableStatsMatchedPerActiveList.add(prefix + AthenaFeatureField.TABLE_STATS_ACTIVE_COUNT);

        //rich features for flow stats
        flowStatsBytePerPacetList.add(prefix + AthenaFeatureField.FLOW_STATS_BYTE_COUNT);
        flowStatsBytePerPacetList.add("div");
        flowStatsBytePerPacetList.add(prefix + AthenaFeatureField.FLOW_STATS_PACKET_COUNT);

        flowStatsPacketPerDurationList.add(prefix + AthenaFeatureField.FLOW_STATS_PACKET_COUNT);
        flowStatsPacketPerDurationList.add("div");
        flowStatsPacketPerDurationList.add(prefix + AthenaFeatureField.FLOW_STATS_DURATION_N_SEC);

        flowStatsBytePerDurationList.add(prefix + AthenaFeatureField.FLOW_STATS_BYTE_COUNT);
        flowStatsBytePerDurationList.add("div");
        flowStatsBytePerDurationList.add(prefix + AthenaFeatureField.FLOW_STATS_DURATION_N_SEC);

        //rich features for port stats
        portStatsRxbytesPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_BYTES);
        portStatsRxbytesPerPacketList.add("div");
        portStatsRxbytesPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_PACKETS);

        portStatsTxbytesPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_TX_BYTES);
        portStatsTxbytesPerPacketList.add("div");
        portStatsTxbytesPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_TX_PACKETS);

        portStatsRxdroppedPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_DROPPED);
        portStatsRxdroppedPerPacketList.add("div");
        portStatsRxdroppedPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_PACKETS);

        portStatsTxdroppedPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_TX_DROPPED);
        portStatsTxdroppedPerPacketList.add("div");
        portStatsTxdroppedPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_TX_PACKETS);

        portStatsRxerrorsPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_ERRORS);
        portStatsRxerrorsPerPacketList.add("div");
        portStatsRxerrorsPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_PACKETS);

        portStatsTxerrorPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_TX_ERRORS);
        portStatsTxerrorPerPacketList.add("div");
        portStatsTxerrorPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_TX_PACKETS);

        portStatsRxframeerrorPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_FRAME_ERROR);
        portStatsRxframeerrorPerPacketList.add("div");
        portStatsRxframeerrorPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_PACKETS);

        portStatsRxovererrorPerPacket.add(prefix + AthenaFeatureField.PORT_STATS_RX_OVER_ERROR);
        portStatsRxovererrorPerPacket.add("div");
        portStatsRxovererrorPerPacket.add(prefix + AthenaFeatureField.PORT_STATS_RX_PACKETS);

        portStatsRxcrcerrorPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_CRC_ERROR);
        portStatsRxcrcerrorPerPacketList.add("div");
        portStatsRxcrcerrorPerPacketList.add(prefix + AthenaFeatureField.PORT_STATS_RX_PACKETS);
    }

}
