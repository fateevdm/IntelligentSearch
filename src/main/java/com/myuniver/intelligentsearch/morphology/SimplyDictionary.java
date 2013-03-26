package com.myuniver.intelligentsearch.morphology;

import com.myuniver.intelligentsearch.util.Dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Dmitry Fateev
 * Date: 04.03.13
 * Time: 3:58
 */
public class SimplyDictionary implements Dictionary<String, String> {

    private Map<String, String> dictionary = new HashMap<>();


    @Override
    public void put(String key, String value) {
        dictionary.put(key, value);
    }

    @Override
    public String get(String key) {
        return dictionary.get(key);
    }

    @Override
    public void delete(String key) {
        dictionary.put(key, null);
    }

    @Override
    public boolean isEmpty() {
        return dictionary.isEmpty();
    }

    @Override
    public boolean contains(String key) {
        return dictionary.containsKey(key);
    }

    @Override
    public int size() {
        return dictionary.size();
    }

    @Override
    public Iterable<String> keys() {
        return dictionary.keySet();
    }
}
