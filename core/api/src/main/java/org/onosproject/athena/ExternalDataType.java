package org.onosproject.athena;


import java.io.Serializable;

/**
 * Created by seunghyeon on 1/21/16.
 */
public class ExternalDataType implements Serializable {

    public static final String ONLINE_FEATURE_REQUEST_REGISTER = "OFRR";
    public static final String ONLINE_FEATURE_REQUEST_UNREGISTER = "OFRU";
    public static final String ONLINE_FEATURE_EVENT = "OFE";
    public static final String ISSUE_FLOW_RULE = "IFR";

    String type;
    int eventType = 0;

    public ExternalDataType(String type) {
        this.type = type;
        if (type.startsWith(ONLINE_FEATURE_EVENT)) {
            eventType = 1;
        } else if (type.startsWith(ONLINE_FEATURE_REQUEST_REGISTER)) {
            eventType = 2;
        } else if (type.startsWith(ONLINE_FEATURE_REQUEST_UNREGISTER)) {
            eventType = 3;
        } else if (type.startsWith(ISSUE_FLOW_RULE)) {
            eventType = 4;
        } else {
            eventType = 0;
        }
    }

    public String getType() {
        return type;
    }

    public int getEventType() {
        return eventType;
    }

    public ExternalDataType() {
    }
}
