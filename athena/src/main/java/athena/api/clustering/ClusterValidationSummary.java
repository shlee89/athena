package athena.api.clustering;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.Summary;
import athena.api.ValidationSummary;
import org.apache.spark.Accumulator;
import org.apache.spark.AccumulatorParam;
import org.apache.spark.SparkContext;
import org.bson.BSONObject;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaIndexField;


/**
 * Created by seunghyeon on 2/10/16.
 */
public class ClusterValidationSummary implements Serializable, ValidationSummary {

    public Indexing indexing = null;
    public Marking marking = null;

    private static final long serialVersionUID = -8887005106476207968L;

    int numberOfCluster = 0;
    long truePositive;
    long falseNegative;
    double detectionRate = 0;

    long falsePositive;
    long trueNegative;

    double falseAlarmRate = 0;

    Accumulator<HashMap<BigInteger, Boolean>> flowCounterMalicious;
    Accumulator<HashMap<BigInteger, Boolean>> flowCounterBenign;
    Accumulator<Long> totalNanoSeconds;
    double averageValidationTime;
    double totalValidationTime;


    long totalCount = 0;


    List<Accumulator<Long>> clusterCounterMaliciousList = new ArrayList<>();
    List<Accumulator<Long>> clusterCounterBenignList = new ArrayList<>();

    Accumulator<Long> longAccumulator;

    Accumulator<Long> totalBenign;
    Accumulator<Long> totalMalicious;

