package org.onosproject.provider.framework.abnormal.feature.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onlab.packet.Ethernet;
import org.onlab.packet.ICMP;
import org.onlab.packet.IPv4;
import org.onlab.packet.IPv6;
import org.onlab.packet.Ip4Prefix;
import org.onlab.packet.Ip6Prefix;
import org.onlab.packet.TCP;
import org.onlab.packet.UDP;
import org.onlab.packet.VlanId;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.feature.AggregateStatisticsFeature;
import org.onosproject.athena.feature.ErrorMessageFeature;
import org.onosproject.athena.feature.FeatureCollectorProviderRegistry;
import org.onosproject.athena.feature.FeatureCollectorProviderService;
import org.onosproject.athena.feature.FeatureIndex;
import org.onosproject.athena.feature.FlowRemovedFeature;
import org.onosproject.athena.feature.FlowStatisticsFeature;
import org.onosproject.athena.feature.PacketInFeature;
import org.onosproject.athena.feature.PortStatisticsFeature;
import org.onosproject.athena.feature.PortStatusFeature;
import org.onosproject.athena.feature.QueueStatisticsFeature;
import org.onosproject.athena.feature.TableStatisticsFeature;
import org.onosproject.athena.feature.UnitAggregateStatistics;
import org.onosproject.athena.feature.UnitErrorMessageInformation;
import org.onosproject.athena.feature.UnitFeature;
import org.onosproject.athena.feature.UnitFlowRemovedInformation;
import org.onosproject.athena.feature.UnitFlowStatistics;
import org.onosproject.athena.feature.UnitPacketInInformation;
import org.onosproject.athena.feature.UnitPortStatistics;
import org.onosproject.athena.feature.UnitPortStatusInformation;
import org.onosproject.athena.feature.UnitQueueStatistics;
import org.onosproject.athena.feature.UnitTableStatistics;
import org.onosproject.cfg.ComponentConfigService;
import org.onosproject.core.CoreService;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.driver.DriverService;
import org.onosproject.net.flow.FlowEntry;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.instructions.Instruction;
import org.onosproject.net.flow.instructions.Instructions;
import org.onosproject.net.packet.DefaultInboundPacket;
import org.onosproject.net.packet.InboundPacket;
import org.onosproject.net.provider.AbstractProvider;
import org.onosproject.net.provider.ProviderId;
import org.onosproject.openflow.controller.Dpid;
import org.onosproject.openflow.controller.OpenFlowAsynchronousEventListener;
import org.onosproject.openflow.controller.OpenFlowController;
import org.onosproject.openflow.controller.OpenFlowPacketContext;
import org.onosproject.openflow.controller.OpenFlowStatisticsListener;
import org.osgi.service.component.ComponentContext;
import org.projectfloodlight.openflow.protocol.OFAggregateStatsReply;
import org.projectfloodlight.openflow.protocol.OFErrorMsg;
import org.projectfloodlight.openflow.protocol.OFFlowRemoved;
import org.projectfloodlight.openflow.protocol.OFFlowStatsEntry;
import org.projectfloodlight.openflow.protocol.OFFlowStatsReply;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPortStatsEntry;
import org.projectfloodlight.openflow.protocol.OFPortStatsReply;
import org.projectfloodlight.openflow.protocol.OFPortStatus;
import org.projectfloodlight.openflow.protocol.OFQueueStatsEntry;
import org.projectfloodlight.openflow.protocol.OFQueueStatsReply;
import org.projectfloodlight.openflow.protocol.OFTableStatsEntry;
import org.projectfloodlight.openflow.protocol.OFTableStatsReply;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IPv6Address;
import org.projectfloodlight.openflow.types.Masked;
import org.projectfloodlight.openflow.types.OFVlanVidMatch;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.toIntExact;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 8/26/15.
 */
