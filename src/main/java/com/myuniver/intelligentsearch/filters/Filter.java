package com.myuniver.intelligentsearch.filters;

import java.util.List;

/**
 * User: Dmitry Fateev
 * Date: 29.03.13
 * Time: 19:06
 * email: wearing.fateev@gmail.com
 */
public interface Filter<T> {

    List<T> filter(List<T> tokens);
}
