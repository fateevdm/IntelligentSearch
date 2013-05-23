package com.myuniver.intelligentsearch.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Dmitry Fateev
 * Date: 29.03.13
 * Time: 19:29
 * email: wearing.fateev@gmail.com
 */
public class TokenFilter implements Filter<String> {
    private Set<String> excluded;

    public TokenFilter(Set<String> excluded) {
        this.excluded = excluded;
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
        return !(excluded.contains(token));
    }
}
