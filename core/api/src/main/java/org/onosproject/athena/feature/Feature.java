package org.onosproject.athena.feature;

/**
 * Created by seunghyeon on 8/18/15.
 */
public interface Feature {
    /**
     * Get type of feature.
     *
     * @return feature type
     */
    FeatureType getFeatureType();

    /**
     * Get category of certain feature.
     *
     * @return feature category
     */
    FeatureCategory getFeatureCategory();

    /**
     * Description for feature.
     *
     * @return description
     */
    String getDescription();


}
