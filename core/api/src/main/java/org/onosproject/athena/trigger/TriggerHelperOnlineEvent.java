package org.onosproject.athena.trigger;

import java.util.List;

/**
 * Created by seunghyeon on 11/9/15.
 */
public class TriggerHelperOnlineEvent {
    private String triggeredFeature;
    private List<String> listOfAffectedFeature;
    private Long userDefinedThreshold;
    private Long valueOfFeature;

    public TriggerHelperOnlineEvent(String triggeredFeature,
                                    List<String> listOfAffectedFeature,
                                    Long userDefinedThreshold,
                                    Long valueOfFeature) {
        this.triggeredFeature = triggeredFeature;
        this.listOfAffectedFeature = listOfAffectedFeature;
        this.userDefinedThreshold = userDefinedThreshold;
        this.valueOfFeature = valueOfFeature;
    }

    public String getTriggeredFeature() {
        return triggeredFeature;
    }

    public List<String> getListOfAffectedFeature() {
        return listOfAffectedFeature;
    }

    public Long getUserDefinedThreshold() {
        return userDefinedThreshold;
    }

    public Long getValueOfFeature() {
        return valueOfFeature;
    }
}
