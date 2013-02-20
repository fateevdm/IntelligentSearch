package test.intelligentsearch.tokenizer;

import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import org.junit.Assert;
import org.junit.Test;

public class TestQuestionNormalizer {
    @Test
    public void testQuestionNormalizer() {
        String question = "� ����� ���� ���� �������� ����?";
        String[] tokens = QuestionNormalizer.tokenize(question);
        for (String word : tokens) {
            System.out.println("word = " + word);
        }

        String[] expectedTokens = {"�", "�����", "����", "����", "��������", "����"};
        Assert.assertArrayEquals("��� ������� ������ ���� ����� �����������", expectedTokens, tokens);

    }
}
