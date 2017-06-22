package org.onosproject.athena.learning;

import org.onosproject.athena.database.OnlineEvent;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.net.provider.ProviderService;

import java.nio.file.Path;

/**
 * Created by seunghyeon on 9/2/15.
 */
public interface MachineLearningProviderService extends ProviderService<MachineLearningProvider> {

    //Get database with advanced query (Weka file Type)
    void getWekaArffResult(QueryIdentifier id, Path path);

    //get online events
    void getAsynchronousOnlineEvent(QueryIdentifier id, OnlineEvent event);


}
