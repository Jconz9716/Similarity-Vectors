import edu.uiowa.cs.similarity.FileFilter;
import edu.uiowa.cs.similarity.SimilarityVector;
import edu.uiowa.cs.similarity.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Part2Test {
    File dirty = new File("cleanup_test.txt");
    File stopWords = new File("stopwords.txt");
    FileFilter sentences = new FileFilter(dirty, stopWords);
    List<List<String>> clean = sentences.getCleanAndStemmedWords();
    List<List<String>> unCleanUnique  = sentences.getDirtyWords();
    SimilarityVector similarityVector = new SimilarityVector(clean, unCleanUnique);

    @Test
    public void wordIsNotInFileTest() {
        Vector vector;
        vector = similarityVector.createVector("I");
        assertTrue(vector.isEmpty());
        vector = similarityVector.createVector("mississippi");
        assertTrue(vector.isEmpty());
    }

    @Test
    public void basicVectorTest() {
        Vector vector;
        vector = similarityVector.createVector("breeze");
        assertTrue(!vector.isEmpty());
        vector = similarityVector.createVector("breezes");
        assertTrue(!vector.isEmpty());

    }
}
