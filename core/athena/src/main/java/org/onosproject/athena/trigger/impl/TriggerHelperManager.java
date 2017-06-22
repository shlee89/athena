package org.onosproject.athena.trigger.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.athena.feature.QueueStatisticsFeature;
import org.onosproject.athena.feature.TableStatisticsFeature;
import org.onosproject.athena.trigger.TriggerHelperEntry;
import org.onosproject.athena.trigger.TriggerHelperProvider;
import org.onosproject.athena.trigger.TriggerHelperProviderService;
import org.onosproject.core.ApplicationId;
import org.onosproject.athena.feature.AggregateStatisticsFeature;
import org.onosproject.athena.feature.ErrorMessageFeature;
import org.onosproject.athena.feature.Feature;
import org.onosproject.athena.feature.FeatureCategory;
import org.onosproject.athena.feature.FeatureIndex;
import org.onosproject.athena.feature.FeatureType;
import org.onosproject.athena.feature.FlowRemovedFeature;
import org.onosproject.athena.feature.FlowStatisticsFeature;
import org.onosproject.athena.feature.PacketInFeature;
import org.onosproject.athena.feature.PortStatisticsFeature;
import org.onosproject.athena.feature.PortStatusFeature;
import org.onosproject.athena.trigger.SimpleTriggerHelperRequester;
import org.onosproject.athena.trigger.TriggerHelperEventListener;
import org.onosproject.athena.trigger.TriggerHelperProviderRegistry;
import org.onosproject.athena.trigger.TriggerHelperService;
import org.onosproject.athena.trigger.TriggerOnlineEventTable;
import org.onosproject.net.provider.AbstractProviderRegistry;
import org.onosproject.net.provider.AbstractProviderService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 11/4/15.
 */
