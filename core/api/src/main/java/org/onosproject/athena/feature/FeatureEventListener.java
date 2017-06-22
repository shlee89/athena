package org.onosproject.athena.feature;

/**
 * Created by seunghyeon on 8/27/15.
 */
public interface FeatureEventListener {
    /**
     * Packet_In feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param packetInFeature feature
     */
    void packetInFeatureProcess(FeatureType featureType,
                                FeatureCategory featureCategory, PacketInFeature packetInFeature);

    /**
     * Flow Removed feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param flowRemovedFeature feature
     */
    void flowRemovedProcess(FeatureType featureType,
                            FeatureCategory featureCategory, FlowRemovedFeature flowRemovedFeature);

    /**
     * Port Status feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param portStatusFeature feature
     */
    void portStatusProcess(FeatureType featureType,
                           FeatureCategory featureCategory, PortStatusFeature portStatusFeature);

    /**
     * Error Message feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param errorMessageFeature feature
     */
    void errorMsgProcess(FeatureType featureType,
                         FeatureCategory featureCategory, ErrorMessageFeature errorMessageFeature);

    /**
     *  Flow statistics feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param flowStatisticsFeature feature
     */
    void flowStatsProcess(FeatureType featureType,
                          FeatureCategory featureCategory, FlowStatisticsFeature flowStatisticsFeature);

    /**
     *  Aggregate statistics feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param aggregateStatisticsFeature feature
     */
    void aggregateStatsProcess(FeatureType featureType,
                               FeatureCategory featureCategory, AggregateStatisticsFeature aggregateStatisticsFeature);

    /**
     *  Port statistics feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param portStatisticsFeature feature
     */
    void portStatsProcess(FeatureType featureType,
                          FeatureCategory featureCategory, PortStatisticsFeature portStatisticsFeature);

    /**
     *  Queue statistics feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param queueStatisticsFeature feature
     */
    void queueStatsProcess(FeatureType featureType,
                           FeatureCategory featureCategory, QueueStatisticsFeature queueStatisticsFeature);

    /**
     *  Table statistics feature handler called by provider service.
     *
     * @param featureType typeOfFeature
     * @param featureCategory category of feature
     * @param tableStatisticsFeature feature
     */
    void tableStatsProcess(FeatureType featureType,
                           FeatureCategory featureCategory, TableStatisticsFeature tableStatisticsFeature);
}
