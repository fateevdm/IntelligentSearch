package test.intelligentsearch.morfology;

import com.myuniver.intelligentsearch.morphology.MorphologicalDictionary;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * User: Dima
 * Date: 22.02.13
 * Time: 0:50
 */
public class MorfologyDictionaryTest {

    @Test
    public void putAndGetTest() {
        MorphologicalDictionary<String> dictionary = new MorphologicalDictionary<>();
        dictionary.put("революция", "rev");
        String val = dictionary.get("революция");
        assertEquals("rev", val);
        assertFalse("если элемента нет в словаре, то должно быть false", dictionary.contains("привет"));
        dictionary.prefixMatch("револ");

    }
}
