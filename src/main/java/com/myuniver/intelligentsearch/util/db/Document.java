package com.myuniver.intelligentsearch.util.db;

import java.util.HashMap;

/**
 * User: Dmitry Fateev
 * Date: 06.03.13
 * Time: 2:00
 */
public class Document implements Comparable<Document> {

    private String text;
    // the terms and their freqs
    private HashMap<String, Integer> terms;

    // the length in bytes of the doc
    private int length;

    // when the doc is retrieved, it gets a score
    private double score = 0;

    public Document(String text) {
        this.text = text;
    }

    public HashMap<String, Integer> getTerms() {
        return terms;
    }

    public void setTerms(HashMap<String, Integer> terms) {
        this.terms = terms;
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

    @Override
    public int compareTo(Document o) {
        if (this.score < o.score) {
            return 1;
        } else if (this.score > o.score) {
            return -1;
        } else return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (!text.equals(document.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
