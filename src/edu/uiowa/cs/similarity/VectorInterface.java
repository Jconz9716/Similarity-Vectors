package edu.uiowa.cs.similarity;

import java.util.List;

public interface VectorInterface<T> {
    public void insert(T x);
    public void increment(T x);
    public boolean contains(T x);
    public int size();
    public T getPair(T x);
    public void printVector();
    public List<T> vectorToList();
}
