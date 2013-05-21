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
import edu.ucla.sspace.common.DocumentVectorBuilder;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import opennlp.tools.tokenize.Tokenizer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    private Connection connection;


    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        connection = DBConfigs.getConnection();
    }


    @Test
    public void runIntegrationTest() throws IOException, SQLException {

        LOGGER.info("=======first step - load data from files =======");
        DictionaryReader reader = new DictionaryReader(dictionaryPath);
        Dictionary<String, Word> dictionary = reader.openConnection();
        Stemmer stemmer = SimpleStemmer.getStemmer();
        LOGGER.info("=======second step - load data from DB =======");

        PreparedStatement statement = connection.prepareStatement("SELECT history_question.question, history_answer.answer,history_answer.question_id, history_question.id\n" +
                "FROM history_question,history_answer\n" +
                "WHERE history_answer.question_id = history_question.id AND history_answer.correct =1;");
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
            int questionId = result.getInt("history_question.id");
            int answerId = result.getInt("history_answer.question_id");
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

//        BiMap<Integer, PrototypeDocument> fromDBToLSA = HashBiMap.create();
        Map<Integer, PrototypeDocument> fromDBToLSA = new HashMap<>();
        LatentSemanticAnalysis analyzer = new LatentSemanticAnalysis(true, 50, new LogEntropyTransform(),
                SVD.getFastestAvailableFactorization(),
                false, new StringBasisMapping());
        Iterator<Document> i = documents.iterator();
        int lsaIndex = 0;
        while (i.hasNext()) {
            PrototypeDocument document = (PrototypeDocument) i.next();
            fromDBToLSA.put(lsaIndex, document);
            analyzer.processDocument(document.reader());
            ++lsaIndex;
        }
        analyzer.processSpace(new Properties());
        int size = analyzer.documentSpaceSize();
        SemanticSpaceIO.save(analyzer, new File("semantic_space_text.sspace"), SemanticSpaceIO.SSpaceFormat.SPARSE_TEXT);
        SemanticSpaceIO.save(analyzer, new File("semantic_space_text_sparse.sspace"), SemanticSpaceIO.SSpaceFormat.SPARSE_TEXT);
        LOGGER.info("document space size {}", size);
        int countWords = analyzer.getWords().size();
        LOGGER.info("words {}", countWords);
        DoubleVector doubleVector = analyzer.getDocumentVector(size - 1);
        LOGGER.info("length {};\nmagnitude {};\nvector {}", doubleVector.length(), doubleVector.magnitude(), doubleVector);
        edu.ucla.sspace.vector.Vector vector1 = analyzer.getVector("месяц");
        LOGGER.info("\nword vector\nlength {};\nmagnitude {};\nvector {}", vector1.length(), vector1.magnitude(), vector1);
        edu.ucla.sspace.vector.Vector vector2 = analyzer.getVector("апрель");
        LOGGER.info("\nword vector\nlength {};\nmagnitude {};\nvector {}", vector2.length(), vector2.magnitude(), vector2);
        double sim = Similarity.getSimilarity(Similarity.SimType.KL_DIVERGENCE, vector1, vector2);
        LOGGER.info("sim {}", sim);
        DocumentVectorBuilder vectorBuilder = new DocumentVectorBuilder(analyzer);
        String toCheck = "Екатерина Великая";
        String[] words = tokenizer.tokenize(toCheck);
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
        Document document = new PrototypeDocument(filteredText, 0, stemmedTokens, PrototypeDocument.QUESTION, toCheck);

        DoubleVector stringVector = vectorBuilder.buildVector(document.reader(), new DenseVector(50));
        TreeMap<Double, PrototypeDocument> score2Document = new TreeMap<>();
        LOGGER.info("fromDb2LSA size {}", fromDBToLSA.size());
        for (Map.Entry<Integer, PrototypeDocument> entry : fromDBToLSA.entrySet()) {
            int index = entry.getKey();
            DoubleVector vector = analyzer.getDocumentVector(index);
            double similarity = Similarity.getSimilarity(Similarity.SimType.COSINE, vector, stringVector);
            PrototypeDocument doc = entry.getValue();
            doc.setScore(similarity);
            score2Document.put(similarity, doc);
        }
        LOGGER.info("score2Document size {}", score2Document.size());
        for (Map.Entry<Double, PrototypeDocument> entry : score2Document.entrySet()) {
            LOGGER.info("entry: {}", entry);
        }
        LOGGER.info("first elem: {}", score2Document.firstEntry());
        LOGGER.info("last elem: {}", score2Document.lastEntry());


    }

    private String getDocumentById(int docId) throws SQLException {
        docId = 12333;
        PreparedStatement statement = connection.prepareStatement("SELECT history_question.question, history_question.id\n" +
                "FROM history_question,history_answer\n" +
                "WHERE history_question.id =?");
        statement.setInt(1, docId);
        ResultSet result = statement.executeQuery();
        String originText = result.getString("question");
        String fact = result.getString("answer");
        int questionId = result.getInt("math_question.id");
        int answerId = result.getInt("math_answer.question_id");
        return originText;
    }

    static class Pair {
        int idFromDB;
        int idFromLSA;

        Pair(int idFromDB, int idFromLSA) {
            this.idFromDB = idFromDB;
            this.idFromLSA = idFromLSA;
        }
    }
}
