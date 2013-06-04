package intelligentsearch.morfology;

import com.google.common.base.Strings;
import com.myuniver.intelligentsearch.dao.io.file.DictionaryReader;
import com.myuniver.intelligentsearch.morphology.PrototypeSimpleDictionary;
import com.myuniver.intelligentsearch.morphology.Word;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.structure.Dictionary;
import com.myuniver.intelligentsearch.util.Config;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;


/**
 * User: Dmitry Fateev
 * Date: 26.03.13
 * Time: 1:31
 */
public class SimpleDictionaryTest {
    String dictionaryPath = Config.getConfig().getProperty("dictionary.simple");
    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleDictionaryTest.class);

    @Test
    public void dictionaryTest() throws IOException {
        DictionaryReader reader = new DictionaryReader(dictionaryPath);
        Dictionary<String, Word> dictionary = reader.open();
        assertFalse(dictionary.isEmpty());
        int count = 0;
        for (Map.Entry<String, Word> entry : dictionary.entrySet()) {
            assertNotNull("must not be null", entry);
            assertTrue(!Strings.isNullOrEmpty(entry.getKey()));
            LOGGER.info("    ***** line [{}]   *****   key is [{}]   *****   value is [{}]", (++count), entry.getKey(), entry.getValue());
        }

        LOGGER.info("size dic {}", dictionary.size());

        Set<Word> words = reader.getWordsSet();
        LOGGER.info("words set size {}", words.size());
        for (Word word : words) {
            assertNotNull("must not be null", word);
        }


    }

    @Test
    public void dictionaryTest_2() {
        Dictionary<String, Word> dict = PrototypeSimpleDictionary.create();
        Word word1 = new Word("являться", SimpleStemmer.getStemmer().stemm("является")).addMorpheme("является");
        dict.put("является", word1);
        Word word2 = new Word("являться", SimpleStemmer.getStemmer().stemm("являлось")).addMorpheme("являлось");
        dict.put("являлось", word2);
        for (Map.Entry<String, Word> entry : dict.entrySet()) {
            LOGGER.info("key {}; value {}", entry.getKey(), entry.getValue());
        }
    }
}
