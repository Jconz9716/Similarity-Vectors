package edu.uiowa.cs.similarity;

import java.util.List;

public class FileSimVectors extends SimilarityVector{
    public FileSimVectors(List<List<String>> cleanedWords, List<List<String>> dirtyWords) {
        super(cleanedWords, dirtyWords);
    }
}
