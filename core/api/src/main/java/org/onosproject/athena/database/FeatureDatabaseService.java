package org.onosproject.athena.database;


import org.onosproject.core.ApplicationId;

/**
 * Created by seunghyeon on 8/27/15.
 */
public interface FeatureDatabaseService {
    //To be added "Register/Unregister AthenaFeatureEventListener"
    void addDatabaseEventListener(int prioirity, AthenaFeatureEventListener listener);

    void removeDatabaseEventListener(AthenaFeatureEventListener listener);

    /**
     * It returns features requested from user.
     *
     * @param applicationId          identifies Who requested.
     * @param athenaFeatureRequester It includes constrains and other detials.
     */
    void requestFeatures(ApplicationId applicationId, AthenaFeatureRequester athenaFeatureRequester);

    //Request database with advanced query (Online, Offline)
    boolean registerOnlineFeature(ApplicationId applicationId,
                                  QueryIdentifier identifier,
                                  AthenaFeatureRequester athenaFeatureRequester);

    boolean unRegisterOnlineFeature(ApplicationId applicationId, QueryIdentifier identifier);

    Object kyroSerializerInitialize();

    void debugCommand1(String param);

    void debugCommand2(String param);

    void debugCommand3(String param);
}
