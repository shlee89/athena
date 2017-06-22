package athena.northbound.impl;

import athena.northbound.AthenaQueryHelper;
import athena.northbound.DataQueryManager;
import athena.util.DatabaseConnector;
import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.AthenaFeatureRequestrType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation for DataQueryManager.
 * Created by seunghyeon on 4/3/16.
 */
public class DataQueryManagerImpl implements DataQueryManager{
    DatabaseConnector databaseConnector;
    AthenaQueryHelper athenaQueryHelper = new AthenaQueryHelperImpl();

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Construct a DataQueryManager instance with DatabaseConnector instance, which enables to communicate a distributed Databases.
     * @param databaseConnector The DatabaseConnector instance. It
     */
    public DataQueryManagerImpl(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }


    @Override
    public void displayFeatures(AthenaFeatureRequester athenaFeatureRequester, AthenaFeatures athenaFeatures) {
        databaseConnector.printAllFeatures(athenaFeatureRequester, athenaFeatures);
    }

    @Override
    public AthenaFeatures requestAthenaFeatures(FeatureConstraint featureConstraint,
                                                AdvancedFeatureConstraint advancedFeatureConstraint) {
        AthenaFeatureRequester athenaFeatureRequester =
                new AthenaFeatureRequester(AthenaFeatureRequestrType.REQUEST_FEATURES,
                        featureConstraint, advancedFeatureConstraint, null);
        return databaseConnector.requestToDatabase(athenaFeatureRequester);
    }


}
