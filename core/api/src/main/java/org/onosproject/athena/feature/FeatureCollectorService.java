package org.onosproject.athena.feature;

/**
 * Created by seunghyeon on 8/26/15.
 */
public interface FeatureCollectorService {

    void addFeatureEventListener(int prioirity, FeatureEventListener listener);

    void removeFeatureEventListener(FeatureEventListener listener);

}
