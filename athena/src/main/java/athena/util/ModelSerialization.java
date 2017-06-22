package athena.util;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.Boosting.GradientBoostedTrees.GradientBoostedTreesDetectionModel;
import athena.api.ClassificationMarkingElement;
import athena.api.DetectionModel;
import athena.api.Indexing;
import athena.api.Marking;
import athena.api.classification.DecisionTree.DecisionTreeDetectionModel;
import athena.api.classification.LogisticRegression.LogisticRegressionDetectionModel;
import athena.api.classification.NaiveBayes.NaiveBayesDetectionModel;
import athena.api.classification.RandomForest.RandomForestDetectionModel;
import athena.api.classification.SVM.SVMDetectionModel;
import athena.api.clustering.ClusterModelSummary;
import athena.api.clustering.gaussianMixture.GaussianMixtureDetectionModel;
import athena.api.clustering.kmeans.KMeansDetectionAlgorithm;
import athena.api.clustering.kmeans.KMeansDetectionModel;
import athena.api.clustering.kmeans.KmeansModelSummary;
import athena.api.regression.Lasso.LassoDetectionModel;
import athena.api.regression.LinearRegression.LinearRegressionDetectionModel;
import athena.api.regression.RidgeRegression.RidgeRegressionDetectionModel;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.Accumulator;
import org.objenesis.strategy.SerializingInstantiatorStrategy;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaField;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.athena.database.impl.FeatureDatabaseMgmtManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by seungyheon on 7/20/16.
 */
public class ModelSerialization {

    String serializedPath = null;
    String storedPath = null;

    Kryo kryo = null;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public ModelSerialization() {
        kryo = this.getKryoObject();
        serializedPath = "./AthenaModel.";
    }

/*
    public boolean saveModelToFile(DetectionModel detectionModel, String path) {
        if (detectionModel == null) {
            log.warn("[ModelSerialization][saveModelToFile] detectionmodel MUST not be null");
            return false;
        }

        if (path != null) {
            serializedPath = path;
            System.out.println("[ModelSerialization][saveModelToFile] Set default path as :" + serializedPath);
        }

        Output output;
        try {
            storedPath = serializedPath;

            if (detectionModel instanceof RidgeRegressionDetectionModel) {
                storedPath = storedPath + "RidgeRegressionDetectionModel";
            } else if (detectionModel instanceof LinearRegressionDetectionModel) {
                storedPath = storedPath + "LinearRegressionDetectionModel";
            } else if (detectionModel instanceof LassoDetectionModel) {
                storedPath = storedPath + "LassoDetectionModel";
            } else if (detectionModel instanceof KMeansDetectionModel) {
                storedPath = storedPath + "KMeansDetectionModel";
            } else if (detectionModel instanceof GaussianMixtureDetectionModel) {
                storedPath = storedPath + "GaussianMixtureDetectionModel";
            } else if (detectionModel instanceof SVMDetectionModel) {
                storedPath = storedPath + "SVMDetectionModel";
            } else if (detectionModel instanceof RandomForestDetectionModel) {
                storedPath = storedPath + "RandomForestDetectionModel";
            } else if (detectionModel instanceof NaiveBayesDetectionModel) {
                storedPath = storedPath + "NaiveBayesDetectionModel";
            } else if (detectionModel instanceof LogisticRegressionDetectionModel) {
                storedPath = storedPath + "LogisticRegressionDetectionModel";
            } else if (detectionModel instanceof DecisionTreeDetectionModel) {
                storedPath = storedPath + "DecisionTreeDetectionModel";
            } else if (detectionModel instanceof GradientBoostedTreesDetectionModel) {
                storedPath = storedPath + "GradientBoostedTreesDetectionModel";
            } else {
                log.warn("Not supported algorithm");
                return false;
            }
            FileOutputStream fout = new FileOutputStream(storedPath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(detectionModel);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public DetectionModel loadModelFromFile(String path) {
        if (path == null) {
            log.warn("[ModelSerialization][loadModelFromFile]Path MUST not be null");
            return null;
        }

        if (path != null) {
            serializedPath = path;
        }
        log.info("[ModelSerialization][loadModelFromFile] Set path as :" + serializedPath);

        DetectionModel detectionModel = null;

        try {

            FileInputStream fin = new FileInputStream(serializedPath);
            ObjectInputStream ois = new ObjectInputStream(fin);

            if (path.endsWith("RidgeRegressionDetectionModel")) {
                return (RidgeRegressionDetectionModel) ois.readObject();
            } else if (path.endsWith("LinearRegressionDetectionModel")) {
                return (LinearRegressionDetectionModel) ois.readObject();

            } else if (path.endsWith("LassoDetectionModel")) {
                return (LassoDetectionModel) ois.readObject();

            } else if (path.endsWith("KMeansDetectionModel")) {
                return (KMeansDetectionModel) ois.readObject();

            } else if (path.endsWith("GaussianMixtureDetectionModel")) {
                return (GaussianMixtureDetectionModel) ois.readObject();

            } else if (path.endsWith("SVMDetectionModel")) {
                return (SVMDetectionModel) ois.readObject();

            } else if (path.endsWith("RandomForestDetectionModel")) {
                return (RandomForestDetectionModel) ois.readObject();

            } else if (path.endsWith("NaiveBayesDetectionModel")) {
                return (NaiveBayesDetectionModel) ois.readObject();

            } else if (path.endsWith("LogisticRegressionDetectionModel")) {
                return (LogisticRegressionDetectionModel) ois.readObject();

            } else if (path.endsWith("DecisionTreeDetectionModel")) {
                return (DecisionTreeDetectionModel) ois.readObject();

            } else if (path.endsWith("GradientBoostedTreesDetectionModel")) {
                return (GradientBoostedTreesDetectionModel) ois.readObject();

            } else {
                log.warn("Not supported algorithm");
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
*/

