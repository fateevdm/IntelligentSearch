package intelligentsearch.indexer;

import edu.ucla.sspace.common.DocumentVectorBuilder;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.StaticSemanticSpace;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.Vector;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: Dmitry Fateev
 * Date: 11.05.13
 * Time: 18:31
 */
public class VectorBuilderTest {
    String document1 = "";
    String document2 = "";

    @Test
    public void vectorSpaceBuilder() throws IOException {
        StaticSemanticSpace sspace = new StaticSemanticSpace(document1);
        DocumentVectorBuilder builder = new DocumentVectorBuilder(sspace);
        BufferedReader br = new BufferedReader(new FileReader(document1));
        Vector documentVector = builder.buildVector(br, new CompactSparseVector());
        br = new BufferedReader(new FileReader(document2));
        Vector documentVector2 = builder.buildVector(br, new CompactSparseVector());

        System.out.println(Similarity.cosineSimilarity(documentVector, documentVector2));
    }
}
