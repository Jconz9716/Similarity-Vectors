package edu.uiowa.cs.similarity;

import clojure.core.Vec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class KMeans {
    private List<Vector> centroids;
    private EuclideanDistance distance = new EuclideanDistance();
    private int k;
    private List<List<Vector>> clusters = new LinkedList<>();
    private double minimum = 1;

    public KMeans(List<Vector> centroids) {
        this.centroids = centroids;
        this.k = centroids.size();
    }

    public List<List<Vector>> calcKmeans(Iterator<Vector> vectorIterator) {
        while (vectorIterator.hasNext()) {
            distance.setVectorToCompare(vectorIterator.next());
            for (int i = 0; i<centroids.size(); i++) {
                distance.setBaseVector(centroids.get(i));
                if (distance.getEucDistance() < minimum) {
                    clusters.get(i).add(distance.getVectorToCompare());
                }
            }
        }
        return clusters;
    }

    public void calcCentroid() {
        List<Vector> nCentroids = new LinkedList<>();
        Iterator<List<Vector>> clusterIterator = clusters.iterator();
        Iterator<Vector> cluster;
        Iterator<Vector> vectorIterator;
        Vector centroid;
        Vector v;
        int num = centroids.size();

        //Recalculating centroid values
        for (int i = 0; i<num; i++) {
            centroid = centroids.remove(0);
            while (clusterIterator.hasNext()) {
                cluster = clusterIterator.next().iterator();
                while (cluster.hasNext()) {
                    v = cluster.next();

                }
            }

        }
    }
}
