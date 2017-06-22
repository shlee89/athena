package org.onosproject.athena.feature;



/**
 * Created by seunghyeon on 8/19/15.
 */
public class PacketInFeature implements Feature {
    private FeatureIndex fi = null;
    private UnitPacketInInformation upii = null;

    public boolean addFeatureData(FeatureIndex fi, UnitPacketInInformation upii) {
        if (fi == null || upii == null) {
            return false;
        }
        this.fi = fi;
        this.upii = upii;

        return true;
    }

    public FeatureIndex getFeatureindex() {
        if (fi == null) {
            return null;
        }
        return fi;
    }

    public UnitPacketInInformation getUnitPacketInInformation() {
        if (upii == null) {
            return null;
        }
        return upii;
    }

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.ASYNCHRONOUS_EVENT;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.ASYNCHRONOUS_PACKET_IN;
    }

    @Override
    public String getDescription() {
        return "The Packet_In feature object";
    }


}
