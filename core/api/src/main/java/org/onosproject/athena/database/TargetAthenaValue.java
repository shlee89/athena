package org.onosproject.athena.database;

import java.io.Serializable;
import java.util.Date;

/**
 * This code could be optimized, when finising athena prototpe
 * Created by seunghyeon on 4/7/16.
 */
public class TargetAthenaValue implements Serializable {
    private int flags = 0;
    private FeatureConstraint featureConstraint;
    private Date dateAthenaFeature;
    private Integer integerAthenaFeature;
    private Long longAthenaFeature;

    public TargetAthenaValue(Object obj) {
        if (obj instanceof FeatureConstraint) {
            this.flags = 1;
            this.featureConstraint = (FeatureConstraint) obj;
        } else if (obj instanceof Date) {
            this.flags = 2;
            this.dateAthenaFeature = (Date) obj;

        } else if (obj instanceof Integer) {
            this.flags = 3;
            this.integerAthenaFeature = (Integer) obj;

        } else if (obj instanceof Long) {
            this.flags = 4;
            this.longAthenaFeature = (Long) obj;

        }
    }

    public TargetAthenaValue(FeatureConstraint featureConstraint) {
        this.flags = 1;
        this.featureConstraint = featureConstraint;
    }

    public TargetAthenaValue(Date dateAthenaFeature) {
        this.flags = 2;
        this.dateAthenaFeature = dateAthenaFeature;
    }

    public TargetAthenaValue(Integer integerAthenaFeature) {
        this.flags = 3;
        this.integerAthenaFeature = integerAthenaFeature;
    }

    public TargetAthenaValue(Long longAthenaFeature) {
        this.flags = 4;
        this.longAthenaFeature = longAthenaFeature;
    }

    public Object getTargetAthenaValue() {
        switch (flags) {
            case 1:
                return featureConstraint;
            case 2:
                return dateAthenaFeature;
            case 3:
                return integerAthenaFeature;
            case 4:
                return longAthenaFeature;
            default:
                return null;
        }
    }
}
