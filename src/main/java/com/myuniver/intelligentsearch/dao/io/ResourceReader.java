package com.myuniver.intelligentsearch.dao.io;

/**
 * User: Dmitry Fateev
 * Date: 22.05.13
 * Time: 0:45
 */
public interface ResourceReader<T> extends Iterable<T> {


    void open();
}
