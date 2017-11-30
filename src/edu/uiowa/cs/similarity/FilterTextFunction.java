package edu.uiowa.cs.similarity;

import java.util.List;

public interface FilterTextFunction<InT, OutT> {
        List<List<String>> filterText(InT x);
}
