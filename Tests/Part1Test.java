import edu.uiowa.cs.similarity.FileFilter;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

import static org.junit.Assert.*;

public class Part1Test {
    @Test
    public void cleanupTest() {
        List<List<String>> actual = new LinkedList<>();
        List<String> sentence = new LinkedList<>();
        String[] s = {"look", "glum", "nightcap"};
        actual.add(Arrays.asList(s));
        s = new String[]{"feel", "littl", "breez"};
        actual.add(Arrays.asList(s));
        s = new String[]{"ah"};
        actual.add(Arrays.asList(s));
        s = new String[]{"whatev", "mai", "sai", "good", "aliv", "dear", "amd"};
        actual.add(Arrays.asList(s));
        List<List<String>> expected = new FileFilter(new File("cleanup_test.txt"), new File("stopwords.txt")).getCleanAndStemmedWords();
        assertEquals(expected, actual);
    }

}