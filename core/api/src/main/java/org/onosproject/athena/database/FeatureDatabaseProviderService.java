package org.onosproject.athena.database;

import org.onosproject.athena.feature.Feature;
import org.onosproject.athena.feature.FeatureCategory;
import org.onosproject.athena.feature.FeatureType;
import org.onosproject.net.provider.ProviderService;

/**
 * Created by seunghyeon on 8/27/15.
 */
public interface FeatureDatabaseProviderService extends ProviderService<FeatureDatabaseProvider> {
    void featureHandler(FeatureType featureType, FeatureCategory featureCategory, Feature feature);
}
