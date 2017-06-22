package org.onosproject.athena.feature;




import java.util.ArrayList;
import java.util.List;


/**
 * Created by seunghyeon on 8/18/15.
 */
public class FeatureIndex {


    private long switchDatapathId = 0;
    private int switchPortId = 0;
    private long switchQeueueId = 0;
    private short switchTableId = 0;
    //match.get(MatchField.IN_PORT).getPortNumber())
    private int matchInPort = 0;
    //match.get(MatchField.IN_PHY_PORT).getPortNumber())
    private int matchInPhyPort = 0;
    //match.get(MatchField.ETH_DST).getLong()
    private long matchEthDst = 0;
    //match.get(MatchField.ETH_SRC).getLong()
    private long matchEthSrc = 0;
    //match.get(MatchField.ETH_TYPE).getValue()
    private int matchEthType = 0;
    //match.get(MatchField.VLAN_VID).getVlan()
    private short matchVlanVid = 0;
    //match.get(MatchField.VLAN_PCP).getValue()
    private byte matchVlanPcp = 0;
    //match.get(MatchField.IP_DSCP).getDscpValue()
    private byte matchIpDscp = 0;
    //match.get(MatchField.IP_ECN).getEcnValue()
    private byte matchIpEcn = 0;
    //match.get(MatchField.IP_PROTO).getIpProtocolNumber()
    private short matchIpProto = 0;
    /**
     * if isPartiallyMasked is on.
     * Masked<IPv4Address> maskedIp = match.getMasked(MatchField.IPV4_SRC)
     * maskedIp.getValue().getInt()
     * else
     * match.get(MatchField.IPV4_DST).getInt()
     */
    private int matchIpv4Src = 0;
    /**
     * if isPartiallyMasked is on.
     * Masked<IPv4Address> maskedIp = match.getMasked(MatchField.IPV4_SRC)
     * maskedIp.getMask().asCidrMaskLength()
     * else
     * Ip4Prefix.MAX_MASK_LENGTH
     * <p>
     * Converting!!!
     * ip4Prefix = Ip4Prefix.valueOf(
     * maskedIp.getValue().getInt(),
     * maskedIp.getMask().asCidrMaskLength());
     */
    private int matchIpv4SrcMask = 0;
    //Same with DST.
    private int matchIpv4Dst = 0;
    private int matchIpv4DstMask = 0;
    //match.get(MatchField.TCP_SRC).getPort()
    private int matchTcpSrc = 0;
    //match.get(MatchField.TCP_DST).getPort()
    private int matchTcpDst = 0;
    //match.get(MatchField.UDP_SRC).getPort()
    private int matchUdpSrc = 0;
    //match.get(MatchField.UDP_DST).getPort()
    private int matchUdpDst = 0;
    /**
     * (int) match.get(MatchField.MPLS_LABEL.
     * Extract MplsLabel
     * MplsLabel.mplsLabel((int) match.get(MatchField.MPLS_LABEL).getValue())
     */
    private int matchMplsLabel = 0;
    //match.get(MatchField.SCTP_SRC).getPort()
    private int matchSctpSrc = 0;
    //match.get(MatchField.SCTP_DST).getPort()
    private int matchSctpDst = 0;
    //(byte) match.get(MatchField.ICMPV4_TYPE).getType()
    private byte matchIcmpv4Type = 0;
    //(byte) match.get(MatchField.ICMPV4_CODE).getCode()
    private byte matchIcmpv4Code = 0;
    /**
     * if isPartiallyMasked is on.
     * Masked<IPv6Address> maskedIp = match.getMasked(MatchField.IPV6_SRC)
     * maskedIp.getValue().getBytes()
     * else
     * match.get(MatchField.IPV6_DST).getBytes()
     */
    private byte[] matchIpv6Src;
    /**
     * if isPartiallyMasked is on.
     * Masked<IPv6Address> maskedIp = match.getMasked(MatchField.IPV6_DST)
     * maskedIp.getMask().asCidrMaskLength()
     * else
     * Ip6Prefix.MAX_MASK_LENGTH
     * <p>
     * Convert!!
     * ip6Prefix = Ip6Prefix.valueOf(
     * maskedIp.getValue().getBytes(),
     * maskedIp.getMask().asCidrMaskLength());
     */
    private int matchIpv6SrcMask = 0;
    private byte[] matchIpv6Dst;
    private int matchIpv6DstMask = 0;
    //match.get(MatchField.IPV6_FLABEL).getIPv6FlowLabelValue()
    private int matchIpv6Flabel = 0;
    //(byte) match.get(MatchField.ICMPV6_TYPE).getValue()
    private byte matchIcmpv6Type = 0;
    //(byte) match.get(MatchField.ICMPV6_CODE).getValue()
    private byte matchIcmpv6Code = 0;
    /**
     * match.get(MatchField.IPV6_ND_TARGET).getBytes().
     * Then convert depending on Ip6Address.valueOf(match.get(MatchField.IPV6_ND_TARGET).getBytes())
     */
    private byte[] matchIpv6NdTarget;
    /**
     * match.get(MatchField.IPV6_ND_SLL).getLong().
     * Then convert depending on MacAddress.valueOf(match.get(MatchField.IPV6_ND_SLL).getLong())
     */
    private long matchIpv6NdSll = 0;
    /**
     * match.get(MatchField.IPV6_ND_TLL).getLong().
     * Then convert depending on MacAddress.valueOf(match.get(MatchField.IPV6_ND_TLL).getLong())
     */
    private long matchIpv6NdTll = 0;
    //match.get(MatchField.IPV6_EXTHDR).getValue()
    private int matchIpv6Exthdr = 0;

