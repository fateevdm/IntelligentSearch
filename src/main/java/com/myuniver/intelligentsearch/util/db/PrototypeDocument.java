package com.myuniver.intelligentsearch.util.db;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import edu.ucla.sspace.text.Document;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

/**
 * User: Dmitry Fateev
 * Date: 06.03.13
 * Time: 2:00
 */
public class PrototypeDocument implements Comparable<PrototypeDocument>, Document {

    private final int id;
    private final String text;
    private final String fact;
    private final String originText;
    // the terms and their freqs
    private Multiset<String> terms = HashMultiset.create();

    // the length in bytes of the doc
    private int length;

    // when the doc is retrieved, it gets a score
    private double score = 0;

    public PrototypeDocument(String text, int id, List<String> terms, String fact, String originText) {
        this.fact = fact;
        this.id = id;
        this.text = text;
        this.originText = originText;
        terms.addAll(terms);
    }

    public Multiset<String> getTerms() {
        return ImmutableMultiset.copyOf(terms);
    }

    public int getLength() {
        return length;
    }

    public PrototypeDocument setLength(int length) {
        this.length = length;
        return this;
    }

    public double getScore() {
        return score;
    }

    public PrototypeDocument addScore(double score) {
        this.score += score;
        return this;
    }

    public PrototypeDocument deductScore(double score) {
        this.score -= score;
        return this;
    }

    public PrototypeDocument setScore(double score) {
        this.score = score;
        return this;
    }

    public String getText() {
        return text;
    }

    public String getFact() {
        return fact;
    }

    public int getId() {
        return id;
    }

    public String getOriginText() {
        return originText;
    }

    @Override
    public BufferedReader reader() {
        return new BufferedReader(new StringReader(text));
    }

    @Override
    public int compareTo(PrototypeDocument o) {
        if (this.score < o.score) {
            return 1;
        } else if (this.score > o.score) {
            return -1;
        } else return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PrototypeDocument other = (PrototypeDocument) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.score, other.score);
    }
}
