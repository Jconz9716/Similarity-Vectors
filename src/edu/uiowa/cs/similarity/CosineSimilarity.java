package edu.uiowa.cs.similarity;

import java.util.Iterator;

public class CosineSimilarity {
    private final Vector x;
    private final Vector y;

    public CosineSimilarity(Vector x, Vector y) {
        this.x = x;
        this.y = y;
    }

    public int getCosineSimilarity() {
        int cosineSimilarity = 0;
        cosineSimilarity += getDotProduct()/(Math.sqrt(getMagnitude(x))*Math.sqrt(getMagnitude(y)));
        return cosineSimilarity;
    }

    public int getDotProduct() {
        int dotProduct = 0;
        Iterator<String> keysofX = x.getKeySet().iterator();
        Iterator<String> keysofY = y.getKeySet().iterator();
        String key;
        if (x.size() < y.size()) {
            while (keysofX.hasNext()) {
                key = keysofX.next();
                if (y.contains(key)) {
                    dotProduct += x.getSimValue(key) * y.getSimValue(key);
                }
            }
        }else {
            while (keysofY.hasNext()) {
                key = keysofY.next();
                if (x.contains(key)) {
                    dotProduct += x.getSimValue(key) * y.getSimValue(key);
                }
            }
        }
        return dotProduct;
    }

    public int getMagnitude(Vector v) {
        int magniutde = 0;
        int square;
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
