package com.myuniver.intelligentsearch.tokenizer;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

/**
 * User: Dmitry Fateev
 * Date: 17.03.13
 * Time: 20:10
 */
public class SimpleTokenizer implements Tokenizer {
    private Tokenizer tokenizer = opennlp.tools.tokenize.SimpleTokenizer.INSTANCE;

    @Override
    public String[] tokenize(String s) {

        return tokenizer.tokenize(s);
    }

    @Override
    public Span[] tokenizePos(String s) {
        return new Span[0];
    }
}
