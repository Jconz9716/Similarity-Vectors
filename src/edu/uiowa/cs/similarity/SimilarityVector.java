package edu.uiowa.cs.similarity;

import opennlp.tools.stemmer.*;

import java.util.*;

public class SimilarityVector extends Vector {
    private List<List<String>> cleanedWords;
    private List<List<String>> dirtyWords;
    private List<String> sentence;
    private String word;
    private PorterStemmer stemmer = new PorterStemmer();

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
        Vector vector = new Vector(base);
        Iterator<List<String>> sentences = cleanedWords.iterator();
        //Outer loop increments sentences, inner loop words in each sentence
        while (sentences.hasNext()) {
            sentence = sentences.next();
            //System.out.println(sentence);
//            System.out.println("\n-------------------\n" + "Sentence contains " + vector.getBase() + ": "  + increase);
            if (containsBase(sentence, vector.getStemmedBase())) {
                for (int x = 0; x<sentence.size(); x++) {
                    word =  sentence.get(x);
//                System.out.println(word + " equals " + vector.getStemmedBase() + " --> " + word.equals(vector.getStemmedBase()));
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
        //vector.printVector();
        return vector;
    }

    public Map<String, Vector> makeAllVectors() {
        PriorityQueue<String> words = new PriorityQueue<>(getUniqueWords(dirtyWords));
        Map<String, Integer> done = new HashMap<>();
        Map<String, Vector> vectors = new HashMap<>();
        Vector tmp;
        String w;
        while (!words.isEmpty()) {
            w = words.poll();
            if (!done.containsKey(stemmer.stem(w))) {
                tmp = createVector(w);
                //System.out.println(words.get(i));
                //tmp.printVector();
                vectors.put(tmp.getStemmedBase(), tmp);
                done.put(tmp.getStemmedBase(), 0);
            }/*else {
                System.out.println("done already");
            }*/
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
