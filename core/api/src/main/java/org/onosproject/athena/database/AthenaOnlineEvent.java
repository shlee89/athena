package org.onosproject.athena.database;

import java.util.HashMap;

/**
 * Created by seunghyeon on 4/7/16.
 */
public class AthenaOnlineEvent {
    HashMap<String, Object> event;

    public AthenaOnlineEvent(HashMap<String, Object> event) {
        this.event = event;
    }

    public HashMap<String, Object> getEvent() {
        return event;
    }

}