    public ClusterValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing, Marking marking) {
        this.numberOfCluster = numberOfCluster;
        this.indexing = indexing;
        this.marking = marking;
        this.initializeVariables(sc);
    }


    public ClusterValidationSummary(SparkContext sc, int numberOfCluster, Indexing indexing) {
        this.numberOfCluster = numberOfCluster;
        this.indexing = indexing;
        this.initializeVariables(sc);

    }

    public ClusterValidationSummary(SparkContext sc, int numberOfCluster) {
        this.numberOfCluster = numberOfCluster;
        this.initializeVariables(sc);

    }

    public double getTotalValidationTime() {
        return totalValidationTime;
    }

    public void setTotalValidationTime(double totalValidationTime) {
        this.totalValidationTime = totalValidationTime;
    }

    public void initializeVariables(SparkContext sc) {
        for (int i = 0; i < this.numberOfCluster; i++) {
            longAccumulator = sc.accumulator(0L, new LongAccumulatorParam());
            clusterCounterMaliciousList.add(longAccumulator);

            longAccumulator = sc.accumulator(0L, new LongAccumulatorParam());
            clusterCounterBenignList.add(longAccumulator);
        }


        longAccumulator = sc.accumulator(0L, new LongAccumulatorParam());

        totalBenign = sc.accumulator(0L, new LongAccumulatorParam());
        totalMalicious = sc.accumulator(0L, new LongAccumulatorParam());

        totalNanoSeconds = sc.accumulator(0L, "totalNanoSeconds", new LongAccumulatorParam());
        flowCounterMalicious = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
        flowCounterBenign = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
    }

    public boolean checkMarking(BSONObject index, BSONObject feature) {
        return this.marking.checkElements(index, feature);
    }

    public void clusterPerCounter(boolean origin, int index) {

        Accumulator<Long> local;


        try {
            if (origin) {
                //increase malicious
                local = clusterCounterMaliciousList.get(index);
                local.add(1L);
                clusterCounterMaliciousList.set(index, local);
                totalMalicious.add(1L);
            } else {
                local = clusterCounterBenignList.get(index);
                local.add(1L);
                clusterCounterBenignList.set(index, local);
                totalBenign.add(1L);
            }
        } catch (Exception e) {
            System.out.println(e.toString() + numberOfCluster + "idx" + index);
        }

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

    public void updateSummary(int clusterId, BSONObject index, BSONObject feature) {

        boolean origin = checkMarking(index, feature);
        clusterPerCounter(origin, clusterId);
        flowCounterGenerator(origin, index);
    }

    // proto 6 TCP / 17 UDP
    public void flowCounterGenerator(boolean origin, BSONObject index) {


        BigInteger bi = generateHashIndex(index);

        if (origin) {
            // malicious

            if (!flowCounterMalicious.localValue().containsKey(bi)) {
                // unique flow
                HashMap<BigInteger, Boolean> map = new HashMap<BigInteger, Boolean>();
                map.put(bi, true);
//                flowCounterMalicious.put(bi, true);
                flowCounterMalicious.add(map);
            }
        } else {
            // benign
            if (!flowCounterBenign.localValue().containsKey(bi)) {
                // unique flow
                HashMap<BigInteger, Boolean> map = new HashMap<BigInteger, Boolean>();
                map.put(bi, true);
//                flowCounterMalicious.put(bi, true);
                flowCounterBenign.add(map);
            }
        }

    }

    // True positive: malicious / malicious Sick people correctly identified as
    // sick
    // False positive: benign / malicious Healthy people incorrectly identified
    // as sick
    // True negative: benign / benign Healthy people correctly identified as
    // healthy
    // False negative: malicious / benign Sick people incorrectly identified as
    // healthy


    @Override
    public void printResults() {
        detectionRate = truePositive / (double) (truePositive + falseNegative);
        falseAlarmRate = falsePositive / (double) (trueNegative + falsePositive);
        System.out.println("------------------Summary (Validation) ----------------");
        System.out.println("Total     : " + totalCount + " entries");
        System.out.println("Benign    : " + totalBenign + " entries (" + flowCounterBenign.value().size() +
                " unique flows)");
        System.out.println("Malicious    : " + totalMalicious + " entries (" + flowCounterMalicious.value().size() +
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


        for (int i = 0; i < numberOfCluster; i++) {
            System.out.println("Cluster #" + i + ": Benign (" + clusterCounterBenignList.get(i).value() +
                    " entries), " +
                    "Malicious (" + clusterCounterMaliciousList.get(i).value() +
                    " entries)");

        }
        System.out.println("------------------Time----------------");
        System.out.println("Average Validation Time (per entry) : " + getAverageNanoSeconds() + " ns");
        System.out.println("Total Validation Time : " + (totalValidationTime / 1000000000.0) + " seconds");
        System.out.println("");

    }


    public void calculateDetectionRate() {
        truePositive = 0;
        trueNegative = 0;
        falsePositive = 0;
        falseNegative = 0;
        totalCount = 0;
        for (int i = 0; i < this.numberOfCluster; i++) {
            long clusterCounterMalicious = clusterCounterMaliciousList.get(i).value(); //malicious (true)

            long clusterCounterBenign = clusterCounterBenignList.get(i).value(); //benign false!
//                    getClutserCounter(false, i).value();

            // cluster == malicious
            if (clusterCounterMalicious >= clusterCounterBenign) {
                truePositive += clusterCounterMalicious;
                falsePositive += clusterCounterBenign;
                // cluster == benign
            } else {
                falseNegative += clusterCounterMalicious;
                trueNegative += clusterCounterBenign;
            }
        }
        totalCount = truePositive + trueNegative + falsePositive + falseNegative;
    }

    //true == malicious cluster
    public List<Boolean> getMaliciousCluster() {
        List<Boolean> listOfMaliciousCluster = new ArrayList<>();

        for (int i = 0; i < this.numberOfCluster; i++) {
            if (clusterCounterMaliciousList.get(i).value() >
                    clusterCounterBenignList.get(i).value()) {
                listOfMaliciousCluster.add(i, Boolean.TRUE);
            } else {
                listOfMaliciousCluster.add(i, Boolean.FALSE);
            }
        }
        return  listOfMaliciousCluster;
    }

    public boolean checkMaliciousCluster(int clusterID){
        return this.getMaliciousCluster().get(clusterID);
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