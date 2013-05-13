package intelligentsearch.morfology;

import com.google.common.collect.Multiset;
import com.myuniver.intelligentsearch.Word;
import com.myuniver.intelligentsearch.morphology.PrototypeSimpleDictionary;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.util.Config;
import com.myuniver.intelligentsearch.util.Dictionary;
import com.myuniver.intelligentsearch.util.io.DictionaryReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * User: Dmitry Fateev
 * Date: 26.03.13
 * Time: 1:31
 */
public class SimpleDictionaryTest {
    String dictionaryPath = Config.getConfig().getProperty("dictionary.simple");
    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleDictionaryTest.class);

    @Test
    public void dictionaryTest() throws FileNotFoundException {
        DictionaryReader reader = new DictionaryReader(dictionaryPath);
        Dictionary<String, Word> dictionary = reader.openConnection();
        assertFalse(dictionary.isEmpty());
        int count = 0;
        for (Map.Entry<String, Word> entry : dictionary.entrySet()) {
            assertNotNull("must not be null", entry);
            LOGGER.info("    ***** line [{}]   *****   key is [{}]   *****   value is [{}]", (++count), entry.getKey(), entry.getValue());
        }

        LOGGER.info("size dic {}", dictionary.size());

        Multiset<Word> words = reader.getWordsSet();
        LOGGER.info("words set size {}", words.size());
        for (Multiset.Entry<Word> entry : words.entrySet()) {
            LOGGER.info("  word [{}] ;   count [{}]  ", entry.getElement(), entry.getCount());
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
