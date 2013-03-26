package intelligentsearch.tokenizer;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

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
        for (String word : tokens) {
            System.out.println("word = " + word);
        }
        String[] expectedTokens = {"В", "каком", "году", "было", ",", "Крещение", "Руси", "?"};
        assertArrayEquals("массивы должны быть равны: ", expectedTokens, tokens);

    }
}
