package edu.uiowa.cs.similarity;

import java.util.*;

import opennlp.tools.stemmer.*;

public class Vector implements VectorInterface<String> {
    String base;
    private Map<String, SimValue> vector = new HashMap<>();

    public Vector() {
        this.base = null;
    }

    public Vector(String s) {
        this.base = s;
    }

    public void insert(String s) {
        SimValue x = new SimValue(0);
        vector.put(s, x);
    }

    public void insert(String s, SimValue v) {
        vector.put(s, v);
    }

    public void increment(String s) {
        SimValue x = new SimValue(0);
        if (!vector.containsKey(s)) {
            vector.put(s, x);
        }
        vector.get(s).incrementSim();
    }

    public void increment(String s, int x) {
        vector.get(s).addToSim(x);
    }

    public boolean contains(String key) {
        return vector.containsKey(key);
    }

    public List<String> getPair(String key) {
        List<String> pair = new ArrayList<>();
        if (contains(key)) {
            pair.add(key);
            pair.add(vector.get(key).getAsString());
        }
        return pair;
    }

    //Only for getting the key and value pair, cannot change the sim value
    public String getPairAsString(String key) {
        List<Object> pair = new ArrayList<>();
        if (contains(key)) {
            pair.add(key);
            pair.add(vector.get(key).getAsString());
        }
        return pair.toString();
    }

    public int size() {
        return vector.size();
    }

    public String getBase() {
        if (this.base.isEmpty()) {
            throw new IllegalStateException("Vector has no base");
        }
        return this.base;
    }

    public String getStemmedBase() {
        PorterStemmer stemmer = new PorterStemmer();
        return stemmer.stem(this.base);
    }

    public boolean isEmpty() {
        return vector.isEmpty();
    }

    public void printVector() {
        if (vector.isEmpty()) {
            System.err.println("*** The keyword '" + getBase() + "' does not exist in this text ***");
        }else {
            List<String> pVector = new ArrayList<>();
            pVector = printVectorHelper(vectorToList(), pVector);
            Collections.sort(pVector);
            System.out.println("Word: " + getBase() + " -> " + pVector);
        }
    }

    private List<String> printVectorHelper(List<List<String>> vls, List<String> vs) {
        if (vls.isEmpty()){
            return vs;
        }else {
            vs.add(vls.remove(0).toString());
            return printVectorHelper(vls, vs);
        }
    }

    //Converts vector to list to help with debugging
    public List<List<String>> vectorToList() {
        List<List<String>> pairs = new ArrayList<>();
        List<String> listBase = new ArrayList<>();
        listBase.add("Base: " + getBase());
        vector.forEach((key, value) -> pairs.add(getPair(key)));
        return pairs;
    }

    public Set<String> getKeySet() {
        return vector.keySet();
    }

    public int getSimValue(String key) {
        return vector.get(key).getAsInt();
    }

    public void setSimValue(String key, int i) {
        vector.get(key).setSimValue(i);
    }

    public String cleanWord(String word) {
        PorterStemmer stem = new PorterStemmer();
        //Filters out all of the extra characters, then  stop words
        if (word.isEmpty()) {
            throw new IllegalStateException();
        }
        return stem.stem(word);
    }

    public static class SimValue {
        private int value;
        public SimValue(int i) {
            this.value = i;
        }

        public void incrementSim() { value++;}
        public void addToSim(int i) { value += i;}
        public void setSimValue(int i) { value = i;}
        public int getAsInt() { return value; }
        public String getAsString() {
            return String.valueOf(value);
        }
    }
}