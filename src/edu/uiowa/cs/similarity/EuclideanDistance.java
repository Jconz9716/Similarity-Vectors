package edu.uiowa.cs.similarity;

import java.util.Iterator;
import java.util.PriorityQueue;

public class EuclideanDistance {
    private Vector baseVector;
    private Vector toCompareVector;

    public EuclideanDistance() {
        this.baseVector = null;
        this.toCompareVector = null;
    }
    public double calcEuclidean(PriorityQueue<String> allKeys) {
        String key;
        Double distance = 0d;

        while (!allKeys.isEmpty()) {
            key = allKeys.poll();
            if (baseVector.contains(key) && toCompareVector.contains(key)) {
                //System.out.println(baseVector.getSimValue(key) + " --> " + vectorToCompare.getSimValue(key));
                distance += Math.pow(baseVector.getSimValue(key) - toCompareVector.getSimValue(key), 2);
            } else if (baseVector.contains(key) && !toCompareVector.contains(key)) {
                distance += Math.pow(baseVector.getSimValue(key), 2);
            }else if (toCompareVector.contains(key) && !baseVector.contains(key)){
                distance += Math.pow(-toCompareVector.getSimValue(key), 2);
            }
        }

        return -Math.sqrt(distance);
    }

    public double getNormalizedEuclideanDistance() {
        return 0d;
    }

    public void setBaseVector(Vector base) {
        this.baseVector = base;
    }

    public Vector getBaseVector() {
        return this.baseVector;
    }

    public void setVectorToCompare(Vector compare) {
        this.toCompareVector = compare;
    }

    public Vector getVectorToCompare() {
        return this.toCompareVector;
    }

}
