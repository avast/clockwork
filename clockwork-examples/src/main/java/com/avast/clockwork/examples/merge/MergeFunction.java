package com.avast.clockwork.examples.merge;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 9:57 PM
 */
public interface MergeFunction<T> {

    T merge(T value1, T value2) throws Exception;

}
