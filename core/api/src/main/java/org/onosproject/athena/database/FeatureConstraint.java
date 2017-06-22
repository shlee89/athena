package org.onosproject.athena.database;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The FeatureConstraint supports a complex query mechanism with logical and comparable operators.
 * It provides a heierarical query statement to request more complex data.
 * This class is unified constraint to be used for Online Event Delivery, Data Query, ML tasks.
 * AthenaField: A super class for Athena indexes and Athena Features.
 * FeatureConstraintOperator: Unified operators(Comprable: EQ, NE, GE, GT, LE, GT; Logical: AND, OR, IN)
 * FeatureConstraintOperatorType: The type of an operator (Comprable, Logical)
 * Object list: FeatureConstraint Objs , Athena values :Long, Date, IP addr
 * Created by seunghyeon on 9/13/15.
 */
public class FeatureConstraint implements Serializable {

    //AthenaFeatureField or AthenaIndexField
    private AthenaField featureName;
    private FeatureConstraintOperator featureConstraintOperator;
    private FeatureConstraintOperatorType featureConstraintOperatorType;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //TODO this value could be desprated... later version of Athena
    private FeatureConstraintType featureConstraintType;

    // To be FeatureConstraint, Integer, long, Date
    private List<TargetAthenaValue> dataRequestObjectValueList = new ArrayList<>();


    public FeatureConstraint(FeatureConstraintType featureConstraintType,
                             FeatureConstraintOperatorType featureConstraintOperatorType,
                             FeatureConstraintOperator featureConstraintOperator,
                             AthenaField featureName) {
        this.featureConstraintType = featureConstraintType;
        this.featureConstraintOperatorType = featureConstraintOperatorType;
        this.featureConstraintOperator = featureConstraintOperator;
        this.featureName = featureName;
    }

    public FeatureConstraint(FeatureConstraintType featureConstraintType,
                             FeatureConstraintOperatorType featureConstraintOperatorType,
                             FeatureConstraintOperator featureConstraintOperator,
                             AthenaField featureName, TargetAthenaValue targetAthenaValue) {
        this.featureConstraintType = featureConstraintType;
        this.featureConstraintOperatorType = featureConstraintOperatorType;
        this.featureConstraintOperator = featureConstraintOperator;
        this.featureName = featureName;
        dataRequestObjectValueList.add(targetAthenaValue);
    }

    public FeatureConstraint(FeatureConstraintOperatorType featureConstraintOperatorType,
                             FeatureConstraintOperator featureConstraintOperator) {
        this.featureConstraintOperatorType = featureConstraintOperatorType;
        this.featureConstraintOperator = featureConstraintOperator;
    }


    public FeatureConstraintOperatorType getFeatureConstraintOperatorType() {
        return featureConstraintOperatorType;
    }

    public FeatureConstraintType getFeatureConstraintType() {
        return featureConstraintType;
    }

    public AthenaField getFeatureName() {
        return featureName;
    }

    public FeatureConstraintOperator getFeatureConstraintOperator() {
        return featureConstraintOperator;
    }

    public List<TargetAthenaValue> getDataRequestObjectValueList() {
        return dataRequestObjectValueList;
    }

    public void appenValue(TargetAthenaValue targetAthenaValue) {
        dataRequestObjectValueList.add(targetAthenaValue);
    }

    /* Previous version (Not suitable for general usage)
    private LinkedHashMap<Object, Object> lhm = new LinkedHashMap<>();

    public FeatureConstraint() {
    }

    public FeatureConstraint(final Object key, final Object value) {
        lhm.put(key, value);
    }

    public void removeField(final Object key) {
        lhm.remove(key);
    }

    public void append(final Object key, final Object value) {
        lhm.put(key, value);

    }

    public LinkedHashMap<Object, Object> getLhm() {
        return lhm;
    }
    */
}
