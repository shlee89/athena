package org.onosproject.athena.database;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seunghyeon on 10/1/15.
 */
public class SortedUserDefinedFeatures {
    private List<String> listOfFeatures = new ArrayList<>();
    private int startingPointOfIndexFeature = 1;
    private int startingPointOfFeature = 1;
    private boolean isHasRichFeature = false;
    private boolean isHasFeature = false;

    public List<String> getListOfFeatures() {
        return listOfFeatures;
    }

    public int getStartingPointOfIndexFeature() {
        return startingPointOfIndexFeature;
    }

    public int getStartingPointOfFeature() {
        return startingPointOfFeature;
    }

    public void setStartingPointOfFeature(int startingPointOfFeature) {
        this.startingPointOfFeature = startingPointOfFeature;
    }

    public void setIsHasFeature(boolean isHasFeature) {
        this.isHasFeature = isHasFeature;
    }

    public boolean isHasFeature() {

        return isHasFeature;
    }

    public boolean isHasRichFeature() {
        return isHasRichFeature;
    }

    public void setListOfFeatures(List<String> listOfFeatures) {
        this.listOfFeatures = listOfFeatures;
    }

    public void setStartingPointOfIndexFeature(int startingPointOfIndexFeature) {
        this.startingPointOfIndexFeature = startingPointOfIndexFeature;
    }

    public void setIsHasRichFeature(boolean isHasRichFeature) {
        this.isHasRichFeature = isHasRichFeature;
    }

    public void addFeatures(String str) {
        listOfFeatures.add(str);
    }

    public void addStartingPointCount() {
        startingPointOfFeature++;
    }
}
