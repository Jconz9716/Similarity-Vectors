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
        //System.out.println("Original simValue: " + vector.get(s).getAsString());
        vector.get(s).incrementSim();
        //System.out.println("New simValue: " + vector.get(s).getAsString());
    }

    public void increment(String s, int x) {
        vector.get(s).incrementSim(x);
    }

    public boolean contains(String key) {
        return vector.containsKey(key);
    }

    public List<String> getPair(String key) {
        List<String> pair = new LinkedList<>();
        if (contains(key)) {
            pair.add(key);
            pair.add(vector.get(key).getAsString());
        }
        return pair;
    }

    //Only for getting the key and value pair, cannot change the sim value
    public String getPairAsString(String key) {
        List<Object> pair = new LinkedList<>();
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

    public void printVector() {
        if (vector.isEmpty()) {
            System.err.println("*** The keyword '" + getBase() + "' does not exist in this text ***");
        }else {
            List<String> pVector = new LinkedList<>();
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

    public List<List<String>> vectorToList() {
        List<List<String>> pairs = new LinkedList<>();
        List<String> listBase = new LinkedList<>();
        listBase.add("Base: " + getBase());
        vector.forEach((key, value) -> pairs.add(getPair(key)));
        pairs.add(0, listBase);
        return pairs;
    }

    public Set<String> getKeySet() {
        return vector.keySet();
    }

    public int getSimValue(String key) {
        return vector.get(key).getAsInt();
    }

    public String cleanWord(String word) {
        PorterStemmer stem = new PorterStemmer();
        //Filters out all of the extra characters, then  stop words
        if (word.isEmpty()) {
            throw new IllegalStateException();
        }
        return stem.stem(word);
    }

    public class SimValue {
        private int value;
        public SimValue(int i) {
            this.value = i;
        }

        public void incrementSim() { value++;}
        public void incrementSim(int i) { value += i;}
        public int getAsInt() { return value; }
        public String getAsString() {
            return String.valueOf(value);
        }
    }
}