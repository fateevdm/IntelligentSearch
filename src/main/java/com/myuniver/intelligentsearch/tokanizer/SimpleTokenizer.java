package com.myuniver.intelligentsearch.tokanizer;

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
    public String[] tokenize(String text) {

        return tokenizer.tokenize(text);
    }

    @Override
    public Span[] tokenizePos(String text) {
        return new Span[0];
    }
}
