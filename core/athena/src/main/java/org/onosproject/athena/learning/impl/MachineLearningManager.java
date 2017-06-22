package org.onosproject.athena.learning.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.athena.database.OnlineEvent;
import org.onosproject.athena.learning.AthenaModel;
import org.onosproject.athena.learning.LearningEventListener;
import org.onosproject.athena.learning.MachineLearningEvent;
import org.onosproject.athena.learning.MachineLearningListener;
import org.onosproject.athena.learning.MachineLearningProvider;
import org.onosproject.athena.learning.MachineLearningProviderService;
import org.onosproject.athena.learning.MachineLearningService;
import org.onosproject.athena.learning.MachineLearningStore;
import org.onosproject.athena.learning.MachineLearningStoreDelegate;
import org.onosproject.core.ApplicationId;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.athena.learning.MachineLearningProviderRegistry;
import org.onosproject.net.provider.AbstractListenerProviderRegistry;
//import org.onosproject.net.provider.AbstractProviderRegistry;
import org.onosproject.net.provider.AbstractProviderService;
import org.slf4j.Logger;

import java.nio.file.Path;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 9/2/15.
 */
@Component(immediate = true)
@Service
public class MachineLearningManager
        extends AbstractListenerProviderRegistry<MachineLearningEvent, MachineLearningListener,
        MachineLearningProvider, MachineLearningProviderService>
        implements MachineLearningService, MachineLearningProviderRegistry {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected MachineLearningStore store;

    /**
     * TODO Define listener, Getting event first!
     */
//    private final Listener listener = new Listener();


    private MachineLearningStoreDelegate delegate = new InternalStoreDelegate();

    @Activate
    public void activate() {
        store.setDelegate(delegate);
        eventDispatcher.addSink(MachineLearningEvent.class, listenerRegistry);
        log.info("started");
    }

    @Deactivate
    public void deactivate() {
        store.unsetDelegate(delegate);
        eventDispatcher.removeSink(MachineLearningEvent.class);
        log.info("Stopped");
    }

    @Override
    protected MachineLearningProviderService createProviderService(MachineLearningProvider provider) {
        return new InternalMachineLearningProviderService(provider);
    }

    @Override
    public void addLearningEventListener(int prioirity, LearningEventListener listener) {

    }

    @Override
    public void removeLearningEventListener(LearningEventListener listener) {

    }

    @Override
    public void registerModelManagementService(ApplicationId applicationId, String model) {

    }

    @Override
    public void unRegisterModelManagementService(ApplicationId applicationId, String model) {

    }

    @Override
    public Iterable<AthenaModel> getAllModel() {
        return null;
    }


    private class InternalMachineLearningProviderService extends AbstractProviderService<MachineLearningProvider>
            implements MachineLearningProviderService {

        InternalMachineLearningProviderService(MachineLearningProvider provider) {
            super(provider);
        }

        @Override
        public void getWekaArffResult(QueryIdentifier id, Path path) {

        }

        @Override
        public void getAsynchronousOnlineEvent(QueryIdentifier id, OnlineEvent event) {

        }
    }

    // Store delegate to re-post events emitted from the store.
    private class InternalStoreDelegate implements MachineLearningStoreDelegate {

        @Override
        public void notify(MachineLearningEvent event) {
            post(event);
        }
    }
/*
* TODO Define custom event (Receive DistDB event from others, then forwards app itself...)
 */
//    private class Listener implements NetworkListener {
//        @Override
//        public void event(NetworkEvent event) {
//            log.info("EVENT_IN_NetworkManager {}", event);
//        }
//    }

}
