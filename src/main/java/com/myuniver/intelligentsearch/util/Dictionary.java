package com.myuniver.intelligentsearch.util;

import java.util.Map;
import java.util.Set;

/**
 * User: Dima
 * Date: 03.02.13
 * Time: 1:52
 */
public interface Dictionary<K, V> {

    Dictionary<K, V> put(K key, V value);

    V get(K key);

    K getByValue(V value);

    V delete(String key);

    boolean isEmpty();

    boolean containsKey(String key);

    boolean containsValue(V value);

    Set<Map.Entry<K, V>> entrySet();

    int size();

    Iterable<String> keys();

    Dictionary<K, V> clear();

    Dictionary<K, V> putAll(Dictionary<? extends K, ? extends V> dict);

}
