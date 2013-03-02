package test.intelligentsearch.morfology;

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
        dictionary.put("���������", "rev");
        dictionary.put("�������������", "rev");
        dictionary.put("����������", "rev");
        String val = dictionary.get("���������");
        assertEquals("rev", val);
        assertFalse("���� �������� ��� � �������, �� ������ ���� false", dictionary.contains("������"));
        Iterable<String> prefixes = dictionary.prefixMatch("�����");
        Iterator<String> iter = prefixes.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        String longestPrefix = dictionary.longestPrefixOf("�������������");
        assertEquals("�����������", longestPrefix);

    }
}
