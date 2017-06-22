package athena.api.regression.LinearRegression;

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
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
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
public class LinearRegressionDistJob implements Serializable {
    public LinearRegressionDistJob() {
    }

    public LinearRegressionModel generateKMeansModel(JavaRDD<LabeledPoint> parsedData,
                                                     LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm,
                                                     LinearRegressionModelSummary linearRegressionModelSummary) {
        LinearRegressionModel model;

        if (linearRegressionDetectionAlgorithm.getMiniBatchFraction() != -1) {
            model = LinearRegressionWithSGD.train(parsedData.rdd(),
                    linearRegressionDetectionAlgorithm.getNumIterations(),
                    linearRegressionDetectionAlgorithm.getStepSize(),
                    linearRegressionDetectionAlgorithm.getMiniBatchFraction());
        } else {
            model = LinearRegressionWithSGD.train(parsedData.rdd(),
                    linearRegressionDetectionAlgorithm.getNumIterations(),
                    linearRegressionDetectionAlgorithm.getStepSize());
        }


        linearRegressionModelSummary.setLinearRegressionDetectionAlgorithm(linearRegressionDetectionAlgorithm);
        return model;
    }


    public LinearRegressionModel generateDecisionTreeWithPreprocessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                                       AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                       LinearRegressionDetectionAlgorithm linearRegressionDetectionAlgorithm,
                                                                       Marking marking,
                                                                       LinearRegressionModelSummary linearRegressionModelSummary) {

        return generateKMeansModel(
                rddPreProcessing(mongoRDD, athenaMLFeatureConfiguration, linearRegressionModelSummary,
                        marking),
                linearRegressionDetectionAlgorithm, linearRegressionModelSummary
        );
    }

    public JavaRDD<LabeledPoint> rddPreProcessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                  AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                  LinearRegressionModelSummary linearRegressionModelSummary,
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

                    linearRegressionModelSummary.updateSummary(idx, feature);
                    return new LabeledPoint(label, normedForVal);
                }
        );

        return parsedData;
    }

    public void validate(JavaPairRDD<Object, BSONObject> mongoRDD,
                         AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                         LinearRegressionDetectionModel linearRegressionDetectionModel,
                         LinearRegressionValidationSummary linearRegressionValidationSummary) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();
        Marking marking = linearRegressionDetectionModel.getMarking();
        LinearRegressionModel model = (LinearRegressionModel) linearRegressionDetectionModel.getDetectionModel();
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


                    linearRegressionValidationSummary.addEntry();
                    return new Tuple2<Double, Double>(prediction, p.label());
                });

        double MSE = new JavaDoubleRDD(valuesAndPreds.map(
                new Function<Tuple2<Double, Double>, Object>() {
                    public Object call(Tuple2<Double, Double> pair) {
                        return Math.pow(pair._1() - pair._2(), 2.0);
                    }
                }
        ).rdd()).mean();
        linearRegressionValidationSummary.setMSE(MSE);
        linearRegressionValidationSummary.setLinearRegressionDetectionAlgorithm((LinearRegressionDetectionAlgorithm) linearRegressionDetectionModel.getDetectionAlgorithm());
    }


}
