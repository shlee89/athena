package org.onosproject.provider.framework.abnormal.learning.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.core.ApplicationId;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.FeatureDatabaseService;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.athena.learning.MachineLearningProviderRegistry;
import org.onosproject.athena.learning.MachineLearningProviderService;
import org.onosproject.net.provider.AbstractProvider;
import org.onosproject.net.provider.ProviderId;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;

import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 9/2/15.
 */
@Component(immediate = true)
public class MachineLearningProvider extends AbstractProvider
        implements org.onosproject.athena.learning.MachineLearningProvider {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FeatureDatabaseService featureDatabaseService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected MachineLearningProviderRegistry providerRegistry;

    private MachineLearningProviderService providerService;

    protected InternalMachineLearningCollector listener = new InternalMachineLearningCollector();

    public MachineLearningProvider() {
        super(new ProviderId("framework", "org.onosproject.provider.framework"));
    }

    @Activate
    public void activate(ComponentContext context) {
        log.info("start");
        providerService = providerRegistry.register(this);
        featureDatabaseService.addDatabaseEventListener(1, listener);
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        log.info("stopped");
        providerRegistry.unregister(this);
        featureDatabaseService.removeDatabaseEventListener(listener);
    }

    private class InternalMachineLearningCollector implements AthenaFeatureEventListener {


        @Override
        public void getRequestedFeatures(ApplicationId applicationId, AthenaFeatures athenaFeatures) {

        }

        @Override
        public void getFeatureEvent(ApplicationId applicationId, QueryIdentifier id, HashMap<String, Object> event) {

        }
    }
}
