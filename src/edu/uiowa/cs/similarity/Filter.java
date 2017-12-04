package edu.uiowa.cs.similarity;

import java.util.List;
import java.util.Scanner;

public interface Filter<T> {
    public List<List<T>> getCleanWords();
}
