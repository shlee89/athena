package org.onosproject.athena.feature;


import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitAggregateStatistics implements UnitFeature {
    private final long packetCount;
    private final long byteCount;
    private final long flowCount;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitAggregateStatistics(long packetCount, long byteCount, long flowCount) {
        this.packetCount = packetCount;
        this.byteCount = byteCount;
        this.flowCount = flowCount;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public long getByteCount() {
        return byteCount;
    }

    public long getFlowCount() {
        return flowCount;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }
}
