package intelligentsearch.filter;

import com.google.common.collect.ImmutableList;
import com.myuniver.intelligentsearch.dao.io.file.StopWordsReader;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.filters.TokenFilter;
import com.myuniver.intelligentsearch.structure.StopWords;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Dmitry Fateev
 * Date: 24.05.13
 * Time: 0:28
 */
public class TokenFilterTest {

    @Test
    public void filterTest() {
        ImmutableList tokens = ImmutableList.of("13", "!", "я", "1812", "работа");
        StopWordsReader stopWordsReader = new StopWordsReader();
        StopWords stopWords = new StopWords(stopWordsReader.getStopWords());
        Filter<String> filter = new TokenFilter(stopWords);
        List<String> filteredTokens = filter.filter(tokens);
        List<String> expected = new ArrayList<>();
        expected.add("работа");
        Assert.assertEquals(expected, filteredTokens);
    }
}
