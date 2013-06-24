package com.avast.clockwork.shuffle;

import java.util.Collection;

/**
 * User: slajchrt
 * Date: 6/19/12
 * Time: 8:38 PM
 * @see ShufflingAccumulator
 */
public interface ValueCollectionFactory<V> {
    Collection<V> createValueCollection();
}
