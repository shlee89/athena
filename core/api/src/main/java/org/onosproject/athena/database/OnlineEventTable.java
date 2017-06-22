package org.onosproject.athena.database;

import org.onosproject.core.ApplicationId;


/**
 * Created by seunghyeon on 11/3/15.
 */
public class OnlineEventTable {
    private ApplicationId applicationId;
    private QueryIdentifier queryIdentifier;
    private AthenaFeatureRequester athenaFeatureRequester;

    public OnlineEventTable(ApplicationId applicationId,
                            QueryIdentifier queryIdentifier,
                            AthenaFeatureRequester athenaFeatureRequester) {
        this.applicationId = applicationId;
        this.queryIdentifier = queryIdentifier;
        this.athenaFeatureRequester = athenaFeatureRequester;
    }

    public ApplicationId getApplicationId() {
        return applicationId;
    }

    public QueryIdentifier getQueryIdentifier() {
        return queryIdentifier;
    }

    public AthenaFeatureRequester getAthenaFeatureRequester() {
        return athenaFeatureRequester;
    }
}
