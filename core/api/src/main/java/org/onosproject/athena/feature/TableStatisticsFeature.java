package org.onosproject.athena.feature;



import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by seunghyeon on 8/19/15.
 */
public class TableStatisticsFeature implements Feature {
    protected CopyOnWriteArrayList<Map.Entry<FeatureIndex, UnitTableStatistics>> featureData
            = new CopyOnWriteArrayList<>();


    @Override
    public FeatureType getFeatureType() {
        return FeatureType.SYNCHRONOUS_STATISTICS;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.SYNCHRONOUS_TABLE_STATISTICS;
    }

    @Override
    public String getDescription() {
        return "The table statistics feature object";
    }

    public boolean addFeatureData(FeatureIndex fi, UnitTableStatistics uts) {
        if (fi == null || uts == null) {
            return false;
        }
        Map.Entry<FeatureIndex, UnitTableStatistics> data
                = new AbstractMap.SimpleEntry<>(fi, uts);
        featureData.add(data);
        return true;
    }

    public FeatureIndex getFeatureIndex(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }
        return featureData.get(index).getKey();
    }

    public UnitTableStatistics getUnitTableStatistics(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }

        return featureData.get(index).getValue();
    }
}
