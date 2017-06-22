package org.onosproject.athena.feature;


import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitErrorMessageInformation implements UnitFeature {
    //Save as ordinal (OFErrorType).
    private final int errType;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitErrorMessageInformation(int errType) {
        this.errType = errType;
    }

    public int getErrType() {
        return errType;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }
}
