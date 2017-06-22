/*
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.security.app;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onlab.packet.Ethernet;
import org.onlab.packet.ICMP;
import org.onlab.packet.ICMP6;
import org.onlab.packet.IPv4;
import org.onlab.packet.IPv6;
import org.onlab.packet.Ip4Prefix;
import org.onlab.packet.Ip6Prefix;
import org.onlab.packet.MacAddress;
import org.onlab.packet.TCP;
import org.onlab.packet.TpPort;
import org.onlab.packet.UDP;
import org.onlab.packet.VlanId;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.Host;
import org.onosproject.net.HostId;
import org.onosproject.net.Link;
import org.onosproject.net.Path;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.FlowEntry;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flow.criteria.Criterion;
import org.onosproject.net.flow.criteria.TcpPortCriterion;
import org.onosproject.net.flow.instructions.Instruction;
import org.onosproject.net.flow.instructions.Instructions;
import org.onosproject.net.flow.instructions.Instructions.OutputInstruction;
import org.onosproject.net.flowobjective.DefaultForwardingObjective;
import org.onosproject.net.flowobjective.FlowObjectiveService;
import org.onosproject.net.flowobjective.ForwardingObjective;
import org.onosproject.net.host.HostService;
import org.onosproject.net.packet.InboundPacket;
import org.onosproject.net.packet.PacketContext;
import org.onosproject.net.packet.PacketProcessor;
import org.onosproject.net.packet.PacketService;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Skeletal ONOS application component.
 */
@Component(immediate = true)
public class SimpleSecurityRouting {

    private static final int DEFAULT_TIMEOUT = 10;
    private static final int DEFAULT_PRIORITY = 10;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected TopologyService topologyService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected PacketService packetService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowObjectiveService flowObjectiveService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    private ApplicationId appId;

    private SecurityRoutingPacketProcessor processor = new SecurityRoutingPacketProcessor();

    @Property(name = "packetOutOnly", boolValue = false,
            label = "Enable packet-out only forwarding; default is false")
    private boolean packetOutOnly = false;

    @Property(name = "packetOutOfppTable", boolValue = false,
            label = "Enable first packet forwarding using OFPP_TABLE port " +
                    "instead of PacketOut with actual port; default is false")
    private boolean packetOutOfppTable = false;

    @Property(name = "flowTimeout", intValue = DEFAULT_TIMEOUT,
            label = "Configure Flow Timeout for installed flow rules; " +
                    "default is 10 sec")
    private int flowTimeout = DEFAULT_TIMEOUT;

    @Property(name = "flowPriority", intValue = DEFAULT_PRIORITY,
            label = "Configure Flow Priority for installed flow rules; " +
                    "default is 10")
    private int flowPriority = DEFAULT_PRIORITY;

    @Property(name = "ipv6Forwarding", boolValue = false,
            label = "Enable IPv6 forwarding; default is false")
    private boolean ipv6Forwarding = false;

    @Property(name = "matchDstMacOnly", boolValue = false,
            label = "Enable matching Dst Mac Only; default is false")
    private boolean matchDstMacOnly = false;

    @Property(name = "matchVlanId", boolValue = false,
            label = "Enable matching Vlan ID; default is false")
    private boolean matchVlanId = false;

    @Property(name = "matchIpv4Address", boolValue = true,
            label = "Enable matching IPv4 Addresses; default is false")
    private boolean matchIpv4Address = true;

    @Property(name = "matchIpv4Dscp", boolValue = false,
            label = "Enable matching IPv4 DSCP and ECN; default is false")
    private boolean matchIpv4Dscp = false;

    @Property(name = "matchIpv6Address", boolValue = false,
            label = "Enable matching IPv6 Addresses; default is false")
    private boolean matchIpv6Address = false;

    @Property(name = "matchIpv6FlowLabel", boolValue = false,
            label = "Enable matching IPv6 FlowLabel; default is false")
    private boolean matchIpv6FlowLabel = false;

    @Property(name = "matchTcpUdpPorts", boolValue = true,
            label = "Enable matching TCP/UDP ports; default is false")
    private boolean matchTcpUdpPorts = true;

