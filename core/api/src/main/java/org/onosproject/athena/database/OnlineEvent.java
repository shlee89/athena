package org.onosproject.athena.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Date;


/**
 * Created by seunghyeon on 9/2/15.
 */
public class OnlineEvent {

    private Multimap<String, Long> features =
            ArrayListMultimap.create();

    private Date date;

    public void addFeatures(String feature, Long value) {
        if (feature == null) {
            return;
        }
        features.put(feature, value);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {

        return date;
    }

    public Multimap<String, Long> getFeatures() {
        return features;
    }
}
