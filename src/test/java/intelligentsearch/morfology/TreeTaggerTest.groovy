package intelligentsearch.morfology

import org.annolab.tt4j.TokenHandler
import org.annolab.tt4j.TreeTaggerWrapper
import org.junit.Test

import static java.util.Arrays.asList

/**
 * User: Dmitry Fateev
 * Date: 18.06.13
 * Time: 2:53
 * e-mail: wearing.fateev@gmail.com
 */
class TreeTaggerTest {

    @Test
    void treeTaggerTest() {
        TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
        System.setProperty("treetagger.home", "D:\\dev\\projects\\IntelligentSearch\\lib\\TreeTagger\\");
        try {
            tt.setModel("D:\\dev\\projects\\IntelligentSearch\\src\\main\\resources\\russian.par");
            tt.setHandler(new TokenHandler<String>() {
                public void token(String token, String pos, String lemma) {
                    System.out.println(token + "\t" + pos + "\t" + lemma);
                }
            });
            tt.process(asList("Это", "просто", "тест", "программы", "."));
        }
        finally {
            tt.destroy();
        }
    }
}
