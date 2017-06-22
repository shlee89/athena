package org.onosproject.provider.framework.abnormal.database.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.athena.database.FeatureDatabaseProviderRegistry;
import org.onosproject.athena.database.FeatureDatabaseProviderService;
import org.onosproject.athena.feature.AggregateStatisticsFeature;
import org.onosproject.athena.feature.ErrorMessageFeature;
import org.onosproject.athena.feature.FeatureCategory;
import org.onosproject.athena.feature.FeatureCollectorService;
import org.onosproject.athena.feature.FeatureEventListener;
import org.onosproject.athena.feature.FeatureType;
import org.onosproject.athena.feature.FlowRemovedFeature;
import org.onosproject.athena.feature.FlowStatisticsFeature;
import org.onosproject.athena.feature.PacketInFeature;
import org.onosproject.athena.feature.PortStatisticsFeature;
import org.onosproject.athena.feature.PortStatusFeature;
import org.onosproject.athena.feature.QueueStatisticsFeature;
import org.onosproject.athena.feature.TableStatisticsFeature;
import org.onosproject.net.provider.AbstractProvider;
import org.onosproject.net.provider.ProviderId;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 8/27/15.
 */
@Component(immediate = true)
public class FeatureDatabaseProvider extends AbstractProvider
        implements org.onosproject.athena.database.FeatureDatabaseProvider {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FeatureCollectorService featureCollectorService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FeatureDatabaseProviderRegistry providerRegistry;

    private FeatureDatabaseProviderService providerService;

    protected InternalFeatureCollector listener = new InternalFeatureCollector();

    public FeatureDatabaseProvider() {
        super(new ProviderId("framework", "org.onosproject.provider.framework"));
    }

    @Activate
    public void activate(ComponentContext context) {
        log.info("start");
        providerService = providerRegistry.register(this);
        featureCollectorService.addFeatureEventListener(1, listener);
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        log.info("end");
        providerRegistry.unregister(this);
        featureCollectorService.removeFeatureEventListener(listener);
    }

    @Modified
    public void modified(ComponentContext context) {

    }

    private class InternalFeatureCollector implements FeatureEventListener {

        @Override
        public void packetInFeatureProcess(FeatureType featureType,
                                           FeatureCategory featureCategory,
                                           PacketInFeature packetInFeature) {
            providerService.featureHandler(featureType, featureCategory, packetInFeature);
        }

        @Override
        public void flowRemovedProcess(FeatureType featureType,
                                       FeatureCategory featureCategory,
                                       FlowRemovedFeature flowRemovedFeature) {
            providerService.featureHandler(featureType, featureCategory, flowRemovedFeature);
        }

        @Override
        public void portStatusProcess(FeatureType featureType,
                                      FeatureCategory featureCategory,
                                      PortStatusFeature portStatusFeature) {
            providerService.featureHandler(featureType, featureCategory, portStatusFeature);
        }

        @Override
        public void errorMsgProcess(FeatureType featureType,
                                    FeatureCategory featureCategory,
                                    ErrorMessageFeature errorMessageFeature) {
            providerService.featureHandler(featureType, featureCategory, errorMessageFeature);
        }

        @Override
        public void flowStatsProcess(FeatureType featureType,
                                     FeatureCategory featureCategory,
                                     FlowStatisticsFeature flowStatisticsFeature) {
            providerService.featureHandler(featureType, featureCategory, flowStatisticsFeature);
        }

        @Override
        public void aggregateStatsProcess(FeatureType featureType,
                                          FeatureCategory featureCategory,
                                          AggregateStatisticsFeature aggregateStatisticsFeature) {
            providerService.featureHandler(featureType, featureCategory, aggregateStatisticsFeature);
        }

        @Override
        public void portStatsProcess(FeatureType featureType,
                                     FeatureCategory featureCategory,
                                     PortStatisticsFeature portStatisticsFeature) {
            providerService.featureHandler(featureType, featureCategory, portStatisticsFeature);
        }

        @Override
        public void queueStatsProcess(FeatureType featureType,
                                      FeatureCategory featureCategory,
                                      QueueStatisticsFeature queueStatisticsFeature) {
            providerService.featureHandler(featureType, featureCategory, queueStatisticsFeature);
        }

        @Override
        public void tableStatsProcess(FeatureType featureType,
                                      FeatureCategory featureCategory,
                                      TableStatisticsFeature tableStatisticsFeature) {
            providerService.featureHandler(featureType, featureCategory, tableStatisticsFeature);
        }
    }

}
