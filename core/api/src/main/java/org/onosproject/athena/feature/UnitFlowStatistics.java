package org.onosproject.athena.feature;

import org.onosproject.core.ApplicationId;

import java.util.Date;

/**
 * Created by seunghyeon on 8/18/15.
 */
public class UnitFlowStatistics implements UnitFeature {

    private final long durationSec;
    private final long durationNsec;
    private final int priority;
    private final int idleTimeout;
    private final int hardTimeout;
    private final long packetCount;
    private final long byteCount;
    private Date date;
    //add the rich features - By Jinwoo
    private double packetCountVar;
    private double byteCountVar;
    private double bytePerPacket;
    private double bytePerPacketVar;
    private double packetPerDuration;
    private double packetPerDurationVar;
    private double bytePerDuration;
    private double bytePerDurationVar;
    //poll interval will be added

    private double totalFlows;
    private double totalFlowsVar;
    private boolean pairFlow;
    private double totalPairFlow;
    private double totalPairFlowVar;
    private double totalSingleFlow;
    private double totalSingleFlowVar;
    private double pairFlowRatio;
    private double pairFlowRatioVar;

    public double getTotalFlows() {
        return totalFlows;
    }

    public void setTotalFlows(double totalFlows) {
        this.totalFlows = totalFlows;
    }

    public double getTotalFlowsVar() {
        return totalFlowsVar;
    }

    public void setTotalFlowsVar(double totalFlowsVar) {
        this.totalFlowsVar = totalFlowsVar;
    }

    public boolean getPairFlow() {
        return pairFlow;
    }

    public void setPairFlow(boolean pairFlow) {
        this.pairFlow = pairFlow;
    }

    public double getTotalPairFlow() {
        return totalPairFlow;
    }

    public void setTotalPairFlow(double totalPairFlow) {
        this.totalPairFlow = totalPairFlow;
    }

    public double getTotalPairFlowVar() {
        return totalPairFlowVar;
    }

    public void setTotalPairFlowVar(double totalPairFlowVar) {
        this.totalPairFlowVar = totalPairFlowVar;
    }

    public double getTotalSingleFlow() {
        return totalSingleFlow;
    }

    public void setTotalSingleFlow(double totalSingleFlow) {
        this.totalSingleFlow = totalSingleFlow;
    }

    public double getTotalSingleFlowVar() {
        return totalSingleFlowVar;
    }

    public void setTotalSingleFlowVar(double totalSingleFlowVar) {
        this.totalSingleFlowVar = totalSingleFlowVar;
    }

    public double getPairFlowRatio() {
        return pairFlowRatio;
    }

    public void setPairFlowRatio(double pairFlowRatio) {
        this.pairFlowRatio = pairFlowRatio;
    }

    public double getPairFlowRatioVar() {
        return pairFlowRatioVar;
    }

    public void setPairFlowRatioVar(double pairFlowRatioVar) {
        this.pairFlowRatioVar = pairFlowRatioVar;
    }

    //Add Application name
    private ApplicationId applicationId;

    public void setApplicationId(ApplicationId applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationId getApplicationId() {

        return applicationId;
    }

    public boolean getActionDrop() {
        return actionDrop;
    }

    public void setActionDrop(boolean actionDrop) {
        this.actionDrop = actionDrop;
    }

    public boolean getActionOutput() {
        return actionOutput;
    }

    //Add Action
    private boolean actionDrop;
    private boolean actionOutput;
    private long actionOutputPort;

    public boolean getactionOutput() {
        return actionOutput;
    }

    public void setActionOutput(boolean actionOutput) {
        this.actionOutput = actionOutput;
    }

    public long getActionOutputPort() {
        return actionOutputPort;
    }

    public void setActionOutputPort(long actionOutputPort) {
        this.actionOutputPort = actionOutputPort;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitFlowStatistics(long durationSec, long durationNsec, int priority, int idleTimeout,
                              int hardTimeout, long packetCount, long byteCount, Date date) {
        this.durationSec = durationSec;
        this.durationNsec = durationNsec;
        this.priority = priority;
        this.idleTimeout = idleTimeout;
        this.hardTimeout = hardTimeout;
        this.packetCount = packetCount;
        this.byteCount = byteCount;
        this.date = date;
    }

    public UnitFlowStatistics(long durationSec, long durationNsec, int priority,
                              int idleTimeout, int hardTimeout, long packetCount, long byteCount) {
        this.durationSec = durationSec;
        this.durationNsec = durationNsec;
        this.priority = priority;
        this.idleTimeout = idleTimeout;
        this.hardTimeout = hardTimeout;
        this.packetCount = packetCount;
        this.byteCount = byteCount;
    }

    public long getDurationSec() {
        return this.durationSec;
    }

    public long getDurationNsec() {
        return this.durationNsec;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getIdleTimeout() {
        return this.idleTimeout;
    }

    public int getHardTimeout() {
        return this.hardTimeout;
    }

    public long getPacketCount() {
        return this.packetCount;
    }

    public long getByteCount() {
        return this.byteCount;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }

    public double getPacketCountVar() {
        return packetCountVar;
    }

    public void setPacketCountVar(double packetCountVar) {
        this.packetCountVar = packetCountVar;
    }

    public double getByteCountVar() {
        return byteCountVar;
    }

    public void setByteCountVar(double byteCountVar) {
        this.byteCountVar = byteCountVar;
    }

    public double getBytePerPacket() {
        return bytePerPacket;
    }

    public double getPacketPerDuration() {
        return packetPerDuration;
    }

    public double getBytePerDuration() {
        return bytePerDuration;
    }

    public void setBytePerPacket(double bytePerPacket) {
        this.bytePerPacket = bytePerPacket;
    }

    public void setPacketPerDuration(double packetPerDuration) {
        this.packetPerDuration = packetPerDuration;
    }

    public void setBytePerDuration(double bytePerDuration) {
        this.bytePerDuration = bytePerDuration;
    }

    public double getBytePerPacketVar() {
        return bytePerPacketVar;
    }

    public void setBytePerPacketVar(double bytePerPacketVar) {
        this.bytePerPacketVar = bytePerPacketVar;
    }

    public double getPacketPerDurationVar() {
        return packetPerDurationVar;
    }

    public void setPacketPerDurationVar(double packetPerDurationVar) {
        this.packetPerDurationVar = packetPerDurationVar;
    }

    public double getBytePerDurationVar() {
        return bytePerDurationVar;
    }

    public void setBytePerDurationVar(double bytePerDurationVar) {
        this.bytePerDurationVar = bytePerDurationVar;
    }

    @Override
    public String toString() {
        return "UnitFlowStatistics [durationSec=" + durationSec + ", durationNsec=" + durationNsec + ", priority="
                + priority + ", idleTimeout=" + idleTimeout + ", hardTimeout=" + hardTimeout + ", packetCount="
                + packetCount + ", byteCount=" + byteCount + ", date=" + date + ", packetCountVar="
                + packetCountVar + ", byteCountVar=" + byteCountVar + ", bytePerPacket="
                + bytePerPacket + ", bytePerPacketVar=" + bytePerPacketVar + ", packetPerDuration="
                + packetPerDuration + ", packetPerDurationVar=" + packetPerDurationVar
                + ", bytePerDuration=" + bytePerDuration + ", bytePerDurationVar=" + bytePerDurationVar
                + "]";
    }
}