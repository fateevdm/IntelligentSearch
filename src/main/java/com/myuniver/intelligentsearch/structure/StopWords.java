package com.myuniver.intelligentsearch.structure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Dmitry Fateev
 * Date: 27.05.13
 * Time: 16:34
 * email: wearing.fateev@gmail.com
 */
public class StopWords {

    private Set<String> symbols = new HashSet<>();

    public StopWords(Set<String> symbols) {
        this.symbols.addAll(symbols);
    }

    public boolean contains(String word) {
        return symbols.contains(word);
    }

    public boolean containsAll(Collection<String> col) {
        return symbols.containsAll(col);
    }

    public int size() {
        return symbols.size();
    }
}
