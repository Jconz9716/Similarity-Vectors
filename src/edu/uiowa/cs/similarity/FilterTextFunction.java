package edu.uiowa.cs.similarity;

public interface FilterTextFunction<InT, OutT> {
        OutT FilterText(InT x);
}