    @Property(name = "matchIcmpFields", boolValue = false,
            label = "Enable matching ICMPv4 and ICMPv6 fields; " +
                    "default is false")
    private boolean matchIcmpFields = false;

    @Property(name = "ignoreIPv4Multicast", boolValue = false,
            label = "Ignore (do not forward) IPv4 multicast packets; default is false")
    private boolean ignoreIpv4McastPackets = false;

//    private final TopologyListener topologyListener = new InternalTopologyListener();

    private final Logger log = LoggerFactory.getLogger(getClass());

    //Kansas City - of:0000000000000007, Houston - of:0000000000000008
//    private DeviceId kansasCity = DeviceId.deviceId("of:0000000000000007");
//    private DeviceId houston = DeviceId.deviceId("of:0000000000000008");
    private List<DeviceId> pathForSecurity;

    //    private HostId ftpserver = HostId.hostId("92:61:8D:5F:F5:20/-1");
//    private HostId webserver1 = HostId.hostId("D2:B2:87:4F:6D:03/-1");
//    private HostId webserver2 = HostId.hostId("D2:B2:87:4F:6D:04/-1");
    private DeviceId securityDeviceId = DeviceId.deviceId("of:0000000000000006");
    private HostId securityHostId = HostId.hostId("00:00:00:00:00:06/-1");
    private HostId gateway1 = HostId.hostId("00:00:00:00:00:01/-1");
    private HostId gateway2 = HostId.hostId("00:00:00:00:00:05/-1");

    @Activate
    protected void activate() {
        appId = coreService.registerApplication("org.onosproject.security");
        pathForSecurity = new ArrayList<DeviceId>();
        pathForSecurity.add(DeviceId.deviceId("of:0000000000000001"));
        pathForSecurity.add(DeviceId.deviceId("of:0000000000000002"));
        pathForSecurity.add(DeviceId.deviceId("of:0000000000000005"));
        pathForSecurity.add(DeviceId.deviceId("of:0000000000000006"));
        pathForSecurity.add(DeviceId.deviceId("of:0000000000000007"));
        pathForSecurity.add(DeviceId.deviceId("of:0000000000000008"));
        // clean all flows of devices
//        cleanAllFlowRules();
        cleanConflictedFlowRules();
        // Register Packet Processor
        packetService.addProcessor(processor, PacketProcessor.director(1));
//        topologyService.addListener(topologyListener);
        log.info("Started");
    }

    @Deactivate
    protected void deactivate() {
        packetService.removeProcessor(processor);
        log.info("Stopped");
    }

    // clean all flow rules which have destination address of ftpserver of
    // wsebserver.
    private void cleanAllFlowRules() {
        Iterable<Device> deviceList = deviceService.getAvailableDevices();

        for (Device d : deviceList) {
            DeviceId id = d.id();
            log.trace("Searching for flow rules to remove from: " + id);
            for (FlowEntry r : flowRuleService.getFlowEntries(id)) {
                boolean match = false;
                for (Instruction i : r.treatment().allInstructions()) {
                    if (i.type() == Instruction.Type.OUTPUT) {
                        OutputInstruction oi = (OutputInstruction) i;
                        // if the flow has matching src and dst
                        for (Criterion cr : r.selector().criteria()) {
                            if (cr.type() == Criterion.Type.ETH_DST || cr.type() == Criterion.Type.ETH_SRC) {
                                match = true;

                            }
                        }
                    }
                }
                if (match) {
                    log.info("Removed flow rule from device: " + id);
                    flowRuleService.removeFlowRules((FlowRule) r);
                }
            }
        }
    }

