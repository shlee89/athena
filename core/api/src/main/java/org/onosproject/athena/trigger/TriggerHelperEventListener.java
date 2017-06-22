package org.onosproject.athena.trigger;

import org.onosproject.core.ApplicationId;

/**
 * Created by seunghyeon on 11/4/15.
 */
public interface TriggerHelperEventListener {
    //Get On-line event
    void getTriggerFeatures(ApplicationId applicationId, TriggerHelperOnlineEvent triggerHelperOnlineEvent);
}