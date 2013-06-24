package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/12/12
 * Time: 2:43 PM
 */
public class Sum2BucketMapper extends Mapper<byte[], long[], Integer, Long> {

    @Override
    protected void map(byte[] sha256, long[] oldAndNewSum, Context context) throws Exception {
        long oldSum = oldAndNewSum[0];
        long newSum = oldAndNewSum[1];

        int oldBucketNo = getBucketNumberForSum(oldSum);
        int newBucketNo = getBucketNumberForSum(newSum);

        // SHA256 distribution correction

        // decrement the old bucket
        incrementAndGetFromHTable(oldBucketNo, -1);
        // increment the new bucket
        long newBucketSize = incrementAndGetFromHTable(newBucketNo, 1);

        emit(newBucketNo, newBucketSize);
    }

    /**
     * @param sum
     * @return the log2 of the sum as the bucket number
     */
    private int getBucketNumberForSum(long sum) {
        int bucketNo = 0;
        while ((sum = sum >> 1) > 0) bucketNo++;
        return bucketNo;
    }

    private long incrementAndGetFromHTable(int bucketNo, long contribution) {
        long total = 0;
        // ... send Increment and Get commands to HBase
        return total;
    }


}
