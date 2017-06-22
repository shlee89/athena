package athena.api.classification;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.ValidationSummary;
import org.apache.spark.Accumulator;
import org.apache.spark.AccumulatorParam;
import org.apache.spark.SparkContext;
import org.bson.BSONObject;
import org.onosproject.athena.database.AthenaIndexField;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by seunghyeon on 2/10/16.
 */
public class ClassificationValidationSummary implements Serializable, ValidationSummary {

    public Indexing indexing = null;
    public Marking marking = null;

    private static final long serialVersionUID = -8887005106476207968L;

    int numberOfLabels = 0;
    double detectionRate = 0;

//    long truePositive;
//    long falseNegative;
//    long falsePositive;
//    long trueNegative;

    Accumulator<Long> truePositive;
    Accumulator<Long> falseNegative;
    Accumulator<Long> falsePositive;
    Accumulator<Long> trueNegative;


    double falseAlarmRate = 0;

    //Only 0 -> benign
    Accumulator<HashMap<BigInteger, Boolean>> flowCounterMalicious;
    Accumulator<HashMap<BigInteger, Boolean>> flowCounterBenign;
    Accumulator<Long> totalNanoSeconds;
    double averageValidationTime;
    double totalValidationTime;


    long totalCount = 0;


    List<Accumulator<Long>> classificationCounterValidatedList = new ArrayList<>();
    List<Accumulator<Long>> classificationCounterOriginList = new ArrayList<>();

    //for unique flows
    List<Accumulator<HashMap<BigInteger, Boolean>>> uniqueOriginEntires = new ArrayList<>();
    List<Accumulator<HashMap<BigInteger, Boolean>>> uniqueValidatedEntires = new ArrayList<>();
    Accumulator<HashMap<BigInteger, Boolean>> flowCounter;

    Accumulator<Long> longAccumulator;

    Accumulator<Long> totalBenign;
    Accumulator<Long> totalMalicious;

    public ClassificationValidationSummary(SparkContext sc, int numberOfLabels, Indexing indexing, Marking marking) {
        this.numberOfLabels = numberOfLabels;
        this.indexing = indexing;
        this.marking = marking;
        this.initializeVariables(sc);
    }


    public ClassificationValidationSummary(SparkContext sc, int numberOfLabels, Indexing indexing) {
        this.numberOfLabels = numberOfLabels;
        this.indexing = indexing;
        this.initializeVariables(sc);

    }

    public ClassificationValidationSummary(SparkContext sc, int numberOfLabels) {
        this.numberOfLabels = numberOfLabels;
        this.initializeVariables(sc);

    }

    public double getTotalValidationTime() {
        return totalValidationTime;
    }

    public void setTotalValidationTime(double totalValidationTime) {
        this.totalValidationTime = totalValidationTime;
    }

    public void initializeVariables(SparkContext sc) {
        for (int i = 0; i < this.numberOfLabels; i++) {
            longAccumulator = sc.accumulator(0L, new LongAccumulatorParam());
            classificationCounterValidatedList.add(longAccumulator);

            longAccumulator = sc.accumulator(0L, new LongAccumulatorParam());
            classificationCounterOriginList.add(longAccumulator);

            //unique entries
            flowCounter = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
            uniqueOriginEntires.add(flowCounter);

            flowCounter = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
            uniqueValidatedEntires.add(flowCounter);
        }


        longAccumulator = sc.accumulator(0L, new LongAccumulatorParam());

        totalBenign = sc.accumulator(0L, new LongAccumulatorParam());
        totalMalicious = sc.accumulator(0L, new LongAccumulatorParam());

        truePositive = sc.accumulator(0L, new LongAccumulatorParam());
        falseNegative = sc.accumulator(0L, new LongAccumulatorParam());
        falsePositive = sc.accumulator(0L, new LongAccumulatorParam());
        trueNegative = sc.accumulator(0L, new LongAccumulatorParam());

        totalNanoSeconds = sc.accumulator(0L, "totalNanoSeconds", new LongAccumulatorParam());
        flowCounterMalicious = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
        flowCounterBenign = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
    }


