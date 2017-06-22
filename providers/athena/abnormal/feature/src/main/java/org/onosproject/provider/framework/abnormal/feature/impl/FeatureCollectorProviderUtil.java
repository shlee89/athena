package org.onosproject.provider.framework.abnormal.feature.impl;

import org.onlab.packet.Ip4Address;
import org.onlab.packet.Ip4Prefix;
import org.onlab.packet.Ip6Address;
import org.onlab.packet.Ip6Prefix;
import org.onlab.packet.MacAddress;
import org.onlab.packet.MplsLabel;
import org.onlab.packet.TpPort;
import org.onlab.packet.VlanId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.TrafficSelector;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IPv6Address;
import org.projectfloodlight.openflow.types.Masked;
import org.projectfloodlight.openflow.types.OFVlanVidMatch;
import org.slf4j.Logger;


import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 1/17/16.
 */
public class FeatureCollectorProviderUtil {
    private final Logger log = getLogger(getClass());

    public FeatureCollectorProviderUtil() {
    }

    /**
     * TODO Need to find more intelligent way.
     *
     * @param match
     * @return
     */
    public TrafficSelector buildSelector(Match match) {
        MacAddress mac;
        Ip4Prefix ip4Prefix;
        Ip6Address ip6Address;
        Ip6Prefix ip6Prefix;
        Ip4Address ip;
        TrafficSelector.Builder builder = DefaultTrafficSelector.builder();
        for (MatchField<?> field : match.getMatchFields()) {
            switch (field.id) {
                case IN_PORT:
                    builder.matchInPort(PortNumber.portNumber(match.get(MatchField.IN_PORT).getPortNumber()));
                    break;
                case IN_PHY_PORT:
                    builder.matchInPhyPort(PortNumber.portNumber(
                            match.get(MatchField.IN_PHY_PORT).getPortNumber()));
                    break;
                case METADATA:
                    long metadata = match.get(MatchField.METADATA).getValue().getValue();
                    builder.matchMetadata(metadata);
                    break;
                case ETH_DST:
                    mac = MacAddress.valueOf(match.get(MatchField.ETH_DST).getLong());
                    builder.matchEthDst(mac);
                    break;
                case ETH_SRC:
                    mac = MacAddress.valueOf(match.get(MatchField.ETH_SRC).getLong());
                    builder.matchEthSrc(mac);
                    break;
                case ETH_TYPE:
                    int ethType = match.get(MatchField.ETH_TYPE).getValue();
                    builder.matchEthType((short) ethType);
                    break;
                case VLAN_VID:
                    VlanId vlanId = null;
                    if (match.isPartiallyMasked(MatchField.VLAN_VID)) {
                        Masked<OFVlanVidMatch> masked = match.getMasked(MatchField.VLAN_VID);
                        if (masked.getValue().equals(OFVlanVidMatch.PRESENT)
                                && masked.getMask().equals(OFVlanVidMatch.PRESENT)) {
                            vlanId = VlanId.ANY;
                        }
                    } else {
                        if (!match.get(MatchField.VLAN_VID).isPresentBitSet()) {
                            vlanId = VlanId.NONE;
                        } else {
                            vlanId = VlanId.vlanId(match.get(MatchField.VLAN_VID).getVlan());
                        }
                    }
                    if (vlanId != null) {
                        builder.matchVlanId(vlanId);
                    }
                    break;
                case VLAN_PCP:
                    byte vlanPcp = match.get(MatchField.VLAN_PCP).getValue();
                    builder.matchVlanPcp(vlanPcp);
                    break;
                case IP_DSCP:
                    byte ipDscp = match.get(MatchField.IP_DSCP).getDscpValue();
                    builder.matchIPDscp(ipDscp);
                    break;
                case IP_ECN:
                    byte ipEcn = match.get(MatchField.IP_ECN).getEcnValue();
                    builder.matchIPEcn(ipEcn);
                    break;
                case IP_PROTO:
                    short proto = match.get(MatchField.IP_PROTO).getIpProtocolNumber();
                    builder.matchIPProtocol((byte) proto);
                    break;
                case IPV4_SRC:
                    if (match.isPartiallyMasked(MatchField.IPV4_SRC)) {
                        Masked<IPv4Address> maskedIp = match.getMasked(MatchField.IPV4_SRC);
                        ip4Prefix = Ip4Prefix.valueOf(
                                maskedIp.getValue().getInt(), maskedIp.getMask().asCidrMaskLength());
                    } else {
                        ip4Prefix = Ip4Prefix.valueOf(
                                match.get(MatchField.IPV4_SRC).getInt(), Ip4Prefix.MAX_MASK_LENGTH);
                    }
                    builder.matchIPSrc(ip4Prefix);
                    break;
                case IPV4_DST:
                    if (match.isPartiallyMasked(MatchField.IPV4_DST)) {
                        Masked<IPv4Address> maskedIp = match.getMasked(MatchField.IPV4_DST);
                        ip4Prefix = Ip4Prefix.valueOf(
                                maskedIp.getValue().getInt(), maskedIp.getMask().asCidrMaskLength());
                    } else {
                        ip4Prefix = Ip4Prefix.valueOf(
                                match.get(MatchField.IPV4_DST).getInt(), Ip4Prefix.MAX_MASK_LENGTH);
                    }
                    builder.matchIPDst(ip4Prefix);
                    break;
                case TCP_SRC:
                    builder.matchTcpSrc(TpPort.tpPort(match.get(MatchField.TCP_SRC).getPort()));
                    break;
                case TCP_DST:
                    builder.matchTcpDst(TpPort.tpPort(match.get(MatchField.TCP_DST).getPort()));
                    break;
                case UDP_SRC:
                    builder.matchUdpSrc(TpPort.tpPort(match.get(MatchField.UDP_SRC).getPort()));
                    break;
                case UDP_DST:
                    builder.matchUdpDst(TpPort.tpPort(match.get(MatchField.UDP_DST).getPort()));
                    break;
                case MPLS_LABEL:
                    builder.matchMplsLabel(MplsLabel.mplsLabel((int) match.get(MatchField.MPLS_LABEL)
                            .getValue()));
                    break;
                case MPLS_BOS:
                    builder.matchMplsBos(match.get(MatchField.MPLS_BOS).getValue());
                    break;
                case SCTP_SRC:
                    builder.matchSctpSrc(TpPort.tpPort(match.get(MatchField.SCTP_SRC).getPort()));
                    break;
                case SCTP_DST:
                    builder.matchSctpDst(TpPort.tpPort(match.get(MatchField.SCTP_DST).getPort()));
                    break;
                case ICMPV4_TYPE:
                    byte icmpType = (byte) match.get(MatchField.ICMPV4_TYPE).getType();
                    builder.matchIcmpType(icmpType);
                    break;
                case ICMPV4_CODE:
                    byte icmpCode = (byte) match.get(MatchField.ICMPV4_CODE).getCode();
                    builder.matchIcmpCode(icmpCode);
                    break;
                case IPV6_SRC:
                    if (match.isPartiallyMasked(MatchField.IPV6_SRC)) {
                        Masked<IPv6Address> maskedIp = match.getMasked(MatchField.IPV6_SRC);
                        ip6Prefix = Ip6Prefix.valueOf(
                                maskedIp.getValue().getBytes(), maskedIp.getMask().asCidrMaskLength());
                    } else {
                        ip6Prefix = Ip6Prefix.valueOf(
                                match.get(MatchField.IPV6_SRC).getBytes(), Ip6Prefix.MAX_MASK_LENGTH);
                    }
                    builder.matchIPv6Src(ip6Prefix);
                    break;
                case IPV6_DST:
                    if (match.isPartiallyMasked(MatchField.IPV6_DST)) {
                        Masked<IPv6Address> maskedIp = match.getMasked(MatchField.IPV6_DST);
                        ip6Prefix = Ip6Prefix.valueOf(maskedIp.getValue().getBytes(),
                                maskedIp.getMask().asCidrMaskLength());
                    } else {
                        ip6Prefix = Ip6Prefix.valueOf(match.get(MatchField.IPV6_DST).getBytes(),
                                Ip6Prefix.MAX_MASK_LENGTH);
                    }
                    builder.matchIPv6Dst(ip6Prefix);
                    break;
                case IPV6_FLABEL:
                    int flowLabel = match.get(MatchField.IPV6_FLABEL).getIPv6FlowLabelValue();
                    builder.matchIPv6FlowLabel(flowLabel);
                    break;
                case ICMPV6_TYPE:
                    byte icmpv6type = (byte) match.get(MatchField.ICMPV6_TYPE).getValue();
                    builder.matchIcmpv6Type(icmpv6type);
                    break;
                case ICMPV6_CODE:
                    builder.matchIcmpv6Code((byte) match.get(MatchField.ICMPV6_CODE).getValue());
                    break;
                case IPV6_ND_TARGET:
                    ip6Address = Ip6Address.valueOf(match.get(MatchField.IPV6_ND_TARGET).getBytes());
                    builder.matchIPv6NDTargetAddress(ip6Address);
                    break;
                case IPV6_ND_SLL:
                    mac = MacAddress.valueOf(match.get(MatchField.IPV6_ND_SLL).getLong());
                    builder.matchIPv6NDSourceLinkLayerAddress(mac);
                    break;
                case IPV6_ND_TLL:
                    mac = MacAddress.valueOf(match.get(MatchField.IPV6_ND_TLL).getLong());
                    builder.matchIPv6NDTargetLinkLayerAddress(mac);
                    break;
                case IPV6_EXTHDR:
                    builder.matchIPv6ExthdrFlags((short) match.get(MatchField.IPV6_EXTHDR).getValue());
                    break;
                case TUNNEL_ID:
                    long tunnelId = match.get(MatchField.TUNNEL_ID).getValue();
                    builder.matchTunnelId(tunnelId);
                    break;
                case ARP_OP:
                    int arpOp = match.get(MatchField.ARP_OP).getOpcode();
                    builder.matchArpOp(arpOp);
                    break;
                case ARP_SHA:
                    mac = MacAddress.valueOf(match.get(MatchField.ARP_SHA).getLong());
                    builder.matchArpSha(mac);
                    break;
                case ARP_SPA:
                    ip = Ip4Address.valueOf(match.get(MatchField.ARP_SPA).getInt());
                    builder.matchArpSpa(ip);
                    break;
                case ARP_THA:
                    mac = MacAddress.valueOf(match.get(MatchField.ARP_THA).getLong());
                    builder.matchArpTha(mac);
                    break;
                case ARP_TPA:
                    ip = Ip4Address.valueOf(match.get(MatchField.ARP_TPA).getInt());
                    builder.matchArpTpa(ip);
                    break;
                default:
                    log.warn("Match type {} not yet implemented.", field.id);
            }
        }
        return builder.build();
    }




}
