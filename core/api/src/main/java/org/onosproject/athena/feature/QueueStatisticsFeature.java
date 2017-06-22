package org.onosproject.athena.feature;


import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by seunghyeon on 8/19/15.
 */
public class QueueStatisticsFeature implements Feature {
    protected CopyOnWriteArrayList<Map.Entry<FeatureIndex, UnitQueueStatistics>> featureData
            = new CopyOnWriteArrayList<>();

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.SYNCHRONOUS_STATISTICS;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.SYNCHRONOUS_QUEUE_STATISTICS;
    }

    @Override
    public String getDescription() {
        return "The queue statistics feature object";
    }


    public boolean addFeatureData(FeatureIndex fi, UnitQueueStatistics uqs) {
        if (fi == null || uqs == null) {
            return false;
        }
        Map.Entry<FeatureIndex, UnitQueueStatistics> data
                = new AbstractMap.SimpleEntry<>(fi, uqs);
        featureData.add(data);
        return true;
    }

    public FeatureIndex getFeatureIndex(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }
        return featureData.get(index).getKey();
    }

    public UnitQueueStatistics getUnitQueueStatistics(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }
        return featureData.get(index).getValue();
    }
}
