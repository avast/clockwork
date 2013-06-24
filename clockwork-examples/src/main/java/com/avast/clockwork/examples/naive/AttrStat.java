package com.avast.clockwork.examples.naive;

/**
 * User: zslajchrt
 * Date: 6/12/13
 * Time: 9:01 PM
 */
public class AttrStat extends BaseStat {

    private final double sum;
    private final double sumSq;

    public AttrStat(double sum, double sumSq, long count) {
        super(count);
        this.sum = sum;
        this.sumSq = sumSq;
    }

    public Double getMean() {
        return sum / getCount();
    }

    public double getVariance() {
        double mean = getMean();
        long count = getCount();
        return  (sumSq - mean * mean * count) / (count - 1);
    }

    @Override
    public AttrStat update(BaseStat other) {
        AttrStat otherAttrStat = (AttrStat) other;
        return new AttrStat(sum + otherAttrStat.sum, sumSq + otherAttrStat.sumSq, getCount() + other.getCount());
    }

    public double getPosterior(double attrVal) {
        double variance = getVariance();
        double mean = getMean();
        return (1f / Math.sqrt(2 * Math.PI * variance)) *
                Math.exp(-(attrVal - mean) * (attrVal - mean) / (2 * variance));
    }

    @Override
    public String toString() {
        return "mean:" + getMean() + ", var:" + getVariance();
    }

}