    private void cleanConflictedFlowRules() {
        Iterable<Device> deviceList = deviceService.getAvailableDevices();

        for (Device d : deviceList) {
            DeviceId id = d.id();
            log.trace("Searching for flow rules to remove from: " + id);
            for (FlowEntry r : flowRuleService.getFlowEntries(id)) {
                boolean match = false;
                for (Instruction i : r.treatment().allInstructions()) {
                    if (i.type() == Instruction.Type.OUTPUT) {
                        OutputInstruction oi = (OutputInstruction) i;
                        // if the flow has matching src and dst
                        for (Criterion cr : r.selector().criteria()) {
                            if (cr.type() == Criterion.Type.TCP_DST || cr.type() == Criterion.Type.TCP_SRC) {
                                int portNumber = ((TcpPortCriterion) cr).tcpPort().toInt();
                                if (portNumber == 20) {
                                    match = true;
                                }
                            }
                        }
                    }
                }
                if (match) {
                    log.trace("Removed flow rule from device: " + id);
                    flowRuleService.removeFlowRules((FlowRule) r);
                }
            }
        }
    }

    /**
     * Packet processor responsible for forwarding packets along their paths.
     */
    private class SecurityRoutingPacketProcessor implements PacketProcessor {

        @Override
        public void process(PacketContext context) {
            // Stop processing if the packet has been handled, since we
            // can't do any more to it.

            if (context.isHandled()) {
                return;
            }

            InboundPacket pkt = context.inPacket();
            Ethernet ethPkt = pkt.parsed();

            if (ethPkt == null) {
                return;
            }

            // Bail if this is deemed to be a control packet.
            if (isControlPacket(ethPkt)) {
                return;
            }

            // Skip IPv6 multicast packet when IPv6 forward is disabled.
            if (!ipv6Forwarding && isIpv6Multicast(ethPkt)) {
                return;
            }

            IPv4 ipv4Packet = (IPv4) ethPkt.getPayload();
            byte ipv4Protocol = ipv4Packet.getProtocol();
            if (ipv4Protocol == IPv4.PROTOCOL_TCP) {
                TCP tcpPacket = (TCP) ipv4Packet.getPayload();
                if (tcpPacket.getSourcePort() != 20 && tcpPacket.getDestinationPort() != 20) {
                    log.info("This packet is not FTP packet !");
                    return;
                }
            }

            HostId id = HostId.hostId(ethPkt.getDestinationMAC());

            // Do not process link-local addresses in any way.
            if (id.mac().isLinkLocal()) {
                return;
            }

            // Do not process IPv4 multicast packets, let mfwd handle them
            if (ignoreIpv4McastPackets && ethPkt.getEtherType() == Ethernet.TYPE_IPV4) {
                if (id.mac().isMulticast()) {
                    return;
                }
            }

            // If PacketOutOnly or ARP packet than forward directly to output port
            if (packetOutOnly || ethPkt.getEtherType() == Ethernet.TYPE_ARP) {
                return;
            }

            // LLDP
//            if (packetOutOnly || ethPkt.getEtherType() == Ethernet.TYPE_LLDP) {
//                return;
//            }

            // Do we know who this is for? If not, flood and bail.
            Host dst = hostService.getHost(id);
            if (dst == null) {
                flood(context);
                return;
            }

            // By Jinwoo
            // if the source or the destination is not ftpserver or webserver,
            // return.
            HostId srcId = HostId.hostId(ethPkt.getSourceMAC());
            if ((!id.mac().toString().equals(gateway1.mac().toString())
                    && !id.mac().toString().equals(gateway2.mac().toString()))
                    && (!srcId.mac().toString().equals(gateway1.mac().toString())
                    && !srcId.mac().toString().equals(gateway2.mac().toString()))) {
                return;
            }

            // Are we on an edge switch that our destination is on? If so,
            // simply forward out to the destination and bail.
            if (pkt.receivedFrom().deviceId().equals(dst.location().deviceId())) {
                if (!context.inPacket().receivedFrom().port().equals(dst.location().port())) {
                    installRule(context, dst.location().port());
                }
                return;
            }

            // Otherwise, get a set of paths that lead from here to the
            // destination edge switch.
            Set<Path> paths =
                    topologyService.getPaths(topologyService.currentTopology(),
                            pkt.receivedFrom().deviceId(),
                            dst.location().deviceId());
            if (paths.isEmpty()) {
                // If there are no paths, flood and bail.
                //flood(context);
                return;
            }

            // Otherwise, pick a path that does not lead back to where we
            // came from; if no such path, flood and bail.
            Path path = pickForwardPathIfPossible(paths, pkt.receivedFrom().port(),
                    ethPkt.getSourceMAC(), ethPkt.getDestinationMAC());
            if (path == null) {
                log.warn("Don't know where to go from here {} for {} -> {}",
                        pkt.receivedFrom(), ethPkt.getSourceMAC(), ethPkt.getDestinationMAC());
                flood(context);
                return;
            }

            // Otherwise forward and be done with it.
            try {
                installRule(context, path.src().port());
            } catch (Exception e) {
                log.error("Error: " + e.toString());
                e.printStackTrace();
            }
//            installRuleOnAllSwithces(context, path);
            // break;
            // }
        }
    }

