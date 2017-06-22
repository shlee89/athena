package org.onosproject.athena.feature;


import java.util.Date;

/**
 * Created by seunghyeon on 8/19/15.
 */
public class UnitTableStatistics implements UnitFeature {
    private final long maxEntries;
    private final long activeCount;
    private final long lookupCount;
    private final long matchedCount;
    private Date date;
    //add the rich features - By Jinwoo
    private double matchedPerLookup;
    private double activePerMax;
    private double lookupPerActive;
    private double matchedPerActive;

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitTableStatistics(long maxEntries, long activeCount,
                               long lookupCount, long matchedCount) {
        this.maxEntries = maxEntries;
        this.activeCount = activeCount;
        this.lookupCount = lookupCount;
        this.matchedCount = matchedCount;
    }

    public long getMaxEntries() {
        return maxEntries;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public long getLookupCount() {
        return lookupCount;
    }

    public long getMatchedCount() {
        return matchedCount;
    }

    @Override
    public Date getTimestamp() {
        return date;
    }

    public double getMatchedPerLookup() {
        return matchedPerLookup;
    }

    public void setMatchedPerLookup(double matchedPerLookup) {
        this.matchedPerLookup = matchedPerLookup;
    }

    public double getActivePerMax() {
        return activePerMax;
    }

    public void setActivePerMax(double activePerMax) {
        this.activePerMax = activePerMax;
    }

    public double getLookupPerActive() {
        return lookupPerActive;
    }

    public void setLookupPerActive(double lookupPerActive) {
        this.lookupPerActive = lookupPerActive;
    }

    public double getMatchedPerActive() {
        return matchedPerActive;
    }

    public void setMatchedPerActive(double matchedPerActive) {
        this.matchedPerActive = matchedPerActive;
    }

    @Override
    public String toString() {
        return "UnitTableStatistics [maxEntries=" + maxEntries + ", activeCount=" + activeCount + ", lookupCount="
                + lookupCount + ", matchedCount=" + matchedCount + ", matchedPerLookup=" + matchedPerLookup
                + ", activePerMax=" + activePerMax + ", lookupPerActive=" + lookupPerActive + ", matchedPerActive="
                + matchedPerActive + "]";
    }
}
