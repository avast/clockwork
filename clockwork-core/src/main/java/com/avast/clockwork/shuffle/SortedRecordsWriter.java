package com.avast.clockwork.shuffle;

import java.util.TreeMap;

/**
 * Implementations of this interface write sorted map-reduce records table to a partition. The partition type is generic
 * and it is on the implementation to assign the meaning to it.
 * <p/>
 * User: slajchrt
 * Date: 6/7/12
 * Time: 6:21 PM
 */
public interface SortedRecordsWriter<K, V, P> {

    /**
     *
     * @param partition the partition to which the content should be written
     * @param sortedRecords the batch table containing rows sorted by the key.
     * @throws Exception
     */
    void write(P partition, TreeMap<K, ? extends Iterable<V>> sortedRecords) throws Exception;

}
