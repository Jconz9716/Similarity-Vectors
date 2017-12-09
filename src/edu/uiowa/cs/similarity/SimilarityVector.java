package edu.uiowa.cs.similarity;

import opennlp.tools.stemmer.*;

import java.util.*;

public class SimilarityVector extends Vector {
    private List<List<String>> cleanedWords;
    private List<List<String>> dirtyWords;
    private List<String> sentence;
    private String word;
    private PorterStemmer stem = new PorterStemmer();

    public SimilarityVector(List<List<String>> cleanedWords, List<List<String>> dirtyWords) {
        this.cleanedWords = cleanedWords;
        this.dirtyWords = dirtyWords;
    }

    public SimilarityVector(List<List<String>> cleanedWords) {
        this.cleanedWords = cleanedWords;
    }

    public List<String> getUniqueWords(List<List<String>> words) {
        List<String> unique = new ArrayList<>();
        for (int i = 0; i<words.size(); i++) {
            List<String> sentence = words.get(i);
            for (int x = 0; x<sentence.size(); x++) {
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
        Vector vector = new Vector(base);
        Iterator<List<String>> sentences = cleanedWords.iterator();
        Iterator<String> s;
        //Outer loop increments sentences, inner loop words in each sentence
        while (sentences.hasNext()) {
            sentence = sentences.next();
            if (contains(sentence, vector.getStemmedBase())) {
                s = sentence.iterator();
                while (s.hasNext()) {
                    word =  s.next();
                    if (!vector.contains(word) && !word.isEmpty()) {
                        vector.insert(word);
                    }
                    if (!word.equals(vector.getStemmedBase())){    //Prevents incrementing sim value of base word
                        vector.increment(word);
                    }
                }
            }
        }
        return vector;
    }

    public Map<String, Vector> makeAllVectors() {
        Iterator<String> words = getUniqueWords(dirtyWords).iterator();
        Map<String, Integer> done = new HashMap<>();
        Map<String, Vector> vectors = new HashMap<>();
        Vector tmp;
        String w;
        while (words.hasNext()) {
            w = words.next();
            if (!done.containsKey(stem.stem(w)) && !stem.stem(w).isEmpty()) {
                tmp = createVector(w);
                vectors.put(tmp.getStemmedBase(), tmp);
                done.put(tmp.getStemmedBase(), 0);
            }
        }
        return vectors;
    }

    private boolean contains(List<String> sentence, String x) { return sentence.contains(x); }

    private List<List<String>> getCleanedWords() { return cleanedWords; }

    private List<List<String>> getDirtyWords() { return dirtyWords; }
}
