package edu.uiowa.cs.similarity;

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;


public class Main {

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "input file to process");
        options.addOption("h", false, "print this help message");
        options.addOption("clean", false, "Cleaning file");
        options.addOption("s", false, "Prints sentences");
        options.addOption("v", false, "Generates semantic descriptor vector");

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
		}else if (cmd.hasOption("clean") && new File(filename).exists()) {
            //Clean file input using Cleanup
            System.out.println("Cleaning file...");

            File dirty = new File(filename);
            File stopWords = new File("stopwords.txt");


            //*** Empty lists *** Need to fix
            //*** Works when clean input is of type List<String> but then words aren't separated into sentences ***
            if (cmd.hasOption("s")) { //int s = 0;
                FindSentences sentences = new FindSentences(dirty, stopWords);
                List<List<String>> cleanInput = sentences.filterText();
                List<String> s;
                for (int i = 0; i<cleanInput.size(); i++) {
                    s = cleanInput.get(i);
                    System.out.println(s);
                    /*for (int x = 0; x<s.size(); x++){
                        System.out.println(s.get(x));
                        System.out.println(cleanInput.get(i));
                    }*/
                }
                /*while (s < cleanInput.size()){
                    System.out.println(cleanInput.get(s));
                    s++;
                }*/
            }else {
                List<List<String>> cleanInput = new FindSentences(dirty, stopWords).filterText();
            }
        }

        if (cmd.hasOption("h")) {
            HelpFormatter helpf = new HelpFormatter();
            helpf.printHelp("Main", options, true);
            System.exit(0);
        }


    }
}
