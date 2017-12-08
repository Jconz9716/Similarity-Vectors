package edu.uiowa.cs.similarity;

import clojure.core.Vec;

import java.util.*;

public class KMeans {
    private List<Vector> centroids;
    private EuclideanDistance distance = new EuclideanDistance();
    private int k;
    private List<List<Vector>> clusters = new LinkedList<>();
    private double minimum;
    private List<List<String>> cleanedWords;
    private SimilarityVector similarityVector;

    public KMeans(List<Vector> centroids, List<List<String>> cleanedWords) {
        this.centroids = centroids;
        this.k = centroids.size();
        this.cleanedWords = cleanedWords;
        this.similarityVector = new SimilarityVector(cleanedWords);

        for (int i = 0; i<k; i++) {
            clusters.add(new LinkedList<>());
        }
    }

    public List<List<Vector>> calcKmeans(Map<String, Vector> vectors) {
        Iterator<String> vectorIterator = vectors.keySet().iterator();
        Iterator<Vector> centroidIterator;
        List<Vector> list = new LinkedList<>();
        //Vector centroidVector;
        double eucDistance;

        int i;
        while (vectorIterator.hasNext()) {
            minimum = 50;
            distance.setVectorToCompare(vectors.get(vectorIterator.next()));
            centroidIterator =  centroids.iterator();
            i = 0;
            while (centroidIterator.hasNext()) {
                distance.setBaseVector(centroidIterator.next());
                //System.out.println("Base vector: " + distance.getBaseVector());
                //System.out.println("Vector to compare to: " + distance.getVectorToCompare());
                eucDistance = distance.getEucDistance();
                // *** Adding vectors multiple times. Need to change to add 1x ***
                if (eucDistance < minimum) {
                    try {
                        clusters.get(i).add(distance.getVectorToCompare());
                    }catch (IndexOutOfBoundsException ex) {
                        clusters.add(i, list);
                        clusters.get(i).add(distance.getVectorToCompare());
                    }
                    minimum = eucDistance;
                }
                i++;
            }
        }
        return clusters;
    }

    public void calcCentroid() {
        List<Vector> newCentroids = new LinkedList<>();
        Iterator<List<Vector>> clusterIterator = clusters.iterator();
        List<String> unique;
        Iterator<Vector> cluster;
        Iterator<String> allKeys;
        Vector centroid;
        Vector currentVector;
        int num = centroids.size();
        Iterator<String> cVectorKeys;
        int numVectors;
        String current;

        unique = similarityVector.getUniqueWords(cleanedWords);
        numVectors = unique.size();
        allKeys = unique.iterator();

        System.out.println("Number of vectors: " + numVectors);

        //Recalculating centroid values
        while (clusterIterator.hasNext()) {
            centroid = centroids.remove(0);
            cluster = clusterIterator.next().iterator();
            while (cluster.hasNext()) {
                currentVector = cluster.next();
                cVectorKeys = currentVector.getKeySet().iterator();
                while (cVectorKeys.hasNext()) {
                    current = cVectorKeys.next();
                    if (centroid.contains(current)) {
                        centroid.increment(current, currentVector.getSimValue(current));
                    } else {
                        centroid.insert(current);
                        centroid.increment(current, currentVector.getSimValue(current));
                    }
                }
                // *** Need to divide all values in new centroid by numVectors. Is equal to total num of unique words ***
                /*for (SimValue value : centroid) {
                    //Set SimValue to current value/num unique words
                }*/
            }
            newCentroids.add(centroid);
        }
        centroids.addAll(newCentroids);
    }
}
