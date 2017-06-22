package athena.api;

import org.onosproject.athena.database.FeatureConstraint;

import java.io.Serializable;

/**
 * Created by seunghyeon on 5/3/16.
 */
public class ClassificationMarkingElement implements Serializable {
    String labelName;
    int label;
    FeatureConstraint featureConstraint = null;
    int srcIpMask = 0;
    int srcComparator = 0;
    int dstIpMask = 0;
    int dstComparator = 0;

    public ClassificationMarkingElement(String labelName, int label,
                                        FeatureConstraint featureConstraint) {
        this.labelName = labelName;
        this.label = label;
        this.featureConstraint = featureConstraint;
    }

    public ClassificationMarkingElement(String labelName, int label) {
        this.labelName = labelName;
        this.label = label;
    }

    public void setSrcMaskMarking(int mask, int comparator) {
        this.srcIpMask = mask;
        this.srcComparator = comparator;
    }

    public void setFeatureConstraint(FeatureConstraint featureConstraint) {
        this.featureConstraint = featureConstraint;
    }

    public int getSrcIpMask() {
        return srcIpMask;
    }

    public int getSrcComparator() {
        return srcComparator;
    }

    public int getDstIpMask() {
        return dstIpMask;
    }

    public int getDstComparator() {
        return dstComparator;
    }

    public FeatureConstraint getFeatureConstraint() {

        return featureConstraint;
    }

    public void setDstMaskMarking(int mask, int comparator) {

        this.dstIpMask = mask;
        this.dstComparator = comparator;

    }

    public String getLabelName() {
        return labelName;
    }

    public int getLabel() {
        return label;
    }
}
