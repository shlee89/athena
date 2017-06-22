package org.onosproject.athena.database;



/**
 * Basic Athena features.
 * This responsibilitiy is chaged to model developers (2015. 12. 23.) .
 * Created by seunghyeon on 10/11/15.
 */
public class AthenaFeatures {
    private DatabaseType databaseType;
    private Object features;

    public AthenaFeatures(DatabaseType databaseType, Object features) {
        this.databaseType = databaseType;
        this.features = features;
    }

    public AthenaFeatures() {
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public Object getFeatures() {
        return features;
    }

    public void setFeatures(Object features) {
        this.features = features;
    }
}
