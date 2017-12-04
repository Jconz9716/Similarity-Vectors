package edu.uiowa.cs.similarity;

import org.apache.commons.cli.*;

import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


public class Main {
    private static List<List<String>> clean;
    private static List<List<String>> unCleanUnique;

    public static void main(String[] args) throws ParseException {
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

            FileFilter sentences = new FileFilter(dirty, stopWords);
            clean = sentences.getCleanWords();
            unCleanUnique = sentences.getDirtyWords();


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
            String printMess = "Calculating vector for %s...";
            printMess = String.format(printMess, vectorBase);
            System.out.println(printMess);
            SimilarityVector vector = new SimilarityVector(clean, unCleanUnique);
//          System.out.println(vector.getCleanUniqueWords());
            Vector v = vector.createVector(vectorBase);
            v.printVector();
        }

        if (cmd.hasOption("t")) {
		    String tmp = cmd.getOptionValue("t");
		    System.out.println(tmp);
		    String[] a = tmp.split(",");
		    String keyword = a[0];
		    int num = Integer.parseInt(a[1]);
		    //Priority queue
            Map<String, Vector> vectors = makeVectors();
            Vector keyVector = vectors.remove(keyword);
            for (int i = 0; i<vectors.size() && i<num; i++) {
                if (!vectors.get(i).getBase().equalsIgnoreCase(keyword)) {
                    System.out.println(new CosineSimilarity(keyVector, vectors.));
                }
            }

        }

        if (cmd.hasOption("h")) {
            HelpFormatter helpf = new HelpFormatter();
            helpf.printHelp("Main", options, true);
            System.exit(0);
        }

    }

    public static Map<String, Vector> makeVectors() {
        System.out.println("Calculating all vectors...");
        SimilarityVector vector = new SimilarityVector(clean, unCleanUnique);
//        System.out.println(vector.getUniqueWords(unCleanUnique));
//        System.out.println(vector.getUniqueWords(clean));
        return vector.makeAllVectors();
    }
}
