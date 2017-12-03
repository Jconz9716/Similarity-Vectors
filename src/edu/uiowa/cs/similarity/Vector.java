package edu.uiowa.cs.similarity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Vector {
    public String base;
    public MutableInt similarity;
    Map<String, MutableInt> vector = new HashMap<>();

    public Vector() {
        this.base = null;
        this.similarity = new MutableInt(0);
    }

    public Vector(String s) {
        this.base = s;
        vector.put(base, new MutableInt(0));
    }

    public void insert(String s) {
        MutableInt x = new MutableInt(0);
        vector.put(s, x);
    }

    public Map<String, MutableInt> getVector() {
        return vector;
    }

    public void increment(String s) {
        MutableInt x = new MutableInt(0);
        if (!vector.containsKey(s)) {
            vector.put(s, x);
        }
        vector.get(s).increment();
    }

    public boolean contains(String s) {
        return vector.containsKey(s);
    }

    //Only for getting the key and value pair, cannot change the sim value
    public String get(String key) {
        List<Object> pair = new LinkedList<>();
        if (contains(key)) {
            pair.add(key);
            pair.add(vector.get(key));
        }
        return pair.toString();
    }

    public int size() {
        return vector.size();
    }

    public void printVector() {
        vector.forEach((key, value) -> System.out.println(key + " : "  + value.getAsString()));
    }

    public class MutableInt {
        private int i;
        public MutableInt() {
            this.i = 0;
        }
        public MutableInt(int i) {
            this.i = i;
        }

        public void increment() { i++;}
        public int get() { return i; }
        public String getAsString() {
            return String.valueOf(i);
        }
    }
}