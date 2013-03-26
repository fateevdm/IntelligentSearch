package intelligentsearch.io;

import com.myuniver.intelligentsearch.util.io.DictionaryReader;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * User: Dima
 * Date: 20.02.13
 * Time: 0:34
 */
public class DictionaryReaderTest {


    @Test
    public void testDAO() throws FileNotFoundException {
        String filePath = "src/main/resources/dictionary/dic_hist.txt";
        DictionaryReader dictionaryReaderFile = new DictionaryReader(filePath);
        dictionaryReaderFile.openConnection();
    }
}
