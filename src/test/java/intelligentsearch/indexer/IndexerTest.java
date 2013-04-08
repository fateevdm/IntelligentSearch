package intelligentsearch.indexer;

import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.text.DocumentPreprocessor;
import edu.ucla.sspace.text.StringDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

/**
 * User: Dmitry Fateev
 * Date: 04.03.13
 * Time: 3:56
 */
public class IndexerTest {
    private String text = "Наука, изучающая не только законы и закономерности общественного развития в целом, но и конкретные процессы становления, развития и преобразования различных стран и народов во всем их многообразии и неповторимости:";

    @Test
    public void testIndexer() throws IOException {
        DocumentPreprocessor docPreprocessor = new DocumentPreprocessor();
//        String document = docPreprocessor.process("Наука, изучающая не только законы и закономерности общественного развития в целом, но и конкретные процессы становления, развития и преобразования различных стран и народов во всем их многообразии и неповторимости:");
//        System.out.println(document);
        StringDocument document = new StringDocument(text);
        LatentSemanticAnalysis lsa = new LatentSemanticAnalysis();
        lsa.processDocument(document.reader());
        Set<String> words = lsa.getWords();
        for (String word : words) {
            System.out.println("word = " + word);
        }


    }
}
