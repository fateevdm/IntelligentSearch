package com.myuniver.intelligentsearch.morphology;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.myuniver.intelligentsearch.util.Dictionary;

import java.util.Map;
import java.util.Set;

/**
 * User: Dmitry Fateev
 * Date: 04.03.13
 * Time: 3:58
 */
public class PrototypeSimpleDictionary<V> implements Dictionary<String, V> {
    private BiMap<String, V> dict;

    private PrototypeSimpleDictionary() {
        dict = HashBiMap.create();
    }

    private PrototypeSimpleDictionary(int expectedSize) {
        dict = HashBiMap.create(expectedSize);
    }

    public static <V> PrototypeSimpleDictionary<V> create() {
        return new PrototypeSimpleDictionary<>();
    }

    public static <V> PrototypeSimpleDictionary<V> create(int expectedSize) {
        return new PrototypeSimpleDictionary<>(expectedSize);
    }

    @Override
    public V get(String key) {
        return dict.get(key);
    }

    @Override
    public String getByValue(V value) {
        BiMap<V, String> invertedDict = dict.inverse();
        return invertedDict.get(value);
    }

    @Override
    public Dictionary<String, V> put(String key, V value) {
        dict.put(key, value);
        return this;
    }

    @Override
    public V delete(String key) {
        return dict.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return dict.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        return dict.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return dict.containsValue(value);
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        return dict.entrySet();
    }

    @Override
    public int size() {
        return dict.size();
    }

    @Override
    public Iterable<String> keys() {
        return dict.keySet();
    }

    @Override
    public Dictionary<String, V> clear() {
        dict.clear();
        return this;
    }

    @Override
    public Dictionary<String, V> putAll(Dictionary<? extends String, ? extends V> dict) {
        for (Map.Entry<? extends String, ? extends V> entry : dict.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public String toString() {
        return "PrototypeSimpleDictionary{" +
                "dict = " + dict +
                '}';
    }
}
