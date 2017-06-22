package org.onosproject.athena.feature;


import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by seunghyeon on 8/19/15.
 */
public class AggregateStatisticsFeature implements Feature {

    protected CopyOnWriteArrayList<Map.Entry<FeatureIndex, UnitAggregateStatistics>> featureData
            = new CopyOnWriteArrayList<>();

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.SYNCHRONOUS_STATISTICS;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.SYNCHRONOUS_AGGREGATE_STATISTICS;
    }

    @Override
    public String getDescription() {
        return "The aggregate statistics feature object";
    }

    public boolean addFeatureData(FeatureIndex fi, UnitAggregateStatistics uas) {
        if (fi == null || uas == null) {
            return false;
        }
        Map.Entry<FeatureIndex, UnitAggregateStatistics> data
                = new AbstractMap.SimpleEntry<>(fi, uas);
        featureData.add(data);
        return true;
    }

    public FeatureIndex getFeatureIndex(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }

        return featureData.get(index).getKey();
    }

    public UnitAggregateStatistics getUnitAggregateStatistics(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }

        return featureData.get(index).getValue();

    }

}