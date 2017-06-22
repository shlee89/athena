package athena.api.regression.RidgeRegression;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.Marking;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.RidgeRegressionModel;
import org.apache.spark.mllib.regression.RidgeRegressionWithSGD;
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
public class RidgeRegressionDistJob implements Serializable {
    public RidgeRegressionDistJob() {
    }

    public RidgeRegressionModel generateKMeansModel(JavaRDD<LabeledPoint> parsedData,
                                                    RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm,
                                                    RidgeRegressionModelSummary ridgeRegressionModelSummary) {
        RidgeRegressionModel model;


        if (ridgeRegressionDetectionAlgorithm.getMiniBatchFraction() != -1) {
            model = RidgeRegressionWithSGD.train(parsedData.rdd(),
                    ridgeRegressionDetectionAlgorithm.getNumIterations(),
                    ridgeRegressionDetectionAlgorithm.getStepSize(),
                    ridgeRegressionDetectionAlgorithm.getRegParam(),
                    ridgeRegressionDetectionAlgorithm.getMiniBatchFraction());
        } else if (ridgeRegressionDetectionAlgorithm.getRegParam() != -1) {
            model = RidgeRegressionWithSGD.train(parsedData.rdd(),
                    ridgeRegressionDetectionAlgorithm.getNumIterations(),
                    ridgeRegressionDetectionAlgorithm.getStepSize(),
                    ridgeRegressionDetectionAlgorithm.getRegParam());
        } else {
            model = RidgeRegressionWithSGD.train(parsedData.rdd(),
                    ridgeRegressionDetectionAlgorithm.getNumIterations());
        }


        ridgeRegressionModelSummary.setRidgeRegressionDetectionAlgorithm(ridgeRegressionDetectionAlgorithm);
        return model;
    }


    public RidgeRegressionModel generateDecisionTreeWithPreprocessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                                      AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                      RidgeRegressionDetectionAlgorithm ridgeRegressionDetectionAlgorithm,
                                                                      Marking marking,
                                                                      RidgeRegressionModelSummary ridgeRegressionModelSummary) {

        return generateKMeansModel(
                rddPreProcessing(mongoRDD, athenaMLFeatureConfiguration, ridgeRegressionModelSummary,
                        marking),
                ridgeRegressionDetectionAlgorithm, ridgeRegressionModelSummary
        );
    }

    public JavaRDD<LabeledPoint> rddPreProcessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                  AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                  RidgeRegressionModelSummary ridgeRegressionModelSummary,
                                                  Marking marking) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();

        int numberOfTargetValue = listOfTargetFeatures.size();
        Normalizer normalizer = new Normalizer();

        JavaRDD<LabeledPoint> parsedData = mongoRDD.map(
                (Function<Tuple2<Object, BSONObject>, LabeledPoint>) t -> {

                    BSONObject feature = (BSONObject) t._2().get(AthenaFeatureField.FEATURE);
                    BSONObject idx = (BSONObject) t._2();
                    int label = marking.checkClassificationMarkingElements(idx, feature);
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
                    }

                    //remove errors
                    for (int i = 0; i < numberOfTargetValue; i++) {
                        if (Double.isInfinite(values[i]) || Double.isNaN(values[i])) {
                            for (int j = 0; j < numberOfTargetValue; j++) {
                                values[j] = 0;

                            }
                            return new LabeledPoint(label, Vectors.dense(values));
                        }
                    }


                    Vector normedForVal;
                    if (athenaMLFeatureConfiguration.isNormalization()) {
                        normedForVal = normalizer.transform(Vectors.dense(values));
                    } else {
                        normedForVal = Vectors.dense(values);
                    }

                    ridgeRegressionModelSummary.updateSummary(idx, feature);
                    return new LabeledPoint(label, normedForVal);
                }
        );

        return parsedData;
    }

    public void validate(JavaPairRDD<Object, BSONObject> mongoRDD,
                         AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                         RidgeRegressionDetectionModel ridgeRegressionDetectionModel,
                         RidgeRegressionValidationSummary ridgeRegressionValidationSummary) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();
        Marking marking = ridgeRegressionDetectionModel.getMarking();
        RidgeRegressionModel model = (RidgeRegressionModel) ridgeRegressionDetectionModel.getDetectionModel();
        Normalizer normalizer = new Normalizer();

        int numberOfTargetValue = listOfTargetFeatures.size();

        JavaRDD<Tuple2<Double, Double>> valuesAndPreds = mongoRDD.map(
                (Function<Tuple2<Object, BSONObject>, Tuple2<Double, Double>>) t -> {

                    BSONObject feature = (BSONObject) t._2().get(AthenaFeatureField.FEATURE);
                    BSONObject idx = (BSONObject) t._2();
                    int originLabel = marking.checkClassificationMarkingElements(idx, feature);

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
                                System.out.println("not supported");
//                                return;
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

                    Vector normedForVal;
                    if (athenaMLFeatureConfiguration.isNormalization()) {
                        normedForVal = normalizer.transform(Vectors.dense(values));
                    } else {
                        normedForVal = Vectors.dense(values);
                    }

                    LabeledPoint p = new LabeledPoint(originLabel, normedForVal);
                    //Only SVM!!

                    double prediction = model.predict(p.features());


                    ridgeRegressionValidationSummary.addEntry();
                    return new Tuple2<Double, Double>(prediction, p.label());
                });

        double MSE = new JavaDoubleRDD(valuesAndPreds.map(
                new Function<Tuple2<Double, Double>, Object>() {
                    public Object call(Tuple2<Double, Double> pair) {
                        return Math.pow(pair._1() - pair._2(), 2.0);
                    }
                }
        ).rdd()).mean();
        ridgeRegressionValidationSummary.setMSE(MSE);
        ridgeRegressionValidationSummary.setRidgeRegressionDetectionAlgorithm((RidgeRegressionDetectionAlgorithm) ridgeRegressionDetectionModel.getDetectionAlgorithm());
    }


}
