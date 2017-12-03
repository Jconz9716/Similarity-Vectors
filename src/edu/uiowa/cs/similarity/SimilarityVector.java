package edu.uiowa.cs.similarity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimilarityVector extends Vector {
    private List<List<String>> cleanedWords;
    private Vector tmpVector;

    public SimilarityVector(String b, List<List<String>> cleanedWords) {
        this.tmpVector = new Vector(b);
        this.cleanedWords = cleanedWords;
    }

    public List<String> unique() {
        List<String> unique = new LinkedList<>();
        List<List<String>> clean = getCleanedWords();
        for (int i = 0; i<clean.size(); i++) {
            List<String> sentence = clean.get(i);
            for (int x = 0; x<sentence.size(); x++) {
                //System.out.println(sentence);
                String check = sentence.get(x);
                if (!unique.contains(check)){
                    unique.add(check);
                }
            }

        }
        return unique;
    }

    public Vector similarity() {
        tmpVector.base = "man";
        tmpVector.insert("man");
        List<String> sentence;
        String word;
        //Outer loop increments sentences, inner loop words in each sentence
        for (int i = 0; i<cleanedWords.size(); i++) {
            for (int x = 0; x<cleanedWords.get(i).size(); x++) {
                sentence = cleanedWords.get(i);
                word =  sentence.get(x);
                if (sentence.contains(base)) {  //If current sentence contains the base, increments each word in s.
                    if (!tmpVector.contains(word)) {
                        tmpVector.insert(word);
                    }
                    if (!word.equals(base)){    //Prevents incrementing sim value of base word
                        tmpVector.increment(word);
                    }
                }else {                         //If sentence !contains the base, adds new words but doesn't increment
                    if (!tmpVector.contains(word)) {
                        tmpVector.insert(word);
                    }
                }
            }
        }
        return tmpVector;
    }

    public String getPairAsString(String key) {
        return tmpVector.get(key);
    }

    public int size() {
        return tmpVector.size();
    }

    private List<List<String>> getCleanedWords() {
        return cleanedWords;
    }
}
