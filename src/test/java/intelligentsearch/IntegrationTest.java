package intelligentsearch;

import com.myuniver.intelligentsearch.Word;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.filters.TokenFilter;
import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.tokenizer.SimpleTokenizer;
import com.myuniver.intelligentsearch.util.Config;
import com.myuniver.intelligentsearch.util.Dictionary;
import com.myuniver.intelligentsearch.util.db.DBConfigs;
import com.myuniver.intelligentsearch.util.db.PrototypeDocument;
import com.myuniver.intelligentsearch.util.io.DictionaryReader;
import com.myuniver.intelligentsearch.util.io.StopWordReader;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.vector.DoubleVector;
import opennlp.tools.tokenize.Tokenizer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * User: Dmitry Fateev
 * Date: 05.05.13
 * Time: 1:13
 */
public class IntegrationTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);
    String dictionaryPath = Config.getConfig().getProperty("dictionary.simple");

    @Test
    public void runIntegrationTest() throws IOException, SQLException, ClassNotFoundException {
        LOGGER.info("=======first step - load data from files =======");
        DictionaryReader reader = new DictionaryReader(dictionaryPath);
        Dictionary<String, Word> dictionary = reader.openConnection();
        Stemmer stemmer = SimpleStemmer.getStemmer();
        LOGGER.info("=======second step - load data from DB =======");
        Connection connection = DBConfigs.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT math_question.question, math_answer.answer,math_answer.question_id, math_question.id\n" +
                "FROM math_question,math_answer\n" +
                "WHERE math_answer.question_id = math_question.id AND math_answer.correct =1;");
        ResultSet result = statement.executeQuery();
        LOGGER.info("=======third step - process documents from DB=======");
        StopWordReader stopWordReader = new StopWordReader();
        Set<String> stopWords = stopWordReader.getData();
//        Multiset<String> tokens = TreeMultiset.create();
        Filter filter = new TokenFilter(stopWords);
        Tokenizer tokenizer = new SimpleTokenizer();
        List<Document> documents = new ArrayList<>();
        while (result.next()) {
            String originText = result.getString("question");
            String fact = result.getString("answer");
            int questionId = result.getInt("math_question.id");
            int answerId = result.getInt("math_answer.question_id");
            assertEquals("id вопроса должен совпадать с полем question_id", answerId, questionId);
            String[] words = tokenizer.tokenize(originText);
            List<String> filteredTokens = filter.filter(words);
            List<String> stemmedTokens = new ArrayList<>(filteredTokens.size());
            for (String token : filteredTokens) {
                Word lemma = dictionary.get(token);
                if (lemma == null) {
                    String stemm = stemmer.stemm(token);
                    stemmedTokens.add(stemm);
                } else {
                    stemmedTokens.add(lemma.getStemma());
                }

            }
            String filteredText = QuestionNormalizer.concatWithSpace(stemmedTokens);
            Document document = new PrototypeDocument(filteredText, questionId, stemmedTokens, fact, originText);
            documents.add(document);
        }

        LatentSemanticAnalysis lsa = new LatentSemanticAnalysis(true, 300, new LogEntropyTransform(),
                SVD.getFastestAvailableFactorization(),
                false, new StringBasisMapping());
        Iterator<Document> i = documents.iterator();
        while (i.hasNext()) {
            Document document = i.next();
            lsa.processDocument(document.reader());
        }
        lsa.processSpace(new Properties());
        int size = lsa.documentSpaceSize();
        LOGGER.info("document space size {}", size);
        int countWords = lsa.getWords().size();
        LOGGER.info("words {}", countWords);
        DoubleVector doubleVector = lsa.getDocumentVector(size - 1);
        LOGGER.info("length {};\nmagnitude {};\nvector {}", doubleVector.length(), doubleVector.magnitude(), doubleVector);
        edu.ucla.sspace.vector.Vector vector = lsa.getVector("месяц");
        LOGGER.info("\nword vector\nlength {};\nmagnitude {};\nvector {}", vector.length(), vector.magnitude(), vector);
        edu.ucla.sspace.vector.Vector vector2 = lsa.getVector("апрель");
        LOGGER.info("\nword vector\nlength {};\nmagnitude {};\nvector {}", vector2.length(), vector2.magnitude(), vector2);
        double sim = Similarity.getSimilarity(Similarity.SimType.COSINE, vector, vector2);
        LOGGER.info("sim {}", sim);

//        while (size>=0){
//            DoubleVector doubleVector= lsa.getDocumentVector(size);
//            LOGGER.info("double vector {}", doubleVector);
//            --size;
//        }

    }
}
