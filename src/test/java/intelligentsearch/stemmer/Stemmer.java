package intelligentsearch.stemmer;

import org.junit.Test;
import org.tartarus.snowball.ext.RussianStemmer;

/**
 * User: Dmitry Fateev
 * Date: 16.03.13
 * Time: 0:21
 */
public class Stemmer {

    @Test
    public void stemmerTest() {
        RussianStemmer stemmer = new RussianStemmer();
        String[] expectedTokens = {"В", "каком", "году", "было", "Крещение", "Руси"};
        for (String word : expectedTokens) {
            stemmer.setCurrent(word);
            stemmer.stem();
            System.out.println("stem= " + stemmer.getCurrent());
        }
        String question = "Наука, изучающая не только законы и закономерности общественного развития в целом, но и конкретные процессы становления, развития и преобразования различных стран и народов во всем их многообразии и неповторимости:";


    }
}
