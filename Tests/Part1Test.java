import edu.uiowa.cs.similarity.FindSentences;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.io.*;

import static org.junit.Assert.*;

public class Part1Test {

    public Part1Test() {

    }

    @Test
    public void cleanupTest() {
        List<String> s = Arrays.asList("look", "glum", "night-cap");
        List<List<String>> cleanInput = new FindSentences(new File("cleanup_test.txt")).filterText();
        assertFalse("Implement test", true);
    }

}