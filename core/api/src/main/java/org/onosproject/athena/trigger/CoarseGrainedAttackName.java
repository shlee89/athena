package org.onosproject.athena.trigger;

import org.onosproject.athena.database.AthenaFeatureField;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 11/9/15.
 */
public class CoarseGrainedAttackName {
    private final Logger log = getLogger(getClass());

    List<String> listOfFeatures = new ArrayList<>();

    HashMap<String, List<String>> listOfRelatedLowLevelFeature = new HashMap<>();

    List<String> relatedToErrorMsg = new ArrayList<>();
    List<String> relatedToPacketIn = new ArrayList<>();
    List<String> relatedToPacketInMatch = new ArrayList<>();
    List<String> relatedToPortStatus = new ArrayList<>();
    List<String> relatedToFlowStatistics = new ArrayList<>();
    List<String> relatedToQueueStatistics = new ArrayList<>();
    List<String> relatedToTableStatistics = new ArrayList<>();
    List<String> relatedToAggregatedStatistics = new ArrayList<>();
    List<String> relatedToPortStatistics = new ArrayList<>();

    private String value;

    public CoarseGrainedAttackName() {
        listOfFeatures.add(AMPLIFICATION_FACTOR);
        listOfFeatures.add(OPENRESOLVER_AMPLIFICATION_FACTOR);
        listOfFeatures.add(PACKET_IN_FLOODING);
        listOfFeatures.add(OPENRESOLVER_RATIO);
        listOfFeatures.add(SWITCH_ABNORMAL);
        listOfFeatures.add(LINK_ABNORMAL);
        listOfFeatures.add(LINK_STABLE_FACTOR);
        listOfFeatures.add(SPRINKLE);
        listOfFeatures.add(FLOW_UTILIZATION_MATRIC);
        listOfFeatures.add(PAIR_FLOW_RATIO);

        setRelatedFeatures();
    }

    public boolean checkExistedFeatures(String feature) {
        return listOfFeatures.contains(feature);
    }

    public List<String> getRelatedAttackFeatures(String affectedFeature) {
        return listOfRelatedLowLevelFeature.get(affectedFeature);
    }

    private void setRelatedFeatures() {
        //flow
        relatedToFlowStatistics.add(AMPLIFICATION_FACTOR);
        relatedToFlowStatistics.add(SPRINKLE);
        relatedToFlowStatistics.add(FLOW_UTILIZATION_MATRIC);
        relatedToFlowStatistics.add(PAIR_FLOW_RATIO);
        relatedToFlowStatistics.add(OPENRESOLVER_RATIO);
        listOfRelatedLowLevelFeature.put(AthenaFeatureField.FLOW_STATS, relatedToFlowStatistics);

        // packet in
        relatedToPacketIn.add(PACKET_IN_FLOODING);
        listOfRelatedLowLevelFeature.put(AthenaFeatureField.PACKET_IN, relatedToPacketIn);

        //port
        relatedToPortStatistics.add(SWITCH_ABNORMAL);
        relatedToPortStatistics.add(LINK_ABNORMAL);
        relatedToPortStatistics.add(LINK_STABLE_FACTOR);
        listOfRelatedLowLevelFeature.put(AthenaFeatureField.PORT_STATS, relatedToPortStatistics);

    }

    public List<String> getListOfFeatures() {
        return listOfFeatures;
    }

    public CoarseGrainedAttackName(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String getDescription() {
        return "The available attacks that could be distinguished by framework";
    }

    public static final String AMPLIFICATION_FACTOR = "AmplificaionFactor";
    public static final String OPENRESOLVER_AMPLIFICATION_FACTOR = "OpenresolverAmplificationFactor";
    public static final String PACKET_IN_FLOODING = "PacketInFlooding";
    public static final String OPENRESOLVER_RATIO = "OpenResolverRatio";
    public static final String SWITCH_ABNORMAL = "SwitchAbnormal";
    public static final String LINK_ABNORMAL = "LinkAbnormal";
    public static final String LINK_STABLE_FACTOR = "LinkStableFactor";
    public static final String SPRINKLE = "Sprinkle";
    public static final String FLOW_UTILIZATION_MATRIC = "FlowUtilizationMatric";
    public static final String PAIR_FLOW_RATIO = "PairFlowRatio";

}
