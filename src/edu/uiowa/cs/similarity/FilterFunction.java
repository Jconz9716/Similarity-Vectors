package edu.uiowa.cs.similarity;

public interface FilterFunction<InT, OutT> {
    OutT filter(InT x);
}
