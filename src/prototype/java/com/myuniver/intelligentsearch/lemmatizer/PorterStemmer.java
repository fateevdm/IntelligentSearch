package com.myuniver.intelligentsearch.lemmatizer;


import org.tartarus.snowball.ext.russianStemmer;

/**
 * User: Dmitry Fateev
 * Date: 15.03.13
 * Time: 23:25
 */
public class PorterStemmer {
    private final russianStemmer stemmer = new russianStemmer();

    public String stemm(String string) {
        String copy = string;
        stemmer.setCurrent(copy);
        if (stemmer.stem()) {
            return stemmer.getCurrent();
        }
        return string;
    }


}