    public BigInteger generateHashIndex(BSONObject index) {
        List<AthenaIndexField> ListofAthenaFeatureField = indexing.getListOfTargetFeatures();

        BigInteger result = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            // allocate byte buffer for all feature index
            // TODO : should be more intelligent way
            ByteBuffer bb = ByteBuffer.allocate(ListofAthenaFeatureField.size() * Long.BYTES);

            for (int i = 0; i < ListofAthenaFeatureField.size(); i++) {
                Object value = index.get(ListofAthenaFeatureField.get(i).getValue());

                if (value == null) {
                    bb.putLong(0);
                } else {
                    if (value instanceof Integer) {
                        bb.putLong(((Integer) value).longValue());
                    } else if (value instanceof Long) {
                        bb.putLong(((Long) value).longValue());
                    } else {
                        bb.putLong(0);
                    }
                }
            }

            // generate hash index using MD5
            byte[] hashIndex = md.digest(bb.array());
            bb.clear();
            md.reset();

            result = new BigInteger(hashIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void updateSummary(int validatedLabel, BSONObject index, BSONObject feature) {

        int originLabel = this.marking.checkClassificationMarkingElements(index, feature);

//        boolean origin = checkMarking(index, feature);
        updateLableCounter(originLabel, validatedLabel);
        updateDetectionInfo(originLabel, validatedLabel);
        updateUniqueFlowEntries(originLabel, validatedLabel, index);
        //TODO unique flows
//        flowCounterGenerator(origin, index);
    }

    // true (0) == benign
    // false(others) == malicious
    public void updateDetectionInfo(int originLabel, int validatedLabel) {
        boolean origin = originLabel == 0;
        boolean validated = validatedLabel == 0;

        if (!origin && !validated) {
            truePositive.add(1L);
        } else if (origin && !validated) {
            falsePositive.add(1L);
        } else if (origin && validated) {
            trueNegative.add(1L);
        } else {
            falseNegative.add(1L);
        }

    }

    public void updateUniqueFlowEntries(int originLabel, int validatedLabel, BSONObject index) {
        BigInteger bi = generateHashIndex(index);

        Accumulator<HashMap<BigInteger, Boolean>> local;
        try {
            //update origins
            local = uniqueOriginEntires.get(originLabel);
            if (!local.localValue().containsKey(bi)) {
                HashMap<BigInteger, Boolean> map = new HashMap<BigInteger, Boolean>();
                map.put(bi, true);
                local.add(map);
            }

            local = uniqueValidatedEntires.get(originLabel);
            if (!local.localValue().containsKey(bi)) {
                HashMap<BigInteger, Boolean> map = new HashMap<BigInteger, Boolean>();
                map.put(bi, true);
                local.add(map);
            }

        } catch (Exception e) {
            System.out.println(e.toString() + numberOfLabels + "ori:" + originLabel + " val" + validatedLabel);
        }

    }
//
//    // proto 6 TCP / 17 UDP
//    public void flowCounterGenerator(boolean origin, BSONObject index) {
//
//        BigInteger bi = generateHashIndex(index);
//
//        if (origin) {
//            // malicious
//
//            if (!flowCounterMalicious.localValue().containsKey(bi)) {
//                // unique flow
//                HashMap<BigInteger, Boolean> map = new HashMap<BigInteger, Boolean>();
//                map.put(bi, true);
////                flowCounterMalicious.put(bi, true);
//                flowCounterMalicious.add(map);
//            }
//        } else {
//            // benign
//            if (!flowCounterBenign.localValue().containsKey(bi)) {
//                // unique flow
//                HashMap<BigInteger, Boolean> map = new HashMap<BigInteger, Boolean>();
//                map.put(bi, true);
////                flowCounterMalicious.put(bi, true);
//                flowCounterBenign.add(map);
//            }
//        }
//
//    }


    // True positive: malicious / malicious Sick people correctly identified as
    // sick
    // False positive: benign / malicious Healthy people incorrectly identified
    // as sick
    // True negative: benign / benign Healthy people correctly identified as
    // healthy
    // False negative: malicious / benign Sick people incorrectly identified as
    // healthy


    public void updateLableCounter(int originLabel, int validatedLabel) {

        Accumulator<Long> local;
        try {
            //update validated
            local = classificationCounterValidatedList.get(validatedLabel);
            local.add(1L);
            classificationCounterValidatedList.set(validatedLabel, local);

            //update origins
            local = classificationCounterOriginList.get(originLabel);
            local.add(1L);
            classificationCounterOriginList.set(originLabel, local);


        } catch (Exception e) {
            System.out.println(e.toString() + numberOfLabels + "ori:" + originLabel + " val" + validatedLabel);
        }

        if (validatedLabel == 0) {
            //update Total beign
            totalBenign.add(1L);
        } else {
            //update total malicious
            totalMalicious.add(1L);
        }

    }


    @Override
    public void printResults() {

        long truePositive = this.truePositive.value();
        long falseNegative = this.falseNegative.value();
        long falsePositive = this.falsePositive.value();
        long trueNegative = this.trueNegative.value();
        int totalUniqueMalicious = 0;
        int totalUniqueBenign = uniqueValidatedEntires.get(0).value().size();
        for (int i = 0 ; i < numberOfLabels; i++){
            totalUniqueMalicious += uniqueOriginEntires.get(i).value().size();
        }
        detectionRate = truePositive / (double) (truePositive + falseNegative);
        falseAlarmRate = falsePositive / (double) (trueNegative + falsePositive);
        totalCount = truePositive + trueNegative + falsePositive + falseNegative;



        System.out.println("------------------Summary (Validation) ----------------");
        System.out.println("Total     : " + totalCount + " entries");
        System.out.println("Benign    : " + totalBenign + " entries (" + totalUniqueBenign +
                " unique flows)");
        System.out.println("Malicious    : " + totalMalicious + " entries (" + totalUniqueMalicious +
                " unique flows)");
        System.out.println();
        System.out.println("True Positive   : " + truePositive + " entries");
        System.out.println("False Positive  : " + falsePositive + " entries");
        System.out.println("True Negative   : " + trueNegative + " entries");
        System.out.println("False Negative  : " + falseNegative + " entries");
        System.out.println();
        System.out.println("Detection Rate  : " + detectionRate);
        System.out.println("False Alarm Rate: " + falseAlarmRate);
        System.out.println();


        for (int i = 0; i < numberOfLabels; i++) {
            System.out.println("Label #" + i + " " + marking.converLableToLableName(i) + " : origin (" + classificationCounterOriginList.get(i).value() +
                    " entries \""+ uniqueOriginEntires.get(i).value().size() +"unique entries\"), " +
                    "validated (" + classificationCounterValidatedList.get(i).value() +
                    " entries \""+ uniqueValidatedEntires.get(i).value().size() +"unique entries\")");

        }
        System.out.println("------------------Time----------------");
        System.out.println("Average Validation Time (per entry) : " + getAverageNanoSeconds() + " ns");
        System.out.println("Total Validation Time : " + (totalValidationTime / 1000000000.0) + " seconds");
        System.out.println("");

    }


    public double getAverageNanoSeconds() {
        averageValidationTime = (double) totalNanoSeconds.value() / totalCount;
        return averageValidationTime;
    }

    public void addTotalNanoSeconds(long time) {
        totalNanoSeconds.add(time);
    }


    class LongAccumulatorParam implements AccumulatorParam<Long> {
        private static final long serialVersionUID = -2435492528997106813L;

        @Override
        public Long zero(Long arg0) {
            // TODO Auto-generated method stub
            return new Long(arg0);
        }

        @Override
        public Long addInPlace(Long arg0, Long arg1) {
            // TODO Auto-generated method stub
            return arg0 + arg1;
        }

        @Override
        public Long addAccumulator(Long arg0, Long arg1) {
            // TODO Auto-generated method stub
            return arg0 + arg1;
        }
    }

    class UniqueFlowAccumulatorParam implements AccumulatorParam<HashMap<BigInteger, Boolean>> {
        private static final long serialVersionUID = 1L;

        @Override
        public HashMap<BigInteger, Boolean> addInPlace(HashMap<BigInteger, Boolean> arg0, HashMap<BigInteger, Boolean> arg1) {
            // TODO Auto-generated method stub
            arg0.putAll(arg1);
            return arg0;
        }

        @Override
        public HashMap<BigInteger, Boolean> zero(HashMap<BigInteger, Boolean> arg0) {
            // TODO Auto-generated method stub
            return new HashMap<BigInteger, Boolean>();
        }

        public HashMap<BigInteger, Boolean> addAccumulator(HashMap<BigInteger, Boolean> arg0, HashMap<BigInteger, Boolean> arg1) {
            arg0.putAll(arg1);
            return arg0;
        }
    }

}