package com.myuniver.intelligentsearch.util.io;

import java.util.Iterator;

/**
 * User: Dmitry Fateev
 * Date: 22.05.13
 * Time: 0:45
 */
public interface ResourceReader {

    <T> Iterator<T> iterator();

    <V extends ResourceReader> V open();
}
