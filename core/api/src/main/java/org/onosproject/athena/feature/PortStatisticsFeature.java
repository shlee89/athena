package org.onosproject.athena.feature;



import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by seunghyeon on 8/19/15.
 */
public class PortStatisticsFeature implements Feature {

    protected CopyOnWriteArrayList<Map.Entry<FeatureIndex, UnitPortStatistics>> featureData
            = new CopyOnWriteArrayList<>();

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.SYNCHRONOUS_STATISTICS;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.SYNCHRONOUS_PORT_STATISTICS;
    }

    @Override
    public String getDescription() {
        return "The port statistics feature object";
    }

    public boolean addFeatureData(FeatureIndex fi, UnitPortStatistics ups) {
        if (fi == null || ups == null) {
            return false;
        }
        Map.Entry<FeatureIndex, UnitPortStatistics> data
                = new AbstractMap.SimpleEntry<>(fi, ups);
        featureData.add(data);
        return true;
    }

    public FeatureIndex getFeatureIndex(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }
        return featureData.get(index).getKey();
    }

    public UnitPortStatistics getUnitPortStatistics(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }

        return featureData.get(index).getValue();
    }
}