    // Indicates whether this is a control packet, e.g. LLDP, BDDP
    private boolean isControlPacket(Ethernet eth) {
        short type = eth.getEtherType();
        return type == Ethernet.TYPE_LLDP || type == Ethernet.TYPE_BSN;
    }

    // Indicated whether this is an IPv6 multicast packet.
    private boolean isIpv6Multicast(Ethernet eth) {
        return eth.getEtherType() == Ethernet.TYPE_IPV6 && eth.isMulticast();
    }

    // Selects a path from the given set that does not lead back to the
    // specified port if possible.
    // By Jinwoo
    private Path pickForwardPathIfPossible(Set<Path> paths, PortNumber notToPort,
                                           MacAddress srcHost, MacAddress destHost) {
        Path lastPath = null;
        boolean match = false;
        List<DeviceId> route = pathForSecurity;

        for (Path path : paths) {
//            for (int i = 0; i < 2; i++) {
//                Path targetPath = i == 0 ? path.primary() : path.backup();
                Path targetPath = path;

                for (Link link : targetPath.links()) {
                    DeviceId srcDpid = link.src().deviceId();
                    DeviceId dstDpid = link.dst().deviceId();

                    if (route.contains(srcDpid) && route.contains(dstDpid)) {
                        match = true;
                    } else {
                        match = false;
                        break;
                    }
                }
            if (match) {
                lastPath = targetPath;
                break;
            }
//        }
//            if (match) {
//                break;
//            }
        }
        return lastPath;
    }

    // Floods the specified packet if permissible.
    private void flood(PacketContext context) {
        if (topologyService.isBroadcastPoint(topologyService.currentTopology(),
                context.inPacket().receivedFrom())) {
            packetOut(context, PortNumber.FLOOD);
        } else {
            context.block();
        }
    }

    // Sends a packet out the specified port.
    private void packetOut(PacketContext context, PortNumber portNumber) {
        context.treatmentBuilder().setOutput(portNumber);
        context.send();
    }

