package intelligentsearch.io;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.myuniver.intelligentsearch.tokenizer.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
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

    @Test
    public void dbTest() throws SQLException, IOException {
        String driverClassName = "com.mysql.jdbc.Driver";
        String connectionUrl = "jdbc:mysql://localhost:3306/moi_univer";
        String dbUser = "root";
        String dbPwd = "matans11";
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
        PreparedStatement statement = connection.prepareStatement("SELECT math_question.question, math_answer.answer\n" +
                "FROM math_question,math_answer\n" +
                "WHERE math_answer.question_id = math_question.id AND math_answer.correct =1;");
        ResultSet result = statement.executeQuery();
        Multiset<String> tokens = TreeMultiset.create();
        Tokenizer tokenizer = new SimpleTokenizer();
        while (result.next()) {
            String text = result.getString("math_question.question") + " " + result.getString("answer");
//            System.out.println(result.getString("math_question.question") + " " + result.getString("answer"));
            String[] words = tokenizer.tokenize(text);
            Collections.addAll(tokens, words);

        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tokens_count.csv")));
        out.println("#token | count");
        Set<String> elements = tokens.elementSet();

        List<Pair> pairsTokens = new ArrayList<>();
        for (String token : elements) {
//            out.println(token + "\t" + tokens.count(token));

            pairsTokens.add(new Pair(token, tokens.count(token)));
        }
        assertEquals("размеры коллекций должны быть равны: ", elements.size(), pairsTokens.size());
        Collections.sort(pairsTokens);
        for (Pair pair : pairsTokens) {
            out.println(pair.first + " ; " + pair.second);
            System.out.println(pair);
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
