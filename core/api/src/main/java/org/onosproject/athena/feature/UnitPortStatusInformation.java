package org.onosproject.athena.feature;

import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitPortStatusInformation implements UnitFeature {
    //save as a ordinal (OFPortReason)
    private final int reason;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitPortStatusInformation(int reason) {
        this.reason = reason;
    }

    public int getReason() {
        return reason;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }
}
