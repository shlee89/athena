package athena.api;

/**
 * DetectionAlgorithm is an interface of each detection algorithms.
 * Each detection algorithms receive paramters to build up a detection model.
 * Created by seunghyeon on 4/7/16.
 */
public interface DetectionAlgorithm {
    DetectionAlgorithmType getDetectionAlgorithmType();

    DetectionStrategy getDetectionStrategy();

}
