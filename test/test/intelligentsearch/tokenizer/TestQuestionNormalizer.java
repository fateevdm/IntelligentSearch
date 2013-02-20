package test.intelligentsearch.tokenizer;

import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import org.junit.Assert;
import org.junit.Test;

public class TestQuestionNormalizer {
    @Test
    public void testQuestionNormalizer() {
        String question = "В каком году было Крещение руси?";
        String[] tokens = QuestionNormalizer.tokenize(question);
        for (String word : tokens) {
            System.out.println("word = " + word);
        }

        String[] expectedTokens = {"В", "каком", "году", "было", "Крещение", "руси"};
        Assert.assertArrayEquals("два массива должны быть равны поэлементно", expectedTokens, tokens);

    }
}
