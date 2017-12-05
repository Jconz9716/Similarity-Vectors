package edu.uiowa.cs.similarity;


import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import opennlp.tools.stemmer.*;

public class FileFilter implements Filter<String> {
    private final File text;
    private final File stopWords;

    public FileFilter(File text, File grosMots) {
        this.text = text;
        this.stopWords = grosMots;
    }

    public List<List<String>> getCleanAndStemmedWords() {
        Scanner s = null;
        //Throws error if file can't be found
        try {
            s = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return meatAndPotatoes(s);
    }

    /*This is where the action happens
    Goes until there are no lines left in the document. It's ok if the last line is blank since most end
    in a blank line */
    private List<List<String>> meatAndPotatoes(Scanner scanner) {
        List<List<String>> sentencesList = new LinkedList<>();
        List<String> stop = stopWords();
        String cleanWord;
        String stemmedWord;
        String line;
        String[] sentence;
        PorterStemmer stem = new PorterStemmer();

        scanner.useDelimiter("[.!?]");


        while (scanner.hasNext()) {
            List<String> stemSentence = new LinkedList<>();
            line = scanner.next().toLowerCase();

            line = line.replaceAll("\n", " ");

            sentence = line.split(" ");

            //Filters out all of the extra characters, then  stop words
            for (int i = 0; i < sentence.length; i++) {
                cleanWord = sentence[i].replaceAll("[;:,--\"\\s]", "");
                cleanWord = cleanWord.replaceAll("[^a-zA-Z]", "");
                if (!cleanWord.isEmpty() && !stop.contains(cleanWord)) {
//                    Removing "'" after checking for stop words in order to catch contractions
//                    Ex. don't, can't, wouldn't, etc.
                    cleanWord = cleanWord.replaceAll("[']", "");
//                    System.out.println(cleanWord);
                    stemmedWord = stem.stem(cleanWord);
//                    System.out.println("Stemmed --> " + stemmedWord);
                    stemSentence.add(stemmedWord);
                }
            }
            //Will only print stemmed sentence if the element is not empty
            if (!stemSentence.isEmpty()) {
                sentencesList.add(stemSentence);
//                System.out.println("Sentence: " + stemSentence);
            }
        }
        return sentencesList;

    }

    public List<List<String>> getDirtyWords() {
        Scanner s = null;

        //Throws error if file can't be found
        try {
            s = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return getDirtyWordsHelper(s);
    }

    private List<List<String>> getDirtyWordsHelper(Scanner scanner) {
        List<List<String>> sentencesList = new LinkedList<>();
        List<String> stop = stopWords();
        String cleanWord;
        String line;
        String[] sentence;

        scanner.useDelimiter("[.!?]");

        int index = 0;
        while (scanner.hasNext()) {
            List<String> sentenceList = new LinkedList<>();
            line = scanner.next().toLowerCase();

            line = line.replaceAll("\n", " ");
            line = line.replaceAll("-(\\s*-)+", " ");

            sentence = line.split(" ");

            //Filters out all of the extra characters, then  stop words
            for (int i = 0; i < sentence.length; i++) {
                cleanWord = sentence[i].replaceAll("[;:,\".*()\\s]", "");
                cleanWord = cleanWord.replaceAll("[^a-zA-Z]", "");
                //System.out.println(cleanWord);
                if (!cleanWord.isEmpty() && !stop.contains(cleanWord)) {
//                    Removing "'" after checking for stop words in order to catch contractions
//                    Ex. don't, can't, wouldn't, etc.
                    cleanWord = cleanWord.replaceAll("[']", "");
                    sentenceList.add(cleanWord);
                }
            }
            //Will only print stemmed sentence if the element is not empty
            if (!sentenceList.isEmpty()) {
                sentencesList.add(index, sentenceList);
                index++;
//                System.out.println("Sentence: " + stemSentence);
            }
        }
        return sentencesList;
    }

    //Calculate number of unique words in file
    public int getNumUniqueWords() {
        Iterator<List<String>> sentences = getCleanAndStemmedWords().iterator();
        int count = 0;
        while (sentences.hasNext()) {
            count += sentences.next().size();
        }
        return count;
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
                tmp.useDelimiter("[\\s\n]");
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
