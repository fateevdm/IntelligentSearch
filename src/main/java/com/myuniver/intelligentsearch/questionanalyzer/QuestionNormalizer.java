package com.myuniver.intelligentsearch.questionanalyzer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * This class provides methods that modify a question to facilitate pattern
 * matching and to anticipate the format of text passages that answer the
 * question.
 */
public class QuestionNormalizer {

    public static final String WHITESPACE = " ";
    private static Function<String, String> trim = new Function<String, String>() {
        @Override
        public String apply(String input) {
            return input.trim();
        }
    };


    public static String concatWithSpace(List<String> stemms) {
        List transform = Lists.transform(stemms, trim);
        return Joiner.on(WHITESPACE).join(transform);
    }
}