    // Install a rule forwarding the packet to the specified port.
    private void installRuleOnAllSwithces(PacketContext context, Path path) {
        //
        // We don't support (yet) buffer IDs in the Flow Service so
        // packet out first.
        //
        Ethernet inPkt = context.inPacket().parsed();
        TrafficSelector.Builder selectorBuilder = DefaultTrafficSelector.builder();

        //
        // If matchDstMacOnly
        //    Create flows matching dstMac only
        // Else
        //    Create flows with default matching and include configured fields
        //
        if (matchDstMacOnly) {
            selectorBuilder.matchEthDst(inPkt.getDestinationMAC());
        } else {
            selectorBuilder.matchInPort(context.inPacket().receivedFrom().port())
                    .matchEthSrc(inPkt.getSourceMAC())
                    .matchEthDst(inPkt.getDestinationMAC());

            // If configured Match Vlan ID
            if (matchVlanId && inPkt.getVlanID() != Ethernet.VLAN_UNTAGGED) {
                selectorBuilder.matchVlanId(VlanId.vlanId(inPkt.getVlanID()));
            }

            //
            // If configured and EtherType is IPv4 - Match IPv4 and
            // TCP/UDP/ICMP fields
            //
//            if (matchIpv4Address && inPkt.getEtherType() == Ethernet.TYPE_IPV4) {
            if (inPkt.getEtherType() == Ethernet.TYPE_IPV4) {
                IPv4 ipv4Packet = (IPv4) inPkt.getPayload();
                byte ipv4Protocol = ipv4Packet.getProtocol();
                Ip4Prefix matchIp4SrcPrefix =
                        Ip4Prefix.valueOf(ipv4Packet.getSourceAddress(),
                                Ip4Prefix.MAX_MASK_LENGTH);
                Ip4Prefix matchIp4DstPrefix =
                        Ip4Prefix.valueOf(ipv4Packet.getDestinationAddress(),
                                Ip4Prefix.MAX_MASK_LENGTH);
                selectorBuilder.matchEthType(Ethernet.TYPE_IPV4)
                        .matchIPSrc(matchIp4SrcPrefix)
                        .matchIPDst(matchIp4DstPrefix);

                if (matchIpv4Dscp) {
                    byte dscp = ipv4Packet.getDscp();
                    byte ecn = ipv4Packet.getEcn();
                    selectorBuilder.matchIPDscp(dscp).matchIPEcn(ecn);
                }

//                    if (matchTcpUdpPorts && ipv4Protocol == IPv4.PROTOCOL_TCP) {
                if (ipv4Protocol == IPv4.PROTOCOL_TCP) {
                    TCP tcpPacket = (TCP) ipv4Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv4Protocol)
                            .matchTcpSrc(TpPort.tpPort(tcpPacket.getSourcePort()))
                            .matchTcpDst(TpPort.tpPort(tcpPacket.getDestinationPort()));
                }
//                    if (matchTcpUdpPorts && ipv4Protocol == IPv4.PROTOCOL_UDP) {
                if (ipv4Protocol == IPv4.PROTOCOL_UDP) {
                    UDP udpPacket = (UDP) ipv4Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv4Protocol)
                            .matchUdpSrc(TpPort.tpPort(udpPacket.getSourcePort()))
                            .matchUdpDst(TpPort.tpPort(udpPacket.getDestinationPort()));
                }
                if (matchIcmpFields && ipv4Protocol == IPv4.PROTOCOL_ICMP) {
                    ICMP icmpPacket = (ICMP) ipv4Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv4Protocol)
                            .matchIcmpType(icmpPacket.getIcmpType())
                            .matchIcmpCode(icmpPacket.getIcmpCode());
                }
            }

            //
            // If configured and EtherType is IPv6 - Match IPv6 and
            // TCP/UDP/ICMP fields
            //
            if (matchIpv6Address && inPkt.getEtherType() == Ethernet.TYPE_IPV6) {
                IPv6 ipv6Packet = (IPv6) inPkt.getPayload();
                byte ipv6NextHeader = ipv6Packet.getNextHeader();
                Ip6Prefix matchIp6SrcPrefix =
                        Ip6Prefix.valueOf(ipv6Packet.getSourceAddress(),
                                Ip6Prefix.MAX_MASK_LENGTH);
                Ip6Prefix matchIp6DstPrefix =
                        Ip6Prefix.valueOf(ipv6Packet.getDestinationAddress(),
                                Ip6Prefix.MAX_MASK_LENGTH);
                selectorBuilder.matchEthType(Ethernet.TYPE_IPV6)
                        .matchIPv6Src(matchIp6SrcPrefix)
                        .matchIPv6Dst(matchIp6DstPrefix);

                if (matchIpv6FlowLabel) {
                    selectorBuilder.matchIPv6FlowLabel(ipv6Packet.getFlowLabel());
                }

//                if (matchTcpUdpPorts && ipv6NextHeader == IPv6.PROTOCOL_TCP) {
                if (ipv6NextHeader == IPv6.PROTOCOL_TCP) {
                    TCP tcpPacket = (TCP) ipv6Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv6NextHeader)
                            .matchTcpSrc(TpPort.tpPort(tcpPacket.getSourcePort()))
                            .matchTcpDst(TpPort.tpPort(tcpPacket.getDestinationPort()));
                }
