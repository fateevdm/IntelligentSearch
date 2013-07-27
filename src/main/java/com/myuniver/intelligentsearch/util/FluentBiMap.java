package com.myuniver.intelligentsearch.util;

import com.google.common.collect.BiMap;

/**
 * User: Dmitry Fateev
 * Date: 25.07.13
 * Time: 23:43
 * e-mail: wearing.fateev@gmail.com
 */
public class FluentBiMap<K, V> {

    private BiMap<K, V> biMap;

    private FluentBiMap(BiMap<K, V> instance) {
        this.biMap = instance;
    }

    @SuppressWarnings("uncheked")
    public static <K, V> FluentBiMap<K, V> wrap(BiMap<K, V> forWrap) {

        return new FluentBiMap<K, V>(forWrap);
    }

    public FluentBiMap<K, V> put(K key, V value) {
        biMap.put(key, value);
        return this;
    }

    public FluentBiMap<K, V> put(Pair<K, V>... pairs) {
        for (Pair<K, V> pair : pairs) biMap.put(pair.first(), pair.second());
        return this;
    }

    public FluentBiMap<K, V> put(Iterable<BiMap.Entry<K, V>> entries) {
        for (BiMap.Entry<K, V> entry : entries) biMap.put(entry.getKey(), entry.getValue());
        return this;
    }

    public int size() {
        return biMap.size();
    }
}
