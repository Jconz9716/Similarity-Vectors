package edu.uiowa.cs.similarity;


import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import opennlp.tools.stemmer.*;

public class FindSentences{
    private final File text;
    private final File stopWords;

    private List<List<String>> sentencesList = new LinkedList<>();

    public FindSentences(File text, File grosMots) {
        this.text = text;
        stopWords = grosMots;
    }

    public List<List<String>> filterText() {
        List<String> stemSentence = new LinkedList<>();
        List<String> stop = stopWords();
        String cleanWord;
        String stemmedWord;
        String line;
        String[] sentence;
        PorterStemmer stem = new PorterStemmer();
        Scanner s = null;

        System.out.println(stop);
        System.out.println(stop.contains("same"));

        //Throws error if file can't be found
        try {
            s = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Could possibly throw nullPointerException. Only an issue if the last line doesn't end with
        //punctuation. Ex. What the fudge
        assert s != null;
        s.useDelimiter("[!?.]");

        //Goes until there are no lines left in the document. It's ok if the last line is blank since most end
        //in a blank line
        while (s.hasNext()) {
            line = s.next().toLowerCase();


            line = line.replaceAll("\n", " ");
            //For debugging
            System.out.println("Sentence: " + line);

            sentence = line.split(" ");

            //Filters out all of the stop words, then extra characters
            for (int i = 0; i < sentence.length; i++) {
                cleanWord = sentence[i].replaceAll("[;:',--\"\\s]", "");
                if (!cleanWord.isEmpty() && !stop.contains(cleanWord)) {
                    stemmedWord = stem.stem(cleanWord);
                    //System.out.println(stemmedWord);
                    stemSentence.add(stemmedWord);
                }
            }

            //For debugging
            System.out.println("Stem: " + stemSentence);

            sentencesList.add(stemSentence);
            stemSentence.clear();
        }

        return sentencesList;
    }

    //*** Might need to change to account for words on the same line in stopwords.txt
    private List<String> stopWords() {
        List<String> words = new LinkedList<>();
        Scanner s2 = null;
        try {
            s2 = new Scanner(stopWords);
        } catch (FileNotFoundException er) {
            er.printStackTrace();
        }

        if (s2 != null)
            while (s2.hasNextLine()) {
                Scanner tmp = new Scanner(s2.nextLine());
                tmp.useDelimiter("[\\s]");
                while (tmp.hasNext()) {
                    String x = tmp.next();
                    if (!x.isEmpty()){
                        words.add(x);
                    }
                }
            }

        return words;
    }
}
