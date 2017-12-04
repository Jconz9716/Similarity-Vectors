package edu.uiowa.cs.similarity;

import opennlp.tools.stemmer.PorterStemmer;

import java.util.LinkedList;
import java.util.List;

public class SimilarityVector extends Vector {
    private List<List<String>> cleanedWords;
    private List<List<String>> dirtyWords;
    private String keyword;

    SimilarityVector(List<List<String>> cleanedWords, List<List<String>> dirtyWords) {
        this.cleanedWords = cleanedWords;
        this.dirtyWords = dirtyWords;
    }

    List<String> getUniqueWords(List<List<String>> words) {
        List<String> unique = new LinkedList<>();
        for (int i = 0; i<words.size(); i++) {
            List<String> sentence = words.get(i);
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

    Vector createVector(String base) {
        List<String> sentence;
        String word;
        PorterStemmer stem = new PorterStemmer();
        Vector vector = new Vector(base);
        //Outer loop increments sentences, inner loop words in each sentence
        for (int i = 0; i<cleanedWords.size(); i++) {
            sentence = cleanedWords.get(i);
            boolean increase = containsBase(sentence, vector.getStemmedBase());
//            System.out.println("\n-------------------\n" + "Sentence contains " + vector.getBase() + ": "  + increase);
            for (int x = 0; x<sentence.size(); x++) {
                word =  sentence.get(x);
                word = stem.stem(word);
//                System.out.println(word + " equals base: " + word.equals(vector.getBase()));
                if (increase) {  //If current sentence contains the base, increments each word in s.
                    if (!vector.contains(word)) {
                        vector.insert(word);
                    }
                    if (!word.equals(vector.getStemmedBase())){    //Prevents incrementing sim value of base word
//                        System.out.println("Increasing...");
//                        System.out.println(word + " isn't equal to " + tmpVector.getBase());
                        vector.increment(word);
//                        System.out.println(vector.getPairAsString(word));
                    }
                }
            }
        }
        return vector;
    }

    List<Vector> makeAllVectors() {
        List<String> words = getUniqueWords(dirtyWords);
        List<Vector> vectors = new LinkedList<>();
        for (int i = 0; i< getUniqueWords(dirtyWords).size(); i++) {
            vectors.add(createVector(words.get(i)));
        }
        return vectors;
    }

    private boolean containsBase(List<String> sentence, String x) {
        return sentence.contains(x);
    }

    private List<List<String>> getCleanedWords() {
        return cleanedWords;
    }

    public List<List<String>> getDirtyWords() { return dirtyWords; }
}
