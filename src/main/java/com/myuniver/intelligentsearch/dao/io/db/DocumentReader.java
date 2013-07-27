package com.myuniver.intelligentsearch.dao.io.db;

import com.google.common.base.Throwables;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.morphology.Word;
import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.structure.Dictionary;
import com.myuniver.intelligentsearch.util.DBConfigs;
import edu.ucla.sspace.text.Document;
import opennlp.tools.tokenize.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * User: Dmitry Fateev
 * Date: 26.05.13
 * Time: 18:27
 * e-mail: wearing.fateev@gmail.com
 */
public class DocumentReader implements DBReader<Document> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentReader.class);
    private Tokenizer tokenizer;
    private Filter<String> filter;
    private Stemmer stemmer;
    private Dictionary<String, Word> dictionary;
    private ResultSet set;

    public DocumentReader(Tokenizer tokenizer, Filter<String> filter, Stemmer stemmer, Dictionary<String, Word> dictionary) {
        this.tokenizer = tokenizer;
        this.filter = filter;
        this.stemmer = stemmer;
        this.dictionary = dictionary;
    }

    public Iterator<Document> iterator() {
        return new DocumentIter();
    }

    @Override
    public Collection<Document> getAll() {

        return null;
    }

    @Override
    public Document getById(int id) {

        return null;
    }

    @Override
    public boolean update(Document item) {
        return false;
    }

    @Override
    public int insert(Document item) {
        return -1;
    }

    @Override
    public Collection<Integer> insertAll(Collection<Document> items) {
        return null;
    }

    @Override
    public boolean removeById(Document item) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean removeAll(Collection<Document> items) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void open() {
        try {
            Connection con = DBConfigs.getConnection();
            set = getAllDocuments(con);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("", e);
            throw Throwables.propagate(e.getCause());
        }
    }

    private ResultSet getAllDocuments(Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement(
                "SELECT questions.question, answers.answer,answers.question_id, questions.id\n" +
                        "FROM questions,answers\n" +
                        "WHERE answers.question_id = questions.id;");
        return statement.executeQuery();
    }

    private Document processRow(Row row) {
        String[] words = tokenizer.tokenize(row.getText());
        List<String> filtered = filter.filter(Arrays.asList(words));
        List<String> stemmed = new ArrayList<>(filtered.size());
        List<Word> terms = new ArrayList<>();
        for (String token : filtered) {
            Word lemma = dictionary.get(token);
            if (lemma == null) {
                String stemm = stemmer.stemm(token);
                stemmed.add(stemm);
                terms.add(new Word("", stemm, ""));
            } else {
                terms.add(lemma);
                stemmed.add(lemma.getNormalForm());
            }
        }
        String text = QuestionNormalizer.concatWithSpace(stemmed);
        return new PrototypeDocument(text, row.getQuestionId(), terms, row.getFact(), row.getText());
    }


    private class DocumentIter implements Iterator<Document> {

        private DocumentIter() {
        }

        @Override
        public boolean hasNext() {
            try {
                return hasNext(set);
            } catch (SQLException e) {
                LOGGER.error("", e);
                throw new IllegalStateException("Can not retrieve information about next element. Check resource", e.getCause());
            }
        }

        @Override
        public Document next() {
            try {
                Row row;
                row = loadHis();
                return processRow(row);
            } catch (SQLException e) {
                LOGGER.error("", e);
                throw Throwables.propagate(e.getCause());
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove unsupported throught iterator. Use method 'removeById' or 'removeAll'");
        }

        private boolean hasNext(ResultSet resultSet) throws SQLException {
            return resultSet.next();
        }

        private Row loadHis() throws SQLException {
            String originText = set.getString("questions.question");
            String fact = set.getString("answers.answer");
            int questionId = set.getInt("questions.id");
            int answerId = set.getInt("answers.question_id");

            return Row.Builder.start().
                    setOriginText(originText).
                    setFact(fact).setQuestionId(questionId).
                    setAnswerId(answerId).
                    build();
        }

    }
}