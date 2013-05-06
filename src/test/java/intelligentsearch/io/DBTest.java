package intelligentsearch.io;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.filters.TokenFilter;
import com.myuniver.intelligentsearch.tokenizer.SimpleTokenizer;
import com.myuniver.intelligentsearch.util.db.DBConfigs;
import com.myuniver.intelligentsearch.util.io.StopWordReader;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        PreparedStatement statement = connection.prepareStatement("SELECT math_question.question, math_answer.answer,math_answer.id, math_question.id\n" +
                "FROM math_question,math_answer\n" +
                "WHERE math_answer.question_id = math_question.id AND math_answer.correct =1;");
        ResultSet result = statement.executeQuery();
        Multiset<String> tokens = TreeMultiset.create();
        StopWordReader stopWordReader = new StopWordReader();
        Set<String> stopWords = stopWordReader.getData();
        Filter filter = new TokenFilter(stopWords);
        Tokenizer tokenizer = new SimpleTokenizer();
        while (result.next()) {
            String text = result.getString("math_question.question") + " " + result.getString("answer");
            String[] words = tokenizer.tokenize(text);
            List<String> filteredTokens = filter.filter(words);
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
