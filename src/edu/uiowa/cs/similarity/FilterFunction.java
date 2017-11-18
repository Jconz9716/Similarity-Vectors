package edu.uiowa.cs.similarity;

public interface FilterFunction<InT> {
    public OutT filter(InT x);
}
