package edu.uiowa.cs.similarity;

import org.apache.commons.cli.*;

import java.io.*;
import java.text.ParseException;
import java.util.*;


public class Main {
    private static List<List<String>> clean;
    private static List<List<String>> unCleanUnique;

    public static void main(String[] args) throws ParseException {
        long start = System.currentTimeMillis();
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "input file to process");
        options.addOption("h", false, "print this help message");
        options.addOption("clean", false, "Cleaning file");
        options.addOption("s", false, "Prints sentences");
        options.addOption("v", false, "Generates semantic descriptor vector");
        options.addOption("t", true, "Cosine similarity");
        options.addOption("m", true, "More similarity");
        options.addOption("k", true, "K-means");

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
        }

        assert cmd != null;
        String filename = cmd.getOptionValue("f");
        if (!new File(filename).exists()) {
            System.err.println("file does not exist "+filename);
            System.exit(1);
        }else {
            //Clean file input using Cleanup
            System.out.println("Cleaning file...");
            File dirty = new File(filename);
            File stopWords = new File("stopwords.txt");

            long filterStart = System.currentTimeMillis();
            FileFilter sentences = new FileFilter(dirty, stopWords);
            clean = sentences.getCleanAndStemmedWords();
            unCleanUnique = sentences.getDirtyWords();
            long filterStop = System.currentTimeMillis();
            System.out.println("Word filter time: " + (filterStop - filterStart)/1000 + " seconds");

            //Prints cleaned sentences. For debugging only
            if (cmd.hasOption("s")) {
                List<String> s;
                for (int i = 0; i<clean.size(); i++) {
                    s = clean.get(i);
                    System.out.println(s);
                }
                System.out.println("\n");
            }
        }

        //Only creates 1 vector and requires an argument to create that vector
        if (cmd.hasOption("v")) {
            String vectorBase = cmd.getOptionValue("v");
            System.out.println("Calculating all vectors...\n");
            SimilarityVector vector = new SimilarityVector(clean, unCleanUnique);
//          System.out.println(vector.getCleanUniqueWords());
            Map<String, Vector> vectors = vector.makeAllVectors();
            Iterator<String> w = vectors.keySet().iterator();

            while (w.hasNext()) {
                vectors.get(w.next()).printVector();
            }
        }

        if (cmd.hasOption("t")) {
            String tmp = cmd.getOptionValue("t");
            String[] a = tmp.split(",");
            String keyword = a[0];
            int num = Integer.parseInt(a[1]);

            System.out.println("Calculating all vectors...\n");
            long startMakeVector = System.currentTimeMillis();
            Vector myVector = new Vector();
            SimilarityVector v = new SimilarityVector(clean, unCleanUnique);
            Map<String, Vector> vectors = v.makeAllVectors();
            long stopMakeVector = System.currentTimeMillis();
            String holyGrail;
            String message;
            Value info;
            int count = 0;
            PriorityQueue<Value> eucOrdered = new PriorityQueue<>(Collections.reverseOrder());

            System.out.println("Vector make time: " + (stopMakeVector - startMakeVector) / 1000 + " seconds");
            System.out.println("There are " + vectors.size() + " unique vectors\n");

            if (cmd.hasOption("m")) {
                long startEuc = System.currentTimeMillis();
                String sim = cmd.getOptionValue("m");
                EuclideanDistance distance = new EuclideanDistance(vectors.get(myVector.cleanWord(keyword)));
                String key;

                if (sim.equalsIgnoreCase("euc")) {
                    System.out.println("The Euclidean distance will be based on the similarity vector of " + keyword);
                    System.out.println("");
                    Iterator<String> eucKeyIterator = vectors.keySet().iterator();
                    //distance.setBaseVector(vectors.get(myVector.cleanWord(keyword)));
                    holyGrail = distance.getBaseVector().getBase();

                    while (eucKeyIterator.hasNext()) {
                        key = eucKeyIterator.next();
                        if (!key.equalsIgnoreCase(myVector.cleanWord(keyword))){
                            distance.setVectorToCompare(vectors.get(key));
                            message =  ("Euclidean distance of " + holyGrail + " -> ");
                            message += (distance.getVectorToCompare().getBase() + ":  ");
                            info = new Value(distance.getEucDistance(), message);
                            eucOrdered.add(info);
                        }
                    }

                    long stopEuc = System.currentTimeMillis();
                    Value toPrint;
                    while (!eucOrdered.isEmpty() && count<num) {
                        toPrint = eucOrdered.poll();
                        System.out.println(toPrint.getValue() + toPrint.getKey());
                        count++;
                    }
                    System.out.println("\nEuclidean distance time: " + (stopEuc - startEuc)/1000 + " seconds");

//                  Normalized vectors
                } else {
                    System.out.println("The euclidean distance between normalized vectors for " + keyword);
                    System.out.println("");
                    Iterator<String> eucKeyIterator = vectors.keySet().iterator();
                    //distance.setBaseVector(vectors.get(myVector.cleanWord(keyword)));
                    holyGrail = distance.getBaseVector().getBase();

                    while (eucKeyIterator.hasNext()) {
                        key = eucKeyIterator.next();
                        if (!key.equalsIgnoreCase(myVector.cleanWord(keyword))){
                            distance.setVectorToCompare(vectors.get(key));
                            message =  ("Euclidean distance of " + holyGrail + " -> ");
                            message += (distance.getVectorToCompare().getBase() + ":  ");
                            info = new Value(distance.getNormEucDistance(), message);
                            if (!Double.isNaN(info.getKey())) {
                                eucOrdered.add(info);
                            }
                        }
                    }

                    long stopEuc = System.currentTimeMillis();
                    Value toPrint;
                    while (!eucOrdered.isEmpty() && count<num) {
                        toPrint = eucOrdered.poll();
                        System.out.println(toPrint.getValue() + toPrint.getKey());
                        count++;
                    }
                    System.out.println("\nEuclidean distance time: " + (stopEuc - startEuc)/1000 + " seconds");
                }

            }else {
                long startCosine = System.currentTimeMillis();
                PriorityQueue<Value> ordered = new PriorityQueue<>(Collections.reverseOrder());
                System.out.println("Cosine similarity values will be for words compared to " + keyword);
                CosineSimilarity similarity = new CosineSimilarity();
                similarity.setBaseVector(vectors.remove(myVector.cleanWord(keyword)));
                Iterator<String> cosKeyIterator = vectors.keySet().iterator();
                holyGrail = similarity.getBaseVector().getBase();

                while (cosKeyIterator.hasNext()) {
                    similarity.setVectorToCompare(vectors.get(cosKeyIterator.next()));
                    message =  ("Cosine similarity of " + holyGrail + " -> ");
                    message += (similarity.getVectorToCompare().getBase() + ":  ");
                    info = new Value(similarity.calculateCosineSim(), message);
                    ordered.add(info);
                }

                long stopCosine = System.currentTimeMillis();
                System.out.println("Cosine similarity time: " + (stopCosine - startCosine)/1000 + " seconds");

                Value toPrint;
                while (!ordered.isEmpty() && count<num) {
                    toPrint = ordered.poll();
                    System.out.println(toPrint.getValue() + toPrint.getKey());
                    count++;
                }
            }
            long stop = System.currentTimeMillis();
            System.out.println("Total execution time: " + (stop - start)/1000 + " seconds");
        }

        if (cmd.hasOption("k")) {
            String x = cmd.getOptionValue("k");
            String[] y = x.split(",");
            int numClust = Integer.parseInt(y[0]);
            int numIter = Integer.parseInt(y[1]);

            System.out.println("Calculating all vectors...\n");
            SimilarityVector vector = new SimilarityVector(clean, unCleanUnique);
//          System.out.println(vector.getCleanUniqueWords());
            Map<String, Vector> vectors = vector.makeAllVectors();
            System.out.println(vectors.size() + " vectors");
            Iterator<String> vIterator = vectors.keySet().iterator();
            String current;

            List<Vector> centroids = new LinkedList<>();

            for (int i = 0; i<numClust; i++) {
                if (vIterator.hasNext()) {
                    current = vIterator.next();
                    centroids.add(vectors.get(current));
                }
            }

            KMeans kmeans = new KMeans(centroids, clean);
            List<List<Vector>> clusters;
            Iterator<Vector> vectorIterator;

            for (int z = 0; z<numIter; z++) {
                clusters = kmeans.calcKmeans(vectors);
                kmeans.calcCentroid();
                for (int in = 0; in<clusters.size(); in++) {
                    System.out.println("Cluster " + in + "...");
                    for (List<Vector> vec : clusters) {
                        vectorIterator = vec.iterator();
                        while (vectorIterator.hasNext())
                        System.out.println(vectorIterator.next().vectorToList());
                    }
                }
            }

        }

        if (cmd.hasOption("h")) {
            HelpFormatter helpf = new HelpFormatter();
            helpf.printHelp("Main", options, true);
            System.exit(0);
        }

    }

}
