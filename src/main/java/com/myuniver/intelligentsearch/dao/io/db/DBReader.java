package com.myuniver.intelligentsearch.dao.io.db;

import com.myuniver.intelligentsearch.dao.io.ResourceReader;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: Dmitry Fateev
 * Date: 21.05.13
 * Time: 19:10
 * email: wearing.fateev@gmail.com
 */
public interface DBReader<T> extends ResourceReader<T> {

    public Iterator<T> iterator();

    public Collection<T> getAll();

    public T getById(int id);

    public boolean update(T item);

    public int insert(T item);

    public Collection<Integer> insertAll(Collection<T> items);

    public boolean removeById(T item);

    public boolean removeAll(Collection<T> items);

}
