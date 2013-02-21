package com.myuniver.intelligentsearch.util;

/**
 * User: Dima
 * Date: 03.02.13
 * Time: 1:52
 */
public interface Dictionary<K, V> {

    void put(K key, V value);

    V get(K key);

    void delete(String key);

    boolean isEmpty();

    boolean contains(String key);

    int size();

    Iterable<String> keys();

    int hashCode();

    boolean equals(Object o);

}
