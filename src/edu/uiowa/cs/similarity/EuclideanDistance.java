package edu.uiowa.cs.similarity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class EuclideanDistance {
    private Vector baseVector;
    private Vector toCompareVector;
    private PriorityQueue<String> allKeys = new PriorityQueue<>();

    public EuclideanDistance() {
        this.baseVector = null;
        this.toCompareVector = null;
    }
    public double calcEuclidean() {
        String key;
        Double distance = 0d;

        //System.out.println("Vector: " + toCompareVector.getBase());

        Map<String, Integer> done = new HashMap<>();
        allKeys.addAll(baseVector.getKeySet());
        allKeys.addAll(toCompareVector.getKeySet());

        while (!allKeys.isEmpty()) {
            key = allKeys.poll();
            if (!done.containsKey(key)) {
                //System.out.println(key);
                if (baseVector.contains(key) && toCompareVector.contains(key)) {
                    //System.out.println(baseVector.getSimValue(key) + " --> " + vectorToCompare.getSimValue(key));
                    distance += Math.pow(baseVector.getSimValue(key) - toCompareVector.getSimValue(key), 2);
                } else if (baseVector.contains(key) && !toCompareVector.contains(key)) {
                    distance += Math.pow(baseVector.getSimValue(key), 2);
                }else if (toCompareVector.contains(key) && !baseVector.contains(key)){
                    distance += Math.pow(-toCompareVector.getSimValue(key), 2);
                }
                done.put(key, 0);
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
