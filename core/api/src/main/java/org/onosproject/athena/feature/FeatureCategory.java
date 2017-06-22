package org.onosproject.athena.feature;


/**
 * Created by seunghyeon on 8/18/15.
 */
public enum FeatureCategory {

    /**
     * Indicates that a synchronous type category for flow statistics.
     */
    SYNCHRONOUS_FLOW_STATISTICS,

    /**
     * Indicates that a synchronous type category for aggregate statistics.
     */
    SYNCHRONOUS_AGGREGATE_STATISTICS,

    /**
     * Indicates that a synchronous type category for table statistics.
     */
    SYNCHRONOUS_TABLE_STATISTICS,

    /**
     * Indicates that a synchronous type category for port statistics.
     */
    SYNCHRONOUS_PORT_STATISTICS,

    /**
     * Indicates that a synchronous type category for queue statistics.
     */
    SYNCHRONOUS_QUEUE_STATISTICS,

    /**
     * Indicates that a asynchronous type category for port_status.
     */
    ASYNCHRONOUS_PORT_STATUS,

    /**
     * Indicates that a asynchronous type category for packet_in.
     */
    ASYNCHRONOUS_PACKET_IN,

    /**
     * Indicates that a asynchronous type category for flow_removed.
     */
    ASYNCHRONOUS_FLOW_REMOVED,

    /**
     * Indicates that a asynchronous type category for error_msg.
     */
    ASYNCHRONOUS_ERROR_MSG,

    /**
     * Indicates that a management type category for TCP success and fail.
     */
    ASYNCHRONOUS_TCP_SUCCESS_FAIL
}