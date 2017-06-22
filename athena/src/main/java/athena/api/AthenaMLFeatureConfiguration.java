package athena.api;

import org.apache.commons.collections.map.HashedMap;
import org.onosproject.athena.database.AthenaFeatureField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AthenaMLFeatureConfiguration pre-processes Athena features according to user-defined pre-processing parameters.
 * listOfTargetFeatures : candidate Athena features to be used for a feature of an anomlay detection.
 * Weigh: Wieght for each features.
 * Normalization: Flag for a normalization (p Normalization in L^p^ space, p = 2 by default.).
 * Sampling: Sampling the Athena features (0.0 - 100).
 * Created by seunghyeon on 4/7/16.
 */
public class AthenaMLFeatureConfiguration implements Serializable {

    // if non, all of entries in feature feilds are target entries
    List<AthenaFeatureField> listOfTargetFeatures = new ArrayList<>();

    Map<AthenaFeatureField, Integer> weight = new HashedMap();

    boolean normalization = false;
    double p = 2;

    //Percentage
    long samplingRate = 0;

    boolean absolute = false;

    public boolean isAbsolute() {
        return absolute;
    }

    public void setAbsolute(boolean absolute) {
        this.absolute = absolute;
    }
    //FeatureConstraint, (op)weight, (op)normalization, (op)samplingRate, (op)replication, (op)listofTargetFeatures

    public AthenaMLFeatureConfiguration() {
    }

    public void setListOfTargetFeatures(List<AthenaFeatureField> listOfTargetFeatures) {
        this.listOfTargetFeatures = listOfTargetFeatures;
    }

    public void setWeight(Map<AthenaFeatureField, Integer> weight) {
        this.weight = weight;
    }

    public void setNormalization(boolean normalization) {
        this.normalization = normalization;
    }

    public void setNormalizationWithP(boolean normalization, double p) {
        this.normalization = normalization;
        this.p = p;
    }


    public int numberOfTargetFeature() {
        return listOfTargetFeatures.size();
    }

    public int numberofWeightedFeature() {
        return weight.size();
    }

    public void addWeight(AthenaFeatureField athenaFeatureField, int weight) {
        this.weight.putIfAbsent(athenaFeatureField, weight);
    }

    public List<AthenaFeatureField> getListOfTargetFeatures() {
        return listOfTargetFeatures;
    }

    public Map<AthenaFeatureField, Integer> getWeight() {
        return weight;
    }

    public boolean isNormalization() {
        return normalization;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public long getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(long samplingRate) {
        this.samplingRate = samplingRate;
    }

    public void addTargetFeatures(AthenaFeatureField targetField) {
        listOfTargetFeatures.add(targetField);
    }

    public void setSampling(long samplingRate) {
        if (samplingRate > 0 && samplingRate < 100) {
            this.samplingRate = samplingRate;
        }
    }


}
