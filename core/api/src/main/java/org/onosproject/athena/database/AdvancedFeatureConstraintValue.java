package org.onosproject.athena.database;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * parameters for advanced options
 * Created by seunghyeon on 1/18/16.
 */
public class AdvancedFeatureConstraintValue {
    List<String> value = new ArrayList<>();

    public AdvancedFeatureConstraintValue() {
    }

    public AdvancedFeatureConstraintValue(List<String> value) {
        this.value = value;
    }

    public AdvancedFeatureConstraintValue(String... values) {
        for (int i = 0; i < values.length; i++) {
            this.append(values[i]);
        }
    }


    public void append(String value) {
        this.value.add(value);
    }

    public Iterator<String> getValue() {
        return value.iterator();
    }
}
