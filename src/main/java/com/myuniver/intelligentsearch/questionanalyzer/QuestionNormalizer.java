package com.myuniver.intelligentsearch.questionanalyzer;

import opennlp.tools.util.Span;

/**
 * This class provides methods that modify a question to facilitate pattern
 * matching and to anticipate the format of text passages that answer the
 * question.
 */
public class QuestionNormalizer {

    //    private static final String TOKENIZER = ",.;()[]{}<>!?:/|\\\"'«»„”“`´‘’‛′…¿¡\t\n\r";
    private static final String TOKENIZER = "[.,!?^:;'`~><-]+";
    private static final String WHITESPACE = " ";

    public Span[] tokenizePos(String s) {
        return new Span[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static String[] tokenize(String string) {
        String trimmedString = string.trim();
        String replacedString = trimmedString.replaceAll("[,;:\\?]", "");
        String[] tokens = replacedString.split(WHITESPACE);
        return tokens;

    }
}

