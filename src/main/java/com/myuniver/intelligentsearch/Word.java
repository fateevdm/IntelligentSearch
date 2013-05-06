package com.myuniver.intelligentsearch;


import com.google.common.base.Objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Dima
 * Date: 23.10.12
 * Time: 2:39
 */
public class Word implements Comparable<Word> {
    private final String normalForm;
    private final String stemma;
    private Set<String> derivativeForms;
    private double weight;

    public Word(String normalForm, String stemma) {
        this.normalForm = normalForm;
        this.stemma = stemma;
        this.derivativeForms = new HashSet<>();
    }

    public String getNormalForm() {
        return normalForm;
    }

    public Set<String> getDerivativeForms() {
        return derivativeForms;
    }

    public double getWeight() {
        return weight;
    }

    public Word setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public Word addMorpheme(String morpheme) {
        this.derivativeForms.add(morpheme);
        return this;
    }

    public Word addMorphems(Collection<String> morphems) {
        derivativeForms.addAll(morphems);
        return this;
    }

    @Override
    public int compareTo(Word o) {
        return this.stemma.compareToIgnoreCase(o.stemma);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(normalForm, stemma, derivativeForms);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Word other = (Word) obj;
        return Objects.equal(this.normalForm, other.normalForm) && Objects.equal(this.stemma, other.stemma) && Objects.equal(this.derivativeForms, other.derivativeForms);
    }

    @Override
    public String toString() {
        return "Word{" +
                "normalForm='" + normalForm + '\'' +
                ", stemma='" + stemma + '\'' +
                ", derivativeForms=" + derivativeForms +
                ", weight=" + weight +
                '}';
    }
}
