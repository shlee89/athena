package athena.northbound;

import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaField;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.AdvancedFeatureConstraintType;
import org.onosproject.athena.database.AdvancedFeatureConstraintValue;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.TargetAthenaValue;

import java.util.Date;

/**
 * Created by seunghyeon on 4/7/16.
 */
public interface AthenaQueryHelper {

    /**
     * parse string date to Athena value (Date)
     *
     * @param date yyyy-MM-dd-HH:mm
     * @return c
     */
    Date dateToAthenaValue(String date);

    /**
     * parse string(ip) to athena value (Integer)
     *
     * @param ip c
     * @return c
     */
    Integer ipv4ToAthenaValue(String ip);

    /**
     * parse string(numeric) to athena value (Long)
     *
     * @param value numeric
     * @return c
     */
    Long numericToAthenaValue(String value);

    AthenaFeatureField generateAthenaFeatureField(String nameOfField);

    AthenaIndexField generateAthenaIndexField(String nameOfField);

    FeatureConstraint generateComparableDataRequestObject(FeatureConstraintType featureConstraintType,
                                                          FeatureConstraintOperatorType featureConstraintOperatorType,
                                                          FeatureConstraintOperator featureConstraintOperator,
                                                          AthenaField featureName, TargetAthenaValue value);

    FeatureConstraint generateLogicalDataRequestObject(FeatureConstraintOperatorType featureConstraintOperatorType,
                                                       FeatureConstraintOperator featureConstraintOperator);

    FeatureConstraint appendDataRequestObject(FeatureConstraint parent, FeatureConstraint child);

    /**
     * @param type logical or feature
     * @return c
     */
    FeatureConstraintOperatorType generateRequestOperatorType(String type);

    /**
     * @param type logical in, or, and
     * @return c
     */
    FeatureConstraintOperator generateLogicalRequestOperator(String type);

    /**
     * @param type gt, lt
     * @return c
     */
    FeatureConstraintOperator generateComparableRequestOperator(String type);

    /**
     * the type of target fields
     *
     * @param type Feature feild or Index field
     * @return c
     */
    FeatureConstraintType generateDataRequestObjectType(String type);

    AdvancedFeatureConstraint generateDataRequestAdvancedObject();

    AdvancedFeatureConstraint appendDataRequestAdvancedObject(
            AdvancedFeatureConstraint advancedFeatureConstraint,
            AdvancedFeatureConstraintType option,
            AdvancedFeatureConstraintValue value);

    /**
     * @param type AGGREGATE, SORTING, LIMIT
     * @return c
     */
    AdvancedFeatureConstraintType generateDataRequestAdvancedType(String type);

    AdvancedFeatureConstraintValue generatoeDataRequestAdvancedValue(String... values);


}
