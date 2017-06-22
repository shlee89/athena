package athena.northbound;

import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaField;

/**
 * GUIInterfaceManager shows Athena events with graph interface.
 * Created by seunghyeon on 4/7/16.
 */
public interface GUIInterfaceManager {

    /**
     * Set params for components of graph.
     *
     * @param title          Title of graph.
     * @param athenaFeatures A set of Athena features.
     * @param targetFeature  Y-axis value.
     */
    void setGraphParams(String title, AthenaFeatures athenaFeatures, AthenaField targetFeature);

    /**
     * Display graph.
     */
    void showGraph();

}
