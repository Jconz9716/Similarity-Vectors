package edu.uiowa.cs.similarity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class EuclideanDistance {
    private Vector baseVector;
    private Vector toCompareVector;
    private PriorityQueue<String> allKeys = new PriorityQueue<>();
    Map<String, Integer> done = new HashMap<>();
    private Double baseMagnitude;

    public EuclideanDistance(Vector baseVector) {
        this.baseVector = baseVector;
        this.baseMagnitude = Similarity.getMagnitude(baseVector);
        this.toCompareVector = null;
    }
    public double getEucDistance() {
        String key;
        Double distance = 0d;

        done.clear();

        //Adds all words from both vectors
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

    public double getNormEucDistance() {
        String key;
        Double distance = 0d;
        done.clear();

        Double compareMagnitude = Similarity.getMagnitude(toCompareVector);

        //Adds all words from both vectors
        allKeys.addAll(baseVector.getKeySet());
        allKeys.addAll(toCompareVector.getKeySet());

        while (!allKeys.isEmpty()) {
            key = allKeys.poll();
            if (!done.containsKey(key)) {
                if (baseVector.contains(key) && toCompareVector.contains(key)) {
                    distance += Math.pow(baseVector.getSimValue(key)/baseMagnitude -
                            toCompareVector.getSimValue(key)/compareMagnitude, 2);
                } else if (baseVector.contains(key) && !toCompareVector.contains(key)) {
                    distance += Math.pow(baseVector.getSimValue(key)/baseMagnitude, 2);
                }else if (toCompareVector.contains(key) && !baseVector.contains(key)){
                    distance += Math.pow(-toCompareVector.getSimValue(key)/compareMagnitude, 2);
                }
                done.put(key, 0);
                }
            }

        return -Math.sqrt(distance);
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