//                if (matchTcpUdpPorts && ipv6NextHeader == IPv6.PROTOCOL_UDP) {
                if (ipv6NextHeader == IPv6.PROTOCOL_UDP) {
                    UDP udpPacket = (UDP) ipv6Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv6NextHeader)
                            .matchUdpSrc(TpPort.tpPort(udpPacket.getSourcePort()))
                            .matchUdpDst(TpPort.tpPort(udpPacket.getDestinationPort()));
                }
                if (matchIcmpFields && ipv6NextHeader == IPv6.PROTOCOL_ICMP6) {
                    ICMP6 icmp6Packet = (ICMP6) ipv6Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv6NextHeader)
                            .matchIcmpv6Type(icmp6Packet.getIcmpType())
                            .matchIcmpv6Code(icmp6Packet.getIcmpCode());
                }
            }
        }

        PortNumber portNumber = path.src().port();
        DeviceId dstDeviceId = path.dst().deviceId();
        if (!context.inPacket().receivedFrom().deviceId().equals(securityDeviceId)) {
            for (Link l : path.links()) {
                DeviceId srcDevice = l.src().deviceId();
                if (srcDevice.equals(securityDeviceId)) {
                    continue;
                }
                PortNumber srcPortNumber = l.src().port();
                TrafficTreatment treatment = DefaultTrafficTreatment.builder()
                        .setOutput(srcPortNumber)
                        .build();

                ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                        .withSelector(selectorBuilder.build())
                        .withTreatment(treatment)
                        .withPriority(flowPriority)
                        .withFlag(ForwardingObjective.Flag.VERSATILE)
                        .fromApp(appId)
                        .makeTemporary(30)
                        .add();

                flowObjectiveService.forward(srcDevice,
                        forwardingObjective);
            }
        } else {
            if (!context.inPacket().receivedFrom().deviceId().equals(dstDeviceId)) {
                TrafficTreatment treatment = DefaultTrafficTreatment.builder()
                        .setOutput(portNumber)
                        .build();

                ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                        .withSelector(selectorBuilder.build())
                        .withTreatment(treatment)
                        .withPriority(flowPriority)
                        .withFlag(ForwardingObjective.Flag.VERSATILE)
                        .fromApp(appId)
                        .makeTemporary(30)
                        .add();

                flowObjectiveService.forward(context.inPacket().receivedFrom().deviceId(),
                        forwardingObjective);
            }
            TrafficTreatment treatment = DefaultTrafficTreatment.builder()
                    .setOutput(portNumber)
                    .build();

            ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                    .withSelector(selectorBuilder.build())
                    .withTreatment(treatment)
                    .withPriority(flowPriority)
                    .withFlag(ForwardingObjective.Flag.VERSATILE)
                    .fromApp(appId)
                    .makeTemporary(flowTimeout)
                    .add();

            flowObjectiveService.forward(context.inPacket().receivedFrom().deviceId(),
                    forwardingObjective);
        }

        // If packetOutOfppTable
        //  Send packet back to the OpenFlow pipeline to match installed flow
        // Else
        //  Send packet direction on the appropriate port
        //
        if (packetOutOfppTable) {
            packetOut(context, PortNumber.TABLE);
        } else {
            packetOut(context, portNumber);
            if (context.inPacket().receivedFrom().deviceId().equals(securityDeviceId)) {
                Host securityHost = hostService.getHost(securityHostId);
                PortNumber securityHostPortNumber = securityHost.location().port();
                packetOut(context, securityHostPortNumber);
            }
        }
    }

    // Install a rule forwarding the packet to the specified port.
    private void installRule(PacketContext context, PortNumber portNumber) {
        //
        // We don't support (yet) buffer IDs in the Flow Service so
        // packet out first.
        //
        Ethernet inPkt = context.inPacket().parsed();
        TrafficSelector.Builder selectorBuilder = DefaultTrafficSelector.builder();

        // If PacketOutOnly or ARP packet than forward directly to output port
        if (packetOutOnly || inPkt.getEtherType() == Ethernet.TYPE_ARP) {
            packetOut(context, portNumber);
            return;
        }

        //
        // If matchDstMacOnly
        //    Create flows matching dstMac only
        // Else
        //    Create flows with default matching and include configured fields
        //
        if (matchDstMacOnly) {
            selectorBuilder.matchEthDst(inPkt.getDestinationMAC());
        } else {
            selectorBuilder.matchInPort(context.inPacket().receivedFrom().port())
                    .matchEthSrc(inPkt.getSourceMAC())
                    .matchEthDst(inPkt.getDestinationMAC());

            // If configured Match Vlan ID
            if (matchVlanId && inPkt.getVlanID() != Ethernet.VLAN_UNTAGGED) {
                selectorBuilder.matchVlanId(VlanId.vlanId(inPkt.getVlanID()));
            }

            //
            // If configured and EtherType is IPv4 - Match IPv4 and
            // TCP/UDP/ICMP fields
            //
//            if (matchIpv4Address && inPkt.getEtherType() == Ethernet.TYPE_IPV4) {
            if (inPkt.getEtherType() == Ethernet.TYPE_IPV4) {
                IPv4 ipv4Packet = (IPv4) inPkt.getPayload();
                byte ipv4Protocol = ipv4Packet.getProtocol();
                Ip4Prefix matchIp4SrcPrefix =
                        Ip4Prefix.valueOf(ipv4Packet.getSourceAddress(),
                                Ip4Prefix.MAX_MASK_LENGTH);
                Ip4Prefix matchIp4DstPrefix =
                        Ip4Prefix.valueOf(ipv4Packet.getDestinationAddress(),
                                Ip4Prefix.MAX_MASK_LENGTH);
                selectorBuilder.matchEthType(Ethernet.TYPE_IPV4)
                        .matchIPSrc(matchIp4SrcPrefix)
                        .matchIPDst(matchIp4DstPrefix);

                if (matchIpv4Dscp) {
                    byte dscp = ipv4Packet.getDscp();
                    byte ecn = ipv4Packet.getEcn();
                    selectorBuilder.matchIPDscp(dscp).matchIPEcn(ecn);
                }

//                    if (matchTcpUdpPorts && ipv4Protocol == IPv4.PROTOCOL_TCP) {
                if (ipv4Protocol == IPv4.PROTOCOL_TCP) {
                    TCP tcpPacket = (TCP) ipv4Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv4Protocol)
                            .matchTcpSrc(TpPort.tpPort(tcpPacket.getSourcePort()))
                            .matchTcpDst(TpPort.tpPort(tcpPacket.getDestinationPort()));
                }
//                    if (matchTcpUdpPorts && ipv4Protocol == IPv4.PROTOCOL_UDP) {
                if (ipv4Protocol == IPv4.PROTOCOL_UDP) {
                    UDP udpPacket = (UDP) ipv4Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv4Protocol)
                            .matchUdpSrc(TpPort.tpPort(udpPacket.getSourcePort()))
                            .matchUdpDst(TpPort.tpPort(udpPacket.getDestinationPort()));
                }
                if (matchIcmpFields && ipv4Protocol == IPv4.PROTOCOL_ICMP) {
                    ICMP icmpPacket = (ICMP) ipv4Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv4Protocol)
                            .matchIcmpType(icmpPacket.getIcmpType())
                            .matchIcmpCode(icmpPacket.getIcmpCode());
                }
            }

            //
            // If configured and EtherType is IPv6 - Match IPv6 and
            // TCP/UDP/ICMP fields
            //
            if (matchIpv6Address && inPkt.getEtherType() == Ethernet.TYPE_IPV6) {
                IPv6 ipv6Packet = (IPv6) inPkt.getPayload();
                byte ipv6NextHeader = ipv6Packet.getNextHeader();
                Ip6Prefix matchIp6SrcPrefix =
                        Ip6Prefix.valueOf(ipv6Packet.getSourceAddress(),
                                Ip6Prefix.MAX_MASK_LENGTH);
                Ip6Prefix matchIp6DstPrefix =
                        Ip6Prefix.valueOf(ipv6Packet.getDestinationAddress(),
                                Ip6Prefix.MAX_MASK_LENGTH);
                selectorBuilder.matchEthType(Ethernet.TYPE_IPV6)
                        .matchIPv6Src(matchIp6SrcPrefix)
                        .matchIPv6Dst(matchIp6DstPrefix);

                if (matchIpv6FlowLabel) {
                    selectorBuilder.matchIPv6FlowLabel(ipv6Packet.getFlowLabel());
                }

//                if (matchTcpUdpPorts && ipv6NextHeader == IPv6.PROTOCOL_TCP) {
                if (ipv6NextHeader == IPv6.PROTOCOL_TCP) {
                    TCP tcpPacket = (TCP) ipv6Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv6NextHeader)
                            .matchTcpSrc(TpPort.tpPort(tcpPacket.getSourcePort()))
                            .matchTcpDst(TpPort.tpPort(tcpPacket.getDestinationPort()));
                }
