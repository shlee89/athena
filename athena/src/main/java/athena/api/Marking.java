package athena.api;

import org.bson.BSONObject;
import org.bson.Document;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.impl.FeatureDatabaseMgmtManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Marking specifies which feature has what characteristics. In generic anomaly detection task, it could be specified as a malicious and a benign.
 * In prototype implementation, we just cover just one Marking elemenets. True represents a compartible with marked elements and it is malicious entry.
 * And, we cannot use CIDR based featureConstraint and unified Athena Feautre constraint due to its implementation. This also could be improved later.
 * <p>
 * If a target value satised with Feature constraint, it will marked as...
 * Created by seunghyeon on 4/7/16.
 */
public class Marking implements Serializable {

    /////////////////////////////////////////this is for clustering////////////////////////////////////
    FeatureConstraint featureConstraint = null;
    int srcIpMask = 0;
    int srcComparator = 0;
    int dstIpMask = 0;
    int dstComparator = 0;

    public FeatureDatabaseMgmtManager featureDatabaseMgmtManager = new FeatureDatabaseMgmtManager();

    public Marking() {
        ClassificationMarkingElement classificationMarkingElement =
                new ClassificationMarkingElement("Benign", listofClassificationMarkingElement.size());
        listofClassificationMarkingElement.add(classificationMarkingElement);
    }

    public void setSrcMaskMarking(int mask, int comparator) {
        this.srcIpMask = mask;
        this.srcComparator = comparator;
    }

    public void setDstMaskMarking(int mask, int comparator) {
        this.dstIpMask = mask;
        this.dstComparator = comparator;
    }

    public Marking(FeatureConstraint featureConstraint) {
        this.featureConstraint = featureConstraint;
    }

    public FeatureConstraint getFeatureConstraint() {
        return featureConstraint;
    }

    public boolean checkElements(BSONObject index, BSONObject feature) {

        if (srcIpMask == 0 && dstIpMask == 0) {
            //marking according to featureConstraint
            return featureDatabaseMgmtManager.isSatisfyOnlineEvent(
                    (Document) index,
                    (Document) feature,
                    featureConstraint);
        }

        if (srcIpMask != 0 && index.get(AthenaIndexField.MATCH_IPV4_SRC) != null ) {
            int ipsrc = (Integer) index.get(AthenaIndexField.MATCH_IPV4_SRC);
            if ((ipsrc & this.srcIpMask) == srcComparator) {
                return true;
            }
        }

        if (dstIpMask != 0 && index.get(AthenaIndexField.MATCH_IPV4_DST) != null) {
            int ipdst = (Integer) index.get(AthenaIndexField.MATCH_IPV4_DST);
            if ((ipdst & this.dstIpMask) == dstComparator) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////this is for classification////////////////////////////////////
    List<ClassificationMarkingElement> listofClassificationMarkingElement = new ArrayList<>();

    public void setSrcLabeledMarking(String labelName, int mask, int comparator) {
        ClassificationMarkingElement classificationMarkingElement = new ClassificationMarkingElement(labelName, listofClassificationMarkingElement.size());
        classificationMarkingElement.setSrcMaskMarking(mask, comparator);
        this.listofClassificationMarkingElement.add(classificationMarkingElement);
    }

    public void setDstLabeledMarking(String labelName, int mask, int comparator) {
        ClassificationMarkingElement classificationMarkingElement = new ClassificationMarkingElement(labelName, listofClassificationMarkingElement.size());
        classificationMarkingElement.setDstMaskMarking(mask, comparator);
        this.listofClassificationMarkingElement.add(classificationMarkingElement);
    }

    public void setFeatureConstraint(String labelName, FeatureConstraint featureConstraint) {
        ClassificationMarkingElement classificationMarkingElement = new ClassificationMarkingElement(labelName, listofClassificationMarkingElement.size());
        classificationMarkingElement.setFeatureConstraint(featureConstraint);
        this.listofClassificationMarkingElement.add(classificationMarkingElement);
    }

    public String converLableToLableName(int label){
        try {
            return listofClassificationMarkingElement.get(label).getLabelName();
        }catch (Exception e){
            System.out.println(e.toString());
            return "outOfIndex";
        }
    }

    public int checkClassificationMarkingElements(BSONObject index, BSONObject feature) {
        ClassificationMarkingElement classificationMarkingElement;
        boolean check = false;
        for (int i = 1 ; i < listofClassificationMarkingElement.size() ; i++){
            classificationMarkingElement = listofClassificationMarkingElement.get(i);
            check = checkElements(index,feature,
                    classificationMarkingElement.featureConstraint,
                    classificationMarkingElement.getSrcIpMask(),
                    classificationMarkingElement.getSrcComparator(),
                    classificationMarkingElement.getDstIpMask(),
                    classificationMarkingElement.getDstComparator());

            if(check){
                return classificationMarkingElement.getLabel();
            }
        }
        return 0;
    }

    public boolean checkElements(BSONObject index,
                                 BSONObject feature,
                                 FeatureConstraint featureConstraint,
                                 int srcIpMask,
                                 int srcComparator,
                                 int dstIpMask,
                                 int dstComparator) {

        if (srcIpMask == 0 && dstIpMask == 0) {
            //marking according to featureConstraint
            return featureDatabaseMgmtManager.isSatisfyOnlineEvent(
                    (Document) index,
                    (Document) feature,
                    featureConstraint);
        }

        if (srcIpMask != 0) {
            int ipsrc = (Integer) index.get(AthenaIndexField.MATCH_IPV4_SRC);
            if ((ipsrc & srcIpMask) == srcComparator) {
                return true;
            }
        }

        if (dstIpMask != 0) {
            int ipdst = (Integer) index.get(AthenaIndexField.MATCH_IPV4_DST);
            if ((ipdst & dstIpMask) == dstComparator) {
                return true;
            }
        }
        return false;
    }

    public int numberOfMarkingElements(){
        return listofClassificationMarkingElement.size();
    }

}
