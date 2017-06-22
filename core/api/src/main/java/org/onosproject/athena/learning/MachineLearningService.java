package org.onosproject.athena.learning;

import org.onosproject.core.ApplicationId;

/**
 * Created by seunghyeon on 9/2/15.
 */
public interface MachineLearningService {
    //add MachineLearning Event Dispatcher
    void addLearningEventListener(int prioirity, LearningEventListener listener);

    void removeLearningEventListener(LearningEventListener listener);

    void registerModelManagementService(ApplicationId applicationId, String model);

    void unRegisterModelManagementService(ApplicationId applicationId, String model);

    //return all of models
    Iterable<AthenaModel> getAllModel();


    /**
     *
     @Override
     public Iterable<Host> getHosts() {
     return ImmutableSet.copyOf(hosts.values());
     }
     */

}