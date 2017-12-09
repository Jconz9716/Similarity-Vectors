package edu.uiowa.cs.similarity;

import java.util.*;

public class KMeans {
    public List<Vector> centroids;
    private EuclideanDistance distance = new EuclideanDistance();
    private int k;
    public List<List<Vector>> clusters = new ArrayList<>();
    private double minimum;
    private List<List<String>> cleanedWords;
    private SimilarityVector similarityVector;
    List<Vector> emptyList = new ArrayList<>();

    public KMeans(List<Vector> centroids, List<List<String>> cleanedWords) {
        this.centroids = centroids;
        this.k = centroids.size();
        this.cleanedWords = cleanedWords;
        this.similarityVector = new SimilarityVector(cleanedWords);

        for (int i = 0; i<k; i++) {
            clusters.add(new ArrayList<>(emptyList));
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
                    eucDistance = distance.getEucDistance();
                    if (eucDistance < minimum) {
                        indexToInsert = i;
                        minimum = eucDistance;
                    }
                    i++;
                }
                clusters.get(indexToInsert).add(distance.getVectorToCompare());
            }
        }
        return clusters;
    }

    public void calcCentroid() {
        List<Vector> newCentroids = new LinkedList<>();
        Iterator<List<Vector>> clusterIterator = clusters.iterator();
        Iterator<Vector> cluster;
        List<Vector> listOfVectorsInCluster;
        Vector centroid;
        Vector currentVector;
        int clusterSize;
        Iterator<String> cVectorKeys;
        String current;

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
                    if (centroid.contains(current)) {
                        centroid.setSimValue(current, centroid.getSimValue(current) + currentVector.getSimValue(current));
                    } else {
                        centroid.insert(current, new Vector.SimValue(currentVector.getSimValue(current)));
                        centroid.increment(current, currentVector.getSimValue(current));
                    }
                }

                Iterator<String> newCentIterator = centroid.getKeySet().iterator();
                String x;
                while (newCentIterator.hasNext()) {
                    x = newCentIterator.next();
                    centroid.setSimValue(x, centroid.getSimValue(x)/clusterSize + 1);
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
            this.clusters.add(new ArrayList<>(emptyList));
        }
    }
}