@Component(immediate = true)
public class FeatureCollectorProvider
        extends AbstractProvider
        implements org.onosproject.athena.feature.FeatureCollectorProvider {
    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FeatureCollectorProviderRegistry providerRegistry;


    //register componentConfigService
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected ComponentConfigService cfgService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DriverService driverService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected OpenFlowController controller;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    protected InternalStatisticsCollector listener = new InternalStatisticsCollector();
    private FeatureCollectorProviderService providerService;
    protected FeatureCollectorProviderUtil featureCollectorProviderUtil = new FeatureCollectorProviderUtil();

    // Local feature tables - By Jinwoo
    ConcurrentHashMap<BigInteger, UnitFlowStatistics> fStatisticsLF = new ConcurrentHashMap<BigInteger,
            UnitFlowStatistics>();
    ConcurrentHashMap<BigInteger, UnitPortStatistics> pStatisticsLF = new ConcurrentHashMap<BigInteger,
            UnitPortStatistics>();
    // for print out
//    ConcurrentHashMap<String, UnitTableStatistics> tStatisticsLFT = new ConcurrentHashMap<String,
//    UnitTableStatistics>();
//    ConcurrentHashMap<String, UnitFlowRemovedInformation> frStatisticsLFT = new ConcurrentHashMap<String,
//    UnitFlowRemovedInformation>();

    // threshold value for polling rate - By Jinwoo
    static final long THRESHOLD = 3;
    static final long FLOW_POLL_INTERVAL = 5;
    static final long PORT_POLL_INTERVAL = 5;
    static final long TIME_THRESHOLD_SECONDS = 10;
    public long packetIn;

    public FeatureCollectorProvider() {

        super(new ProviderId("framework", "org.onosproject.provider.framework"));
    }

    @Activate
    public void activate(ComponentContext context) {
        log.info("Starts!");
        providerService = providerRegistry.register(this);
        controller.addAsynchronousListener(0, listener);
        controller.addStatisticsListener(0, listener);
        //add expiration collector
        Timer timer = new Timer(false);
        timer.schedule(new ExpirationCollector(), TIME_THRESHOLD_SECONDS * 1000, TIME_THRESHOLD_SECONDS * 1000);
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        controller.removeAsynchronousListener(listener);
        controller.removeStatisticsListener(listener);
        log.info("End");
        providerRegistry.unregister(this);

    }

    @Modified
    public void modified(ComponentContext context) {

    }

    private class InternalStatisticsCollector
            implements OpenFlowStatisticsListener, OpenFlowAsynchronousEventListener {

        @Override
        public void packetInProcess(Dpid dpid, OFPacketIn packetIn, OpenFlowPacketContext pktCtx) {
            //exclude abnormal packet -- Jinwoo Kim
            //only store ipv4 packet to DB
            try {
                short etherType = convertPacketContextToInboundPacket(pktCtx).parsed().getEtherType();
                if (etherType != 2048) {
                    return;
                }
            } catch (Exception e) {
                log.error(e.toString());
            }

            InboundPacket ip = convertPacketContextToInboundPacket(pktCtx);
            PacketInFeature pif = new PacketInFeature();
            UnitPacketInInformation ufii;
            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            if (packetIn.getVersion() == OFVersion.OF_13) {
                FeatureIndex pfi = matchToFeatureIndex(packetIn.getMatch());
                pfi = extractElementsFromInboundPkt(pfi, ip);
                ufii = new UnitPacketInInformation(packetIn.getTotalLen(),
                        packetIn.getReason().ordinal(), pfi);
                // in case of OF_10, just store dummy match value
                // Jinwoo kim
            } else {
                ufii = new UnitPacketInInformation();
//                return;
            }
            ufii.setDate(date);

            FeatureIndex fi = new FeatureIndex();
            fi.setSwitchDatapathId(dpid.value());
            fi.setSwitchPortId(toIntExact(ip.receivedFrom().port().toLong()));

            pif.addFeatureData(fi, ufii);

            providerService.packetInFeatureHandler(pif);
        }

        @Override
        public void flowRemovedProcess(Dpid dpid, OFFlowRemoved flowRemoved) {
            FlowRemovedFeature frf = new FlowRemovedFeature();

            UnitFlowRemovedInformation ufri = new UnitFlowRemovedInformation(flowRemoved.getReason(),
                    flowRemoved.getDurationSec(), flowRemoved.getDurationNsec(),
                    flowRemoved.getIdleTimeout(), flowRemoved.getHardTimeout(),
                    flowRemoved.getPacketCount().getValue(), flowRemoved.getByteCount().getValue());
            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            ufri.setDate(date);

            FeatureIndex fi = matchToFeatureIndex(flowRemoved.getMatch());
            fi.setSwitchDatapathId(dpid.value());

            // extract rich feature -> store to UnitFlowstatistics
            ufri = (UnitFlowRemovedInformation) extractRichFeature(fi, ufri, AthenaFeatureField.FLOW_REMOVED);
            frf.addFeatureData(fi, ufri);

//            printFlowRemovedLFT(frStatisticsLFT);
            providerService.flowRemovedHandler(frf);
        }

        @Override
        public void portStatusProcess(Dpid dpid, OFPortStatus portStatus) {
            PortStatusFeature psf = new PortStatusFeature();

            UnitPortStatusInformation upsi =
                    new UnitPortStatusInformation(portStatus.getReason().ordinal());

            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            upsi.setDate(date);

            FeatureIndex fi = new FeatureIndex();

            fi.setSwitchDatapathId(dpid.value());

            psf.addFeatureData(fi, upsi);

            providerService.portStatusHandler(psf);
        }

        @Override
        public void errorMsgProcess(Dpid dpid, OFErrorMsg errorMsg) {
            ErrorMessageFeature emf = new ErrorMessageFeature();

            UnitErrorMessageInformation uemi =
                    new UnitErrorMessageInformation(errorMsg.getErrType().ordinal());

            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            uemi.setDate(date);

            FeatureIndex fi = new FeatureIndex();


            fi.setSwitchDatapathId(dpid.value());

            emf.addFeatureData(fi, uemi);

            providerService.errorMsgHandler(emf);
        }

        @Override
        public void aggregateStatsProcess(Dpid dpid, OFAggregateStatsReply reply) {
            //magic number
            if (reply.getXid() != 1126) {
                return;
            }
            AggregateStatisticsFeature asf = new AggregateStatisticsFeature();
            UnitAggregateStatistics uas =
                    new UnitAggregateStatistics(reply.getPacketCount().getValue(),
                            reply.getByteCount().getValue(), reply.getFlowCount());

            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            uas.setDate(date);

            FeatureIndex fi = new FeatureIndex();


            fi.setSwitchDatapathId(dpid.value());

            asf.addFeatureData(fi, uas);

            providerService.aggregateStatsHandler(asf);
        }

        public Map<OFFlowStatsEntry, FlowEntry> getApplicationInfoFromInternalFlowTable(
                Dpid dpid,
                List<OFFlowStatsEntry> statsEntries) {

            //private DeviceId deviceId = DeviceId.deviceId("of:0000000000000001");
            DeviceId deviceId = DeviceId.deviceId(dpid.uri(dpid));

            /**
             *TODO optimize mechanism for comparing between incoming and internal one.
             */
            Map<OFFlowStatsEntry, FlowEntry> flowEntiresWithFlowInformation = new HashMap<>();

            Iterable<FlowEntry> internalFlowEntries = flowRuleService.getFlowEntries(deviceId);

            for (int i = 0; i < statsEntries.size(); i++) {
                OFFlowStatsEntry entry = statsEntries.get(i);
                TrafficSelector inSelector =
                        featureCollectorProviderUtil.buildSelector(entry.getMatch());
                for (FlowEntry flowEntry : internalFlowEntries) {
                    if (flowEntry.selector().equals(inSelector)) {
                        flowEntiresWithFlowInformation.put(entry, flowEntry);
                        break;
                    }

                }
            }
            return flowEntiresWithFlowInformation;
        }

        public double[] getPairFlowInformation(Map<OFFlowStatsEntry, FlowEntry> table,
                                               Map<OFFlowStatsEntry, Boolean> pairFlowSet) {
            double[] vals = new double[4];

            //total flow
            vals[0] = table.size();

            //pair flow
            vals[1] = pairFlowSet.size();

            //single flow
            vals[2] = vals[0] - vals[1];

            //pairflowratio flow
            if (vals[1] == 0) {
                vals[3] = 0;
            } else {
                vals[3] = vals[1] / vals[0];
            }
            return vals;
        }


        public Map<OFFlowStatsEntry, Boolean> getPairFlowSet(Map<OFFlowStatsEntry, FlowEntry> table) {
            Map<OFFlowStatsEntry, Boolean> pairFlowSet = new HashMap<>();
            Map<OFFlowStatsEntry, int[]> pairFlowInfo = new HashMap<>();
            for (Map.Entry<OFFlowStatsEntry, FlowEntry> entry : table.entrySet()) {
                OFFlowStatsEntry e = entry.getKey();
                Match match = e.getMatch();
                //0 - src, 1 - dst, 2 - proto
                int[] vals = new int[3];
                vals[0] = 0;
                vals[1] = 0;
                vals[2] = 0;
                for (MatchField<?> field : match.getMatchFields()) {
                    switch (field.id) {
                        case IPV4_SRC:
                            vals[0] = match.get(MatchField.IPV4_SRC).getInt();
                            break;
                        case IPV4_DST:
                            vals[1] = match.get(MatchField.IPV4_DST).getInt();
                            break;
                        case IP_PROTO:
                            vals[2] = match.get(MatchField.IP_PROTO).getIpProtocolNumber();
                            break;
                        default:
                            break;
                    }
                }
                if (vals[0] == 0 || vals[1] == 0) {
                    continue;
                }
                pairFlowInfo.put(e, vals);
            }

            //extract pairflow
            for (Map.Entry<OFFlowStatsEntry, int[]> entryCur : pairFlowInfo.entrySet()) {
                boolean pairflow = false;
                int[] valsCur = entryCur.getValue();

                for (Map.Entry<OFFlowStatsEntry, int[]> entryTar : pairFlowInfo.entrySet()) {
                    int[] valsTar = entryTar.getValue();

                    //src(cur) == dst(tar), src(cur) == dst(tar), proto ==
                    if ((valsCur[0] == valsTar[1]) &&
                            (valsCur[1] == valsTar[0]) &&
                            (valsCur[2] == valsTar[2])
                            ) {
                        pairflow = true;
                        break;
                    }
                }

                if (pairflow) {
                    pairFlowSet.put(entryCur.getKey(), true);
                }
            }
            return pairFlowSet;
        }

        @Override
        public void flowStatsProcess(Dpid dpid, OFFlowStatsReply reply) {

            //magic number
            if (reply.getXid() != 1127) {
                return;
            }
            FlowStatisticsFeature fsf = new FlowStatisticsFeature();

            Map<OFFlowStatsEntry, FlowEntry> flowEntiresWithApps =
                    getApplicationInfoFromInternalFlowTable(dpid, reply.getEntries());


            //make a consistent among each entries
            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            //pair flow related
            Map<OFFlowStatsEntry, Boolean> pairFlowSet = getPairFlowSet(flowEntiresWithApps);

            //common pair flow information
            double[] pairFlowInformation = getPairFlowInformation(flowEntiresWithApps,
                    pairFlowSet);

            for (Map.Entry<OFFlowStatsEntry, FlowEntry> entry : flowEntiresWithApps.entrySet()) {
                OFFlowStatsEntry e = entry.getKey();
                FlowEntry fe = entry.getValue();
                UnitFlowStatistics ufs = new UnitFlowStatistics(e.getDurationSec(),
                        e.getDurationNsec(),
                        e.getPriority(),
                        e.getIdleTimeout(),
                        e.getHardTimeout(),
                        e.getPacketCount().getValue(),
                        e.getByteCount().getValue());

                //set pairFlowInformation
                ufs.setTotalFlows(pairFlowInformation[0]);
                ufs.setTotalPairFlow(pairFlowInformation[1]);
                ufs.setTotalSingleFlow(pairFlowInformation[2]);
                ufs.setPairFlowRatio(pairFlowInformation[3]);
                //set Pairflow itself
                if (pairFlowSet.containsKey(e)) {
                    ufs.setPairFlow(true);
                } else {
                    ufs.setPairFlow(false);
                }

                List<Instruction> instructionList = fe.treatment().allInstructions();

                for (int i = 0; i < fe.treatment().allInstructions().size(); i++) {
                    Instruction instruction = instructionList.get(i);
                    //get all actions
                    switch (instruction.type()) {
                        case OUTPUT:
                            PortNumber pn = ((Instructions.OutputInstruction) instruction).port();
                            ufs.setActionOutput(true);
                            ufs.setActionOutputPort(pn.toLong());
                            break;
                        default:
                            log.warn("not supported Instruction Type");
                    }
                }

                //No action in here !
                if (fe.treatment().allInstructions().size() == 0) {
                    ufs.setActionDrop(true);
                }

                ufs.setDate(date);
                ufs.setApplicationId(coreService.getAppId(fe.appId()));

                FeatureIndex fi = matchToFeatureIndex(e.getMatch());

                fi.setSwitchDatapathId(dpid.value());
                fi.setSwitchTableId(e.getTableId().getValue());

                // extract rich feature -> store to UnitFlowstatistics
                ufs = (UnitFlowStatistics) extractRichFeature(fi, ufs, AthenaFeatureField.FLOW_STATS);

                // add Unitstatistics data with a feature index.
                fsf.addFeatureData(fi, ufs);
            }
//            printFlowStatsLFT(fStatisticsLF);
            providerService.flowStatsHandler(fsf);
        }

        @Override
        public void portStatsProcess(Dpid dpid, OFPortStatsReply reply) {
            //magic number
            if (reply.getXid() != 1128) {
                return;
            }
            PortStatisticsFeature psf = new PortStatisticsFeature();

            List<OFPortStatsEntry> opse = reply.getEntries();
            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            for (OFPortStatsEntry p : opse) {
                UnitPortStatistics ups = new UnitPortStatistics(p.getRxPackets().getValue(),
                        p.getTxPackets().getValue(), p.getRxBytes().getValue(),
                        p.getTxBytes().getValue(), p.getRxDropped().getValue(),
                        p.getTxDropped().getValue(), p.getRxErrors().getValue(),
                        p.getTxErrors().getValue(), p.getRxFrameErr().getValue(),
                        p.getRxOverErr().getValue(), p.getRxCrcErr().getValue(),
                        p.getCollisions().getValue());

                ups.setDate(date);

                FeatureIndex fi = new FeatureIndex();


                fi.setSwitchDatapathId((dpid.value()));
                fi.setSwitchPortId(p.getPortNo().getPortNumber());

                // extract rich feature -> store to UnitFlowstatistics
                ups = (UnitPortStatistics) extractRichFeature(fi, ups, AthenaFeatureField.PORT_STATS);

                psf.addFeatureData(fi, ups);
            }
            // to print LFT
//            printPortStatsLFT(pStatisticsLF);

            providerService.portStatsHandler(psf);
        }


        @Override
        public void queueStatsProcess(Dpid dpid, OFQueueStatsReply reply) {
            //magic number
            if (reply.getXid() != 1129) {
                return;
            }
            QueueStatisticsFeature qsf = new QueueStatisticsFeature();

            List<OFQueueStatsEntry> oqsr = reply.getEntries();
            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            for (OFQueueStatsEntry q : oqsr) {
                UnitQueueStatistics uqs = new UnitQueueStatistics(q.getTxBytes().getValue(),
                        q.getTxPackets().getValue(), q.getTxErrors().getValue());

                uqs.setDate(date);

                FeatureIndex fi = new FeatureIndex();


                fi.setSwitchDatapathId(dpid.value());
                fi.setSwitchPortId(q.getPortNo().getPortNumber());
                fi.setSwitchQeueueId(q.getQueueId());

                qsf.addFeatureData(fi, uqs);
            }

            providerService.queueStatsHandler(qsf);
        }

        @Override
        public void tableStatsProcess(Dpid dpid, OFTableStatsReply reply) {
            //magic number
            if (reply.getXid() != 1130) {
                return;
            }
            TableStatisticsFeature tsf = new TableStatisticsFeature();

            List<OFTableStatsEntry> otse = reply.getEntries();
            Date date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            for (OFTableStatsEntry t : otse) {
                if (t.getActiveCount() == 0) {
                    break;
                }

                UnitTableStatistics uts = new UnitTableStatistics(t.getMaxEntries(),
                        t.getActiveCount(), t.getLookupCount().getValue(), t.getMatchedCount().getValue());

                uts.setDate(date);

                FeatureIndex fi = new FeatureIndex();

                fi.setSwitchDatapathId(dpid.value());
                fi.setSwitchTableId(t.getTableId().getValue());

                // extract rich feature -> store to UnitTablestatistics
                uts = (UnitTableStatistics) extractRichFeature(fi, uts, AthenaFeatureField.TABLE_STATS);
                tsf.addFeatureData(fi, uts);

            }

//            printTableStatsLFT(tStatisticsLFT);
            providerService.tableStatsHandler(tsf);
        }
    }


    //Utils.

    public InboundPacket convertPacketContextToInboundPacket(OpenFlowPacketContext pktCtx) {
        //convert OpenFlowPacketContext to PacketContext
        DeviceId id = DeviceId.deviceId(Dpid.uri(pktCtx.dpid().value()));

        DefaultInboundPacket inPkt = new DefaultInboundPacket(
                new ConnectPoint(id, PortNumber.portNumber(pktCtx.inPort())),
                pktCtx.parsed(), ByteBuffer.wrap(pktCtx.unparsed()));
/*
        DefaultOutboundPacket outPkt = null;
        if (!pktCtx.isBuffered()) {
            outPkt = new DefaultOutboundPacket(id, null,
                    ByteBuffer.wrap(pktCtx.unparsed()));
        }


        OpenFlowCorePacketContext context =
                new OpenFlowCorePacketContext(System.currTimeMillis(),
                        inPkt, outPkt, pktCtx.isHandled(), pktCtx);
*/
        return inPkt;
    }

    public FeatureIndex extractElementsFromInboundPkt(FeatureIndex fi, InboundPacket pkt) {
        Ethernet ethPkt = pkt.parsed();

        //EthType.EtherType.ARP.ethType().toShort()
        //EthType ethType = new EthType();

        if (ethPkt == null) {
            return fi;
        }
        if (ethPkt.getDestinationMAC().toLong() != 0) {
            fi.setMatchEthDst(ethPkt.getDestinationMAC().toLong());
        }
        if (ethPkt.getSourceMAC().toLong() != 0) {
            fi.setMatchEthSrc(ethPkt.getSourceMAC().toLong());
        }

        if (ethPkt.getEtherType() == ethPkt.TYPE_ARP) {
            fi.setMatchEthType(ethPkt.getEtherType());
        } else if (ethPkt.getEtherType() == ethPkt.VLAN_UNTAGGED) {
            fi.setMatchEthType(ethPkt.getEtherType());
            fi.setMatchVlanVid(ethPkt.getVlanID());
        } else if (ethPkt.getEtherType() == ethPkt.TYPE_IPV4) {
            fi.setMatchEthType(ethPkt.getEtherType());
            IPv4 ipv4Packet = (IPv4) ethPkt.getPayload();
            byte ipv4Protocol = ipv4Packet.getProtocol();
            fi.setMatchIpv4Src(ipv4Packet.getSourceAddress());
            fi.setMatchIpv4Dst(ipv4Packet.getDestinationAddress());
            fi.setMatchIpDscp(ipv4Packet.getDscp());
            fi.setMatchIpEcn(ipv4Packet.getEcn());

            if (ipv4Protocol == IPv4.PROTOCOL_TCP) {
                TCP tcpPacket = (TCP) ipv4Packet.getPayload();
                fi.setMatchIpProto((short) ipv4Protocol);
                fi.setMatchTcpSrc(tcpPacket.getSourcePort());
                fi.setMatchTcpDst(tcpPacket.getDestinationPort());
            }
            if (ipv4Protocol == IPv4.PROTOCOL_UDP) {
                UDP udpPacket = (UDP) ipv4Packet.getPayload();
                fi.setMatchIpProto((short) ipv4Protocol);
                fi.setMatchUdpSrc(udpPacket.getSourcePort());
                fi.setMatchUdpDst(udpPacket.getDestinationPort());
            }
            if (ipv4Protocol == IPv4.PROTOCOL_ICMP) {
                ICMP icmpPacket = (ICMP) ipv4Packet.getPayload();
                fi.setMatchIpProto((short) ipv4Protocol);
                fi.setMatchIcmpv4Code(icmpPacket.getIcmpCode());
                fi.setMatchIcmpv4Type(icmpPacket.getIcmpType());
            }
            // log.warn("[DEbug] IPv4 !!!!!!!!!!!!!!!!!!!");
        } else if (ethPkt.getEtherType() == ethPkt.TYPE_IPV6) {
            fi.setMatchEthType(ethPkt.getEtherType());
            IPv6 ipv6Packet = (IPv6) ethPkt.getPayload();
            byte ipv6NextHeader = ipv6Packet.getNextHeader();
            fi.setMatchIpv6Src(ipv6Packet.getSourceAddress());
            fi.setMatchIpv6Dst(ipv6Packet.getDestinationAddress());
            fi.setMatchIpv6Flabel(ipv6Packet.getFlowLabel());

            if (ipv6NextHeader == IPv6.PROTOCOL_TCP) {
                TCP tcpPacket = (TCP) ipv6Packet.getPayload();
                fi.setMatchIpProto((short) ipv6NextHeader);
                fi.setMatchTcpSrc(tcpPacket.getSourcePort());
                fi.setMatchTcpDst(tcpPacket.getDestinationPort());
            }
            if (ipv6NextHeader == IPv6.PROTOCOL_UDP) {
                UDP udpPacket = (UDP) ipv6Packet.getPayload();
                fi.setMatchIpProto((short) ipv6NextHeader);
                fi.setMatchUdpSrc(udpPacket.getSourcePort());
                fi.setMatchUdpDst(udpPacket.getDestinationPort());
            }
            if (ipv6NextHeader == IPv6.PROTOCOL_ICMP6) {
                ICMP icmpPacket = (ICMP) ipv6Packet.getPayload();
                fi.setMatchIpProto((short) ipv6NextHeader);
                fi.setMatchIcmpv6Code(icmpPacket.getIcmpCode());
                fi.setMatchIcmpv6Type(icmpPacket.getIcmpType());
            }
        } else {
            //log.info("Not supported Ethernet Type(Packet_In payload)");
            return null;
        }
        return fi;
    }

    //See the FlowEntryBuilder
    public FeatureIndex matchToFeatureIndex(Match match) {

        FeatureIndex fi = new FeatureIndex();
        for (MatchField<?> field : match.getMatchFields()) {
            switch (field.id) {
                case IN_PORT:
                    fi.setMatchInPort(match.get(MatchField.IN_PORT).getPortNumber());
                    break;
                case IN_PHY_PORT:
                    fi.setMatchInPhyPort(match.get(MatchField.IN_PHY_PORT).getPortNumber());
                    break;
                case METADATA:
                    log.info("Skip MEDATADATA");
                    break;
                case ETH_DST:
                    fi.setMatchEthDst(match.get(MatchField.ETH_DST).getLong());
                    break;
                case ETH_SRC:
                    fi.setMatchEthSrc(match.get(MatchField.ETH_SRC).getLong());
                    break;
                case ETH_TYPE:
                    fi.setMatchEthType(match.get(MatchField.ETH_TYPE).getValue());
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
                        vlanId = VlanId.vlanId(match.get(MatchField.VLAN_VID).getVlan());
                    }
                    fi.setMatchVlanVid(vlanId.toShort());
                    break;
                case VLAN_PCP:
                    fi.setMatchVlanPcp(match.get(MatchField.VLAN_PCP).getValue());
                    break;
                case IP_DSCP:
                    fi.setMatchIpDscp(match.get(MatchField.IP_DSCP).getDscpValue());
                    break;
                case IP_ECN:
                    fi.setMatchIpEcn(match.get(MatchField.IP_ECN).getEcnValue());
                    break;
                case IP_PROTO:
                    fi.setMatchIpProto(match.get(MatchField.IP_PROTO).getIpProtocolNumber());
                    break;
                case IPV4_SRC:
                    if (match.isPartiallyMasked(MatchField.IPV4_SRC)) {
                        Masked<IPv4Address> maskedIp = match.getMasked(MatchField.IPV4_SRC);
                        fi.setMatchIpv4Src(maskedIp.getValue().getInt());
                        fi.setMatchIpv4SrcMask(maskedIp.getMask().asCidrMaskLength());
                    } else {
                        fi.setMatchIpv4Src(match.get(MatchField.IPV4_SRC).getInt());
                        fi.setMatchIpv4SrcMask(Ip4Prefix.MAX_MASK_LENGTH);
                    }
                    break;
                case IPV4_DST:
                    if (match.isPartiallyMasked(MatchField.IPV4_DST)) {
                        Masked<IPv4Address> maskedIp = match.getMasked(MatchField.IPV4_DST);
                        fi.setMatchIpv4Dst(maskedIp.getValue().getInt());
                        fi.setMatchIpv4DstMask(maskedIp.getMask().asCidrMaskLength());
                    } else {
                        fi.setMatchIpv4Dst(match.get(MatchField.IPV4_DST).getInt());
                        fi.setMatchIpv4DstMask(Ip4Prefix.MAX_MASK_LENGTH);
                    }
                    break;
                case TCP_SRC:
                    fi.setMatchTcpSrc(match.get(MatchField.TCP_SRC).getPort());
                    break;
                case TCP_DST:
                    fi.setMatchTcpDst(match.get(MatchField.TCP_DST).getPort());
                    break;
                case UDP_SRC:
                    fi.setMatchUdpSrc(match.get(MatchField.UDP_SRC).getPort());
                    break;
                case UDP_DST:
                    fi.setMatchUdpDst(match.get(MatchField.UDP_DST).getPort());
                    break;
                case MPLS_LABEL:
                    fi.setMatchMplsLabel((int) match.get(MatchField.MPLS_LABEL).getValue());
                    break;
                case SCTP_SRC:
                    fi.setMatchSctpSrc(match.get(MatchField.SCTP_SRC).getPort());
                    break;
                case SCTP_DST:
                    fi.setMatchSctpDst(match.get(MatchField.SCTP_DST).getPort());
                    break;
                case ICMPV4_TYPE:
                    fi.setMatchIcmpv4Type((byte) match.get(MatchField.ICMPV4_TYPE).getType());
                    break;
                case ICMPV4_CODE:
                    fi.setMatchIcmpv4Code((byte) match.get(MatchField.ICMPV4_CODE).getCode());
                    break;
                case IPV6_SRC:
                    if (match.isPartiallyMasked(MatchField.IPV6_SRC)) {
                        Masked<IPv6Address> maskedIp = match.getMasked(MatchField.IPV6_SRC);
                        fi.setMatchIpv6Src(maskedIp.getValue().getBytes());
                        fi.setMatchIpv6SrcMask(maskedIp.getMask().asCidrMaskLength());
                    } else {
                        fi.setMatchIpv6Src(match.get(MatchField.IPV6_SRC).getBytes());
                        fi.setMatchIpv6SrcMask(Ip6Prefix.MAX_MASK_LENGTH);
                    }
                    break;
                case IPV6_DST:
                    if (match.isPartiallyMasked(MatchField.IPV6_DST)) {
                        Masked<IPv6Address> maskedIp = match.getMasked(MatchField.IPV6_DST);
                        fi.setMatchIpv6Dst(maskedIp.getValue().getBytes());
                        fi.setMatchIpv6DstMask(maskedIp.getMask().asCidrMaskLength());
                    } else {
                        fi.setMatchIpv6Dst(match.get(MatchField.IPV6_DST).getBytes());
                        fi.setMatchIpv6DstMask(Ip6Prefix.MAX_MASK_LENGTH);
                    }
                    break;
                case IPV6_FLABEL:
                    fi.setMatchIpv6Flabel(match.get(MatchField.IPV6_FLABEL).getIPv6FlowLabelValue());
                    break;
                case ICMPV6_TYPE:
                    fi.setMatchIcmpv6Type((byte) match.get(MatchField.ICMPV6_TYPE).getValue());
                    break;
                case ICMPV6_CODE:
                    fi.setMatchIcmpv6Code((byte) match.get(MatchField.ICMPV6_CODE).getValue());
                    break;
                case IPV6_ND_TARGET:
                    fi.setMatchIpv6NdTarget(match.get(MatchField.IPV6_ND_TARGET).getBytes());
                    break;
                case IPV6_ND_SLL:
                    fi.setMatchIpv6NdSll(match.get(MatchField.IPV6_ND_SLL).getLong());
                    break;
                case IPV6_ND_TLL:
                    fi.setMatchIpv6NdTll(match.get(MatchField.IPV6_ND_TLL).getLong());
                    break;
                case IPV6_EXTHDR:
                    fi.setMatchIpv6Exthdr(match.get(MatchField.IPV6_EXTHDR).getValue());
                    break;
                case OCH_SIGID:
                case OCH_SIGTYPE:
                case ARP_OP:
                case ARP_SHA:
                case ARP_SPA:
                case ARP_THA:
                case ARP_TPA:
                case MPLS_TC:
                case TUNNEL_ID:
                default:
                    log.warn("Match type {} not yet implemented.", field.id);
            }
        }
        return fi;
    }

    // extract rich feature from statistic OF packet - By Jinwoo
    public UnitFeature extractRichFeature(FeatureIndex fi, UnitFeature uf, String fname) {

        BigInteger hashIndex = generateHashIndex(fi);
        try {
            switch (fname) {
                case AthenaFeatureField.FLOW_STATS:
                    UnitFlowStatistics ufs = (UnitFlowStatistics) uf;
                    ufs = calculateFlowStatsRichFeatures(hashIndex, ufs);
                    uf = ufs;
                    break;

                case AthenaFeatureField.PORT_STATS:
                    UnitPortStatistics ups = (UnitPortStatistics) uf;
                    ups = calculatePortStatsRichFeatures(hashIndex, ups);
                    uf = ups;
                    break;

                case AthenaFeatureField.FLOW_REMOVED:
                    UnitFlowRemovedInformation ufri = (UnitFlowRemovedInformation) uf;
                    ufri = calculateFlowRemovedRichFeatures(hashIndex, ufri);
                    uf = ufri;
                    break;

                case AthenaFeatureField.TABLE_STATS:
                    UnitTableStatistics uts = (UnitTableStatistics) uf;
                    uts = calculateTableStatsRichFeatures(hashIndex, uts);
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            log.warn(e.toString());
        }
        return uf;
    }

    //TODO: Could be serious problem...
    // generate HashIndex of LFT - By Jinwoo
    public BigInteger generateHashIndex(FeatureIndex fi) {

        BigInteger result = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            // allocate byte buffer for all feature index
            // TODO : should be more intelligent way
            ByteBuffer bb = ByteBuffer.allocate(4 * Long.BYTES + 18 * Integer.BYTES + 2
                    * Short.BYTES + 3 * Byte.BYTES);

            bb.putLong(fi.getSwitchDatapathId());
            bb.putInt(fi.getSwitchPortId());
            bb.putLong(fi.getSwitchQeueueId());
            bb.putInt(fi.getSwitchTableId());
            bb.putInt(fi.getMatchInPort());
            bb.putInt(fi.getMatchInPhyPort());
            bb.putLong(fi.getMatchEthDst());
            bb.putLong(fi.getMatchEthSrc());
            bb.putInt(fi.getMatchEthType());
            bb.putShort(fi.getMatchVlanVid());
            bb.put(fi.getMatchVlanPcp());
            bb.put(fi.getMatchIpDscp());
            bb.put(fi.getMatchIpEcn());
            bb.putShort(fi.getMatchIpProto());
            bb.putInt(fi.getMatchIpv4Src());
            bb.putInt(fi.getMatchIpv4SrcMask());
            bb.putInt(fi.getMatchIpv4Dst());
            bb.putInt(fi.getMatchIpv4DstMask());
            bb.putInt(fi.getMatchTcpSrc());
            bb.putInt(fi.getMatchTcpDst());
            bb.putInt(fi.getMatchUdpDst());
            bb.putInt(fi.getMatchUdpDst());
            bb.putInt(fi.getMatchMplsLabel());
            bb.putInt(fi.getMatchSctpSrc());
            bb.putInt(fi.getMatchSctpDst());
            bb.putInt(fi.getMatchIcmpv4Code());
            bb.putInt(fi.getMatchIcmpv4Type());

            // generate hash index using MD5
            byte[] hashIndex = md.digest(bb.array());
            bb.clear();
            md.reset();

            // generate string of hashIndex
//            StringBuilder key = new StringBuilder();
//            for (int i = 0; i < hashIndex.length; i++) {
//                key.append(String.format("%02x", hashIndex[i]));
//            }

            result = new BigInteger(hashIndex);

        } catch (Exception e) {
            log.warn(e.toString());
        }

        return result;
    }

    public synchronized UnitFlowStatistics calculateFlowStatsRichFeatures(BigInteger hashIndex,
                                                                          UnitFlowStatistics ufs) {

        try {
            double timeDiff;
            double prevTotalDuration;
            double currTotalDuration = ((ufs.getDurationSec() * (long) Math.pow(10, 9)) + ufs.getDurationNsec());

            // look up LFT using hash index
            UnitFlowStatistics prevUfs = fStatisticsLF.get(hashIndex);
            if (prevUfs == null) {
                timeDiff = -1;
            } else {
                prevTotalDuration = (prevUfs.getDurationSec() * (long) Math.pow(10, 9)) + prevUfs.getDurationNsec();
                timeDiff = (currTotalDuration - prevTotalDuration) / (double) Math.pow(10, 9);
            }

            if (timeDiff == 0) {
                return ufs;
            }

            // extract current packet, byte count and time
            long currPacketCount = ufs.getPacketCount();
            long currByteCount = ufs.getByteCount();
            double currTotalFlow = ufs.getTotalFlows();
            double currTotalPairFlow = ufs.getTotalPairFlow();
            double currTotalSingleFlow = ufs.getTotalSingleFlow();
            double currPairFlowRatio = ufs.getPairFlowRatio();

            // rich feature variables
            double currPacketCountVar = 0;
            double currByteCountVar = 0;
            double currBytePerPacket = 0;
            double currBytePerPacketVar = 0;
            double currPacketPerDuration = 0;
            double currPacketPerDurationVar = 0;
            double currBytePerDuration = 0;
            double currBytePerDurationVar = 0;
            //add (Existing values)
            double currTotalFlowVar = 0;
            double currTotalPairFlowVar = 0;
            double currTotalSingleFlowVar = 0;
            double currPairFlowRatioVar = 0;


            // if current packet count is zero, set bytePerPacket as zero
            if (currPacketCount != 0) {
                currBytePerPacket = currByteCount / (double) currPacketCount;
            }

            // 2. Calculate variation
            if (timeDiff > 0 && timeDiff < FLOW_POLL_INTERVAL * THRESHOLD) {
                // extract previous features
                long prevPacketCount = prevUfs.getPacketCount();
                long prevByteCount = prevUfs.getByteCount();
                double prevTotalFlow = prevUfs.getTotalFlows();
                double prevTotalPairFlow = prevUfs.getTotalPairFlow();
                double prevTotalSingleFlow = prevUfs.getTotalSingleFlow();
                double prevPairFlowRatio = prevUfs.getPairFlowRatio();

                double prevBytePerPacket = prevUfs.getBytePerPacket();
                double prevPacketPerDuration = prevUfs.getPacketPerDuration();
                double prevBytePerDuration = prevUfs.getPacketPerDuration();


                currPacketCountVar = (currPacketCount - prevPacketCount) / timeDiff;
                currByteCountVar = (currByteCount - prevByteCount) / timeDiff;
                currTotalFlowVar = (currTotalFlow - prevTotalFlow) / timeDiff;

                if ((currTotalPairFlow - prevTotalPairFlow) != 0) {
                    currTotalPairFlowVar = (currTotalPairFlow - prevTotalPairFlow) / timeDiff;
                }
                if ((currTotalSingleFlow - prevTotalSingleFlow) != 0) {
                    currTotalSingleFlowVar = (currTotalSingleFlow - prevTotalSingleFlow) / timeDiff;
                }
                if ((currPairFlowRatio - prevPairFlowRatio) != 0) {
                    currPairFlowRatioVar = (currPairFlowRatio - prevPairFlowRatio) / timeDiff;
                }

                currBytePerPacketVar = (currBytePerPacket - prevBytePerPacket) / timeDiff;
                currTotalDuration /= (double) Math.pow(10, 9);
                currPacketPerDuration = currPacketCount / (double) currTotalDuration;
                currPacketPerDurationVar = (currPacketPerDuration - prevPacketPerDuration) / timeDiff;
                currBytePerDuration = currByteCount / (double) currTotalDuration;
                currBytePerDurationVar = (currBytePerDuration - prevBytePerDuration) / timeDiff;


                // prefix + attribute (var) => refer to the prefix
                // currBytePerDuration - prevBytePerDuration / TD
                // 1. new or 3. Expire entries (EC)
            } else if (timeDiff < 0 || timeDiff > FLOW_POLL_INTERVAL * THRESHOLD) {
                currPacketCountVar = currPacketCount / (double) FLOW_POLL_INTERVAL;
                currByteCountVar = currByteCount / (double) FLOW_POLL_INTERVAL;
                currTotalFlowVar = currTotalFlow / (double) FLOW_POLL_INTERVAL;
                if (currTotalPairFlow != 0) {
                    currTotalPairFlowVar = currTotalPairFlow / (double) FLOW_POLL_INTERVAL;
                }
                if (currTotalSingleFlow != 0) {
                    currTotalSingleFlowVar = currTotalSingleFlow / (double) FLOW_POLL_INTERVAL;
                }
                if (currPairFlowRatio != 0) {
                    currPairFlowRatioVar = currPairFlowRatio / (double) FLOW_POLL_INTERVAL;
                }
                // currByteCountVar - prevBytCountVar / TD
                currBytePerPacketVar = currBytePerPacket / (double) FLOW_POLL_INTERVAL;
                currTotalDuration /= (double) Math.pow(10, 9);
                currPacketPerDuration = currPacketCount / (double) currTotalDuration;
                currPacketPerDurationVar = currPacketPerDuration / (double) FLOW_POLL_INTERVAL;
                currBytePerDuration = currByteCount / (double) currTotalDuration;
                currBytePerDurationVar = currBytePerDuration / (double) FLOW_POLL_INTERVAL;
            }

            // update or add new entry to LFT
            ufs.setPacketCountVar(currPacketCountVar);
            ufs.setByteCountVar(currByteCountVar);
            ufs.setTotalFlowsVar(currTotalFlowVar);
            ufs.setTotalPairFlowVar(currTotalPairFlowVar);
            ufs.setTotalSingleFlowVar(currTotalSingleFlowVar);
            ufs.setPairFlowRatioVar(currPairFlowRatioVar);
            ufs.setBytePerPacket(currBytePerPacket);
            ufs.setBytePerPacketVar(currBytePerPacketVar);
            ufs.setBytePerDuration(currBytePerDuration);
            ufs.setBytePerDurationVar(currBytePerDurationVar);
            ufs.setPacketPerDuration(currPacketPerDuration);
            ufs.setPacketPerDurationVar(currPacketPerDurationVar);
            fStatisticsLF.remove(hashIndex);
            fStatisticsLF.put(hashIndex, ufs);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return ufs;
    }

    public synchronized UnitPortStatistics calculatePortStatsRichFeatures(BigInteger hashIndex,
                                                                          UnitPortStatistics ups) {

        try {
            double timeDiff;
            long currTimeStamp = ups.getTimestamp().getTime();
            // get feature
            // look up LFT using hash index
            UnitPortStatistics prevUps = pStatisticsLF.get(hashIndex);
            // if previous entry is empty, regard it as a new entry
            if (prevUps == null) {
                timeDiff = -1;
            } else {
                long prevTimeStamp = prevUps.getTimestamp().getTime();
                timeDiff = currTimeStamp - prevTimeStamp;
                timeDiff /= Math.pow(10, 3);
            }

            if (timeDiff == 0) {
                return ups;
            }

            // extract current features
            long currRxPackets = ups.getRxPackets();
            long currTxPackets = ups.getTxPackets();
            long currRxBytes = ups.getRxBytes();
            long currTxBytes = ups.getTxBytes();
            long currRxDropped = ups.getRxDropped();
            long currTxDropped = ups.getTxDropped();
            long currRxErrors = ups.getRxErrors();
            long currTxErrors = ups.getTxErrors();
            long currRxFrameErr = ups.getRxFrameErr();
            long currRxOverErr = ups.getRxOverErr();
            long currRxCrcErr = ups.getRxCrcErr();
            long currCollisions = ups.getCollisions();

            // current rich feature variables
            double currRxPacketsVar = 0;
            double currTxPacketsVar = 0;
            double currRxBytesVar = 0;
            double currTxBytesVar = 0;
            double currRxDroppedVar = 0;
            double currTxDroppedVar = 0;
            double currRxErrorsVar = 0;
            double currTxErrorsVar = 0;
            double currRxFrameErrVar = 0;
            double currRxOverErrVar = 0;
            double currRxCrcErrVar = 0;
            double currCollisionsVar = 0;
            double currRxBytePerPacket = 0;
            double currRxBytePerPacketVar = 0;
            double currTxBytePerPacket = 0;
            double currTxBytePerPacketVar = 0;
            double currRxDroppedPerPacket = 0;
            double currRxDroppedPerPacketVar = 0;
            double currTxDroppedPerPacket = 0;
            double currTxDroppedPerPacketVar = 0;
            double currRxErrorPerPacket = 0;
            double currRxErrorPerPacketVar = 0;
            double currTxErrorPerPacket = 0;
            double currTxErrorPerPacketVar = 0;
            double currRxFrameErrPerPacket = 0;
            double currRxFrameErrPerPacketVar = 0;
            double currRxOverErrPerPacket = 0;
            double currRxOverErrPerPacketVar = 0;
            double currRxCrcErrPerPacket = 0;
            double currRxCrcErrPerPacketVar = 0;

            // calculate single rich features
            if (currRxPackets != 0) {
                currRxBytePerPacket = currRxBytes / (double) currRxPackets;
                currRxDroppedPerPacket = currRxDropped / (double) currRxPackets;
                currRxErrorPerPacket = currRxErrors / (double) currRxPackets;
                currRxFrameErrPerPacket = currRxFrameErr / (double) currRxPackets;
                currRxOverErrPerPacket = currRxOverErr / (double) currRxPackets;
                currRxCrcErrPerPacket = currRxCrcErr / (double) currRxPackets;
            }
            if (currTxPackets != 0) {
                currTxBytePerPacket = currTxBytes / (double) currTxPackets;
                currTxDroppedPerPacket = currTxDropped / (double) currTxPackets;
                currTxErrorPerPacket = currTxErrors / (double) currTxPackets;
            }

            // 2. Calculate variation
            if (timeDiff > 0 && timeDiff < FLOW_POLL_INTERVAL * THRESHOLD) {
                // extract previous features
                long prevRxPackets = prevUps.getRxPackets();
                long prevTxPackets = prevUps.getTxPackets();
                long prevRxBytes = prevUps.getRxBytes();
                long prevTxBytes = prevUps.getTxBytes();
                long prevRxDropped = prevUps.getRxDropped();
                long prevTxDropped = prevUps.getTxDropped();
                long prevRxErrors = prevUps.getRxErrors();
                long prevTxErrors = prevUps.getTxErrors();
                long prevRxFrameErr = prevUps.getRxFrameErr();
                long prevRxOverErr = prevUps.getRxOverErr();
                long prevRxCrcErr = prevUps.getRxCrcErr();
                long prevCollisions = prevUps.getCollisions();
                double prevRxBytePerPacket = prevUps.getRxBytePerPacket();
                double prevTxBytePerPacket = prevUps.getTxBytePerPacket();
                double prevRxDroppedPerPacket = prevUps.getRxDroppedPerPacket();
                double prevTxDroppedPerPacket = prevUps.getTxDroppedPerPacket();
                double prevRxErrorPerPacket = prevUps.getRxErrorPerPacket();
                double prevTxErrorPerPacket = prevUps.getTxErrorPerPacket();
                double prevRxFrameErrPerPacket = prevUps.getRxFrameErrPerPacket();
                double prevRxOverErrPerPacket = prevUps.getRxOverErrPerPacket();
                double prevRxCrcErrPerPacket = prevUps.getRxCrcErrPerPacket();

                // calculate variation rich features
//                timeDiff /= Math.pow(10, 3);
                currRxPacketsVar = (currRxPackets - prevRxPackets) / timeDiff;
                currTxPacketsVar = (currTxPackets - prevTxPackets) / timeDiff;
                currRxBytesVar = (currRxBytes - prevRxBytes) / timeDiff;
                currTxBytesVar = (currTxBytes - prevTxBytes) / timeDiff;
                currRxDroppedVar = (currRxDropped - prevRxDropped) / timeDiff;
                currTxDroppedVar = (currTxDropped - prevTxDropped) / timeDiff;
                currRxErrorsVar = (currRxErrors - prevRxErrors) / timeDiff;
                currTxErrorsVar = (currTxErrors - prevTxErrors) / timeDiff;
                currRxFrameErrVar = (currRxFrameErr - prevRxFrameErr) / timeDiff;
                currRxOverErrVar = (currRxOverErr - prevRxOverErr) / timeDiff;
                currRxCrcErrVar = (currRxCrcErr - prevRxCrcErr) / timeDiff;
                currCollisionsVar = (currCollisions - prevCollisions) / timeDiff;
                currRxBytePerPacketVar = (currRxBytePerPacket - prevRxBytePerPacket) / timeDiff;
                currTxBytePerPacketVar = (currTxBytePerPacket - prevTxBytePerPacket) / timeDiff;
                currRxDroppedPerPacketVar = (currRxDroppedPerPacket - prevRxDroppedPerPacket) / timeDiff;
                currTxDroppedPerPacketVar = (currTxDroppedPerPacket - prevTxDroppedPerPacket) / timeDiff;
                currRxErrorPerPacketVar = (currRxErrorPerPacket - prevRxErrorPerPacket) / timeDiff;
                currTxErrorPerPacketVar = (currTxErrorPerPacket - prevTxErrorPerPacket) / timeDiff;
                currRxFrameErrPerPacketVar = (currRxFrameErrPerPacket - prevRxFrameErrPerPacket) / timeDiff;
                currRxOverErrPerPacketVar = (currRxOverErrPerPacket - prevRxOverErrPerPacket) / timeDiff;
                currRxCrcErrPerPacketVar = (currRxCrcErrPerPacket - prevRxCrcErrPerPacket) / timeDiff;
                // 1. new or 3. Expire entries (EC)
            } else if (timeDiff < 0 || timeDiff > FLOW_POLL_INTERVAL * THRESHOLD) {
                // calculate variation rich features
                currRxPacketsVar = currRxPackets / (double) PORT_POLL_INTERVAL;
                currTxPacketsVar = currTxPackets / (double) PORT_POLL_INTERVAL;
                currRxBytesVar = currRxBytes / (double) PORT_POLL_INTERVAL;
                currTxBytesVar = currTxBytes / (double) PORT_POLL_INTERVAL;
                currRxDroppedVar = currRxDropped / (double) PORT_POLL_INTERVAL;
                currTxDroppedVar = currTxDropped / (double) PORT_POLL_INTERVAL;
                currRxErrorsVar = currRxErrors / (double) PORT_POLL_INTERVAL;
                currTxErrorsVar = currTxErrors / (double) PORT_POLL_INTERVAL;
                currRxFrameErrVar = currRxFrameErr / (double) PORT_POLL_INTERVAL;
                currRxOverErrVar = currRxOverErr / (double) PORT_POLL_INTERVAL;
                currRxCrcErrVar = currRxCrcErr / (double) PORT_POLL_INTERVAL;
                currCollisionsVar = currCollisions / (double) PORT_POLL_INTERVAL;
                currRxBytePerPacketVar = currRxBytePerPacket / (double) PORT_POLL_INTERVAL;
                currTxBytePerPacketVar = currTxBytePerPacket / (double) PORT_POLL_INTERVAL;
                currRxDroppedPerPacketVar = currRxDroppedPerPacket / (double) PORT_POLL_INTERVAL;
                currTxDroppedPerPacketVar = currTxDroppedPerPacket / (double) PORT_POLL_INTERVAL;
                currRxErrorPerPacketVar = currRxErrorPerPacket / (double) PORT_POLL_INTERVAL;
                currTxErrorPerPacketVar = currTxErrorPerPacket / (double) PORT_POLL_INTERVAL;
                currRxFrameErrPerPacketVar = currRxFrameErrPerPacket / (double) PORT_POLL_INTERVAL;
                currRxOverErrPerPacketVar = currRxOverErrPerPacket / (double) PORT_POLL_INTERVAL;
                currRxCrcErrPerPacketVar = currRxCrcErrPerPacket / (double) PORT_POLL_INTERVAL;
            }
            // update or add new entry to LFT
            ups.setRxPacketsVar(currRxPacketsVar);
            ups.setTxPacketsVar(currTxPacketsVar);
            ups.setRxBytesVar(currRxBytesVar);
            ups.setTxBytesVar(currTxBytesVar);
            ups.setRxDroppedVar(currRxDroppedVar);
            ups.setTxDroppedVar(currTxDroppedVar);
            ups.setRxErrorsVar(currRxErrorsVar);
            ups.setTxErrorsVar(currTxErrorsVar);
            ups.setRxFrameErrVar(currRxFrameErrVar);
            ups.setRxOverErrVar(currRxOverErrVar);
            ups.setRxCrcErrVar(currRxCrcErrVar);
            ups.setCollisionsVar(currCollisionsVar);
            ups.setRxBytePerPacket(currRxBytePerPacket);
            ups.setRxBytePerPacketVar(currRxBytePerPacketVar);
            ups.setTxBytePerPacket(currTxBytePerPacket);
            ups.setTxBytePerPacketVar(currTxBytePerPacketVar);
            ups.setRxDroppedPerPacket(currRxDroppedPerPacket);
            ups.setRxDroppedPerPacketVar(currRxDroppedPerPacketVar);
            ups.setTxDroppedPerPacket(currTxDroppedPerPacket);
            ups.setTxDroppedPerPacketVar(currTxDroppedPerPacketVar);
            ups.setRxErrorPerPacket(currRxErrorPerPacket);
            ups.setRxErrorPerPacketVar(currRxErrorPerPacketVar);
            ups.setTxErrorPerPacket(currTxErrorPerPacket);
            ups.setTxErrorPerPacketVar(currTxErrorPerPacketVar);
            ups.setRxFrameErrPerPacket(currRxFrameErrPerPacket);
            ups.setRxFrameErrPerPacketVar(currRxFrameErrPerPacketVar);
            ups.setRxOverErrPerPacket(currRxOverErrPerPacket);
            ups.setRxOverErrPerPacket(currRxOverErrPerPacketVar);
            ups.setRxCrcErrPerPacket(currRxCrcErrPerPacket);
            ups.setRxCrcErrPerPacketVar(currRxCrcErrPerPacketVar);
            pStatisticsLF.remove(hashIndex);
            pStatisticsLF.put(hashIndex, ups);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return ups;
    }

    public UnitTableStatistics calculateTableStatsRichFeatures(BigInteger hashIndex, UnitTableStatistics uts) {

        try {
            // extract current features
            long currMaxEntries = uts.getMaxEntries();
            long currActiveCount = uts.getActiveCount();
            long currLookupCount = uts.getLookupCount();
            long currMatchedCount = uts.getMatchedCount();

            // calculate rich features

            double matchedPerLookup = 0;
            double activePerMax = 0;
            double lookupPerActive = 0;
            double matchedPerActive = 0;

            if (currLookupCount != 0) {
                matchedPerLookup = currMatchedCount / (double) currLookupCount;
            }

            if (currMaxEntries != 0) {
                activePerMax = currActiveCount / (double) currMaxEntries;
            }

            if (currActiveCount != 0) {
                lookupPerActive = currLookupCount / (double) currActiveCount;
                matchedPerActive = currMatchedCount / (double) currActiveCount;
            }

            uts.setMatchedPerLookup(matchedPerLookup);
            uts.setActivePerMax(activePerMax);
            uts.setLookupPerActive(lookupPerActive);
            uts.setMatchedPerActive(matchedPerActive);
            // tStatisticsLFT.remove(hashIndex);
            // tStatisticsLFT.put(hashIndex, uts);

        } catch (Exception e) {
            log.error(e.toString());
        }
        return uts;
    }

    public UnitFlowRemovedInformation calculateFlowRemovedRichFeatures(BigInteger hashIndex,
                                                                       UnitFlowRemovedInformation ufri) {

        try {
            // extract current features
            long currPacketCount = ufri.getPacketCount();
            long currByteCount = ufri.getByteCount();
            double currTotalDuration = ((ufri.getDurationSec() * (long) Math.pow(10, 9)) + ufri.getDurationNsec());

            // calculate rich features
            double packetPerDuration = 0;
            double bytePerDuration = 0;

            if (currTotalDuration != 0) {
                currTotalDuration /= (double) Math.pow(10, 9);
                packetPerDuration = currPacketCount / (double) currTotalDuration;
                bytePerDuration = currByteCount / (double) currTotalDuration;
            }

            ufri.setPacketPerDuration(packetPerDuration);
            ufri.setBytePerDuration(bytePerDuration);
//        frStatisticsLFT.remove(hashIndex);
//        frStatisticsLFT.put(hashIndex, ufri);
        } catch (Exception e) {
            log.error(e.toString());
        }

        return ufri;
    }

    //Expiration Collector for LFT - By Jinwoo
    public class ExpirationCollector extends TimerTask {

        @Override
        public synchronized void run() {
            long lastTimeStamp;
            long currTimeStamp = System.currentTimeMillis();
            double diffTimeStamp;
            try {
//                log.error("call ExpirationCollector!");
                // for flow statistics LFT
                for (Entry<BigInteger, UnitFlowStatistics> obj : fStatisticsLF.entrySet()) {
                    lastTimeStamp = obj.getValue().getTimestamp().getTime();
                    diffTimeStamp = (currTimeStamp - lastTimeStamp) / Math.pow(10, 3);
//                    log.error("[FLOWLFT] diffTimeStamp: " + diffTimeStamp);
                    if (diffTimeStamp > TIME_THRESHOLD_SECONDS) {
//                        log.error("diffTimeStamp: " + diffTimeStamp + ", remove hashIndex: " + obj.getKey());
                        fStatisticsLF.remove(obj.getKey());
                    }
                }

                // for port statistics LFT
                for (Entry<BigInteger, UnitPortStatistics> obj : pStatisticsLF.entrySet()) {
                    lastTimeStamp = obj.getValue().getTimestamp().getTime();
                    diffTimeStamp = (currTimeStamp - lastTimeStamp) / Math.pow(10, 3);
//                    log.error("[PORTLFT] diffTimeStamp: " + diffTimeStamp);
                    if (diffTimeStamp > TIME_THRESHOLD_SECONDS) {
//                        log.error("diffTimeStamp: " + diffTimeStamp + ", remove hashIndex: " + obj.getKey());
                        pStatisticsLF.remove(obj.getKey());
                    }
                }
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
    }
}

//    public void printFlowStatsLFT(ConcurrentHashMap<BigInteger, UnitFlowStatistics> targetLFT) {
//        for (Entry<BigInteger, UnitFlowStatistics> obj : targetLFT.entrySet()) {
//            System.out.println("[hashIndex]: " + obj.getKey() + " [value]: "
//                    + obj.getValue().toString());
//        }
//        System.out.println("==================================");
//    }
//     to print out LFT for test
//    public void printPortStatsLFT(ConcurrentHashMap<String, UnitPortStatistics> targetLFT) {
//        for (Entry<String, UnitPortStatistics> obj : targetLFT.entrySet()) {
//            System.out.println("[hashIndex]: " + obj.getKey() + " [value]: "
//                    + obj.getValue().toString());
//        }
//        System.out.println("==================================");
//    }

//    public void printTableStatsLFT(ConcurrentHashMap<String, UnitTableStatistics> targetLFT) {
//        for (Entry<String, UnitTableStatistics> obj : targetLFT.entrySet()) {
//            System.out.println("[hashIndex]: " + obj.getKey() + " [value]: "
//                    + obj.getValue().toString());
//        }
//        System.out.println("==================================");
//    }
//
//    public void printFlowRemovedLFT(ConcurrentHashMap<String, UnitFlowRemovedInformation> targetLFT) {
//        for (Entry<String, UnitFlowRemovedInformation> obj : targetLFT.entrySet()) {
//            System.out.println("[hashIndex]: " + obj.getKey() + " [value]: "
//                    + obj.getValue().toString());
//        }
//        System.out.println("==================================");
//    }