    protected List<FeatureIndexField> featureList = new ArrayList<>();

    public FeatureIndex() {

    }


    public List<FeatureIndexField> getFeatureList() {
        return featureList;
    }

    public void setSwitchDatapathId(long switchDatapathId) {
        featureList.add(FeatureIndexField.SWITCH_DATAPATH_ID);
        this.switchDatapathId = switchDatapathId;
    }

    public void setSwitchPortId(int switchPortId) {
        featureList.add(FeatureIndexField.SWITCH_PORT_ID);
        this.switchPortId = switchPortId;
    }

    public void setSwitchQeueueId(long switchQeueueId) {
        featureList.add(FeatureIndexField.SWITCH_QUEUE_ID);
        this.switchQeueueId = switchQeueueId;
    }

    public void setSwitchTableId(short switchTableId) {
        featureList.add(FeatureIndexField.SWITCH_TABLE_ID);
        this.switchTableId = switchTableId;
    }

    public void setMatchInPort(int matchInPort) {
        featureList.add(FeatureIndexField.MATCH_IN_PORT);
        this.matchInPort = matchInPort;
    }

    public void setMatchInPhyPort(int matchInPhyPort) {
        featureList.add(FeatureIndexField.MATCH_IN_PHY_PORT);
        this.matchInPhyPort = matchInPhyPort;
    }

    public void setMatchEthDst(long matchEthDst) {
        featureList.add(FeatureIndexField.MATCH_ETH_DST);
        this.matchEthDst = matchEthDst;
    }

    public void setMatchEthSrc(long matchEthSrc) {
        featureList.add(FeatureIndexField.MATCH_ETH_SRC);
        this.matchEthSrc = matchEthSrc;
    }

    public void setMatchEthType(int matchEthType) {
        featureList.add(FeatureIndexField.MATCH_ETH_TYPE);
        this.matchEthType = matchEthType;
    }

    public void setMatchVlanVid(short matchVlanVid) {
        featureList.add(FeatureIndexField.MATCH_VLAN_VID);
        this.matchVlanVid = matchVlanVid;
    }

    public void setMatchVlanPcp(byte matchVlanPcp) {
        featureList.add(FeatureIndexField.MATCH_VLAN_PCP);
        this.matchVlanPcp = matchVlanPcp;
    }

    public void setMatchIpDscp(byte matchIpDscp) {
        featureList.add(FeatureIndexField.MATCH_IP_DSCP);
        this.matchIpDscp = matchIpDscp;
    }

    public void setMatchIpEcn(byte matchIpEcn) {
        featureList.add(FeatureIndexField.MATCH_IP_ECN);
        this.matchIpEcn = matchIpEcn;
    }

    public void setMatchIpProto(short matchIpProto) {
        featureList.add(FeatureIndexField.MATCH_IP_PROTO);
        this.matchIpProto = matchIpProto;
    }

