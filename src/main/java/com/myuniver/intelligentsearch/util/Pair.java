package com.myuniver.intelligentsearch.util;

import com.google.common.base.Objects;

/**
 * User: Dmitry Fateev
 * Date: 26.07.13
 * Time: 0:17
 * e-mail: wearing.fateev@gmail.com
 */
public class Pair<K, V> {

    private final K first;
    private final V second;

    public static <K, V> Pair<K, V> pair(K first, V second) {
        return new Pair<>(first, second);
    }

    private Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K first() {
        return first;
    }

    public V second() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        return Objects.equal(this.first, other.first) && Objects.equal(this.second, other.second);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }


}

