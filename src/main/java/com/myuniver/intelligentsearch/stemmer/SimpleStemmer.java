package com.myuniver.intelligentsearch.stemmer;

import org.tartarus.snowball.ext.russianStemmer;

/**
 * User: Dima
 * Date: 01.02.13
 * Time: 17:21
 */
public class SimpleStemmer implements Stemmer {
    private russianStemmer stemmer = new russianStemmer();
    private static final Stemmer instance = new SimpleStemmer();

    private SimpleStemmer() {
    }

    @Override
    public String stemm(String token) {
        stemmer.setCurrent(token);
        if (stemmer.stem()) {
            return stemmer.getCurrent();
        }
        return token;
    }

    public static Stemmer getStemmer() {
        return instance;
    }
}
