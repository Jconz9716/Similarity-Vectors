package edu.uiowa.cs.similarity;

import opennlp.tools.stemmer.*;


public interface FilterFunction<InT> {
    public OutT filter(InT x);
}