    public void setMatchIpv4Src(int matchIpv4Src) {
        featureList.add(FeatureIndexField.MATCH_IPV4_SRC);
        this.matchIpv4Src = matchIpv4Src;
    }

    public void setMatchIpv4SrcMask(int matchIpv4SrcMask) {
        featureList.add(FeatureIndexField.MATCH_IPV4_SRC_MASK);
        this.matchIpv4SrcMask = matchIpv4SrcMask;
    }

    public void setMatchIpv4Dst(int matchIpv4Dst) {
        featureList.add(FeatureIndexField.MATCH_IPV4_DST);
        this.matchIpv4Dst = matchIpv4Dst;
    }

    public void setMatchIpv4DstMask(int matchIpv4DstMask) {
        featureList.add(FeatureIndexField.MATCH_IPV4_DST_MASK);
        this.matchIpv4DstMask = matchIpv4DstMask;
    }

    public void setMatchTcpSrc(int matchTcpSrc) {
        featureList.add(FeatureIndexField.MATCH_TCP_SRC);
        this.matchTcpSrc = matchTcpSrc;
    }

    public void setMatchTcpDst(int matchTcpDst) {
        featureList.add(FeatureIndexField.MATCH_TCP_DST);
        this.matchTcpDst = matchTcpDst;
    }

    public void setMatchUdpSrc(int matchUdpSrc) {
        featureList.add(FeatureIndexField.MATCH_UDP_SRC);
        this.matchUdpSrc = matchUdpSrc;
    }

    public void setMatchUdpDst(int matchUdpDst) {
        featureList.add(FeatureIndexField.MATCH_UDP_DST);
        this.matchUdpDst = matchUdpDst;
    }

    public void setMatchMplsLabel(int matchMplsLabel) {
        featureList.add(FeatureIndexField.MATCH_MPLS_LABEL);
        this.matchMplsLabel = matchMplsLabel;
    }

    public void setMatchSctpSrc(int matchSctpSrc) {
        featureList.add(FeatureIndexField.MATCH_SCTP_SRC);
        this.matchSctpSrc = matchSctpSrc;
    }

    public void setMatchSctpDst(int matchSctpDst) {
        featureList.add(FeatureIndexField.MATCH_SCTP_DST);
        this.matchSctpDst = matchSctpDst;
    }

    public void setMatchIcmpv4Type(byte matchIcmpv4Type) {
        featureList.add(FeatureIndexField.MATCH_ICMPV4_TYPE);
        this.matchIcmpv4Type = matchIcmpv4Type;
    }

    public void setMatchIcmpv4Code(byte matchIcmpv4Code) {
        featureList.add(FeatureIndexField.MATCH_ICMPv4_CODE);
        this.matchIcmpv4Code = matchIcmpv4Code;
    }

    public void setMatchIpv6Src(byte[] matchIpv6Src) {
        featureList.add(FeatureIndexField.MATCH_IPV6_SRC);
        this.matchIpv6Src = matchIpv6Src;
    }

    public void setMatchIpv6SrcMask(int matchIpv6SrcMask) {
        featureList.add(FeatureIndexField.MATCH_IPV6_SRC_MASK);
        this.matchIpv6SrcMask = matchIpv6SrcMask;
    }

    public void setMatchIpv6Dst(byte[] matchIpv6Dst) {
        featureList.add(FeatureIndexField.MATCH_IPV6_DST);
        this.matchIpv6Dst = matchIpv6Dst;
    }

    public void setMatchIpv6DstMask(int matchIpv6DstMask) {
        featureList.add(FeatureIndexField.MATCH_IPV6_DST_MASK);
        this.matchIpv6DstMask = matchIpv6DstMask;
    }

    public void setMatchIpv6Flabel(int matchIpv6Flabel) {
        featureList.add(FeatureIndexField.MATCH_IPV6_FLABEL);
        this.matchIpv6Flabel = matchIpv6Flabel;
    }

    public void setMatchIcmpv6Type(byte matchIcmpv6Type) {
        featureList.add(FeatureIndexField.MATCH_ICMPV6_TYPE);
        this.matchIcmpv6Type = matchIcmpv6Type;
    }

