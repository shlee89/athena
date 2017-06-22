package org.onosproject.athena.trigger;

import org.slf4j.Logger;

import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 11/9/15.
 */
public class SimpleTriggerHelperRequester {
    private final Logger log = getLogger(getClass());

    private CoarseGrainedAttackName coarseGrainedAttackName = new CoarseGrainedAttackName();

    HashMap<String, Long> requestFeatures = new HashMap<>();

    public boolean addRequestFeature(String nameOfTriggeredFeature, Long value) {
        if (!coarseGrainedAttackName.checkExistedFeatures(nameOfTriggeredFeature)) {
            log.warn("Not existed feature");
            return false;
        }

        requestFeatures.put(nameOfTriggeredFeature, value);
        return true;
    }

    public HashMap<String, Long> getRequestFeatures() {
        return requestFeatures;
    }

    public SimpleTriggerHelperRequester() {
    }

}
