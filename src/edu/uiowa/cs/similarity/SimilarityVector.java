package edu.uiowa.cs.similarity;

import opennlp.tools.stemmer.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimilarityVector extends Vector {
    private List<List<String>> cleanedWords;
    private List<List<String>> dirtyWords;

    public SimilarityVector(List<List<String>> cleanedWords, List<List<String>> dirtyWords) {
        this.cleanedWords = cleanedWords;
        this.dirtyWords = dirtyWords;
    }

    public List<String> getUniqueWords(List<List<String>> words) {
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

    //Doesn't save a word if the sentence doesn't contain the base.
    //Ex. Word: man won't save any [believ, liver, diseas] because !contains man
    public Vector createVector(String base) {
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
//                System.out.println(word + " equals " + vector.getStemmedBase() + " --> " + word.equals(vector.getStemmedBase()));
                if (increase) {  //If current sentence contains the base, increments each word in s.
                    if (!vector.contains(word)) {
                        vector.insert(word);
                    }
                    if (!word.equals(vector.getStemmedBase())){    //Prevents incrementing sim value of base word
//                        System.out.println("Increasing...");
//                        System.out.println(word + " isn't equal to " + vector.getBase());
                        vector.increment(word);
//                        System.out.println(vector.getPairAsString(word));
                    }
                }
            }
        }
        return vector;
    }

    public Map<String, Vector> makeAllVectors() {
        List<String> words = getUniqueWords(dirtyWords);
        List<String> done = new LinkedList<>();
        Map<String, Vector> vectors = new HashMap<>();
        Vector tmp;
        for (int i = 0; i< getUniqueWords(dirtyWords).size(); i++) {
            tmp = createVector(words.get(i));
            if (!done.contains(tmp.getStemmedBase())) {
                //System.out.println(words.get(i));
                vectors.put(tmp.getStemmedBase(), tmp);
                done.add(tmp.getStemmedBase());
            }
        }
        return vectors;
    }

    private boolean containsBase(List<String> sentence, String x) {
        return sentence.contains(x);
    }

    private List<List<String>> getCleanedWords() {
        return cleanedWords;
    }

    private List<List<String>> getDirtyWords() { return dirtyWords; }
}
