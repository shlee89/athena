package org.onosproject.athena.trigger;

import org.slf4j.Logger;

import java.util.zip.CRC32;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * If newly generated TriggerHelperEntry has different featureIndex, it could be new "Helper Entry".
 * Created by seunghyeon on 11/10/15.
 */
public class TriggerHelperEntry {

    private final Logger log = getLogger(getClass());

    private long switchDatapathId = 0;
    private int switchPortId = 0;
    private long matchEthSrc = 0;
    private long matchEthDst = 0;
    private int matchIpv4Src = 0;
    private int matchIpv4SrcMask = 0;
    private int matchIpv4Dst = 0;
    private int matchIpv4DstMask = 0;
    private int matchIpProto = 0;
    private int matchSrcPort = 0;
    private int matchDstPort = 0;

    private CRC32 checksum = null;
    private RawFeatures rawFeatures;

    public CRC32 getChecksum() {
        if (checksum == null) {
            log.warn("CRC value is null");
        }
        return checksum;
    }

    public boolean comparisionChecksum(CRC32 operand) {

        return checksum.getValue() == operand.getValue();
    }

    public void generateHash() {
        checksum = new CRC32();
        checksum.update(longToByteArray(switchDatapathId), 0, 8);
        checksum.update(intToByteArray(switchPortId), 0, 4);
        checksum.update(longToByteArray(matchEthSrc), 0, 8);
        checksum.update(longToByteArray(matchEthDst), 0, 8);
        checksum.update(intToByteArray(matchIpv4Src), 0, 4);
        checksum.update(intToByteArray(matchIpv4SrcMask), 0, 4);
        checksum.update(intToByteArray(matchIpv4Dst), 0, 4);
        checksum.update(intToByteArray(matchIpv4DstMask), 0, 4);
        checksum.update(intToByteArray(matchIpProto), 0, 4);
        checksum.update(intToByteArray(matchSrcPort), 0, 4);
        checksum.update(intToByteArray(matchDstPort), 0, 4);
    }


    public void setSwitchDatapathId(long switchDatapathId) {
        this.switchDatapathId = switchDatapathId;
    }

    public void setSwitchPortId(int switchPortId) {
        this.switchPortId = switchPortId;
    }

    public void setMatchEthSrc(long matchEthSrc) {
        this.matchEthSrc = matchEthSrc;
    }

    public void setMatchEthDst(long matchEthDst) {
        this.matchEthDst = matchEthDst;
    }

    public void setMatchIpv4Src(int matchIpv4Src) {
        this.matchIpv4Src = matchIpv4Src;
    }

    public void setMatchIpv4SrcMask(int matchIpv4SrcMask) {
        this.matchIpv4SrcMask = matchIpv4SrcMask;
    }

    public void setMatchIpv4Dst(int matchIpv4Dst) {
        this.matchIpv4Dst = matchIpv4Dst;
    }

    public void setMatchIpv4DstMask(int matchIpv4DstMask) {
        this.matchIpv4DstMask = matchIpv4DstMask;
    }

    public void setMatchIpProto(int matchIpProto) {
        this.matchIpProto = matchIpProto;
    }

    public void setMatchSrcPort(int matchSrcPort) {
        this.matchSrcPort = matchSrcPort;
    }

    public void setMatchDstPort(int matchDstPort) {
        this.matchDstPort = matchDstPort;
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

    public static final byte[] longToByteArray(long value) {
        return new byte[]{
                (byte) (value >> 56),
                (byte) (value >> 48),
                (byte) (value >> 40),
                (byte) (value >> 32),
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

}
