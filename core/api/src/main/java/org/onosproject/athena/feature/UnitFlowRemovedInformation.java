package org.onosproject.athena.feature;



import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitFlowRemovedInformation implements UnitFeature {
    private final short reason;
    private final long durationSec;
    private final long durationNsec;
    private final int idleTimeout;
    private final int hardTimeout;
    private final long packetCount;
    private final long byteCount;
    private Date date;
    //add the rich features - By Jinwoo
    private double packetPerDuration;
    private double bytePerDuration;

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitFlowRemovedInformation(short reason, long durationSec,
                                      long durationNsec, int idleTimeout, int hardTimeout,
                                      long packetCount, long byteCount) {
        this.reason = reason;
        this.durationSec = durationSec;
        this.durationNsec = durationNsec;
        this.idleTimeout = idleTimeout;
        this.hardTimeout = hardTimeout;
        this.packetCount = packetCount;
        this.byteCount = byteCount;
    }

    public short getReason() {
        return reason;
    }

    public long getDurationSec() {
        return durationSec;
    }

    public long getDurationNsec() {
        return durationNsec;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public int getHardTimeout() {
        return hardTimeout;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public long getByteCount() {
        return byteCount;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }

    public double getPacketPerDuration() {
        return packetPerDuration;
    }

    public void setPacketPerDuration(double packetPerDuration) {
        this.packetPerDuration = packetPerDuration;
    }

    public double getBytePerDuration() {
        return bytePerDuration;
    }

    public void setBytePerDuration(double bytePerDuration) {
        this.bytePerDuration = bytePerDuration;
    }

    @Override
    public String toString() {
        return "UnitFlowRemovedInformation [reason=" + reason + ", durationSec=" + durationSec + ", durationNsec="
                + durationNsec + ", idleTimeout=" + idleTimeout + ", hardTimeout=" + hardTimeout + ", packetCount="
                + packetCount + ", byteCount=" + byteCount + ", packetPerDuration=" + packetPerDuration
                + ", bytePerDuration=" + bytePerDuration + "]";
    }
}
