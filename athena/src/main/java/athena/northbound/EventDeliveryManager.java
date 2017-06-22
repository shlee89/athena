package athena.northbound;

import org.onosproject.core.ApplicationId;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.QueryIdentifier;

/**
 * The EventDeliveryManager deliveries Athena features with an online manner.
 * It forwards incoming Athena features with online manner according to user-defined constraints.
 * Created by seunghyeon on 4/7/16.
 */
public interface EventDeliveryManager {

    /**
     * Register listener to get online events.
     *
     * @param listener AthenaFeatureEventListener.
     */
    void registerAthenaFeatureEventListener(AthenaFeatureEventListener listener);

    /**
     * Register constraints to get specific features.
     *
     * @param applicationId     Application Id.
     * @param identifier        Constraint identifier.
     * @param featureConstraint User-defined constraints.
     */
    void registerOnlineAthenaFeature(ApplicationId applicationId,
                                     QueryIdentifier identifier,
                                     FeatureConstraint featureConstraint);

    /**
     * Unregister constraint
     *
     * @param applicationId Application Id.
     * @param identifier    Constraint identifier.
     */
    void unRegisterOnlineAthenaFeature(ApplicationId applicationId,
                                       QueryIdentifier identifier);

}
