package athena.northbound.impl;

import athena.northbound.GUIInterfaceManager;
import athena.util.GraphGenerator;
import org.jfree.ui.RefineryUtilities;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaField;

/**
 * Implementation for GUIInterfaceManager.
 * Created by seunghyeon on 4/3/16.
 */
public class GUIInterfaceManagerImpl implements GUIInterfaceManager {
    String title = "Athena data analyzer";
    AthenaFeatures athenaFeatures;
    String targetFeature;

    /**
     * Construct with title, features, and y-axis.
     * @param title title of graph.
     * @param athenaFeatures A set of Athena features.
     * @param targetFeature Y-aixs value.
     */
    public GUIInterfaceManagerImpl(String title, AthenaFeatures athenaFeatures, String targetFeature) {
        this.title = title;
        this.athenaFeatures = athenaFeatures;
        this.targetFeature = targetFeature;
    }

    public GUIInterfaceManagerImpl() {
    }


    @Override
    public void setGraphParams(String title, AthenaFeatures athenaFeatures, AthenaField targetFeature) {
        this.title = title;
        this.athenaFeatures = athenaFeatures;
        this.targetFeature = targetFeature.getValue();

    }

    @Override
    public void showGraph() {
        final GraphGenerator demo = new GraphGenerator(title, athenaFeatures,
                targetFeature);
        demo.pack();
        RefineryUtilities.positionFrameRandomly(demo);
        demo.setVisible(true);
    }

}
