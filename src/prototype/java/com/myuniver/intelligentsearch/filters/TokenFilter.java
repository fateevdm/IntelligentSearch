package com.myuniver.intelligentsearch.filters;

import java.util.List;
import java.util.Set;

/**
 * User: Dmitry Fateev
 * Date: 29.03.13
 * Time: 19:29
 * email: wearing.fateev@gmail.com
 */
public class TokenFilter implements Filter {
    private Set<String> excluded;

    public TokenFilter(Set<String> excluded) {
        this.excluded = excluded;
    }

    @Override
    public List<String> filter(String[] filter) {

        return null;
    }

    protected boolean accept(String token) {
        return excluded.contains(token);
    }
}
