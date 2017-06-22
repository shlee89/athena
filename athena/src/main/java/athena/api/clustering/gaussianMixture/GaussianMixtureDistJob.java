package athena.api.clustering.gaussianMixture;

import athena.api.AthenaMLFeatureConfiguration;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.clustering.GaussianMixture;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.bson.BSONObject;
import org.onosproject.athena.database.AthenaFeatureField;
import scala.Tuple2;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by seunghyeon on 5/2/16.
 */
public class GaussianMixtureDistJob implements Serializable {
    public GaussianMixtureDistJob() {
    }

    public GaussianMixtureModel generateGaussianMixtureModel(JavaRDD<Vector> parsedData,
                                                             GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm,
                                                             GaussianMixtureModelSummary gaussianMixtureModelSummary) {
        GaussianMixture gaussianMixture = new GaussianMixture();
        if (gaussianMixtureDetectionAlgorithm.getK() != -1) {
            gaussianMixture.setK(gaussianMixtureDetectionAlgorithm.getK());
        }
        if (gaussianMixtureDetectionAlgorithm.getMaxIterations() != -1) {
            gaussianMixture.setMaxIterations(gaussianMixtureDetectionAlgorithm.getMaxIterations());
        }
        if (gaussianMixtureDetectionAlgorithm.getConvergenceTol() != -1){
            gaussianMixture.setConvergenceTol(gaussianMixtureDetectionAlgorithm.getConvergenceTol());
        }
        if (gaussianMixtureDetectionAlgorithm.getInitializedModel() != null) {
            gaussianMixture.setInitialModel(gaussianMixtureDetectionAlgorithm.getInitializedModel());
        }
        if (gaussianMixtureDetectionAlgorithm.getSeed() != -1) {
            gaussianMixture.setSeed(gaussianMixtureDetectionAlgorithm.getSeed());
        }

        GaussianMixtureModel gaussianMixtureModel = gaussianMixture.run(parsedData);
        gaussianMixtureModelSummary.setGaussianMixtureDetectionAlgorithm(gaussianMixtureDetectionAlgorithm);
        return gaussianMixtureModel;
    }


    public GaussianMixtureModel generateGaussianMixtureWithPreprocessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                                         AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                         GaussianMixtureDetectionAlgorithm gaussianMixtureDetectionAlgorithm,
                                                                         GaussianMixtureModelSummary gaussianMixtureModelSummary) {
        return generateGaussianMixtureModel(
                rddPreProcessing(mongoRDD, athenaMLFeatureConfiguration, gaussianMixtureModelSummary),
                gaussianMixtureDetectionAlgorithm, gaussianMixtureModelSummary
        );
    }

    public JavaRDD<Vector> rddPreProcessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                            AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                            GaussianMixtureModelSummary gaussianMixtureModelSummary) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();

        int numberOfTargetValue = listOfTargetFeatures.size();

        JavaRDD<Vector> parsedData = mongoRDD.map(
                (Function<Tuple2<Object, BSONObject>, Vector>) t -> {

                    BSONObject feature = (BSONObject) t._2().get(AthenaFeatureField.FEATURE);
                    BSONObject idx = (BSONObject) t._2();

                    double[] values = new double[numberOfTargetValue];
                    for (int j = 0; j < numberOfTargetValue; j++) {
                        if (feature.containsField(listOfTargetFeatures.get(j).getValue())) {
                            Object obj = feature.get(listOfTargetFeatures.get(j).getValue());
                            if (obj instanceof Long) {
                                values[j] = (Long) obj;
                            } else if (obj instanceof Double) {
                                values[j] = (Double) obj;
                            } else if (obj instanceof Boolean) {
                                values[j] = (Boolean) obj ? 1 : 0;
                            } else {
                                values[j] = 0;
                            }

                            //check weight
                            if (weight.containsKey(listOfTargetFeatures.get(j))) {
                                values[j] *= weight.get(listOfTargetFeatures.get(j));
                            }

                            //check absolute
                            if (athenaMLFeatureConfiguration.isAbsolute()){
                                values[j] = Math.abs(values[j]);
                            }
                        }
                    }

                    //remove errors
                    for (int i = 0; i < numberOfTargetValue; i++) {
                        if (Double.isInfinite(values[i]) || Double.isNaN(values[i])) {
                            for (int j = 0; j < numberOfTargetValue; j++) {
                                values[j] = 0;

                            }
                            return Vectors.dense(values);
                        }
                    }
                    gaussianMixtureModelSummary.updateSummary(idx, feature);
                    return Vectors.dense(values);
                }
        );

        Normalizer normalizer = new Normalizer();
        JavaRDD<Vector> normed;
        if (athenaMLFeatureConfiguration.isNormalization()) {
            normed = normalizer.transform(parsedData);
        } else {
            normed = parsedData;
        }

        normed.cache();
        return normed;
    }

    public void validate(JavaPairRDD<Object, BSONObject> mongoRDD,
                         AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                         GaussianMixtureDetectionModel gaussianMixtureDetectionModel,
                         GaussianMixtureValidationSummary gaussianMixtureValidationSummary) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();
        GaussianMixtureModel gaussianMixtureModel = (GaussianMixtureModel) gaussianMixtureDetectionModel.getDetectionModel();

        int numberOfTargetValue = listOfTargetFeatures.size();
        Normalizer normalizer = new Normalizer();

        mongoRDD.foreach(new VoidFunction<Tuple2<Object, BSONObject>>() {
            public void call(Tuple2<Object, BSONObject> t) throws UnknownHostException {
                long start2 = System.nanoTime(); // <-- start
                BSONObject feature = (BSONObject) t._2().get(AthenaFeatureField.FEATURE);
                BSONObject idx = (BSONObject) t._2();
                Vector normedForVal;

                double[] values = new double[numberOfTargetValue];
                for (int j = 0; j < numberOfTargetValue; j++) {
                    values[j] = 0;
                    if (feature.containsField(listOfTargetFeatures.get(j).getValue())) {
                        Object obj = feature.get(listOfTargetFeatures.get(j).getValue());
                        if (obj instanceof Long) {
                            values[j] = (Long) obj;
                        } else if (obj instanceof Double) {
                            values[j] = (Double) obj;
                        } else if (obj instanceof Boolean) {
                            values[j] = (Boolean) obj ? 1 : 0;
                        } else {
                            return;
                        }

                        //check weight
                        if (weight.containsKey(listOfTargetFeatures.get(j))) {
                            values[j] *= weight.get(listOfTargetFeatures.get(j));
                        }

                        //check absolute
                        if (athenaMLFeatureConfiguration.isAbsolute()){
                            values[j] = Math.abs(values[j]);
                        }
                    }
                }

                if (athenaMLFeatureConfiguration.isNormalization()) {
                    normedForVal = normalizer.transform(Vectors.dense(values));
                } else {
                    normedForVal = Vectors.dense(values);
                }
                int detectIdx = gaussianMixtureModel.predict(normedForVal);


                gaussianMixtureValidationSummary.updateSummary(detectIdx, idx, feature);

                long end2 = System.nanoTime();
                long result2 = end2 - start2;
                gaussianMixtureValidationSummary.addTotalNanoSeconds(result2);
            }
        });
        gaussianMixtureValidationSummary.calculateDetectionRate();
        gaussianMixtureValidationSummary.getAverageNanoSeconds();
        gaussianMixtureValidationSummary.setGaussianMixtureDetectionAlgorithm(
                (GaussianMixtureDetectionAlgorithm)gaussianMixtureDetectionModel.getDetectionAlgorithm());
    }


}
