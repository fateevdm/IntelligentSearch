package com.myuniver.intelligentsearch.questionanalyzer;

import com.google.common.base.Joiner;

import java.util.List;

/**
 * This class provides methods that modify a question to facilitate pattern
 * matching and to anticipate the format of text passages that answer the
 * question.
 */
public class QuestionNormalizer {

    public static final String WHITESPACE = " ";

    public static String concatWithSpace(List<String> stemms) {
        return Joiner.on(WHITESPACE).join(stemms);
    }
}

