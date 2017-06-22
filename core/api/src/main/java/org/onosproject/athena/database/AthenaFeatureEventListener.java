package org.onosproject.athena.database;

//import java.nio.file.Path;

import org.onosproject.core.ApplicationId;

import java.util.HashMap;

/**
 * Created by seunghyeon on 9/1/15.
 */
public interface AthenaFeatureEventListener {

    //getRequestFeatures

    void getRequestedFeatures(ApplicationId applicationId, AthenaFeatures athenaFeatures);

    //Get WekaDataFinished
//    void getWekaArffResult(QueryIdentifier id, Path path);

    //Get On-line event
    void getFeatureEvent(ApplicationId applicationId, QueryIdentifier id, HashMap<String, Object> event);

}