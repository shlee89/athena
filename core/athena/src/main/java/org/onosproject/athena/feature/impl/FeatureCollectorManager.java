package org.onosproject.athena.feature.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.athena.feature.QueueStatisticsFeature;
import org.onosproject.athena.feature.TableStatisticsFeature;
import org.onosproject.athena.feature.AggregateStatisticsFeature;
import org.onosproject.athena.feature.ErrorMessageFeature;
import org.onosproject.athena.feature.FeatureCategory;
import org.onosproject.athena.feature.FeatureCollectorProvider;
import org.onosproject.athena.feature.FeatureCollectorProviderRegistry;
import org.onosproject.athena.feature.FeatureCollectorProviderService;
import org.onosproject.athena.feature.FeatureCollectorService;

import org.onosproject.athena.feature.FeatureEventListener;
import org.onosproject.athena.feature.FeatureType;
import org.onosproject.athena.feature.FlowRemovedFeature;
import org.onosproject.athena.feature.FlowStatisticsFeature;
import org.onosproject.athena.feature.PacketInFeature;
import org.onosproject.athena.feature.PortStatisticsFeature;
import org.onosproject.athena.feature.PortStatusFeature;
import org.onosproject.net.provider.AbstractProviderRegistry;
import org.onosproject.net.provider.AbstractProviderService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 8/26/15.
 */
@Component(immediate = true)
@Service
public class FeatureCollectorManager
        extends AbstractProviderRegistry<FeatureCollectorProvider, FeatureCollectorProviderService>
        implements FeatureCollectorService, FeatureCollectorProviderRegistry {

    protected Multimap<Integer, FeatureEventListener> featureEventListener
            = ArrayListMultimap.create();

    private final Logger log = getLogger(getClass());

    @Activate
    public void activate() {

        log.info("started");
    }

    @Deactivate
    public void deactivate() {

        log.info("stopped");
    }

    @Override
    protected FeatureCollectorProviderService createProviderService(FeatureCollectorProvider provider) {
        return new InternalFeatureCollectorProviderService(provider);
    }

    @Override
    public void addFeatureEventListener(int prioirity, FeatureEventListener listener) {
        featureEventListener.put(prioirity, listener);
    }

    @Override
    public void removeFeatureEventListener(FeatureEventListener listener) {
        featureEventListener.values().remove(listener);
    }

    private class InternalFeatureCollectorProviderService
            extends AbstractProviderService<FeatureCollectorProvider>
            implements FeatureCollectorProviderService {
        //use internal provider`s service

        InternalFeatureCollectorProviderService(FeatureCollectorProvider provider) {
            super(provider);
        }


        @Override
        public void packetInFeatureHandler(PacketInFeature packetInFeature) {

            for (FeatureEventListener f : featureEventListener.values()) {
                f.packetInFeatureProcess(FeatureType.ASYNCHRONOUS_EVENT,
                        FeatureCategory.ASYNCHRONOUS_PACKET_IN, packetInFeature);
            }
        }

        @Override

        public void flowRemovedHandler(FlowRemovedFeature flowRemovedFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.flowRemovedProcess(FeatureType.ASYNCHRONOUS_EVENT,
                        FeatureCategory.ASYNCHRONOUS_FLOW_REMOVED, flowRemovedFeature);
            }
        }

        @Override
        public void portStatusHandler(PortStatusFeature portStatusFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.portStatusProcess(FeatureType.ASYNCHRONOUS_EVENT,
                        FeatureCategory.ASYNCHRONOUS_PORT_STATUS, portStatusFeature);
            }

        }

        @Override
        public void errorMsgHandler(ErrorMessageFeature errorMessageFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.errorMsgProcess(FeatureType.ASYNCHRONOUS_EVENT,
                        FeatureCategory.ASYNCHRONOUS_ERROR_MSG, errorMessageFeature);
            }
        }

        @Override
        public void flowStatsHandler(FlowStatisticsFeature flowStatisticsFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.flowStatsProcess(FeatureType.SYNCHRONOUS_STATISTICS,
                        FeatureCategory.SYNCHRONOUS_FLOW_STATISTICS, flowStatisticsFeature);
            }
        }

        @Override
        public void aggregateStatsHandler(AggregateStatisticsFeature aggregateStatisticsFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.aggregateStatsProcess(FeatureType.SYNCHRONOUS_STATISTICS,
                        FeatureCategory.SYNCHRONOUS_AGGREGATE_STATISTICS, aggregateStatisticsFeature);
            }
        }

        @Override
        public void portStatsHandler(PortStatisticsFeature portStatisticsFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.portStatsProcess(FeatureType.SYNCHRONOUS_STATISTICS,
                        FeatureCategory.SYNCHRONOUS_PORT_STATISTICS, portStatisticsFeature);
            }
        }

        @Override
        public void queueStatsHandler(QueueStatisticsFeature queueStatisticsFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.queueStatsProcess(FeatureType.SYNCHRONOUS_STATISTICS,
                        FeatureCategory.SYNCHRONOUS_QUEUE_STATISTICS, queueStatisticsFeature);
            }
        }

        @Override
        public void tableStatsHandler(TableStatisticsFeature tableStatisticsFeature) {
            for (FeatureEventListener f : featureEventListener.values()) {
                f.tableStatsProcess(FeatureType.SYNCHRONOUS_STATISTICS,
                        FeatureCategory.SYNCHRONOUS_TABLE_STATISTICS, tableStatisticsFeature);
            }
        }
    }

}
