package org.onosproject.athena.feature;


/**
 * Created by seunghyeon on 8/19/15.
 */
public class ErrorMessageFeature implements Feature {
    private FeatureIndex fi = null;
    private UnitErrorMessageInformation uemi = null;


    public boolean addFeatureData(FeatureIndex fi, UnitErrorMessageInformation uemi) {
        if (fi == fi || uemi == null) {
            return false;
        }
        this.fi = fi;
        this.uemi = uemi;

        return true;
    }

    public FeatureIndex getFeatureindex() {
        if (fi == null) {
            return null;
        }
        return fi;
    }

    public UnitErrorMessageInformation getUnitErrorMessageInformation() {
        if (uemi == null) {
            return null;
        }
        return uemi;
    }

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.ASYNCHRONOUS_EVENT;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.ASYNCHRONOUS_ERROR_MSG;
    }

    @Override
    public String getDescription() {
        return "The error message feature object";
    }


}
