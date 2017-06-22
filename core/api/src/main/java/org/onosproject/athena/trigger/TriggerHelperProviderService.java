package org.onosproject.athena.trigger;

import org.onosproject.athena.feature.Feature;
import org.onosproject.athena.feature.FeatureCategory;
import org.onosproject.athena.feature.FeatureType;
import org.onosproject.net.provider.ProviderService;

/**
 * Created by seunghyeon on 11/4/15.
 */
public interface TriggerHelperProviderService
        extends ProviderService<TriggerHelperProvider> {
    void featureHandler(FeatureType featureType, FeatureCategory featureCategory, Feature feature);
}
