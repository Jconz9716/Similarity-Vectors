import edu.uiowa.cs.similarity.FileFilter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.io.*;

import static org.junit.Assert.*;

public class Part1Test {
    @Test
    public void cleanupTest() {
        List<String> s = Arrays.asList("look", "glum", "night-cap");
        List<List<String>> cleanInput = new FileFilter(new File("cleanup_test.txt"), new File("stopwords.txt")).steamAndClean();
        assertFalse("Implement test", true);
    }

}