//                if (matchTcpUdpPorts && ipv6NextHeader == IPv6.PROTOCOL_UDP) {
                if (ipv6NextHeader == IPv6.PROTOCOL_UDP) {
                    UDP udpPacket = (UDP) ipv6Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv6NextHeader)
                            .matchUdpSrc(TpPort.tpPort(udpPacket.getSourcePort()))
                            .matchUdpDst(TpPort.tpPort(udpPacket.getDestinationPort()));
                }
                if (matchIcmpFields && ipv6NextHeader == IPv6.PROTOCOL_ICMP6) {
                    ICMP6 icmp6Packet = (ICMP6) ipv6Packet.getPayload();
                    selectorBuilder.matchIPProtocol(ipv6NextHeader)
                            .matchIcmpv6Type(icmp6Packet.getIcmpType())
                            .matchIcmpv6Code(icmp6Packet.getIcmpCode());
                }
            }
        }

        TrafficTreatment treatment = null;
        Host securityHost = hostService.getHost(securityHostId);
        IPv4 ipv4Packet = (IPv4) inPkt.getPayload();
        TCP tcpPacket = (TCP) ipv4Packet.getPayload();
        if (context.inPacket().receivedFrom().deviceId().equals(securityDeviceId)
                && (tcpPacket.getSourcePort() == 20 || tcpPacket.getDestinationPort() == 20)) {
            OutputInstruction oi = Instructions.createOutput(securityHost.location().port());
            treatment = DefaultTrafficTreatment.builder().setOutput(portNumber).add(oi).build();
        } else {
            treatment = DefaultTrafficTreatment.builder()
                    .setOutput(portNumber)
                    .build();
        }

        ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                .withSelector(selectorBuilder.build())
                .withTreatment(treatment)
                .withPriority(flowPriority)
                .withFlag(ForwardingObjective.Flag.VERSATILE)
                .fromApp(appId)
                .makeTemporary(flowTimeout)
                .add();

        flowObjectiveService.forward(context.inPacket().receivedFrom().deviceId(),
                forwardingObjective);

        // install the flow rule to foward the traffic into the security device.
        // By Jinwoo
