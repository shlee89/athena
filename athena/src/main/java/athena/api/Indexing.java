package athena.api;

import org.onosproject.athena.database.AthenaIndexField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Indexing specifies a set of Athena indexes to aggregate Athena features.
 * This is used to derive unique entries of total Athena entries.
 * listOfTargetFeatures: An aggregation features.
 * Created by seunghyeon on 4/7/16.
 */
public class Indexing implements Serializable {
    List<AthenaIndexField> listOfTargetFeatures;

    /**
     * Construct with no parameters.
     */
    public Indexing() {
        this.listOfTargetFeatures = new ArrayList<>();
    }

    /**
     * Add a Athena index.
     *
     * @param athenaIndexField Athena index.
     */
    public void addIndexingElements(AthenaIndexField athenaIndexField) {
        this.listOfTargetFeatures.add(athenaIndexField);
    }

    /**
     * Get a set of Athena indexes.
     *
     * @return List of Athena indexes.
     */
    public List<AthenaIndexField> getListOfTargetFeatures() {
        return listOfTargetFeatures;
    }
}
