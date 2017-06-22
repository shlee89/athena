package org.onosproject.athena.feature;




/**
 * Created by seunghyeon on 8/19/15.
 */
public class FlowRemovedFeature implements Feature {
    private FeatureIndex fi = null;
    private UnitFlowRemovedInformation ufri = null;


    public boolean addFeatureData(FeatureIndex fi, UnitFlowRemovedInformation ufri) {
        if (fi == null || ufri == null) {
            return false;
        }
        this.fi = fi;
        this.ufri = ufri;

        return true;
    }

    public FeatureIndex getFeatureindex() {
        if (fi == null) {
            return null;
        }
        return fi;
    }

    public UnitFlowRemovedInformation getUnitFlowRemovedInformation() {
        if (ufri == null) {
            return null;
        }
        return ufri;
    }

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.ASYNCHRONOUS_EVENT;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.ASYNCHRONOUS_FLOW_REMOVED;
    }

    @Override
    public String getDescription() {
        return "The flow removed feature object";
    }

}
