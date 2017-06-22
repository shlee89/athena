package org.onosproject.athena.feature;



/**
 * Created by seunghyeon on 8/19/15.
 */
public class PortStatusFeature implements Feature {
    private FeatureIndex fi = null;
    private UnitPortStatusInformation upsi = null;


    public boolean addFeatureData(FeatureIndex fi, UnitPortStatusInformation upsi) {
        if (fi == fi || upsi == null) {
            return false;
        }
        this.fi = fi;
        this.upsi = upsi;

        return true;
    }

    public FeatureIndex getFeatureindex() {
        if (fi == null) {
            return null;
        }
        return fi;
    }

    public UnitPortStatusInformation getUnitPortStatusInformation() {
        if (upsi == null) {
            return null;
        }
        return upsi;
    }


    @Override
    public FeatureType getFeatureType() {
        return FeatureType.ASYNCHRONOUS_EVENT;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.ASYNCHRONOUS_PORT_STATUS;
    }

    @Override
    public String getDescription() {
        return "The port status feature object";
    }



}
