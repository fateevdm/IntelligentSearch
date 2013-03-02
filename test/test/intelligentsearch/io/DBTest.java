package test.intelligentsearch.io;

import org.junit.Test;

import java.sql.*;

/**
 * User: Dima
 * Date: 27.02.13
 * Time: 1:28
 */
public class DBTest {

    @Test
    public void dbTest() throws SQLException {
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
        while (result.next()) {
            System.out.println(result.getString("math_question.question") + " " + result.getString("answer"));
        }

    }
}
