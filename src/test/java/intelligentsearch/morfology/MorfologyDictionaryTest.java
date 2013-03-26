package intelligentsearch.morfology;

import com.myuniver.intelligentsearch.morphology.MorphologicalDictionary;
import org.junit.Test;

import java.util.Iterator;

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
        dictionary.put("революционный", "rev");
        dictionary.put("революцией", "rev");
        String val = dictionary.get("революция");
        assertEquals("rev", val);
        assertFalse("если элемента нет в словаре, то должно быть false", dictionary.contains("привет"));
        Iterable<String> prefixes = dictionary.prefixMatch("револ");
        Iterator<String> iter = prefixes.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        String longestPrefix = dictionary.longestPrefixOf("революционная");
        System.out.println("prefix: " + longestPrefix);
        assertEquals("революционн", longestPrefix);

    }
}
