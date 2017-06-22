package org.onosproject.athena.database;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by seunghyeon on 8/31/15.
 */
public class AthenaIndexField implements AthenaField, Serializable {


    List<String> listOfFeatures = new ArrayList<>();
    HashMap<String, String> featureTypeOnDatabase = new HashMap<>();
    String varintType = "varint";
    String bigintType = "bigint";
    String doubleType = "double";
    String boolType = "bool";
    String stringType = "string";
    String timestampType = "timestamp";

    private String value;

    public AthenaIndexField() {
        listOfFeatures.add(SWITCH_DATAPATH_ID);
        featureTypeOnDatabase.put(SWITCH_DATAPATH_ID, bigintType);
        listOfFeatures.add(SWITCH_PORT_ID);
        featureTypeOnDatabase.put(SWITCH_PORT_ID, varintType);
        listOfFeatures.add(SWITCH_QUEUE_ID);
        featureTypeOnDatabase.put(SWITCH_QUEUE_ID, bigintType);
        listOfFeatures.add(SWITCH_TABLE_ID);
        featureTypeOnDatabase.put(SWITCH_TABLE_ID, varintType);
        listOfFeatures.add(MATCH_IN_PORT);
        featureTypeOnDatabase.put(MATCH_IN_PORT, varintType);
        listOfFeatures.add(MATCH_IN_PHY_PORT);
        featureTypeOnDatabase.put(MATCH_IN_PHY_PORT, varintType);
        listOfFeatures.add(MATCH_ETH_DST);
        featureTypeOnDatabase.put(MATCH_ETH_DST, bigintType);
        listOfFeatures.add(MATCH_ETH_SRC);
        featureTypeOnDatabase.put(MATCH_ETH_SRC, bigintType);
        listOfFeatures.add(MATCH_ETH_TYPE);
        featureTypeOnDatabase.put(MATCH_ETH_TYPE, varintType);
        listOfFeatures.add(MATCH_VLAN_VID);
        featureTypeOnDatabase.put(MATCH_VLAN_VID, varintType);
        listOfFeatures.add(MATCH_VLAN_PCP);
        featureTypeOnDatabase.put(MATCH_VLAN_PCP, varintType);
        listOfFeatures.add(MATCH_IP_DSCP);
        featureTypeOnDatabase.put(MATCH_IP_DSCP, varintType);
        listOfFeatures.add(MATCH_IP_ECN);
        featureTypeOnDatabase.put(MATCH_IP_ECN, varintType);
        listOfFeatures.add(MATCH_IP_PROTO);
        featureTypeOnDatabase.put(MATCH_IP_PROTO, varintType);
        listOfFeatures.add(MATCH_IPV4_SRC);
        featureTypeOnDatabase.put(MATCH_IPV4_SRC, stringType);
        listOfFeatures.add(MATCH_IPV4_SRC_MASK);
        featureTypeOnDatabase.put(MATCH_IPV4_SRC_MASK, varintType);
        listOfFeatures.add(MATCH_IPV4_DST);
        featureTypeOnDatabase.put(MATCH_IPV4_DST, stringType);
        listOfFeatures.add(MATCH_IPV4_DST_MASK);
        featureTypeOnDatabase.put(MATCH_IPV4_DST_MASK, varintType);
        listOfFeatures.add(MATCH_TCP_SRC);
        featureTypeOnDatabase.put(MATCH_TCP_SRC, varintType);
        listOfFeatures.add(MATCH_TCP_DST);
        featureTypeOnDatabase.put(MATCH_TCP_DST, varintType);
        listOfFeatures.add(MATCH_UDP_SRC);
        featureTypeOnDatabase.put(MATCH_UDP_SRC, varintType);
        listOfFeatures.add(MATCH_UDP_DST);
        featureTypeOnDatabase.put(MATCH_UDP_DST, varintType);
        listOfFeatures.add(MATCH_MPLS_LABEL);
        featureTypeOnDatabase.put(MATCH_MPLS_LABEL, varintType);
        listOfFeatures.add(MATCH_SCTP_SRC);
        featureTypeOnDatabase.put(MATCH_SCTP_SRC, varintType);
        listOfFeatures.add(MATCH_SCTP_DST);
        featureTypeOnDatabase.put(MATCH_SCTP_DST, varintType);
        listOfFeatures.add(MATCH_ICMPV4_CODE);
        featureTypeOnDatabase.put(MATCH_ICMPV4_CODE, varintType);
        listOfFeatures.add(MATCH_ICMPV4_TYPE);
        featureTypeOnDatabase.put(MATCH_ICMPV4_TYPE, varintType);
        listOfFeatures.add(MATCH_IPV6_SRC);
        featureTypeOnDatabase.put(MATCH_IPV6_SRC, bigintType);
        listOfFeatures.add(MATCH_IPV6_SRC_MASK);
        featureTypeOnDatabase.put(MATCH_IPV6_SRC_MASK, bigintType);
        listOfFeatures.add(MATCH_IPV6_DST);
        featureTypeOnDatabase.put(MATCH_IPV6_DST, bigintType);
        listOfFeatures.add(MATCH_IPV6_DST_MASK);
        featureTypeOnDatabase.put(MATCH_IPV6_DST_MASK, bigintType);
        listOfFeatures.add(MATCH_IPV6_FLABEL); //modified by Jinwoo
        featureTypeOnDatabase.put(MATCH_IPV6_FLABEL, varintType);
        listOfFeatures.add(MATCH_ICMPV6_CODE);
        featureTypeOnDatabase.put(MATCH_ICMPV6_CODE, varintType);
        listOfFeatures.add(MATCH_ICMPV6_TYPE);
        featureTypeOnDatabase.put(MATCH_ICMPV6_TYPE, varintType);
        listOfFeatures.add(MATCH_IPV6_ND_SLL);
        featureTypeOnDatabase.put(MATCH_IPV6_ND_SLL, bigintType);
        listOfFeatures.add(MATCH_IPV6_ND_TARGET);
        featureTypeOnDatabase.put(MATCH_IPV6_ND_TARGET, varintType);
        listOfFeatures.add(MATCH_IPV6_ND_TLL);
        featureTypeOnDatabase.put(MATCH_IPV6_ND_TLL, bigintType);
        listOfFeatures.add(MATCH_IPV6_EXTHDR);
        featureTypeOnDatabase.put(MATCH_IPV6_EXTHDR, varintType);
        listOfFeatures.add(APP_APPLICATION_ID);
        featureTypeOnDatabase.put(APP_APPLICATION_ID, varintType);
        listOfFeatures.add(APP_APPLICATION_NAME);
        featureTypeOnDatabase.put(APP_APPLICATION_NAME, stringType);
        listOfFeatures.add(TIMESTAMP);
        featureTypeOnDatabase.put(TIMESTAMP, timestampType);
        listOfFeatures.add(FEATURE_TYPE);
        featureTypeOnDatabase.put(FEATURE_TYPE, varintType);
        listOfFeatures.add(FEATURE_CATEGORY);
        featureTypeOnDatabase.put(FEATURE_CATEGORY, varintType);
    }

