package edu.uiowa.cs.similarity;

import java.util.LinkedList;
import java.util.List;

public class SimilarityVector {
    private String word;
    private List<List<String>> cleanedWords;

    public SimilarityVector(String word, List<List<String>> cleanedWords) {
        this.word = word;
        this.cleanedWords = cleanedWords;
    }

    public List<Object> unique() {
        List<Object> unique = new LinkedList<>();
        List<List<String>> clean = getCleanedWords();
        for (int i = 0; i<clean.size(); i++) {
            List<String> sentence = clean.get(i);
            for (int x = 0; x<sentence.size(); x++) {
                //System.out.println(sentence);
                String check = sentence.get(x);
                if (!unique.toString().contains(check)){
                    unique.add(check);
                }
            }

        }
        return unique;
    }

    public List<List<Object>> similarity() {

        return null;
    }

    private List<List<String>> getCleanedWords() {
        return cleanedWords;
    }
}
