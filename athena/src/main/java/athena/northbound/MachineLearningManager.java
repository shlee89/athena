package athena.northbound;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.DetectionAlgorithm;
import athena.api.DetectionModel;
import athena.api.ValidationSummary;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.onlineMLEventListener;
import athena.util.ControllerConnector;
import athena.util.DatabaseConnector;
import org.onosproject.athena.database.FeatureConstraint;

/**
 * MachineLearningManager provides a set of APIs to perform an anomaly detection.
 * Created by seunghyeon on 4/7/16.
 */
public interface MachineLearningManager {

    void setArtifactId(String artifactId);

    void setMainClass(String mainClass);

    void setComputingClusterMasterIP(String computingClusterMasterIP);

    void setDatabaseConnector(DatabaseConnector databaseConnector);

    /**
     * Create detection model capable with Athena featuers.
     *
     * @param featureConstraint            User-defined constraints to load Athena feature entries from DB.
     * @param athenaMLFeatureConfiguration Pre-processing for Athena features (Weighting, sampling, ...).
     * @param detectionAlgorithm           Algorithm for a detection strategy (K-Menas, Naive-bayes, ...).
     * @param indexing                     A set of indexes for identifying unique entries(e.g, a set of Athen index fields).
     * @param marking                      Marking Athena features (e.g., malicious, benign).
     * @return DetectionModel capable with the parameters.
     */
    DetectionModel generateAthenaDetectionModel(FeatureConstraint featureConstraint,
                                                AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                DetectionAlgorithm detectionAlgorithm,
                                                Indexing indexing,
                                                Marking marking);

//    *
//     * Validate Athena features with DetectionModel.
//     *
//     * @param featureConstraint            User-defined constraints to load Athena feature entries from DB.
//     * @param athenaMLFeatureConfiguration Pre-processing for Athena features. (Weighting, sampling, ...)
//     * @param detectionModel               DetectionModel.
//     * @param validationParams             Additional options, while validating.
//     * @return Summerization of validation.
//
//    ValidationSummary validateAthenaFeatures(
//                                             FeatureConstraint featureConstraint,
//                                             AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
//                                             DetectionModel detectionModel,
//                                             ValidationParams validationParams);

    /**
     * Validate Athena features with DetectionModel.
     *
     * @param featureConstraint            User-defined constraints to load Athena feature entries from DB.
     * @param athenaMLFeatureConfiguration Pre-processing for Athena features. (Weighting, sampling, ...)
     * @param detectionModel               DetectionModel.
     * @param indexing                     A set of indexes for identifying unique entries(e.g, a set of Athen index fields).
     * @param marking                      Marking Athena features (e.g., malicious, benign).
     * @return Summerization of validation.
     */
    ValidationSummary validateAthenaFeatures(FeatureConstraint featureConstraint,
                                             AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                             DetectionModel detectionModel,
                                             Indexing indexing,
                                             Marking marking);


    /**
     * Save DetectionModel to a persistent storage.
     *
     * @param detectionModel DetectionModel.
     * @param path           Path.
     */
    void saveDetectionModel(DetectionModel detectionModel, String path);

    /**
     * Load DetectionModel from a persistent storage.
     *
     * @param path path.
     * @return DetectionModel
     */
    DetectionModel loadDetectionModel(String path);

    /**
     * Register OnlineValidator.
     *
     * @param featureConstraint            User-defined constraints to load Athena feature entries from DB.
     * @param athenaMLFeatureConfiguration Pre-processing for Athena features (Weighting, sampling, ...).
     * @param detectionModel               DetectionModel.
     * @param listener                     event listener of ML events.
     */
    void registerOnlineValidation(FeatureConstraint featureConstraint,
                                  AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                  DetectionModel detectionModel,
                                  onlineMLEventListener listener,
                                  ControllerConnector controllerConnector);

    /**
     * Unregister OnlineValidator.
     *
     * @param listener registered event listener.
     */
    void unRegisterOnlineValidation(onlineMLEventListener listener);

}