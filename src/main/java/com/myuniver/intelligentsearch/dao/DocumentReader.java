package com.myuniver.intelligentsearch.dao;

import com.myuniver.intelligentsearch.Word;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.util.db.DBReader;
import com.myuniver.intelligentsearch.util.db.PrototypeDocument;
import com.myuniver.intelligentsearch.util.db.Row;
import edu.ucla.sspace.text.Document;
import opennlp.tools.tokenize.Tokenizer;

import java.util.*;

/**
 * User: Dmitry Fateev
 * Date: 26.05.13
 * Time: 18:27
 * e-mail: wearing.fateev@gmail.com
 */
public class DocumentReader extends DBReader<Document> {

    private Tokenizer tokenizer;
    private Filter<String> filter;
    private Stemmer stemmer;
    Dictionary<String, Word> dictionary;


    public Iterator<Document> documentIterator() {
        Iterator<Row> iter = super.iterator();
        return new DocumentIter(iter);
    }

    @Override
    public Collection<Document> getAll() {

        return super.getAll();
    }

    @Override
    public Document getById(int id) {

        return super.getById(id);
    }

    @Override
    public boolean update(Document item) {
        return super.update(item);
    }

    @Override
    public int insert(Document item) {
        return super.insert(item);
    }

    @Override
    public Collection<Integer> insertAll(Collection<Document> items) {
        return super.insertAll(items);
    }
    private Document processRow(Row row){
        String[] words = tokenizer.tokenize(row.getText());
        List<String> filtered = filter.filter(Arrays.asList(words));
        List<String> stemmed = new ArrayList<>(filtered.size());
        for (String token : filtered) {
            Word lemma = dictionary.get(token);
            if (lemma == null) {
                String stemm = stemmer.stemm(token);
                stemmed.add(stemm);
            } else {
                stemmed.add(lemma.getStemma());
            }
        }
        String filteredText = QuestionNormalizer.concatWithSpace(stemmed);
        Document document = new PrototypeDocument(filteredText, row.getQuestionId(), filtered, PrototypeDocument.QUESTION, row.getText());
        return document;
    }
    private class DocumentIter implements Iterator<Document>{
        Iterator<Row> dbIter;
        DocumentIter(Iterator<Row> i){
            this.dbIter = i;
        }
        @Override
        public boolean hasNext() {

            return dbIter.hasNext();
        }

        @Override
        public Document next() {
            Row row = dbIter.next();

            return processRow(row);
        }

        @Override
        public void remove() {
            dbIter.remove();
        }
    }
}
