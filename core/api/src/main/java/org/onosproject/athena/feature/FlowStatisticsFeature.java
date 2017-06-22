package org.onosproject.athena.feature;



import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by seunghyeon on 8/18/15.
 */
public class FlowStatisticsFeature implements Feature {


    protected CopyOnWriteArrayList<Map.Entry<FeatureIndex, UnitFlowStatistics>> featureData
            = new CopyOnWriteArrayList<>();


    @Override
    public FeatureType getFeatureType() {
        return FeatureType.SYNCHRONOUS_STATISTICS;
    }

    @Override
    public FeatureCategory getFeatureCategory() {
        return FeatureCategory.SYNCHRONOUS_FLOW_STATISTICS;
    }

    @Override
    public String getDescription() {
        return "The flow statistics feature object";
    }


    public boolean addFeatureData(FeatureIndex fi, UnitFlowStatistics ufs) {
        if (fi == null || ufs == null) {
            return false;
        }
        Map.Entry<FeatureIndex, UnitFlowStatistics> data
                = new AbstractMap.SimpleEntry<>(fi, ufs);
        featureData.add(data);
        return true;
    }

    public FeatureIndex getFeatureIndex(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }

        return featureData.get(index).getKey();
    }

    public UnitFlowStatistics getUnitFlowStatistics(int index) {
        if ((featureData.size() - 1) < index || index < -1) {
            return null;
        }

        return featureData.get(index).getValue();

    }

    //By Jinwoo
    @Override
    public String toString() {
        return "FlowStatisticsFeature [featureData=" + featureData + "]";
    }
}