    public boolean saveModelToFile(DetectionModel detectionModel, String path) {
        if (detectionModel == null) {
            log.warn("[ModelSerialization][saveModelToFile] detectionmodel MUST not be null");
            return false;
        }

        if (path != null) {
            serializedPath = path;
            System.out.println("[ModelSerialization][saveModelToFile] Set default path as :" + serializedPath);
        }

        Output output;
        try {
            storedPath = serializedPath;

            if (detectionModel instanceof RidgeRegressionDetectionModel) {
                storedPath = storedPath + "RidgeRegressionDetectionModel";
            } else if (detectionModel instanceof LinearRegressionDetectionModel) {
                storedPath = storedPath + "LinearRegressionDetectionModel";
            } else if (detectionModel instanceof LassoDetectionModel) {
                storedPath = storedPath + "LassoDetectionModel";
            } else if (detectionModel instanceof KMeansDetectionModel) {
                storedPath = storedPath + "KMeansDetectionModel";
            } else if (detectionModel instanceof GaussianMixtureDetectionModel) {
                storedPath = storedPath + "GaussianMixtureDetectionModel";
            } else if (detectionModel instanceof SVMDetectionModel) {
                storedPath = storedPath + "SVMDetectionModel";
            } else if (detectionModel instanceof RandomForestDetectionModel) {
                storedPath = storedPath + "RandomForestDetectionModel";
            } else if (detectionModel instanceof NaiveBayesDetectionModel) {
                storedPath = storedPath + "NaiveBayesDetectionModel";
            } else if (detectionModel instanceof LogisticRegressionDetectionModel) {
                storedPath = storedPath + "LogisticRegressionDetectionModel";
            } else if (detectionModel instanceof DecisionTreeDetectionModel) {
                storedPath = storedPath + "DecisionTreeDetectionModel";
            } else if (detectionModel instanceof GradientBoostedTreesDetectionModel) {
                storedPath = storedPath + "GradientBoostedTreesDetectionModel";
            } else {
                log.warn("Not supported algorithm");
                return false;
            }
            output = new Output(new FileOutputStream(storedPath));
            kryo.writeClassAndObject(output, detectionModel);
            System.out.println("[ModelSerialization][saveModelToFile] Model saved:" + storedPath);

            output.flush();
            output.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DetectionModel loadModelFromFile(String path) {
        if (path == null) {
            log.warn("[ModelSerialization][loadModelFromFile]Path MUST not be null");
            return null;
        }

        if (path != null) {
            serializedPath = path;
        }
        log.info("[ModelSerialization][loadModelFromFile] Set path as :" + serializedPath);

        DetectionModel detectionModel = null;

        try {
            Input input = new Input(new FileInputStream(serializedPath));
            if (path.endsWith("RidgeRegressionDetectionModel")) {
                return (RidgeRegressionDetectionModel) kryo.readClassAndObject(input);
            } else if (path.endsWith("LinearRegressionDetectionModel")) {
                return (LinearRegressionDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("LassoDetectionModel")) {
                return (LassoDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("KMeansDetectionModel")) {
                return (KMeansDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("GaussianMixtureDetectionModel")) {
                return (GaussianMixtureDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("SVMDetectionModel")) {
                return (SVMDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("RandomForestDetectionModel")) {
                return (RandomForestDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("NaiveBayesDetectionModel")) {
                return (NaiveBayesDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("LogisticRegressionDetectionModel")) {
                return (LogisticRegressionDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("DecisionTreeDetectionModel")) {
                return (DecisionTreeDetectionModel) kryo.readClassAndObject(input);

            } else if (path.endsWith("GradientBoostedTreesDetectionModel")) {
                return (GradientBoostedTreesDetectionModel) kryo.readClassAndObject(input);

            } else {
                log.warn("Not supported algorithm");
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Kryo getKryoObject() {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new SerializingInstantiatorStrategy());
        kryo.register(KMeansDetectionModel.class);
        kryo.register(AthenaMLFeatureConfiguration.class);
        kryo.register(DetectionModel.class);
        kryo.register(FeatureConstraint.class);
        kryo.register(AthenaFeatureField.class);
        kryo.register(AthenaIndexField.class);
        kryo.register(AthenaField.class);
        kryo.register(FeatureConstraintOperator.class);
        kryo.register(FeatureConstraintOperatorType.class);
        kryo.register(FeatureConstraintType.class);
        kryo.register(AthenaMLFeatureConfiguration.class);
        kryo.register(Marking.class);
        kryo.register(FeatureDatabaseMgmtManager.class);
        kryo.register(Indexing.class);
        kryo.register(ClusterModelSummary.class);
        kryo.register(KMeansDetectionAlgorithm.class);
        kryo.register(KmeansModelSummary.class);
        kryo.register(Accumulator.class);
        kryo.register(ClusterModelSummary.UniqueFlowAccumulatorParam.class);
        kryo.register(ClusterModelSummary.LongAccumulatorParam.class);
        kryo.register(ClassificationMarkingElement.class);
        kryo.register(TargetAthenaValue.class);
        return kryo;
    }

}
