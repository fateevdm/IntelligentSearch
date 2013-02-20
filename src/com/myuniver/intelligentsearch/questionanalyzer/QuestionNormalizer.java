package com.myuniver.intelligentsearch.questionanalyzer;

/**
 * This class provides methods that modify a question to facilitate pattern
 * matching and to anticipate the format of text passages that answer the
 * question.
 */
public class QuestionNormalizer {

    private static final String TOKENIZER = "[^a-zA-Z_0-9a-ÿÀ-ß@+]";
    private static final String PUNCTUATION = "[!$\\^:\\?#<>\\*%]";

    private static final String QU = "[^a-zA-Z_0-9a-ÿÀ-ß@+]";
    private static final String SPLIT = "^[" + TOKENIZER + PUNCTUATION + "]";

    public static String[] tokenize(String string) {

        String trimmedString = string.trim();
        String[] tokens = trimmedString.split(TOKENIZER);
        return tokens;

    }
}

