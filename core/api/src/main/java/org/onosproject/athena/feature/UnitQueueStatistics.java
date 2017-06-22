package org.onosproject.athena.feature;


import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitQueueStatistics implements UnitFeature {
    private final long txBytes;
    private final long txPackets;
    private final long txErrors;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitQueueStatistics(long txBytes, long txPackets, long txErrors) {
        this.txBytes = txBytes;
        this.txPackets = txPackets;
        this.txErrors = txErrors;
    }

    public long getTxBytes() {
        return txBytes;
    }

    public long getTxPackets() {
        return txPackets;
    }

    public long getTxErrors() {
        return txErrors;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }
}
