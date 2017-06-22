package org.onosproject.athena.feature;

import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitPacketInInformation implements UnitFeature {
    private final int totalLen;
    //save as a ordinal (OFPacketInReason).
    private final int reason;
    private int inPort;
    private int inPhyPort;
    //Used for saving Eth / Ip header. (To be separated...)
    private FeatureIndex payloadMatch;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitPacketInInformation(int totalLen, int reason, int inPort, int inPhyPort, FeatureIndex payloadMatch) {
        this.totalLen = totalLen;
        this.reason = reason;
        this.inPort = inPort;
        this.inPhyPort = inPhyPort;
        this.payloadMatch = payloadMatch;
    }

    public UnitPacketInInformation(int totalLen, int reason, FeatureIndex payloadMatch) {
        this.totalLen = totalLen;
        this.reason = reason;
        this.payloadMatch = payloadMatch;
    }
    // for evaluation #2 PacketIn response per seconds
    public UnitPacketInInformation() {
        this.totalLen = 0;
        this.reason = 0;
        this.inPort = 0;
        this.inPhyPort = 0;
        this.payloadMatch = new FeatureIndex();
    }

    public int getTotalLen() {
        return totalLen;
    }

    public int getReason() {
        return reason;
    }

    public int getInPort() {
        return inPort;
    }

    public int getInPhyPort() {
        return inPhyPort;
    }

    public FeatureIndex getPayloadMatch() {
        return payloadMatch;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }
}