@Component(immediate = true)
@Service
public class TriggerHelperManager
        extends AbstractProviderRegistry<TriggerHelperProvider, TriggerHelperProviderService>
        implements TriggerHelperService, TriggerHelperProviderRegistry {
    private final Logger log = getLogger(getClass());

    protected Multimap<Integer, TriggerHelperEventListener> triggerHelperListener
            = ArrayListMultimap.create();

    private List<TriggerOnlineEventTable> triggerOnlineEventTableList = new ArrayList<>();


    @Override
    protected TriggerHelperProviderService createProviderService(TriggerHelperProvider provider) {
        return new InternalTriggerHelperProviderService(provider);
    }

    @Activate
    public void activate() {
        log.info("started - TriggerHelperManager");

    }

    @Deactivate
    public void deactivate() {
        log.info("Stopped");
    }

    @Override
    public void addTriggerHelperEventListener(int priority, TriggerHelperEventListener listener) {
        triggerHelperListener.put(priority, listener);
    }

    @Override
    public void removeTriggerHelperEventListener(TriggerHelperEventListener listener) {
        triggerHelperListener.values().remove(listener);
    }

    @Override
    public boolean registerOnlineFeature(ApplicationId applicationId, SimpleTriggerHelperRequester requester) {
        if (applicationId == null) {
            log.warn("applicationId could not be null");
            return false;
        }

        if (requester == null) {
            log.warn("SimpleTriggerHelperRequester could not be null");
            return false;
        }

        TriggerOnlineEventTable triggeredOnlineEventTable =
                new TriggerOnlineEventTable(applicationId, requester.getRequestFeatures());

        triggerOnlineEventTableList.add(triggeredOnlineEventTable);

        return true;

    }

    @Override
    public boolean unRegisterOnlineFeature(ApplicationId applicationId) {

        TriggerOnlineEventTable triggerOnlineEventTable;

        for (int i = 0; i < triggerOnlineEventTableList.size(); i++) {
            triggerOnlineEventTable = triggerOnlineEventTableList.get(i);

            if (triggerOnlineEventTable.getApplicationId() == applicationId) {
                triggerOnlineEventTableList.remove(i);
                return true;
            }

        }

        return false;
    }


    private class InternalTriggerHelperProviderService extends AbstractProviderService<TriggerHelperProvider>
            implements TriggerHelperProviderService {


        InternalTriggerHelperProviderService(TriggerHelperProvider provider) {
            super(provider);
        }

        @Override
        public void featureHandler(FeatureType featureType, FeatureCategory featureCategory, Feature feature) {

            //Store features to persistent storage
            switch (featureCategory) {
                case ASYNCHRONOUS_PACKET_IN:
                    checkTriggerFromPacketIn(featureType, featureCategory,
                            (PacketInFeature) feature);
                    break;

                case SYNCHRONOUS_FLOW_STATISTICS:
                    checkTriggerFromFlowStatistics(featureType, featureCategory,
                            (FlowStatisticsFeature) feature);
                    break;

                case SYNCHRONOUS_PORT_STATISTICS:
                    checkTriggerFromPortStatistics(featureType, featureCategory,
                            (PortStatisticsFeature) feature);
                    break;

                case ASYNCHRONOUS_PORT_STATUS:
                    checkTriggerFromPortStatus(featureType, featureCategory,
                            (PortStatusFeature) feature);
                    break;

                case SYNCHRONOUS_AGGREGATE_STATISTICS:
                    checkTriggerFromAggregatedStatistics(featureType, featureCategory,
                            (AggregateStatisticsFeature) feature);
                    break;

                case ASYNCHRONOUS_ERROR_MSG:
                    break;

                case SYNCHRONOUS_QUEUE_STATISTICS:
                    break;

                case SYNCHRONOUS_TABLE_STATISTICS:
                    break;

                case ASYNCHRONOUS_FLOW_REMOVED:
                    checkTriggerFromFlowRemoved(featureType, featureCategory,
                            (FlowRemovedFeature) feature);
                    break;

                default:
                    break;
            }

        }

    }

    private void checkTriggerFromPacketIn(FeatureType featureType,
                                          FeatureCategory featureCategory,
                                          PacketInFeature feature) {
        //Set index to Entry
        FeatureIndex fi = feature.getFeatureindex();
        TriggerHelperEntry newEntry = new TriggerHelperEntry();
        newEntry.setSwitchDatapathId(fi.getSwitchDatapathId());
        newEntry.setSwitchPortId(fi.getSwitchPortId());


    }

    private void checkTriggerFromFlowStatistics(FeatureType featureType,
                                                FeatureCategory featureCategory,
                                                FlowStatisticsFeature feature) {



    }

    private void checkTriggerFromPortStatistics(FeatureType featureType,
                                                FeatureCategory featureCategory,
                                                PortStatisticsFeature feature) {

    }

    private void checkTriggerFromPortStatus(FeatureType featureType,
                                            FeatureCategory featureCategory,
                                            PortStatusFeature feature) {
        //Set index to Entry
        FeatureIndex fi = feature.getFeatureindex();
        TriggerHelperEntry newEntry = new TriggerHelperEntry();
        newEntry.setSwitchDatapathId(fi.getSwitchDatapathId());

    }

    private void checkTriggerFromErrorMsg(FeatureType featureType,
                                          FeatureCategory featureCategory,
                                          ErrorMessageFeature feature) {

    }

    private void checkTriggerFromFlowRemoved(FeatureType featureType,
                                             FeatureCategory featureCategory,
                                             FlowRemovedFeature feature) {

    }


    private void checkTriggerFromQueueStatistics(FeatureType featureType,
                                                 FeatureCategory featureCategory,
                                                 QueueStatisticsFeature feature) {

    }

    private void checkTriggerFromTableStatistics(FeatureType featureType,
                                                 FeatureCategory featureCategory,
                                                 TableStatisticsFeature feature) {

    }

    private void checkTriggerFromAggregatedStatistics(FeatureType featureType,
                                                      FeatureCategory featureCategory,
                                                      AggregateStatisticsFeature feature) {

    }


}
