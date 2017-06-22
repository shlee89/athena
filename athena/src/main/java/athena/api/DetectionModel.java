package athena.api;

/**
 * Interface for DetectionModel.
 * Created by seunghyeon on 4/7/16.
 */
public interface DetectionModel {
    /**
     * Type of an algorithm
     *
     * @return Type of an algorithm.
     */
    DetectionAlgorithmType getDetectionAlgorithmType();

    /**
     * A name of algorithm.
     *
     * @return A name of algirithm.
     */
    DetectionStrategy getDetectionStrategy();

    /**
     * Summarization of the model.
     *
     * @return Summerization of the model.
     */
    Summary getSummary();

    /**
     * Detection model
     *
     * @return Detection model.
     */
    Object getDetectionModel();

    /**
     * Return detection algorithms.
     *
     * @return detection algorithm.
     */
    DetectionAlgorithm getDetectionAlgorithm();

    /**
     *
     * @return
     */
    AthenaMLFeatureConfiguration getAthenaMLFeatureConfiguration();
}
