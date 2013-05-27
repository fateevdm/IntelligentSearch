package intelligentsearch.io;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.filters.TokenFilter;
import com.myuniver.intelligentsearch.structure.StopWords;
import com.myuniver.intelligentsearch.tokanizer.SimpleTokenizer;
import com.myuniver.intelligentsearch.util.io.db.DBConfigs;
import com.myuniver.intelligentsearch.util.io.file.StopWordsReader;
import opennlp.tools.tokenize.Tokenizer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * User: Dima
 * Date: 27.02.13
 * Time: 1:28
 */
public class DBTest {

    private Logger log = LoggerFactory.getLogger(DBTest.class);

    @Test
    public void dbTest() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = DBConfigs.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT history_question.question, history_answer.answer,history_answer.id, history_question.id\n" +
                "FROM history_question,history_answer\n" +
                "WHERE history_answer.question_id = history_question.id AND history_answer.correct =1;");
        ResultSet result = statement.executeQuery();
        Multiset<String> tokens = TreeMultiset.create();
        StopWordsReader stopWordsReader = new StopWordsReader();
        StopWords stopWords = new StopWords(stopWordsReader.getStopWords());
        Filter<String> filter = new TokenFilter(stopWords);
        Tokenizer tokenizer = new SimpleTokenizer();
        while (result.next()) {
            String text = result.getString("history_question.question") + " " + result.getString("answer");
            String[] words = tokenizer.tokenize(text);
            List<String> filteredTokens = filter.filter(Arrays.asList(words));
            tokens.addAll(filteredTokens);

        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tokens_count_filtered.csv")));
        out.println("#token ; count");
        Set<String> elements = tokens.elementSet();

        List<Pair> pairsTokens = new ArrayList<>();
        for (String token : elements) {
            pairsTokens.add(new Pair(token, tokens.count(token)));
        }
        assertEquals("размеры коллекций должны быть равны: ", elements.size(), pairsTokens.size());
        Collections.sort(pairsTokens);
        int line = 1;
        for (Pair pair : pairsTokens) {
            out.println(pair.first + " ; " + pair.second);
            log.info("line {}: [token = {}, freq = {}]", line++, pair.first, pair.second);
        }
        out.close();
    }

    @Test
    public void dbTest_2() throws SQLException, ClassNotFoundException {
        Connection connection = DBConfigs.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT history_question.question, history_answer.answer,history_answer.id, history_question.id\n" +
                "FROM history_question,history_answer\n" +
                "WHERE history_answer.question_id = history_question.id AND history_answer.correct =1;");
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            String text = result.getString("question");
            String fact = result.getString("answer");
            int questionId = result.getInt("history_question.id");
            int answerId = result.getInt("history_answer.id");
            int row = result.getRow();
            log.info("\nrow {};\nquestion.id {} = question {};\nanswer.id {} = answer {};", row, questionId, text, answerId, fact);

        }
    }

    private static class Pair<K> implements Comparable<Pair> {
        K first;
        int second;

        Pair(K first, int second) {
            this.first = first;
            this.second = second;
        }

        private K getFirst() {
            return first;
        }

        private int getSecond() {
            return second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (!first.equals(pair.first)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return first.hashCode();
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "first=" + first +
                    ", second=" + second +
                    '}';
        }

        @Override
        public int compareTo(Pair o) {
            return o.second - second;
        }
    }
}
