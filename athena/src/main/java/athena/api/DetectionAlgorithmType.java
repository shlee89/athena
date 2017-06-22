package athena.api;

/**
 * This enum value represents a type of algorithm, which is used for detecting malicious Athena entries.
 * Created by seunghyeon on 4/7/16.
 */
public enum DetectionAlgorithmType {
    /**
     * The classification machine learning algorithms
     */
    CLASSIFICATION,

    /**
     * The clustering machine learning algorithms
     */
    CLUSTERING,
    /**
     * Regression is not suitable to classify an attack. In initial version of Athena, we doesn`t care about that.
     */
    REGRESSION,
    /**
     * Boosting.
     */
    BOOSTING
}
