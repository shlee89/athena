package org.onosproject.athena.learning.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.athena.learning.MachineLearningEvent;
import org.onosproject.athena.learning.MachineLearningStore;
import org.onosproject.athena.learning.MachineLearningStoreDelegate;
import org.onosproject.store.AbstractStore;
import org.onosproject.store.service.StorageService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 12/26/15.
 */
@Component(immediate = true)
@Service
public class DistributedMachineLearningStore
        extends AbstractStore<MachineLearningEvent, MachineLearningStoreDelegate>
        implements MachineLearningStore {
    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected StorageService storageService;

    /*
    * TODO Define distributed variables in here (Model, Information ...)
    */
//    private Map<String, Set<HostId>> networks;
//    private ConsistentMap<String, Set<HostId>> nets;

    /*
    * TODO Define Listener, when the value is changed, it is called
    */
    //private final InternalListener listener = new InternalListener();


    @Activate
    public void activate() {
         /*
        * TODO Define Distributed varialves to storage service, When i need to
        * add additional classes, we need to add additionals
        */
//        nets = storageService.<String, Set<HostId>>consistentMapBuilder()
//                .withSerializer(Serializer.using(KryoNamespaces.API))
//                .withName("byon-networks")
//                .build();
//        nets.addListener(listener);
//        networks = nets.asJavaMap();
    }

    @Deactivate
    public void deactivate() {
        /*
        * TODO remove listeners
         */
//        nets.removeListener(listener);
//        log.info("Stopped");

    }

    public void putData() {
                /*
        * TODO Simply putting
         */
//        networks.putIfAbsent(network, Sets.<HostId>newHashSet());

    }

    /*
    * TODO Define event handler like below... Make custom messages
     */

    /*
    private class InternalListener implements MapEventListener<String, Set<HostId>> {
        @Override
        public void event(MapEvent<String, Set<HostId>> mapEvent) {
            final NetworkEvent.Type type;
            switch (mapEvent.type()) {
                case INSERT:
                    type = NETWORK_ADDED;
                    break;
                case UPDATE:
                    type = NETWORK_UPDATED;
                    break;
                case REMOVE:
                default:
                    type = NETWORK_REMOVED;
                    break;
            }
            notifyDelegate(new NetworkEvent(type, mapEvent.key()));
        }
    }
    */


}
