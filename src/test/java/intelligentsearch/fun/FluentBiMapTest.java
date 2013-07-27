package intelligentsearch.fun;

import com.google.common.collect.HashBiMap;
import com.myuniver.intelligentsearch.util.FluentBiMap;
import org.junit.Test;

import static com.myuniver.intelligentsearch.util.FluentBiMap.wrap;
import static com.myuniver.intelligentsearch.util.Pair.pair;
import static org.junit.Assert.assertEquals;

/**
 * User: Dmitry Fateev
 * Date: 25.07.13
 * Time: 23:39
 * e-mail: wearing.fateev@gmail.com
 */
public class FluentBiMapTest {

    @Test
    public void testChainingFillingMap() {
        FluentBiMap<String, String> biMap = wrap(HashBiMap.<String, String>create());
        biMap.put("a", "b").put("c", "d");
        assertEquals(biMap.size(), 2);
    }

    @Test
    public void testVarargsFillingMap() {
        FluentBiMap<String, String> biMap = wrap(HashBiMap.<String, String>create());
        //noinspection unchecked
        biMap.put(pair("a", "b"), pair("c", "d"));
        assertEquals(biMap.size(), 2);
    }

    @Test
    public void testIterablesFillingMap() {
        FluentBiMap<String, String> biMap = wrap(HashBiMap.<String, String>create());
        biMap.put();
    }
}
