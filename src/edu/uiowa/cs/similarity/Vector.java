package edu.uiowa.cs.similarity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        vector.put(base.toString(), similarity);
    }

    public void insert(String s) {
        SimValue x = new SimValue(0);
        if (vector.isEmpty()) {
            vector.put(base.toString(), x);
        }
        vector.put(s, x);
    }

   /* public Map<String, SimValue> getVector() {
        return vector;
    }*/

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

    //Only for getting the key and value pair, cannot change the sim value
    public String getPair(String key) {
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

    public void printVector() {
        System.out.println("Word: " + getBase() + " -> " + vectorToList());
    }

    public List<String> vectorToList() {
        List<String> sv = new LinkedList<>();
        vector.forEach((key, value) -> sv.add(key + " : "  + value.getAsString()));
        return sv;
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