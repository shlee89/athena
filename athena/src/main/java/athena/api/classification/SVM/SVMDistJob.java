package athena.api.classification.SVM;

import athena.api.AthenaMLFeatureConfiguration;
import athena.api.Marking;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
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
public class SVMDistJob implements Serializable {
    public SVMDistJob() {
    }

    public SVMModel generateKMeansModel(JavaRDD<LabeledPoint> parsedData,
                                        SVMDetectionAlgorithm svmDetectionAlgorithm,
                                        SVMModelSummary SVMModelSummary) {
        SVMModel svmModel;



        if (svmDetectionAlgorithm.getMiniBatchFraction() != -1) {
            svmModel = SVMWithSGD.train(parsedData.rdd(),
                    svmDetectionAlgorithm.getNumIterations(),
                    svmDetectionAlgorithm.getStepSize(),
                    svmDetectionAlgorithm.getRegParam(),
                    svmDetectionAlgorithm.getMiniBatchFraction());
        }else if (svmDetectionAlgorithm.getRegParam() != -1) {
            svmModel = SVMWithSGD.train(parsedData.rdd(),
                    svmDetectionAlgorithm.getNumIterations(),
                    svmDetectionAlgorithm.getStepSize(),
                    svmDetectionAlgorithm.getRegParam());
        }else {
            svmModel = SVMWithSGD.train(parsedData.rdd(),
                    svmDetectionAlgorithm.getNumIterations());
        }

        SVMModelSummary.setSVMDetectionAlgorithm(svmDetectionAlgorithm);
        return svmModel;
    }


    public SVMModel generateDecisionTreeWithPreprocessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                                   AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                                   SVMDetectionAlgorithm SVMDetectionAlgorithm,
                                                                   Marking marking,
                                                                   SVMModelSummary SVMModelSummary) {

        return generateKMeansModel(
                rddPreProcessing(mongoRDD, athenaMLFeatureConfiguration, SVMModelSummary,
                        marking),
                SVMDetectionAlgorithm, SVMModelSummary
        );
    }

    public JavaRDD<LabeledPoint> rddPreProcessing(JavaPairRDD<Object, BSONObject> mongoRDD,
                                                  AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                                                  SVMModelSummary SVMModelSummary,
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
                            return new LabeledPoint(label, Vectors.dense(values));
                        }
                    }


                    Vector normedForVal;
                    if (athenaMLFeatureConfiguration.isNormalization()) {
                        normedForVal = normalizer.transform(Vectors.dense(values));
                    } else {
                        normedForVal = Vectors.dense(values);
                    }

                    SVMModelSummary.updateSummary(idx, feature);
                    return new LabeledPoint(label, normedForVal);
                }
        );

        return parsedData;
    }

    public void validate(JavaPairRDD<Object, BSONObject> mongoRDD,
                         AthenaMLFeatureConfiguration athenaMLFeatureConfiguration,
                         SVMDetectionModel SVMDetectionModel,
                         SVMValidationSummary SVMValidationSummary) {
        List<AthenaFeatureField> listOfTargetFeatures = athenaMLFeatureConfiguration.getListOfTargetFeatures();
        Map<AthenaFeatureField, Integer> weight = athenaMLFeatureConfiguration.getWeight();
        Marking marking = SVMDetectionModel.getMarking();
        SVMModel model = (SVMModel) SVMDetectionModel.getDetectionModel();

        Normalizer normalizer = new Normalizer();

        int numberOfTargetValue = listOfTargetFeatures.size();

        mongoRDD.foreach(new VoidFunction<Tuple2<Object, BSONObject>>() {
            public void call(Tuple2<Object, BSONObject> t) throws UnknownHostException {
                long start2 = System.nanoTime(); // <-- start
                BSONObject feature = (BSONObject) t._2().get(AthenaFeatureField.FEATURE);
                BSONObject idx = (BSONObject) t._2();
                int originLabel = marking.checkClassificationMarkingElements(idx,feature);

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

                Vector normedForVal;
                if (athenaMLFeatureConfiguration.isNormalization()) {
                    normedForVal = normalizer.transform(Vectors.dense(values));
                } else {
                    normedForVal = Vectors.dense(values);
                }

                LabeledPoint p = new LabeledPoint(originLabel,normedForVal);

                //Only SVM!!
                int validatedLabel;// = (int) model.predict(p.features());
                double score = model.predict(p.features());
                if (score > 0){
                    //detection
                    validatedLabel = 1;
                } else {
                    validatedLabel = 0;
                }
                SVMValidationSummary.updateSummary(validatedLabel,idx,feature);

                long end2 = System.nanoTime();
                long result2 = end2 - start2;
                SVMValidationSummary.addTotalNanoSeconds(result2);
            }
        });
        SVMValidationSummary.getAverageNanoSeconds();
        SVMValidationSummary.setSvmDetectionAlgorithm((SVMDetectionAlgorithm) SVMDetectionModel.getDetectionAlgorithm());
    }


}
