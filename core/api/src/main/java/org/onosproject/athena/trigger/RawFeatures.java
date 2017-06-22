package org.onosproject.athena.trigger;

/**
 * Created by seunghyeon on 11/10/15.
 */
public class RawFeatures {
    //Packet_In (Simple init, then cumulative sum)
    private long pInumOfPacketIn = 0;

    //Flow_Statistics (Simple init, then update latest value)
    private long fSbyteCount = 0;
    private long fSpacketcount = 0;
    private long fSdurationSec = 0;

    //Port_Statistics (Remember previous value, then reverse and cumulative sum)
    private long pSrxPackets = 0;
    private long pStxPackets = 0;
    private long pSrxBytes = 0;
    private long pStxBytes = 0;
    private long pSrxDropped = 0;
    private long pStxDropped = 0;

    //port_status (Simple init, then cumulative sum)
    private long fSnumOfFlowRemoved = 0;

    //switch features (Simple init, then update latest value) "Extracted from aggreagte..."
    private long sFtotalNumOfFlow = 0;

}
