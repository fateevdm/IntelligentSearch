package intelligentsearch.io;

import com.google.common.base.Strings;
import com.myuniver.intelligentsearch.util.Config;
import com.myuniver.intelligentsearch.util.io.FileReader;
import com.myuniver.intelligentsearch.util.io.ResourceReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

import static com.myuniver.intelligentsearch.structure.StopWords.STOP_WORD_FILE;
import static org.junit.Assert.assertFalse;

/**
 * User: Dmitry Fateev
 * Date: 10.04.13
 * Time: 0:03
 */
public class StopWordReaderTest {
    private Logger log = LoggerFactory.getLogger(StopWordReaderTest.class);

    @Test
    public void stopWordTest() throws FileNotFoundException {
        ResourceReader<String> fileReader = FileReader.createByFile(Config.getConfig().getProperty(STOP_WORD_FILE));
        fileReader.open();
        for (String word : fileReader) {
            assertFalse("stop word should not be empty or null", Strings.isNullOrEmpty(word));
        }
    }
}