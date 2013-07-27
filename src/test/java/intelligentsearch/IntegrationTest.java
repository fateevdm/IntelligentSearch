package intelligentsearch;

import com.myuniver.intelligentsearch.dao.io.db.DBReader;
import com.myuniver.intelligentsearch.dao.io.db.DocumentReader;
import com.myuniver.intelligentsearch.dao.io.db.PrototypeDocument;
import com.myuniver.intelligentsearch.dao.io.file.DictionaryReader;
import com.myuniver.intelligentsearch.dao.io.file.StopWordsReader;
import com.myuniver.intelligentsearch.filters.Filter;
import com.myuniver.intelligentsearch.filters.TokenFilter;
import com.myuniver.intelligentsearch.morphology.Word;
import com.myuniver.intelligentsearch.questionanalyzer.QuestionNormalizer;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.structure.Dictionary;
import com.myuniver.intelligentsearch.structure.StopWords;
import com.myuniver.intelligentsearch.tokanizer.SimpleTokenizer;
import com.myuniver.intelligentsearch.util.DBConfigs;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.DocumentVectorBuilder;
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * User: Dmitry Fateev
 * Date: 05.05.13
 * Time: 1:13
 */
public class IntegrationTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);
    private Connection connection;
    private Dictionary<String, Word> dictionary;
    private Stemmer stemmer;
    private Tokenizer tokenizer;
    private Filter<String> filter;
    private StopWords stopWords;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        LOGGER.info("=======first step - load data from files =======");
        connection = DBConfigs.getConnection();
        DictionaryReader reader = DictionaryReader.createDefaultDictionary();
        dictionary = reader.open();
        stemmer = SimpleStemmer.getStemmer();
        StopWordsReader stopWordsReader = new StopWordsReader();
        stopWords = new StopWords(stopWordsReader.getStopWords());
        tokenizer = new SimpleTokenizer();
        filter = new TokenFilter(stopWords);
    }

    @Test
    public void runIntegrationTest() throws IOException, SQLException {
//        Scanner sc = new Scanner(System.in);
//        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
//        System.out.print("\nEnter exprs ");
//        String line = sc.nextLine();
//        String line = bf.readLine();
//        System.out.println(line);
        LOGGER.info("=======second step - load data from DB =======");
        DBReader<Document> docReader = new DocumentReader(tokenizer, filter, stemmer, dictionary);
        docReader.open();
        LOGGER.info("=======third step - process documents from DB=======");
        Map<Integer, Document> fromDBToLSA = new HashMap<>();
        int dimension = 200;
        LatentSemanticAnalysis analyzer = new LatentSemanticAnalysis(true, dimension, new LogEntropyTransform(),
                SVD.getFastestAvailableFactorization(),
                false, new StringBasisMapping());

        int lsaIndex = 0;
        for (Document doc : docReader) {
            fromDBToLSA.put(lsaIndex, doc);
            analyzer.processDocument(doc.reader());
            ++lsaIndex;
        }
        LOGGER.info("size: {}", lsaIndex);
        assertTrue(lsaIndex == 1932);
        analyzer.processSpace(new Properties());
        int size = analyzer.documentSpaceSize();
//        SemanticSpaceIO.save(analyzer, new File("semantic_space_text.sspace"), SemanticSpaceIO.SSpaceFormat.SPARSE_TEXT);
//        SemanticSpaceIO.save(analyzer, new File("semantic_space_text_sparse.sspace"), SemanticSpaceIO.SSpaceFormat.SPARSE_TEXT);
        LOGGER.info("document space size {}", size);
        int countWords = analyzer.getWords().size();
        LOGGER.info("words {}", countWords);
        DocumentVectorBuilder vectorBuilder = new DocumentVectorBuilder(analyzer);
        String question = "когда была сталинградская битва ..><>?<?>?<";
        String[] words = tokenizer.tokenize(question);
        List<String> filtered = filter.filter(Arrays.asList(words));
        List<String> stemmed = new ArrayList<>(filtered.size());
        List<Word> terms = new ArrayList<>();
        for (String token : filtered) {
            Word lemma = dictionary.get(token);
            if (lemma == null) {
                String stemm = stemmer.stemm(token);
                stemmed.add(stemm);
                terms.add(new Word("", stemm, ""));
            } else {
                terms.add(lemma);
                stemmed.add(lemma.getNormalForm());
            }
        }
        String filteredText = QuestionNormalizer.concatWithSpace(stemmed);
        PrototypeDocument document = new PrototypeDocument(filteredText, -1, terms, PrototypeDocument.QUESTION, question);

        DoubleVector stringVector = vectorBuilder.buildVector(document.reader(), new DenseVector(dimension));
        LinkedList<PrototypeDocument> ranged = new LinkedList<>();
        LOGGER.info("fromDb2LSA size {}", fromDBToLSA.size());
        for (Map.Entry<Integer, Document> entry : fromDBToLSA.entrySet()) {
            int index = entry.getKey();
            DoubleVector vector = analyzer.getDocumentVector(index);
            double similarity = Similarity.getSimilarity(Similarity.SimType.COSINE, vector, stringVector);
            PrototypeDocument doc = (PrototypeDocument) entry.getValue();
            doc.setScore(similarity);
            ranged.add(doc);
        }
        Collections.sort(ranged);
        LOGGER.info("ranged size {}", ranged.size());
        List<PrototypeDocument> topDocs = ranged.subList(0, 10);
//        for (PrototypeDocument doc : topDocs) {
//            LOGGER.info("\nscore [{}],\n[{}]", doc.getScore(), doc);
//        }
        LOGGER.info("\nFIRST ELEM SCORE: {};\nELEM: {}\n", ranged.getFirst().getScore(), ranged.getFirst());
        LOGGER.info("\nLAST ELEM SCORE: {};\nELEM: {}\n", ranged.getLast().getScore(), ranged.getLast());
        LOGGER.info("fact: {}", ranged.getFirst().getFact());
        LinkedList<PrototypeDocument> wheihted = new LinkedList<>(weightDocs(topDocs, document));
        Collections.sort(wheihted);
//            for (PrototypeDocument doc : wheihted) {
//                LOGGER.info("text {}\nwheight {}\nfact {}", doc.getOriginText(), doc.getScore(), doc.getFact());
//            }
        PrototypeDocument answer = wheihted.getFirst();
        LOGGER.info("answer {}\nwheight {}\nfact {}", answer.getOriginText(), answer.getScore(), answer.getFact());
    }

    private List<PrototypeDocument> weightDocs(List<PrototypeDocument> topDocs, PrototypeDocument query) {
        List<PrototypeDocument> wheightedDocs = new ArrayList<>(topDocs.size());
        for (PrototypeDocument doc : topDocs) {
//            List<String> document = Arrays.asList(tokenizer.tokenize(doc.getText()));
            int sum = 0;
            for (Word queryTerm : query.getTerms()) {
                for (Word docTerm : doc.getTerms()) {
                    if (queryTerm.equals(docTerm)) {
                        ++sum;
                    }
                }
            }
            double wheight = (double) sum / query.getTerms().size();
            doc.setScore(wheight);
            wheightedDocs.add(doc);
        }
        return wheightedDocs;
    }
}