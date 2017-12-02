package edu.uiowa.cs.similarity;

import java.util.*;

public class LineFileReader implements Iterator<String> {
    private final Scanner scanner;
    private String nextline;

    public LineFileReader(String filename) {
        this.scanner = new Scanner(filename);
        this.nextline = scanner.nextLine();
    }

    @Override
    public boolean hasNext() {
        return nextline != null;
    }

    @Override
    public String next() {
        if (!hasNext()) { throw new IllegalStateException();}
        nextline = scanner.nextLine();

        return nextline;
    }

}
