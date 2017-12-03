package edu.uiowa.cs.similarity;


import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import opennlp.tools.stemmer.*;

public class FindSentences{
    private final File text;
    private final File stopWords;

    public FindSentences(File text, File grosMots) {
        this.text = text;
        //Bad words are "gros mots" in French
        stopWords = grosMots;
    }

    public List<List<String>> filterText() {
        List<List<String>> sentencesList = new LinkedList<>();
        List<String> stop = stopWords();
        String cleanWord;
        String stemmedWord;
        String line;
        String[] sentence;
        PorterStemmer stem = new PorterStemmer();
        Scanner s = null;

        //Throws error if file can't be found
        try {
            s = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert s != null;
        s.useDelimiter("[!?.]");

        //Goes until there are no lines left in the document. It's ok if the last line is blank since most end
        //in a blank line
        int count = 0;
        while (s.hasNext()) {
            List<String> stemSentence = new LinkedList<>();
            line = s.next().toLowerCase();

            line = line.replaceAll("\n", " ");

            sentence = line.split(" ");

            //Filters out all of the extra characters, then  stop words
            for (int i = 0; i < sentence.length; i++) {
                cleanWord = sentence[i].replaceAll("[;:,--\"\\s]", "");
                if (!cleanWord.isEmpty() && !stop.contains(cleanWord)) {
                    //Removing "'" after checking for stop words in order to catch contractions
                    // Ex. don't, can't, wouldn't, etc.
                    cleanWord = cleanWord.replaceAll("[']", "");
                    stemmedWord = stem.stem(cleanWord);
                    stemSentence.add(stemmedWord);
                }
            }
            //Will only print stemmed sentence if the element is not empty
            if (!stemSentence.isEmpty()) {
                sentencesList.add(count, stemSentence);
                count++;
                //System.out.println("Sentence: " + stemSentence);
            }
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
