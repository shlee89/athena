package athena.api.clustering.kmeans;

import athena.api.AthenaMLFeatureConfiguration;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
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
public class KMeansDistJob implements Serializable {
    public KMeansDistJob() {
    }

    public KMeansModel generateKMeansModel(JavaRDD<Vector> parsedData,
                                           KMeansDetectionAlgorithm kMeansDetectionAlgorithm,
                                           KmeansModelSummary kmeansModelSummary) {
        KMeans kMeans = new KMeans();
        if (kMeansDetectionAlgorithm.getK() != -1) {
            kMeans.setK(kMeansDetectionAlgorithm.getK());
        }
        if (kMeansDetectionAlgorithm.getMaxIterations() != -1) {
            kMeans.setMaxIterations(kMeansDetectionAlgorithm.getMaxIterations());
        }
        if (kMeansDetectionAlgorithm.getRuns() != -1) {
            kMeans.setRuns(kMeansDetectionAlgorithm.getRuns());
        }
        if (kMeansDetectionAlgorithm.getInitializationMode() != null) {
            kMeans.setInitializationMode(kMeansDetectionAlgorithm.getInitializationMode());
        }
        if (kMeansDetectionAlgorithm.getSeed() != -1) {
            kMeans.setSeed(kMeansDetectionAlgorithm.getSeed());
        }
        if (kMeansDetectionAlgorithm.getEpsilon() != -1) {
            kMeans.setEpsilon(kMeansDetectionAlgorithm.getEpsilon());
        }


        KMeansModel kMeansModel = kMeans.run(parsedData.rdd());
        kmeansModelSummary.setWSSSE(kMeansModel.computeCost(parsedData.rdd()));
        kmeansModelSummary.setkMeansDetectionAlgorithm(kMeansDetectionAlgorithm);
        return kMeansModel;
    }


    public KMeansModel generateKmeansWithPreprocessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                       AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                       KMeansDetectionAlgorithm kMeansDetectionAlgorithm,
                                                       KmeansModelSummary kmeansModelSummary) {
        return generateKMeansModel(
                rddPreProcessing(mongoRDD, athenaMLFeatureConfiguration, kmeansModelSummary),
                kMeansDetectionAlgorithm, kmeansModelSummary
        );
    }

    public JavaRDD<Vector> rddPreProcessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                            AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                            KmeansModelSummary kmeansModelSummary) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();

        int numberOfTargetValue = listOfTargetFeatures.size();
//        int numberOfTargetValue = 5;

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
                            if (athenaMLFeatureConfiguration.isAbsolute()) {
                                values[j] = Math.abs(values[j]);
                            }
                        }
//                        values[j] = 0;
                    }

//                    //remove errors
                    for (int i = 0; i < numberOfTargetValue; i++) {
                        if (Double.isInfinite(values[i]) || Double.isNaN(values[i])) {
                            for (int j = 0; j < numberOfTargetValue; j++) {
                                values[j] = 0;

                            }
                            return Vectors.dense(values);
                        }
                    }
                    kmeansModelSummary.updateSummary(idx, feature);
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
                         KMeansDetectionModel kMeansDetectionModel,
                         KmeansValidationSummary kmeansValidationSummary) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();
        KMeansModel cluster = (KMeansModel) kMeansDetectionModel.getDetectionModel();
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
                        if (athenaMLFeatureConfiguration.isAbsolute()) {
                            values[j] = Math.abs(values[j]);
                        }
                    }


                }


                if (athenaMLFeatureConfiguration.isNormalization()) {
                    normedForVal = normalizer.transform(Vectors.dense(values));
                } else {
                    normedForVal = Vectors.dense(values);
                }

                int detectIdx = cluster.predict(normedForVal);

                kmeansValidationSummary.updateSummary(detectIdx, idx, feature);

                long end2 = System.nanoTime();
                long result2 = end2 - start2;
                kmeansValidationSummary.addTotalNanoSeconds(result2);
            }
        });
        kmeansValidationSummary.calculateDetectionRate();
        kmeansValidationSummary.getAverageNanoSeconds();
        kmeansValidationSummary.setkMeansDetectionAlgorithm((KMeansDetectionAlgorithm) kMeansDetectionModel.getDetectionAlgorithm());
    }


}
