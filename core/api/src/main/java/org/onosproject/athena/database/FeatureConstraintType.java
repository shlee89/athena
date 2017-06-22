package org.onosproject.athena.database;


/**
 * Created by seunghyeon on 9/30/15.
 */
public enum FeatureConstraintType {
    /**
     * Feature fields.
     */
    FEATURE,
    /**
     * Index fields.
     */
    INDEX,
    /**
     * PacketIn Payload fields.
     */
    PACKET_IN_PAYLOAD_MATCH,
}
