package com.myuniver.intelligentsearch;


import java.util.List;

/**
 * User: Dima
 * Date: 23.10.12
 * Time: 2:39
 */
public class Word implements Comparable<Word> {
    private String normalForm;
    private List<String> derivativeForms;
    private double weight;

    public Word(String normalForm, List<String> derivativeForms, double weight) {
        this.normalForm = normalForm;
        this.derivativeForms = derivativeForms;
        this.weight = weight;
    }

    public String getNormalForm() {
        return normalForm;
    }

    public List<String> getDerivativeForms() {
        return derivativeForms;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Word o) {
        return this.normalForm.compareToIgnoreCase(o.normalForm);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (!normalForm.equals(word.normalForm)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return normalForm.hashCode();
    }

}
