package org.onosproject.athena.database;

/**
 * Created by seunghyeon on 4/7/16.
 */
public interface AthenaField {
    String getTypeOnDatabase(String feature);

    String getValue();

    void setValue(String value);

    String getDescription();

    boolean isElements(String str);
}
