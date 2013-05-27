package intelligentsearch;

import com.myuniver.intelligentsearch.Word;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.filters.TokenFilter;
import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.structure.StopWords;
import com.myuniver.intelligentsearch.tokanizer.SimpleTokenizer;
import com.myuniver.intelligentsearch.util.Config;
import com.myuniver.intelligentsearch.util.Dictionary;
import com.myuniver.intelligentsearch.util.db.DBConfigs;
import com.myuniver.intelligentsearch.util.db.DBReader;
import com.myuniver.intelligentsearch.util.db.PrototypeDocument;
import com.myuniver.intelligentsearch.util.db.Row;
import com.myuniver.intelligentsearch.util.io.DictionaryReader;
import com.myuniver.intelligentsearch.util.io.ResourceReader;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: Dmitry Fateev
 * Date: 05.05.13
 * Time: 1:13
 */
public class IntegrationTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);
    String dictionaryPath = Config.getConfig().getProperty("dictionary.simple");
    private Connection connection;
    DictionaryReader reader;
    private Dictionary<String, Word> dictionary;
    private Stemmer stemmer;
    private StopWords stopWordsData;
    private Tokenizer tokenizer;
    private Filter filter;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException, FileNotFoundException {
        LOGGER.info("=======first step - load data from files =======");
        connection = DBConfigs.getConnection();
        reader = new DictionaryReader(dictionaryPath);
        dictionary = reader.openConnection();
        stemmer = SimpleStemmer.getStemmer();
        stopWordsData = new StopWords();
        tokenizer = new SimpleTokenizer();
    }

    @Test
    public void runIntegrationTest() throws IOException, SQLException {
        Set<String> stopWords = stopWordsData.getStopWords();
        LOGGER.info("=======second step - load data from DB =======");
        ResourceReader<Row> dbReader = new DBReader();
        dbReader.open();
        LOGGER.info("=======third step - process documents from DB=======");
        filter = new TokenFilter(stopWords);
        List<Document> documents = processDocs(dbReader);
        LOGGER.info("document size before: {}", documents.size());
        List<Row> templateDoc = getTemplateDocument();
        documents.addAll(processDocs(templateDoc));
        LOGGER.info("document size after: {}", documents.size());
        Map<Integer, PrototypeDocument> fromDBToLSA = new HashMap<>();
        int dimension = 100;
        LatentSemanticAnalysis analyzer = new LatentSemanticAnalysis(true, dimension, new LogEntropyTransform(),
                SVD.getFastestAvailableFactorization(),
                false, new StringBasisMapping());

        int lsaIndex = 0;
        for (Document doc : documents) {
            PrototypeDocument document = (PrototypeDocument) doc;
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
        DocumentVectorBuilder vectorBuilder = new DocumentVectorBuilder(analyzer);
//        String toCheck = "начало второй мировой войны";
        String toCheck = "период правление Александра";
        String[] words = tokenizer.tokenize(toCheck);
        List<String> filtered = filter.filter(Arrays.asList(words));
        List<String> stemmed = new ArrayList<>(filtered.size());
        for (String token : filtered) {
            Word lemma = dictionary.get(token);
            if (lemma == null) {
                String stemm = stemmer.stemm(token);
                stemmed.add(stemm);
            } else {
                stemmed.add(lemma.getStemma());
            }
        }
        String text = QuestionNormalizer.concatWithSpace(filtered);
        Document document = new PrototypeDocument(text, -1, filtered, PrototypeDocument.QUESTION, toCheck);

        DoubleVector stringVector = vectorBuilder.buildVector(document.reader(), new DenseVector(dimension));
        LinkedList<PrototypeDocument> ranged = new LinkedList<>();
        LOGGER.info("fromDb2LSA size {}", fromDBToLSA.size());
        for (Map.Entry<Integer, PrototypeDocument> entry : fromDBToLSA.entrySet()) {
            int index = entry.getKey();
            DoubleVector vector = analyzer.getDocumentVector(index);
            double similarity = Similarity.getSimilarity(Similarity.SimType.COSINE, vector, stringVector);
            PrototypeDocument doc = entry.getValue();
            doc.setScore(similarity);
            ranged.add(doc);
        }
        Collections.sort(ranged);
        LOGGER.info("ranged size {}", ranged.size());
        List<PrototypeDocument> topDocs = ranged.subList(0, 10);
        for (PrototypeDocument doc : topDocs) {
            LOGGER.info("\nscore [{}],\n[{}]", doc.getScore(), doc);
        }
        LOGGER.info("\nFIRST ELEM SCORE: {};\nELEM: {}\n", ranged.getFirst().getScore(), ranged.getFirst());
        LOGGER.info("\nLAST ELEM SCORE: {};\nELEM: {}\n", ranged.getLast().getScore(), ranged.getLast());
        LOGGER.info("fact: {}", ranged.getFirst().getFact());

    }

    private List<Document> processDocs(Iterable<Row> rows) {
        List<Document> docs = new ArrayList<>();
        for (Row row : rows) {
            String[] words = tokenizer.tokenize(row.getText());
            List<String> filtered = filter.filter(Arrays.asList(words));
            List<String> stemmed = new ArrayList<>(filtered.size());
            for (String token : filtered) {
                Word lemma = dictionary.get(token);
                String stemm;
                if (lemma == null) {
                    stemm = stemmer.stemm(token);
                    stemmed.add(stemm);
                } else {
                    stemmed.add(lemma.getStemma());
                }
            }
            String filteredText = QuestionNormalizer.concatWithSpace(filtered);
            Document document = new PrototypeDocument(filteredText, row.getQuestionId(), filtered, row.getFact(), row.getText());
            docs.add(document);
        }
        return docs;
    }

    private List<Row> getTemplateDocument() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT history_fatherland_question.question, history_fatherland_question.id\n" +
                "FROM history_fatherland_question\n");
        ResultSet result = statement.executeQuery();
        List<Row> documents = new ArrayList<>();
        while (result.next()) {
            String originText = result.getString("history_fatherland_question.question");
            int questionId = result.getInt("history_fatherland_question.id");
            Row row = Row.Builder.start().setOriginText(originText).setQuestionId(questionId).build();
            documents.add(row);
        }

        return documents;
    }

}
