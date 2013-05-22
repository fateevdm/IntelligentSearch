package intelligentsearch.tokenizer;

import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * User: Dmitry Fateev
 * Date: 14.03.13
 * Time: 0:12
 */
public class TokenizerTest {
    Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

    @Test
    public void testToken() throws IOException {
        String[] tokens = tokenizer.tokenize("В каком году было, Крещение Руси?");
        String[] expectedTokens = {"В", "каком", "году", "было", ",", "Крещение", "Руси", "?"};
        assertArrayEquals("массивы должны быть равны: ", expectedTokens, tokens);

    }

    @Test
    public void testToken_2() {
        tokenizer = new com.myuniver.intelligentsearch.tokenizer.SimpleTokenizer();
        String[] tokens = tokenizer.tokenize("А.Д.Михайлов)");
        assertArrayEquals(tokens, new String[]{"А", ".", "Д", ".", "Михайлов", ")"});
    }

    @Test
    public void detokenizerTest() {
        String[] expectedTokens = {"В", "каком", "году", "было", "Крещение", "Руси"};
        String detoken = QuestionNormalizer.concatWithSpace(Arrays.asList(expectedTokens));
        assertEquals(detoken, "В каком году было Крещение Руси");
    }
}
