package org.onosproject.athena.trigger;

import org.onosproject.core.ApplicationId;

import java.util.HashMap;

/**
 * Created by seunghyeon on 11/9/15.
 */
public class TriggerOnlineEventTable {
    private ApplicationId applicationId;
    private HashMap<String, Long> requestFeatures = new HashMap<>();

    public TriggerOnlineEventTable(ApplicationId applicationId,
                                   HashMap<String, Long> requestFeatures) {
        this.applicationId = applicationId;
        this.requestFeatures = requestFeatures;
    }

    public ApplicationId getApplicationId() {
        return applicationId;
    }

    public HashMap<String, Long> getRequestFeatures() {
        return requestFeatures;
    }
}
