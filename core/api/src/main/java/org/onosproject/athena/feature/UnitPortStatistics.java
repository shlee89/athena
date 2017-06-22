package org.onosproject.athena.feature;


import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitPortStatistics implements UnitFeature {

    private final long rxPackets;
    private final long txPackets;
    private final long rxBytes;
    private final long txBytes;
    private final long rxDropped;
    private final long txDropped;
    private final long rxErrors;
    private final long txErrors;
    private final long rxFrameErr;
    private final long rxOverErr;
    private final long rxCrcErr;
    private final long collisions;
    private Date date;
    //add the rich features - By Jinwoo
    private double rxPacketsVar;
    private double txPacketsVar;
    private double rxBytesVar;
    private double txBytesVar;
    private double rxDroppedVar;
    private double txDroppedVar;
    private double rxErrorsVar;
    private double txErrorsVar;
    private double rxFrameErrVar;
    private double rxOverErrVar;
    private double rxCrcErrVar;
    private double collisionsVar;
    private double rxBytePerPacket;
    private double rxBytePerPacketVar;
    private double txBytePerPacket;
    private double txBytePerPacketVar;
    private double rxDroppedPerPacket;
    private double rxDroppedPerPacketVar;
    private double txDroppedPerPacket;
    private double txDroppedPerPacketVar;
    private double rxErrorPerPacket;
    private double rxErrorPerPacketVar;
    private double txErrorPerPacket;
    private double txErrorPerPacketVar;
    private double rxFrameErrPerPacket;
    private double rxFrameErrPerPacketVar;
    private double rxOverErrPerPacket;
    private double rxOverErrPerPacketVar;
    private double rxCrcErrPerPacket;
    private double rxCrcErrPerPacketVar;
    //poll interval will be added

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitPortStatistics(long rxPackets, long txPackets, long rxBytes,
                              long txBytes, long rxDropped, long txDropped,
                              long rxErrors, long txErrors, long rxFrameErr,
                              long rxOverErr, long rxCrcErr, long collisions) {
        this.rxPackets = rxPackets;
        this.txPackets = txPackets;
        this.rxBytes = rxBytes;
        this.txBytes = txBytes;
        this.rxDropped = rxDropped;
        this.txDropped = txDropped;
        this.rxErrors = rxErrors;
        this.txErrors = txErrors;
        this.rxFrameErr = rxFrameErr;
        this.rxOverErr = rxOverErr;
        this.rxCrcErr = rxCrcErr;
        this.collisions = collisions;
    }

    public long getRxPackets() {
        return rxPackets;
    }

    public long getTxPackets() {
        return txPackets;
    }

    public long getRxBytes() {
        return rxBytes;
    }

    public long getTxBytes() {
        return txBytes;
    }

    public long getRxDropped() {
        return rxDropped;
    }

    public long getTxDropped() {
        return txDropped;
    }

    public long getRxErrors() {
        return rxErrors;
    }

    public long getTxErrors() {
        return txErrors;
    }

    public long getRxFrameErr() {
        return rxFrameErr;
    }

    public long getRxOverErr() {
        return rxOverErr;
    }

    public long getRxCrcErr() {
        return rxCrcErr;
    }

    public long getCollisions() {
        return collisions;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }

    public double getRxPacketsVar() {
        return rxPacketsVar;
    }

    public void setRxPacketsVar(double rxPacketsVar) {
        this.rxPacketsVar = rxPacketsVar;
    }

    public double getTxPacketsVar() {
        return txPacketsVar;
    }

    public void setTxPacketsVar(double txPacketsVar) {
        this.txPacketsVar = txPacketsVar;
    }

    public double getRxBytesVar() {
        return rxBytesVar;
    }

    public void setRxBytesVar(double rxBytesVar) {
        this.rxBytesVar = rxBytesVar;
    }

    public double getTxBytesVar() {
        return txBytesVar;
    }

    public void setTxBytesVar(double txBytesVar) {
        this.txBytesVar = txBytesVar;
    }

    public double getRxDroppedVar() {
        return rxDroppedVar;
    }

    public void setRxDroppedVar(double rxDroppedVar) {
        this.rxDroppedVar = rxDroppedVar;
    }

    public double getTxDroppedVar() {
        return txDroppedVar;
    }

    public void setTxDroppedVar(double txDroppedVar) {
        this.txDroppedVar = txDroppedVar;
    }

    public double getRxErrorsVar() {
        return rxErrorsVar;
    }

    public void setRxErrorsVar(double rxErrorsVar) {
        this.rxErrorsVar = rxErrorsVar;
    }

    public double getTxErrorsVar() {
        return txErrorsVar;
    }

    public void setTxErrorsVar(double txErrorsVar) {
        this.txErrorsVar = txErrorsVar;
    }

    public double getRxFrameErrVar() {
        return rxFrameErrVar;
    }

    public void setRxFrameErrVar(double rxFrameErrVar) {
        this.rxFrameErrVar = rxFrameErrVar;
    }

    public double getRxOverErrVar() {
        return rxOverErrVar;
    }

    public void setRxOverErrVar(double rxOverErrVar) {
        this.rxOverErrVar = rxOverErrVar;
    }

    public double getRxCrcErrVar() {
        return rxCrcErrVar;
    }

    public void setRxCrcErrVar(double rxCrcErrVar) {
        this.rxCrcErrVar = rxCrcErrVar;
    }

    public double getRxBytePerPacket() {
        return rxBytePerPacket;
    }

    public void setRxBytePerPacket(double rxBytePerPacket) {
        this.rxBytePerPacket = rxBytePerPacket;
    }

    public double getRxBytePerPacketVar() {
        return rxBytePerPacketVar;
    }

    public void setRxBytePerPacketVar(double rxBytePerPacketVar) {
        this.rxBytePerPacketVar = rxBytePerPacketVar;
    }

    public double getTxBytePerPacket() {
        return txBytePerPacket;
    }

    public void setTxBytePerPacket(double txBytePerPacket) {
        this.txBytePerPacket = txBytePerPacket;
    }

    public double getTxBytePerPacketVar() {
        return txBytePerPacketVar;
    }

    public void setTxBytePerPacketVar(double txBytePerPacketVar) {
        this.txBytePerPacketVar = txBytePerPacketVar;
    }

    public double getRxDroppedPerPacket() {
        return rxDroppedPerPacket;
    }

    public void setRxDroppedPerPacket(double rxDroppedPerPacket) {
        this.rxDroppedPerPacket = rxDroppedPerPacket;
    }

    public double getRxDroppedPerPacketVar() {
        return rxDroppedPerPacketVar;
    }

    public void setRxDroppedPerPacketVar(double rxDroppedPerPacketVar) {
        this.rxDroppedPerPacketVar = rxDroppedPerPacketVar;
    }

    public double getTxDroppedPerPacket() {
        return txDroppedPerPacket;
    }

    public void setTxDroppedPerPacket(double txDroppedPerPacket) {
        this.txDroppedPerPacket = txDroppedPerPacket;
    }

    public double getTxDroppedPerPacketVar() {
        return txDroppedPerPacketVar;
    }

    public void setTxDroppedPerPacketVar(double txDroppedPerPacketVar) {
        this.txDroppedPerPacketVar = txDroppedPerPacketVar;
    }

    public double getRxErrorPerPacket() {
        return rxErrorPerPacket;
    }

    public void setRxErrorPerPacket(double rxErrorPerPacket) {
        this.rxErrorPerPacket = rxErrorPerPacket;
    }

    public double getRxErrorPerPacketVar() {
        return rxErrorPerPacketVar;
    }

    public void setRxErrorPerPacketVar(double rxErrorPerPacketVar) {
        this.rxErrorPerPacketVar = rxErrorPerPacketVar;
    }

    public double getTxErrorPerPacket() {
        return txErrorPerPacket;
    }

    public void setTxErrorPerPacket(double txErrorPerPacket) {
        this.txErrorPerPacket = txErrorPerPacket;
    }

    public double getTxErrorPerPacketVar() {
        return txErrorPerPacketVar;
    }

    public void setTxErrorPerPacketVar(double txErrorPerPacketVar) {
        this.txErrorPerPacketVar = txErrorPerPacketVar;
    }

    public double getRxFrameErrPerPacket() {
        return rxFrameErrPerPacket;
    }

    public void setRxFrameErrPerPacket(double rxFrameErrPerPacket) {
        this.rxFrameErrPerPacket = rxFrameErrPerPacket;
    }

    public double getRxFrameErrPerPacketVar() {
        return rxFrameErrPerPacketVar;
    }

    public void setRxFrameErrPerPacketVar(double rxFrameErrPerPacketVar) {
        this.rxFrameErrPerPacketVar = rxFrameErrPerPacketVar;
    }

    public double getRxOverErrPerPacket() {
        return rxOverErrPerPacket;
    }

    public void setRxOverErrPerPacket(double rxOverErrPerPacket) {
        this.rxOverErrPerPacket = rxOverErrPerPacket;
    }

    public double getRxOverErrPerPacketVar() {
        return rxOverErrPerPacketVar;
    }

    public void setRxOverErrPerPacketVar(double rxOverErrPerPacketVar) {
        this.rxOverErrPerPacketVar = rxOverErrPerPacketVar;
    }

    public double getRxCrcErrPerPacket() {
        return rxCrcErrPerPacket;
    }

    public void setRxCrcErrPerPacket(double rxCrcErrPerPacket) {
        this.rxCrcErrPerPacket = rxCrcErrPerPacket;
    }

    public double getRxCrcErrPerPacketVar() {
        return rxCrcErrPerPacketVar;
    }

    public void setRxCrcErrPerPacketVar(double rxCrcErrPerPacketVar) {
        this.rxCrcErrPerPacketVar = rxCrcErrPerPacketVar;
    }

    public double getCollisionsVar() {
        return collisionsVar;
    }

    public void setCollisionsVar(double collisionsVar) {
        this.collisionsVar = collisionsVar;
    }

    @Override
    public String toString() {
        return "UnitPortStatistics [rxPackets=" + rxPackets + ", txPackets=" + txPackets + ", rxBytes=" + rxBytes
                + ", txBytes=" + txBytes + ", rxDropped=" + rxDropped + ", txDropped=" + txDropped + ", rxErrors="
                + rxErrors + ", txErrors=" + txErrors + ", rxFrameErr=" + rxFrameErr + ", rxOverErr=" + rxOverErr
                + ", rxCrcErr=" + rxCrcErr + ", collisions=" + collisions + ", rxPacketsVar=" + rxPacketsVar
                + ", txPacketsVar=" + txPacketsVar + ", rxBytesVar=" + rxBytesVar + ", txBytesVar=" + txBytesVar
                + ", rxDroppedVar=" + rxDroppedVar + ", txDroppedVar=" + txDroppedVar + ", rxErrorsVar=" + rxErrorsVar
                + ", txErrorsVar=" + txErrorsVar + ", rxFrameErrVar=" + rxFrameErrVar + ", rxOverErrVar=" + rxOverErrVar
                + ", rxCrcErrVar=" + rxCrcErrVar + ", collisionsVar=" + collisionsVar + ", rxBytePerPacket="
                + rxBytePerPacket + ", rxBytePerPacketVar=" + rxBytePerPacketVar + ", txBytePerPacket="
                + txBytePerPacket + ", txBytePerPacketVar=" + txBytePerPacketVar + ", rxDroppedPerPacket="
                + rxDroppedPerPacket + ", rxDroppedPerPacketVar=" + rxDroppedPerPacketVar + ", txDroppedPerPacket="
                + txDroppedPerPacket + ", txDroppedPerPacketVar=" + txDroppedPerPacketVar + ", rxErrorPerPacket="
                + rxErrorPerPacket + ", rxErrorPerPacketVar=" + rxErrorPerPacketVar + ", txErrorPerPacket="
                + txErrorPerPacket + ", txErrorPerPacketVar=" + txErrorPerPacketVar + ", rxFrameErrPerPacket="
                + rxFrameErrPerPacket + ", rxFrameErrPerPacketVar=" + rxFrameErrPerPacketVar + ", rxOverErrPerPacket="
                + rxOverErrPerPacket + ", rxOverErrPerPacketVar=" + rxOverErrPerPacketVar + ", rxCrcErrPerPacket="
                + rxCrcErrPerPacket + ", rxCrcErrPerPacketVar=" + rxCrcErrPerPacketVar + "]";
    }
}