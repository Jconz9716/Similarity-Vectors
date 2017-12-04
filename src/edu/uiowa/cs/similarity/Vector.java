package edu.uiowa.cs.similarity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import opennlp.tools.stemmer.*;

public class Vector implements VectorInterface<String> {
    Object base;
    private SimValue similarity;
    private Map<String, SimValue> vector = new HashMap<>();

    public Vector() {
        this.base = null;
        this.similarity = new SimValue(0);
    }

    public Vector(String s) {
        this.base = s;
        this.similarity = new SimValue(0);
    }

    public void insert(String s) {
        SimValue x = new SimValue(0);
        vector.put(s, x);
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

    public boolean contains(String s) {
        return vector.containsKey(s);
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
        return this.base.toString();
    }

    public String getStemmedBase() {
        PorterStemmer stemmer = new PorterStemmer();
        return stemmer.stem(getBase());
    }

    public void printVector() {
        if (vector.isEmpty()) {
            System.err.println("*** The keyword '" + getBase() + "' does not exist in this text ***");
        }else {
            List<String> vectorString = new LinkedList<>();
            System.out.println("Word: " + getBase() + " -> " + printVectorHelper(vectorToList(), vectorString));
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
        vector.forEach((key, value) -> pairs.add(getPair(key)));
        return pairs;
    }

    public class SimValue {
        private int value;
        public SimValue() {
            this.value = 0;
        }
        public SimValue(int i) {
            this.value = i;
        }

        public void incrementSim() { value++;}
        public int getSimValue() { return value; }
        public String getAsString() {
            return String.valueOf(value);
        }
    }
}