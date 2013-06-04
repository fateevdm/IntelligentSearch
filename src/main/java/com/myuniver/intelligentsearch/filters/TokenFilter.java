package com.myuniver.intelligentsearch.filters;

import com.google.common.base.CharMatcher;
import com.myuniver.intelligentsearch.structure.StopWords;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Dmitry Fateev
 * Date: 29.03.13
 * Time: 19:29
 * email: wearing.fateev@gmail.com
 */
public class TokenFilter implements Filter<String> {
    private StopWords stopWords;

    public TokenFilter(StopWords stopWords) {
        this.stopWords = stopWords;
    }

    @Override
    public List<String> filter(List<String> tokens) {
        List<String> accepted = new ArrayList<>();
        for (String token : tokens) {
            token = token.toLowerCase();
            if (accept(token)) {
                accepted.add(token);
            }
        }
        return accepted;
    }


    protected boolean accept(String token) {
        return !((stopWords.contains(token)) || CharMatcher.DIGIT.matchesAllOf(token));
    }
}
