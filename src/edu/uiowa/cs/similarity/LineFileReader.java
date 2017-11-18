package edu.uiowa.cs.similarity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class LineFileReader implements Iterator<String> {
    private Scanner file;
    private String sentences = new String();

    public LineFileReader(String filename) {
        this.file = new Scanner(filename);
    }

    @Override
    public boolean hasNext() {
        return file.hasNextLine();
    }

    @Override
    public String next() throws FileNotFoundException {
        String line;
        String[] splitLines;
        while (file.hasNextLine()){
            line = file.nextLine();
            //line = line.replaceAll("[\\,\\--\\:\\;\\"\\’]", "");
            if (line.contains(". ") || line.contains("! ") || line.contains("? ")){
                splitLines = line.split(".");

            }

        }
        return sentences;
    }

}
