package edu.uiowa.cs.similarity;

import java.util.LinkedList;
import java.util.List;
import opennlp.tools.stemmer.*;

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
        List<String> sentence;
        String word;
        PorterStemmer stem = new PorterStemmer();
        //Outer loop increments sentences, inner loop words in each sentence
        for (int i = 0; i<cleanedWords.size(); i++) {
            sentence = cleanedWords.get(i);
            boolean increase = containsBase(sentence, tmpVector.getBase());
//            System.out.println("\n-------------------\n" + "Sentence contains " + tmpVector.getBase() + ": "  + increase);
            for (int x = 0; x<sentence.size(); x++) {
                word =  sentence.get(x);
                word = stem.stem(word);
//                System.out.println(word + " equals base: " + word.equals(tmpVector.getBase()));
                if (increase) {  //If current sentence contains the base, increments each word in s.
                    if (!tmpVector.contains(word)) {
                        tmpVector.insert(word);
                    }
                    if (!word.equals(tmpVector.getBase())){    //Prevents incrementing sim value of base word
                        System.out.println("Increasing...");
//                        System.out.println(word + " isn't equal to " + tmpVector.getBase());
                        tmpVector.increment(word);
                        System.out.println(getPairAsString(word));
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
        return tmpVector.getPair(key);
    }

    private boolean containsBase(List<String> sentence, String x) {
        return sentence.contains(x);
    }

    public int size() {
        return tmpVector.size();
    }

    private List<List<String>> getCleanedWords() {
        return cleanedWords;
    }
}