//        Host securityHost = hostService.getHost(securityHostId);
//        if (context.inPacket().receivedFrom().deviceId().equals(securityDevice.location().deviceId())) {
//            TrafficTreatment treatmentOfSecurityDevice = DefaultTrafficTreatment.builder()
//                    .setOutput(securityDevice.location().port())
//                    .build();
//
//            ForwardingObjective forwardingObjectiveOfSecurityDevice = DefaultForwardingObjective.builder()
//                    .withSelector(selectorBuilder.build())
//                    .withTreatment(treatmentOfSecurityDevice)
//                    .withPriority(flowPriority)
//                    .withFlag(ForwardingObjective.Flag.VERSATILE)
//                    .fromApp(appId)
//                    .makeTemporary(flowTimeout)
//                    .add();
//            flowObjectiveService.forward(context.inPacket().receivedFrom().deviceId(),
//                    forwardingObjectiveOfSecurityDevice);
//        }

        //
        // If packetOutOfppTable
        //  Send packet back to the OpenFlow pipeline to match installed flow
        // Else
        //  Send packet direction on the appropriate port
        //
        if (packetOutOfppTable) {
            packetOut(context, PortNumber.TABLE);
        } else {
            packetOut(context, portNumber);
//            if (context.inPacket().receivedFrom().deviceId().equals(securityDeviceId)) {
//                packetOut(context, securityHost.location().port());
//            }
        }
    }
}
