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
            Iterator<String> lines = new LineFileReader(filename);
        }

        if (cmd.hasOption("h")) {
            HelpFormatter helpf = new HelpFormatter();
            helpf.printHelp("Main", options, true);
            System.exit(0);
        }
        
        
        /*  ADDITIONAL OPTIONS BELOW  */
        
        if(cmd.hasOption("s")){
            // print sentences & # of sentences
        }
        
        if(cmd.hasOption("v")){
            // print vectors
        }
        
        if(cmd.hasOption("t")){
            // Q,J will run Top-J query: find J words most similar
            // to Q
        }
        
        if(cmd.hasOption("m")){
            // SIM, where SIM can be cosine/euc/eucnorm. Will 
            // affect which sim meas unit used for Top-J
        }
        
        if(cmd.hasOption("k")){
            // k, iters will run K-means clustering w/ k clusters
            // and iters iterations
        }
        
        if(cmd.hasOption("j")){
            // k,iters J will run K-means clustering w/ k clusters
            // and iters iterations. For each cluster it prints the
            // J words closest to the cluster mean
        }
        
    }
}
