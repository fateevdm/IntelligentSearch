package intelligentsearch.indexer;

import edu.ucla.sspace.common.DocumentVectorBuilder;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.StaticSemanticSpace;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.Vector;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * User: Dmitry Fateev
 * Date: 11.05.13
 * Time: 18:31
 */
public class VectorBuilderTest {
    String toCheck = "Куликовская битва в 1380";
    String document2 = "Дмитрий Донской победил в Куликовской битве";
    String semanticSpace = "semantic_space_text.sspace";

    @Test
    public void vectorSpaceBuilder() throws IOException {
        StaticSemanticSpace sspace = new StaticSemanticSpace(semanticSpace);
        DocumentVectorBuilder builder = new DocumentVectorBuilder(sspace);
        BufferedReader br = new BufferedReader(new StringReader(toCheck));
        Vector documentVector = builder.buildVector(br, new CompactSparseVector());
        br = new BufferedReader(new StringReader(document2));
        Vector documentVector2 = builder.buildVector(br, new CompactSparseVector());

        System.out.println(Similarity.getSimilarity(Similarity.SimType.SPEARMAN_RANK_CORRELATION, documentVector, documentVector2));
    }
}
