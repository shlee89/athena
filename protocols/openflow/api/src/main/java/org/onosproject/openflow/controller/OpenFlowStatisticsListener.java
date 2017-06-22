package org.onosproject.openflow.controller;

import org.projectfloodlight.openflow.protocol.OFAggregateStatsReply;
import org.projectfloodlight.openflow.protocol.OFFlowStatsReply;
import org.projectfloodlight.openflow.protocol.OFPortStatsReply;
import org.projectfloodlight.openflow.protocol.OFQueueStatsReply;
import org.projectfloodlight.openflow.protocol.OFTableStatsReply;

/**
 * To receive statistics information from data_plane.
 * Applications implement this interface.
 * The prototype version does not provide any parameter (e.g., DPID, Table ID, ...)
 * Supported type: Aggregate, Flow, Port, Queue, Table
 * Created by seunghyeon on 8/17/15.
 */
public interface OpenFlowStatisticsListener {

    /**
     * Notify that the aggregate statistics event is arrived.
     * @param dpid the switch where the event occured.
     * @param reply the aggregate statistics.
     */
    void aggregateStatsProcess(Dpid dpid, OFAggregateStatsReply reply);

    /**
     * Notify that the flow statistics event is arrived.
     * @param dpid the switch where the event occured.
     * @param reply the flow statistics.
     */
    void flowStatsProcess(Dpid dpid, OFFlowStatsReply reply);

    /**
     * Notify that the port statistics event is arrived.
     * @param dpid the switch where the event occured.
     * @param reply the port statistics.
     */
    void portStatsProcess(Dpid dpid, OFPortStatsReply reply);

    /**
     * Notify that the queue statistics event is arrived.
     * @param dpid the switch where the event occured.
     * @param reply the queue statistics.
     */
    void queueStatsProcess(Dpid dpid, OFQueueStatsReply reply);

    /**
     * Notify that the table statistics event is arrived.
     * @param dpid the switch where the event occured.
     * @param reply the table statistics.
     */
    void tableStatsProcess(Dpid dpid, OFTableStatsReply reply);



}
