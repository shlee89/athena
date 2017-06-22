package org.onosproject.athena.database;


import java.util.HashMap;
import java.util.Map;


/**
 * The AdvancedFeatureConstrait supports pre-processing for Athena features.
 * Created by seunghyeon on 1/2/16.
 */
public class AdvancedFeatureConstraint {
    Map<AdvancedFeatureConstraintType, AdvancedFeatureConstraintValue> advancedOptions;


    public AdvancedFeatureConstraint() {
        advancedOptions = new HashMap<>();
    }

    public void addAdvancedOptions(AdvancedFeatureConstraintType option, AdvancedFeatureConstraintValue value) {
        this.advancedOptions.putIfAbsent(option, value);
    }

    public Map<AdvancedFeatureConstraintType, AdvancedFeatureConstraintValue> getAdvancedOptions() {
        return advancedOptions;
    }

}
