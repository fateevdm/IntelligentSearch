package com.myuniver.intelligentsearch.util.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * User: Dmitry Fateev
 * Date: 21.05.13
 * Time: 19:10
 * email: wearing.fateev@gmail.com
 */
public class DB {

    private ResultSet set;
    private static final Logger log = LoggerFactory.getLogger(DB.class);

    public Iterator<Row> teratorThroughDocuments() {
        return new DBIterator();
    }


    private static class DBIterator implements Iterator<Row> {

        @Override
        public boolean hasNext() {
            try {
                return set.next();
            } catch (SQLException e) {
                log.error("", e);
            }
            return false;
        }

        @Override
        public Row next() {

            return null;
        }

        @Override
        public void remove() {

        }
    }

}
