package athena.northbound;

import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.FeatureConstraint;


/**
 * The DataQueryManager provides a comprehensive query mechanism to retrieve the Athena features on a distributed databases.
 * <p>
 * Created by seunghyeon on 4/7/16.
 */
public interface DataQueryManager {

    /**
     * Display Athena features to CLI interface with with user-defined highlighting.
     *
     * @param athenaFeatureRequester The requested Athena feature information. This value is used for highlighting certain Athena features according to "FeatureConstraint" Object.
     * @param athenaFeatures         A set of actual Athena features to be displayed at CLI interface.
     */
    void displayFeatures(AthenaFeatureRequester athenaFeatureRequester, AthenaFeatures athenaFeatures);

    /**
     * Request Athena features with user-defined constraints.
     *
     * @param featureConstraint         The user-defined constraints. This value is hierarchical relationship to represent a complex constraint.
     * @param advancedFeatureConstraint The user-defined advanced options. It does not affect actual entries of Athena features, but it supports additional data pre-processings such as aggregation, sorting, and limiting.
     * @return A set of Athena features.
     */
    AthenaFeatures requestAthenaFeatures(
            FeatureConstraint featureConstraint,
            AdvancedFeatureConstraint advancedFeatureConstraint);

}
