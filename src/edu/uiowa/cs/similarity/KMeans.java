package edu.uiowa.cs.similarity;

import clojure.core.Vec;

import java.util.*;

public class KMeans {
    private List<Vector> centroids;
    private EuclideanDistance distance = new EuclideanDistance();
    private int k;
    private List<List<Vector>> clusters = new LinkedList<>();
    private double minimum = 1;
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
        Vector centroidVector;
        double eucDistance;

        int i;
        while (vectorIterator.hasNext()) {
            distance.setVectorToCompare(vectors.get(vectorIterator.next()));
            centroidIterator =  centroids.iterator();
            i = 0;
            while (centroidIterator.hasNext()) {
                distance.setBaseVector(centroidIterator.next());
                //System.out.println("Base vector: " + distance.getBaseVector());
                //System.out.println("Vector to compare to: " + distance.getVectorToCompare());
                eucDistance = distance.getEucDistance();
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
        Iterator<Vector> cluster;
        Vector centroid;
        Vector currentVector;
        int num = centroids.size();
        Iterator<String> cVectorKeys;
        //Iterator<String> allKeys;
        String current;

        //allKeys = similarityVector.getUniqueWords(cleanedWords).iterator();

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

            }
            newCentroids.add(centroid);
        }
        centroids.addAll(newCentroids);
    }
}
