package org.onosproject.openflow.controller;

import org.projectfloodlight.openflow.protocol.OFErrorMsg;
import org.projectfloodlight.openflow.protocol.OFFlowRemoved;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPortStatus;

/**
 * To receive Asynchronous events from data_plane.
 * Applications implement this interface.
 * Supported type : Packet_IN, Flow_Removed, Port_Status, Error_Msg
 * Created by seunghyeon on 8/17/15.
 */
public interface OpenFlowAsynchronousEventListener {

    void packetInProcess(Dpid dpid, OFPacketIn packetIn, OpenFlowPacketContext pktCtx);

    void flowRemovedProcess(Dpid dpid, OFFlowRemoved flowRemoved);

    void portStatusProcess(Dpid dpid, OFPortStatus portStatus);

    void errorMsgProcess(Dpid dpid, OFErrorMsg errorMsg);
}