    public void setMatchIcmpv6Code(byte matchIcmpv6Code) {
        featureList.add(FeatureIndexField.MATCH_ICMPV6_CODE);
        this.matchIcmpv6Code = matchIcmpv6Code;
    }

    public void setMatchIpv6NdTarget(byte[] matchIpv6NdTarget) {
        featureList.add(FeatureIndexField.MATCH_IPV6_ND_TARGET);
        this.matchIpv6NdTarget = matchIpv6NdTarget;
    }

    public void setMatchIpv6NdSll(long matchIpv6NdSll) {
        featureList.add(FeatureIndexField.MATCH_IPV6_ND_SLL);
        this.matchIpv6NdSll = matchIpv6NdSll;
    }

    public void setMatchIpv6NdTll(long matchIpv6NdTll) {
        featureList.add(FeatureIndexField.MATCH_IPV6_ND_TLL);
        this.matchIpv6NdTll = matchIpv6NdTll;
    }

    public void setMatchIpv6Exthdr(int matchIpv6Exthdr) {
        featureList.add(FeatureIndexField.MATCH_IPV6_EXTHDR);
        this.matchIpv6Exthdr = matchIpv6Exthdr;
    }

    public long getSwitchDatapathId() {
        return switchDatapathId;
    }

    public int getSwitchPortId() {
        return switchPortId;
    }

    public long getSwitchQeueueId() {
        return switchQeueueId;
    }

    public short getSwitchTableId() {
        return switchTableId;
    }

    public int getMatchInPort() {
        return matchInPort;
    }

    public int getMatchInPhyPort() {
        return matchInPhyPort;
    }

    public long getMatchEthDst() {
        return matchEthDst;
    }

    public long getMatchEthSrc() {
        return matchEthSrc;
    }

    public int getMatchEthType() {
        return matchEthType;
    }

    public short getMatchVlanVid() {
        return matchVlanVid;
    }

    public byte getMatchVlanPcp() {
        return matchVlanPcp;
    }

    public byte getMatchIpDscp() {
        return matchIpDscp;
    }

    public byte getMatchIpEcn() {
        return matchIpEcn;
    }

    public short getMatchIpProto() {
        return matchIpProto;
    }

    public int getMatchIpv4Src() {
        return matchIpv4Src;
    }

    public int getMatchIpv4SrcMask() {
        return matchIpv4SrcMask;
    }

    public int getMatchIpv4Dst() {
        return matchIpv4Dst;
    }

    public int getMatchIpv4DstMask() {
        return matchIpv4DstMask;
    }

    public int getMatchTcpSrc() {
        return matchTcpSrc;
    }

    public int getMatchTcpDst() {
        return matchTcpDst;
    }

    public int getMatchUdpSrc() {
        return matchUdpSrc;
    }

    public int getMatchUdpDst() {
        return matchUdpDst;
    }

    public int getMatchMplsLabel() {
        return matchMplsLabel;
    }

    public int getMatchSctpSrc() {
        return matchSctpSrc;
    }

    public int getMatchSctpDst() {
        return matchSctpDst;
    }

    public byte getMatchIcmpv4Type() {
        return matchIcmpv4Type;
    }

    public byte getMatchIcmpv4Code() {
        return matchIcmpv4Code;
    }

    public byte[] getMatchIpv6Src() {
        return matchIpv6Src;
    }

    public int getMatchIpv6SrcMask() {
        return matchIpv6SrcMask;
    }

    public byte[] getMatchIpv6Dst() {
        return matchIpv6Dst;
    }

    public int getMatchIpv6DstMask() {
        return matchIpv6DstMask;
    }

    public int getMatchIpv6Flabel() {
        return matchIpv6Flabel;
    }

    public byte getMatchIcmpv6Type() {
        return matchIcmpv6Type;
    }

    public byte getMatchIcmpv6Code() {
        return matchIcmpv6Code;
    }

    public byte[] getMatchIpv6NdTarget() {
        return matchIpv6NdTarget;
    }

    public long getMatchIpv6NdSll() {
        return matchIpv6NdSll;
    }

    public long getMatchIpv6NdTll() {
        return matchIpv6NdTll;
    }

    public int getMatchIpv6Exthdr() {
        return matchIpv6Exthdr;
    }


}