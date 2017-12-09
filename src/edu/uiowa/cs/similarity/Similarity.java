package edu.uiowa.cs.similarity;

import java.util.Iterator;

public class Similarity {
    public static double getMagnitude(Vector v) {
        double magniutde = 0;
        double square;
        Iterator<String> keys = v.getKeySet().iterator();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            square = v.getSimValue(key);
            magniutde += square * square;
        }
        return magniutde;
    }
}
