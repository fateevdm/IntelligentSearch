package intelligentsearch.io;

import com.myuniver.intelligentsearch.util.io.StopWordReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * User: Dmitry Fateev
 * Date: 10.04.13
 * Time: 0:03
 */
public class StopWordReaderTest {
    private Logger log = LoggerFactory.getLogger(StopWordReaderTest.class);

    @Test
    public void stopWordTest() throws FileNotFoundException {
        StopWordReader stopWordReader = new StopWordReader();
        Set<String> stopWords = stopWordReader.getData();
        assertNotNull("collection should not be null", stopWords);
        assertFalse("list stop words should not be empty", stopWords.isEmpty());
        log.info("before");
        int lineNumber = 1;
        for (String symbol : stopWords) {
            log.info("line {}, symbol {}", lineNumber++, symbol);
        }
    }
}
