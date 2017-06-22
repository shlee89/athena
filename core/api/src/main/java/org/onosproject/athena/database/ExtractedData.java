package org.onosproject.athena.database;


import java.util.Date;

/**
 * Created by seunghyeon on 1/24/16.
 */
public class ExtractedData {
    String key;
    Integer intValue = null;
    Double doubleValue = null;
    Date dateValue = null;
    Short shortValue = null;
    String stringValue = null;
    Boolean booleanValue = null;
    Long longValue = null;

    public ExtractedData(String key, Object value) {
        this.key = key;
        if (value instanceof Integer) {
            intValue = (Integer) value;
        } else if (value instanceof Long) {
            longValue = (Long) value;
        } else if (value instanceof Date) {
            dateValue = (Date) value;
        } else if (value instanceof Short) {
            shortValue = (Short) value;
        } else if (value instanceof String) {
            stringValue = (String) value;
        } else if (value instanceof Boolean) {
            booleanValue = (Boolean) value;
        } else if (value instanceof Double) {
            doubleValue = (Double) value;
        } else {
            System.out.println("notsupported");
        }
    }

    public String getKey() {
        return key;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public Short getShortValue() {
        return shortValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public Long getLongValue() {
        return longValue;
    }
}
