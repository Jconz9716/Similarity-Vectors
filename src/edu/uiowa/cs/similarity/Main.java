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
            if (cmd.hasOption("s")) { int s = 0;
                List<List<String>> cleanInput = new FindSentences(dirty).filterText();
                while (s < cleanInput.size()){
                    System.out.println(cleanInput.get(s));
                    s++;
                }
            }else {
                new FindSentences(dirty).filterText();
            }
        }

        if (cmd.hasOption("h")) {
            HelpFormatter helpf = new HelpFormatter();
            helpf.printHelp("Main", options, true);
            System.exit(0);
        }


    }
}
