package edu.uiowa.cs.similarity;



public class FindSentences<InT, OutT> {
    private final FilterTextFunction<InT, OutT> f;

    public FindSentences(FilterTextFunction<InT, OutT> f){
        this.f = f;
    }
}
