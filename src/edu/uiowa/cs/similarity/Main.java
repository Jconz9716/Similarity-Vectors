package edu.uiowa.cs.similarity;

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "input file to process");
        options.addOption("h", false, "print this help message");
        options.addOption("clean", false, "Cleaning file");
        options.addOption("s", false, "Prints sentences");
        options.addOption("v", true, "Generates semantic descriptor vector");

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
		}
		if (cmd.hasOption("clean") && new File(filename).exists()) {
            //Clean file input using Cleanup
            System.out.println("Cleaning file...");

            File dirty = new File(filename);
            File stopWords = new File("stopwords.txt");

            FindSentences sentences = new FindSentences(dirty, stopWords);
            List<List<String>> clean = sentences.filterText();

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

        if (cmd.hasOption("v")) {
            String getVector = cmd.getOptionValue("v");
            String printMess = "Calculating vector for: %s...";
            printMess = String.format(printMess, getVector);
            System.out.println(printMess);
        }

        if (cmd.hasOption("h")) {
            HelpFormatter helpf = new HelpFormatter();
            helpf.printHelp("Main", options, true);
            System.exit(0);
        }


    }
}
