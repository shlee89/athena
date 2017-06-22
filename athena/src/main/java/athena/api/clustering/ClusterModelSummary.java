package athena.api.clustering;

import athena.api.Indexing;
import athena.api.Marking;
import athena.api.Summary;
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
 * We need to minimize the implementation as two Makred option - malicious/ benign (0 - benign/ 1 - malicious)
 * Created by seunghyeon on 4/7/16.
 */
public class ClusterModelSummary implements Serializable, Summary {

    public Indexing indexing = null;
    public Marking marking = null;

    private static final long serialVersionUID = -8887005106476207968L;


    Accumulator<HashMap<BigInteger, Boolean>> flowCounterMalicious;
    Accumulator<HashMap<BigInteger, Boolean>> flowCounterBenign;
    double totalLearningTime;

    public ClusterModelSummary() {
    }

    long totalCount = 0;

    Accumulator<Long> longAccumulator;

    Accumulator<Long> totalBenign;
    Accumulator<Long> totalMalicious;

    public ClusterModelSummary(SparkContext sc, Indexing indexing, Marking marking) {
        this.indexing = indexing;
        this.marking = marking;
        this.initializeVariables(sc);
    }


    public ClusterModelSummary(SparkContext sc, Indexing indexing) {
        this.indexing = indexing;
        this.initializeVariables(sc);

    }

    public ClusterModelSummary(SparkContext sc) {
        this.initializeVariables(sc);

    }

    public double getTotalLearningTime() {
        return totalLearningTime;
    }

    public void setTotalLearningTime(double totalLearningTime) {
        this.totalLearningTime = totalLearningTime;
    }

    public void initializeVariables(SparkContext sc) {


        longAccumulator = sc.accumulator(0L, new LongAccumulatorParam());

        totalBenign = sc.accumulator(0L, new LongAccumulatorParam());
        totalMalicious = sc.accumulator(0L, new LongAccumulatorParam());

        flowCounterMalicious = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
        flowCounterBenign = sc.accumulator(new HashMap<BigInteger, Boolean>(), new UniqueFlowAccumulatorParam());
    }

    public boolean checkMarking(BSONObject index, BSONObject feature) {
        return this.marking.checkElements(index, feature);
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

    public void totalCounter(boolean origin){
        if (origin) {
            totalMalicious.add(1L);
        }else {
            totalBenign.add(1L);
        }
    }

    public void updateSummary(BSONObject index, BSONObject feature) {

        boolean origin = checkMarking(index, feature);
        totalCounter(origin);
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
    public void printSummary() {
        totalCount = totalBenign.value() + totalMalicious.value();
        System.out.println("------------------Summary (Learning) ----------------");
        System.out.println("Total     : " + totalCount + " entries");
        System.out.println("Benign    : " + totalBenign + " entries (" + flowCounterBenign.value().size() +
                " unique flows)");
        System.out.println("Malicious    : " + totalMalicious + " entries (" + flowCounterMalicious.value().size() +
                " unique flows)");
        System.out.println();



        System.out.println("------------------Time----------------");
        System.out.println("Total Learning Time : " + (totalLearningTime /1000000000.0) + " seconds");
        System.out.println("");

    }



    public class LongAccumulatorParam implements AccumulatorParam<Long> {
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

    public class UniqueFlowAccumulatorParam implements AccumulatorParam<HashMap<BigInteger, Boolean>> {
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
