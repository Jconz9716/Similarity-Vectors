package edu.uiowa.cs.similarity;

import java.util.Iterator;

public class CosineSimilarity {
    private Vector baseVector;
    private Vector vectorToCompare;
    private double cosineSimilarity;
    private double denominator;

    public CosineSimilarity() {
        this.baseVector = null;
        this.vectorToCompare = null;
    }

    public void printCosineSimilarity() {
        String message = "Cosine similarity of %s -> %s: ";
        //System.out.println(getBaseVector());
        //System.out.println(getVectorToCompare().base);
        message = String.format(message, baseVector.base, vectorToCompare.base);
        System.out.println(message + calculateCosineSim());
    }

    public double getDenominator() {
        denominator = (Math.sqrt(Similarity.getMagnitude(baseVector))*Math.sqrt(Similarity.getMagnitude(vectorToCompare)));
        if (denominator > 0 && !Double.isNaN(denominator)) {
            return denominator;
        }
        return 0;
    }

    public double calculateCosineSim() {
        cosineSimilarity = 0;
        if (getDenominator() == 0) {
            return cosineSimilarity;
        }
        //System.out.println(getDotProduct());
        cosineSimilarity += getDotProduct()/denominator;
        return cosineSimilarity;
    }

    public double getDotProduct() {
        double dotProduct = 0;
        Iterator<String> keysofX = baseVector.getKeySet().iterator();
        Iterator<String> keysofY = vectorToCompare.getKeySet().iterator();
        String key;
        if (baseVector.size() < vectorToCompare.size()) {
            while (keysofX.hasNext()) {
                key = keysofX.next();
                if (vectorToCompare.contains(key)) {
                    //System.out.println(baseVector.getSimValue(key) + " --> " + vectorToCompare.getSimValue(key));
                    dotProduct += baseVector.getSimValue(key) * vectorToCompare.getSimValue(key);
                }
            }
        }else {
            while (keysofY.hasNext()) {
                key = keysofY.next();
                if (baseVector.contains(key)) {
                    //System.out.println(baseVector.getSimValue(key) + " --> " + vectorToCompare.getSimValue(key));
                    dotProduct += vectorToCompare.getSimValue(key) * baseVector.getSimValue(key);
                }
            }
        }
        //System.out.println("Dot product: " + dotProduct);
        return dotProduct;
    }

    public void setBaseVector(Vector baseVector) {
        this.baseVector = baseVector;
    }

    public Vector getBaseVector() {
        return baseVector;
    }

    public void setVectorToCompare(Vector toCompare) {
        this.vectorToCompare = toCompare;
    }

    public Vector getVectorToCompare() {
        return vectorToCompare;
    }
}
