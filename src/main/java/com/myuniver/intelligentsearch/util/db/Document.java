package com.myuniver.intelligentsearch.util.db;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

/**
 * User: Dmitry Fateev
 * Date: 06.03.13
 * Time: 2:00
 */
public class Document implements Comparable<Document> {

    private int id;
    private String text;
    private String fact;
    // the terms and their freqs
    private Multiset<String> terms;

    // the length in bytes of the doc
    private int length;

    // when the doc is retrieved, it gets a score
    private double score = 0;

    public Document(String text, int id, Multiset<String> terms, String fact) {
        this.fact = fact;
        this.terms = terms;
        this.id = id;
        this.text = text;
    }

    public Multiset<String> getTerms() {
        return ImmutableMultiset.copyOf(terms);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getScore() {
        return score;
    }

    public void addScore(double score) {
        this.score += score;
    }

    public void setScore(double score) {
        this.score = score;
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

    @Override
    public int compareTo(Document o) {
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
        final Document other = (Document) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.score, other.score);
    }
}
