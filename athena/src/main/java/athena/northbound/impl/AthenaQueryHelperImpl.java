package athena.northbound.impl;

import org.onosproject.athena.database.TargetAthenaValue;
import athena.northbound.AthenaQueryHelper;
import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaField;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.AthenaValueGenerator;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.AdvancedFeatureConstraintType;
import org.onosproject.athena.database.AdvancedFeatureConstraintValue;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by seunghyeon on 4/7/16.
 */
public class AthenaQueryHelperImpl implements AthenaQueryHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Date dateToAthenaValue(String date) {

        return AthenaValueGenerator.parseDataToAthenaValue(date);
    }

    @Override
    public Integer ipv4ToAthenaValue(String ip) {

        return AthenaValueGenerator.parseIPv4ToAthenaValue(ip);
    }

    @Override
    public Long numericToAthenaValue(String value) {

        return AthenaValueGenerator.generateAthenaValue(value);
    }

    @Override
    public AthenaFeatureField generateAthenaFeatureField(String nameOfField) {
        return new AthenaFeatureField(nameOfField);
    }

    @Override
    public AthenaIndexField generateAthenaIndexField(String nameOfField) {
        return new AthenaIndexField(nameOfField);
    }

    @Override
    public FeatureConstraint generateComparableDataRequestObject(FeatureConstraintType featureConstraintType,
                                                                 FeatureConstraintOperatorType featureConstraintOperatorType,
                                                                 FeatureConstraintOperator featureConstraintOperator,
                                                                 AthenaField featureName,
                                                                 TargetAthenaValue value) {
        FeatureConstraint dataRequestobject = new FeatureConstraint(featureConstraintType,
                featureConstraintOperatorType,
                featureConstraintOperator,
                featureName,
                value);

        return dataRequestobject;
    }

    @Override
    public FeatureConstraint generateLogicalDataRequestObject(FeatureConstraintOperatorType featureConstraintOperatorType,
                                                              FeatureConstraintOperator featureConstraintOperator) {
        FeatureConstraint dataRequestobject = new FeatureConstraint(featureConstraintOperatorType, featureConstraintOperator);

        return dataRequestobject;
    }

    @Override
    public FeatureConstraint appendDataRequestObject(FeatureConstraint parent, FeatureConstraint child) {
        parent.appenValue(new TargetAthenaValue(child));
        return parent;
    }

    @Override
    public FeatureConstraintOperatorType generateRequestOperatorType(String type) {
        if (type.startsWith("LOGICAL")) {
            return FeatureConstraintOperatorType.LOGICAL;
        } else if (type.startsWith("COMPARABLE")) {
            return FeatureConstraintOperatorType.COMPARABLE;
        } else {
            log.warn("Not supported type");
            return null;
        }
    }

    @Override
    public FeatureConstraintOperator generateLogicalRequestOperator(String type) {
        return new FeatureConstraintOperator(type);
    }

    @Override
    public FeatureConstraintOperator generateComparableRequestOperator(String type) {
        return new FeatureConstraintOperator(type);
    }

    @Override
    public FeatureConstraintType generateDataRequestObjectType(String type) {
        if (type.startsWith("FEATURE")) {
            return FeatureConstraintType.FEATURE;
        } else if (type.startsWith("INDEX")) {
            return FeatureConstraintType.INDEX;
        } else {
            log.warn("Not supported type");
            return null;
        }
    }

    @Override
    public AdvancedFeatureConstraint generateDataRequestAdvancedObject() {
        return new AdvancedFeatureConstraint();
    }

    @Override
    public AdvancedFeatureConstraint appendDataRequestAdvancedObject(
            AdvancedFeatureConstraint advancedFeatureConstraint,
            AdvancedFeatureConstraintType option,
            AdvancedFeatureConstraintValue value) {
        advancedFeatureConstraint.addAdvancedOptions(option, value);
        return advancedFeatureConstraint;
    }

    @Override
    public AdvancedFeatureConstraintType generateDataRequestAdvancedType(String type) {
        if (type.startsWith("AGGREGATE")) {
            return AdvancedFeatureConstraintType.AGGREGATE;
        } else if (type.startsWith("SORTING")) {
            return AdvancedFeatureConstraintType.SORTING;
        } else if (type.startsWith("LIMIT")) {
            return AdvancedFeatureConstraintType.LIMIT_FEATURE_COUNTS;
        } else {
            log.warn("not supported type");
            return null;
        }
    }

    @Override
    public AdvancedFeatureConstraintValue generatoeDataRequestAdvancedValue(String... values) {
        return new AdvancedFeatureConstraintValue(values);
    }

}
