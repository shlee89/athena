package org.onosproject.athena.feature;



/**
 * IndexField currently supports a part of OF1.3 protocol.
 * ONOS only supports below Match fields (onos-1.2.1).
 * Notsupported Match feilds are:
 * case ARP_OP:
 * case ARP_SHA:
 * case ARP_SPA:
 * case ARP_THA:
 * case ARP_TPA:
 * case MPLS_TC:
 * case TUNNEL_ID:
 * case OCH_SIGID:
 * case OCH_SIGTYPE:
 * Created by seunghyeon on 8/18/15.
 */
public enum FeatureIndexField {
    /**
     * Indicates that Dpid.
     */
    SWITCH_DATAPATH_ID,
    /**
     * Indicates that switch port.
     */
    SWITCH_PORT_ID,
    /**
     * Indicates that queue id.
     */
    SWITCH_QUEUE_ID,
    /**
     * Indicates that table id.
     */
    SWITCH_TABLE_ID,
    /**
     * Indicates that IN_PORT in MatchField.
     */
    MATCH_IN_PORT,
    /**
     * Indicates that IN_PHY_PORT in MatchField.
     */
    MATCH_IN_PHY_PORT,
    /**
     * Indicates that ETH_DST in MatchField.
     */
    MATCH_ETH_DST,
    /**
     * Indicates that ETH_SRC in MatchField.
     */
    MATCH_ETH_SRC,
    /**
     * Indicates that ETH_TYPE in MatchField.
     */
    MATCH_ETH_TYPE,
    /**
     * Indicates that VLAN_VID in MatchField.
     */
    MATCH_VLAN_VID,
    /**
     * Indicates that VLAN_VID_MASK in MatchField.
     */
    MATCH_VLAN_VID_MASK,
    /**
     * Indicates that VLAN_PCP in MatchField.
     */
    MATCH_VLAN_PCP,
    /**
     * Indicates that IP_DSCP in MatchField.
     */
    MATCH_IP_DSCP,
    /**
     * Indicates that IP_ECN in MatchField.
     */
    MATCH_IP_ECN,
    /**
     * Indicates that IP_PROTO in MatchField.
     */
    MATCH_IP_PROTO,
    /**
     * Indicates that IPV4_SRC in MatchField.
     */
    MATCH_IPV4_SRC,
    /**
     * Indicates that IPV4_SRC_MASK in MatchField.
     */
    MATCH_IPV4_SRC_MASK,
    /**
     * Indicates that IPV4_DST in MatchField.
     */
    MATCH_IPV4_DST,
    /**
     * Indicates that IPV4_DST_MASK in MatchField.
     */
    MATCH_IPV4_DST_MASK,
    /**
     * Indicates that TCP_SRC in MatchField.
     */
    MATCH_TCP_SRC,
    /**
     * Indicates that TCP_DST in MatchField.
     */
    MATCH_TCP_DST,
    /**
     * Indicates that UDP_SRC in MatchField.
     */
    MATCH_UDP_SRC,
    /**
     * Indicates that UDP_DST in MatchField.
     */
    MATCH_UDP_DST,
    /**
     * Indicates that MPLS_LABEL in MatchField.
     */
    MATCH_MPLS_LABEL,
    /**
     * Indicates that SCTP_SRC in MatchField.
     */
    MATCH_SCTP_SRC,
    /**
     * Indicates that SCTP_DST in MatchField.
     */
    MATCH_SCTP_DST,
    /**
     * Indicates that ICMPV4_TYPE in MatchField.
     */
    MATCH_ICMPV4_TYPE,
    /**
     * Indicates that ICMPv4_CODE in MatchField.
     */
    MATCH_ICMPv4_CODE,
    /**
     * Indicates that IPV6_SRC in MatchField.
     */
    MATCH_IPV6_SRC,
    /**
     * Indicates that IPV6_SRC_MASK in MatchField.
     */
    MATCH_IPV6_SRC_MASK,
    /**
     * Indicates that IPV6_DST in MatchField.
     */
    MATCH_IPV6_DST,
    /**
     * Indicates that IPV6_DST_MAS in MatchField.
     */
    MATCH_IPV6_DST_MASK,
    /**
     * Indicates that IPV6_FLABEL in MatchField.
     */
    MATCH_IPV6_FLABEL,
    /**
     * Indicates that ICMPV6_TYPE in MatchField.
     */
    MATCH_ICMPV6_TYPE,
    /**
     * Indicates that ICMPV6_CODE in MatchField.
     */
    MATCH_ICMPV6_CODE,
    /**
     * Indicates that IPV6_ND_TARGET in MatchField.
     */
    MATCH_IPV6_ND_TARGET,
    /**
     * Indicates that IPV6_ND_SLL in MatchField.
     */
    MATCH_IPV6_ND_SLL,
    /**
     * Indicates that IPV6_ND_TLL in MatchField.
     */
    MATCH_IPV6_ND_TLL,
    /**
     * Indicates that IPV6_EXTHDR in MatchField.
     */
    MATCH_IPV6_EXTHDR

}