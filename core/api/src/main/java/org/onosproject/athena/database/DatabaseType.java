package org.onosproject.athena.database;


/**
 *
 * This is usde for querying to database.
 * This responsibilitiy is chaged to model developers (2015. 12. 23.) .
 * Created by seunghyeon on 10/11/15.
 */
public class DatabaseType {

    public static final String MONGO_DB = "MONGODB";
    public static final String CASSANDRA = "CASSANDRA";
    private String value;


    public DatabaseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }
}
