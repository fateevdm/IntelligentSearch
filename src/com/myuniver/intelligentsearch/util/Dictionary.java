package com.myuniver.intelligentsearch.util;

/**
 * User: Dima
 * Date: 03.02.13
 * Time: 1:52
 */
public interface Dictionary<V> {

    void put(V value);

    V get();

    void delete(String key);

    boolean isEmpty();

    boolean contains(String key);

    int size();

    Iterable<String> keys();

    int hashCode();

    boolean equals(Object o);

}
