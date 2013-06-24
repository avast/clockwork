package com.avast.clockwork.examples.naive;

/**
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 11:44 AM
 */
public class BaseStat {

    private final long count;

    public BaseStat(long count) {
        this.count = count;
    }

    public BaseStat update(BaseStat other) {
        return new BaseStat(count + other.count);
    }

    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "count: " + count;
    }
}
