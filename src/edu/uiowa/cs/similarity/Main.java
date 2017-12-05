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
        options.addOption("v", true, "Generates semantic descriptor vector");
        options.addOption("t", true, "Calculates top-J similarity");

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
            System.out.println("Calculating vector for " + vectorBase + "...\n");
            SimilarityVector vector = new SimilarityVector(clean, unCleanUnique);
//          System.out.println(vector.getCleanUniqueWords());
            Vector v = vector.createVector(vectorBase);
            v.printVector();
        }

        if (cmd.hasOption("t")) {
            String tmp = cmd.getOptionValue("t");
            String[] a = tmp.split(",");
            String keyword = a[0];
            int num = Integer.parseInt(a[1]);

            //System.out.println("Number of vectors to print: " + num);
            System.out.println("Cosine similarity values will be for words compared to " + keyword);


            System.out.println("Calculating all vectors...\n");

            long startMakeVector = System.currentTimeMillis();

            Vector myVector = new Vector();
            SimilarityVector v = new SimilarityVector(clean, unCleanUnique);
            Map<String, Vector> vectors = v.makeAllVectors();

            long stopMakeVector = System.currentTimeMillis();
            System.out.println("Vector make time: " + (stopMakeVector - startMakeVector)/1000 + " seconds");

            long startCosine = System.currentTimeMillis();
            Value info;
            CosineSimilarity similarity = new CosineSimilarity();
            PriorityQueue<Value> ordered = new PriorityQueue<>(Collections.reverseOrder());
            String message;

            //System.out.println(vectors.containsKey(myVector.cleanWord(keyword)) + " " + keyword);

           //Iterator<String> i = vectors.keySet().iterator();
            System.out.println("There are " +  + " unique vectors\n");
            System.out.println("There are " + vectors.size() + " unique vectors\n");
            /*while (i.hasNext()) {
                System.out.println(i.next());
            }*/

            similarity.setBaseVector(vectors.remove(myVector.cleanWord(keyword)));
            Iterator<String> keyIterator = vectors.keySet().iterator();
            String holyGrail = similarity.getBaseVector().getBase();

            while (keyIterator.hasNext()) {
                similarity.setVectorToCompare(vectors.get(keyIterator.next()));
                message =  ("Cosine similarity of " + holyGrail + " -> ");
                message += (similarity.getVectorToCompare().getBase() + ":  ");
                info = new Value(similarity.calculateCosineSim(), message);
                ordered.add(info);
            }

            long stopCosine = System.currentTimeMillis();
            System.out.println("Cosine similarity time: " + (stopCosine - startCosine)/1000 + " seconds");

            int count = 0;
            Value toPrint;
            while (!ordered.isEmpty() && count<num) {
                toPrint = ordered.poll();
                System.out.println(toPrint.getValue() + toPrint.getKey());
                count++;
            }
            long stop = System.currentTimeMillis();
            System.out.println("Total execution time: " + (stop - start)/1000 + " seconds");
        }

        if (cmd.hasOption("h")) {
            HelpFormatter helpf = new HelpFormatter();
            helpf.printHelp("Main", options, true);
            System.exit(0);
        }

    }

}
