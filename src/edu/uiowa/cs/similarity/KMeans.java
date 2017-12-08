package edu.uiowa.cs.similarity;

import clojure.core.Vec;

import java.util.*;

public class KMeans {
    public List<Vector> centroids;
    private EuclideanDistance distance = new EuclideanDistance();
    private int k;
    private List<List<Vector>> clusters = new LinkedList<>();
    private double minimum;
    private List<List<String>> cleanedWords;
    private SimilarityVector similarityVector;
    List<Vector> emptyList = new LinkedList<>();

    public KMeans(List<Vector> centroids, List<List<String>> cleanedWords) {
        this.centroids = centroids;
        this.k = centroids.size();
        this.cleanedWords = cleanedWords;
        this.similarityVector = new SimilarityVector(cleanedWords);

        for (int i = 0; i<k; i++) {
            clusters.add(new LinkedList<>(emptyList));
        }
    }

    public List<List<Vector>> calcKmeans(Map<String, Vector> vectors) {
        Iterator<String> vectorIterator = vectors.keySet().iterator();
        Iterator<Vector> centroidIterator;
        //Vector centroidVector;
        double eucDistance;

        int i;
        int indexToInsert;
        while (vectorIterator.hasNext()) {
            minimum = 50;
            distance.setVectorToCompare(vectors.get(vectorIterator.next()));
            if (!centroids.contains(distance.getVectorToCompare())) {
                centroidIterator =  centroids.iterator();
                i = 0;
                indexToInsert = 0;
                while (centroidIterator.hasNext()) {
                    distance.setBaseVector(centroidIterator.next());
                    //System.out.println("Base vector: " + distance.getBaseVector());
                    //System.out.println("Vector to compare to: " + distance.getVectorToCompare());
                    eucDistance = distance.getEucDistance();
                    // *** Adding vectors multiple times. Need to change to add 1x ***
                    if (eucDistance < minimum) {
                        indexToInsert = i;
                        minimum = eucDistance;
                    }
                    i++;
                }
                clusters.get(indexToInsert).add(distance.getVectorToCompare());
                //System.out.println(distance.getVectorToCompare().getBase() + " is most similar to " + centroids.get(indexToInsert).getBase());
            }
        }
        return clusters;
    }

    public void calcCentroid() {
        List<Vector> newCentroids = new LinkedList<>();
        Iterator<List<Vector>> clusterIterator = clusters.iterator();
        List<String> unique;
        Iterator<Vector> cluster;
        List<Vector> listOfVectorsInCluster;
        Iterator<String> allKeys;
        Vector centroid;
        Vector currentVector;
        int num = centroids.size();
        int clusterSize;
        Iterator<String> cVectorKeys;
        int numVectors;
        String current;

        unique = similarityVector.getUniqueWords(cleanedWords);
        numVectors = unique.size();
        allKeys = unique.iterator();

        //System.out.println("Number of vectors: " + numVectors);

        System.out.println("---------- Iterating -----------");

        //Recalculating centroid values
        while (clusterIterator.hasNext()) {
            centroid = centroids.remove(0);
            listOfVectorsInCluster = clusterIterator.next();
            clusterSize = listOfVectorsInCluster.size();
            System.out.println("Cluster size: " + clusterSize);
            cluster = listOfVectorsInCluster.iterator();
            while (cluster.hasNext()) {
                currentVector = cluster.next();
                cVectorKeys = currentVector.getKeySet().iterator();
                while (cVectorKeys.hasNext()) {
                    current = cVectorKeys.next();
                    //System.out.println(current);
                    if (centroid.contains(current)) {
                        centroid.setSimValue(current, centroid.getSimValue(current) + currentVector.getSimValue(current));
                    } else {
                        centroid.insert(current, new Vector.SimValue(currentVector.getSimValue(current)));
                        centroid.increment(current, currentVector.getSimValue(current));
                    }
                }
                // *** Need to divide all values in new centroid by numVectors. Is equal to total num of unique words ***
                Iterator<String> newCentIterator = centroid.getKeySet().iterator();
                String x;
                while (newCentIterator.hasNext()) {
                    x = newCentIterator.next();
                    //System.out.println("Current sim value: " + centroid.getSimValue(x));
                    centroid.setSimValue(x, centroid.getSimValue(x)/clusterSize);
                    //Set SimValue to current value/num unique words
                    //System.out.println("Final sim value: " + centroid.getSimValue(x));
                }
            }
            newCentroids.add(centroid);
        }
        centroids.clear();
        centroids.addAll(newCentroids);
    }

    public void resetClusters() {
        this.clusters.clear();
        System.out.println("Resetting clusters...");
        for (int i = 0; i<k; i++) {
            this.clusters.add(new LinkedList<>(emptyList));
        }
    }
}