    public List<String> getListOfFeatures() {
        return listOfFeatures;
    }

    public AthenaIndexField(String value) {
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
    public boolean isElements(String str) {

        for (int i = 0; i < listOfFeatures.size(); i++) {
            if (str.startsWith(listOfFeatures.get(i))) {
                return true;
            }
        }

        return false;
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
        return "The name of Indexes on Database";
    }

    public static final String SWITCH_DATAPATH_ID = "SdatapathId";
    public static final String SWITCH_PORT_ID = "SportId";
    public static final String SWITCH_QUEUE_ID = "SqueueId";
    public static final String SWITCH_TABLE_ID = "StableId";
    public static final String MATCH_IN_PORT = "MinPort";
    public static final String MATCH_IN_PHY_PORT = "MinPhyPort";
    public static final String MATCH_ETH_DST = "MethDst";
    public static final String MATCH_ETH_SRC = "MethSrc";
    public static final String MATCH_ETH_TYPE = "MethType";
    public static final String MATCH_VLAN_VID = "MvlanVid";
    public static final String MATCH_VLAN_PCP = "MvlanPcp";
    public static final String MATCH_IP_DSCP = "MipDscp";
    public static final String MATCH_IP_ECN = "MipEcn";
    public static final String MATCH_IP_PROTO = "MipProto";
    public static final String MATCH_IPV4_SRC = "Mipv4Src";
    public static final String MATCH_IPV4_SRC_MASK = "Mipv4SrcMask";
    public static final String MATCH_IPV4_DST = "Mipv4Dst";
    public static final String MATCH_IPV4_DST_MASK = "Mipv4DstMask";
    public static final String MATCH_TCP_SRC = "MtcpSrc";
    public static final String MATCH_TCP_DST = "MtcpDst";
    public static final String MATCH_UDP_SRC = "MudpSrc";
    public static final String MATCH_UDP_DST = "MudpDst";
    public static final String MATCH_MPLS_LABEL = "MmplsLabel";
    public static final String MATCH_SCTP_SRC = "MsctpSrc";
    public static final String MATCH_SCTP_DST = "MsctpDst";
    public static final String MATCH_ICMPV4_CODE = "Micmpv4Code";
    public static final String MATCH_ICMPV4_TYPE = "Micmpv4Type";
    public static final String MATCH_IPV6_SRC = "Mipv6Src";
    public static final String MATCH_IPV6_SRC_MASK = "Mipv6SrcMask";
    public static final String MATCH_IPV6_DST = "Mipv6Dst";
    public static final String MATCH_IPV6_DST_MASK = "Mipv6DstMask";
    public static final String MATCH_IPV6_FLABEL = "Mipv6Flabel"; // modified by Jinwoo
    public static final String MATCH_ICMPV6_CODE = "Micmpv6Code";
    public static final String MATCH_ICMPV6_TYPE = "Micmpv6Type";
    public static final String MATCH_IPV6_ND_SLL = "Mipv6NdSll";
    public static final String MATCH_IPV6_ND_TARGET = "Mipv6NdTarget";
    public static final String MATCH_IPV6_ND_TLL = "Mipv6NdTll";
    public static final String MATCH_IPV6_EXTHDR = "Mipv6Exthdr";
    public static final String APP_APPLICATION_ID = "AappId";
    public static final String APP_APPLICATION_NAME = "AappName";

    public static final String TIMESTAMP = "timestamp";
    public static final String FEATURE_TYPE = "featureType";
    public static final String FEATURE_CATEGORY = "featureCategory";

}
