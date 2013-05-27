package com.myuniver.intelligentsearch.util.db;

import com.google.common.base.Throwables;
import com.myuniver.intelligentsearch.util.io.ResourceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import static com.myuniver.intelligentsearch.util.db.Row.Builder;

/**
 * User: Dmitry Fateev
 * Date: 21.05.13
 * Time: 19:10
 * email: wearing.fateev@gmail.com
 */
public class DBReader<T> implements ResourceReader<Row> {

    private static final Logger log = LoggerFactory.getLogger(DBReader.class);
    private ResultSet set;

    public Iterator<Row> iterator() {
        return new DBIterator();
    }

    public Collection<T> getAll(){
        return null;
    }

    public T getById(int id){
        return null;
    }

    public boolean update(T item){
        return true;
    }

    public int insert(T item){
        return -1;
    }

    public Collection<Integer> insertAll(Collection<T> items){
        return null;
    }

    @Override
    public void open() {
        try {
            Connection con = DBConfigs.getConnection();
            set = prepareDocuments(con);
        } catch (ClassNotFoundException | SQLException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private ResultSet prepareDocuments(Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement("SELECT history_question.question, history_answer.answer,history_answer.question_id, history_question.id\n" +
                "FROM history_question,history_answer\n" +
                "WHERE history_answer.question_id = history_question.id AND history_answer.correct =1;");
        return statement.executeQuery();
    }

    private class DBIterator implements Iterator<Row> {

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
            try {
                String originText = set.getString("question");
                String fact = set.getString("answer");
                int questionId = set.getInt("history_question.id");
                int answerId = set.getInt("history_answer.question_id");
                return Builder.start().setOriginText(originText).setFact(fact).setQuestionId(questionId).setAnswerId(answerId).build();
            } catch (SQLException e) {
                throw Throwables.propagate(e.getCause());
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
