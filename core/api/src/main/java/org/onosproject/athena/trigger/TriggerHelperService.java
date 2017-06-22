package org.onosproject.athena.trigger;

import org.onosproject.core.ApplicationId;

/**
 * Created by seunghyeon on 11/4/15.
 */
public interface TriggerHelperService {
    //used for user

    void addTriggerHelperEventListener(int priority, TriggerHelperEventListener listener);

    void removeTriggerHelperEventListener(TriggerHelperEventListener listener);


    //Request database with advanced query (Online, Offline)
    boolean registerOnlineFeature(ApplicationId applicationId, SimpleTriggerHelperRequester requester);

    boolean unRegisterOnlineFeature(ApplicationId applicationId);

}
