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

    public FindSentences(File text) {
        this.text = text;
        stopWords = new File("stopwords.txt");
    }

    public List<List<String>> filterText() {
        if (text == null){ throw new IllegalStateException(); }

        Scanner s = null;
        try {
            s = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        s.useDelimiter("[!?.]");

        String stemmedWord;
        String line;
        List<String> stemSentence = new LinkedList<>();
        String[] sentence;
        PorterStemmer stem = new PorterStemmer();

        while (s.hasNext()){

            //Filter out stop words before removing characters but after making lowercase?

            line = s.next().toLowerCase();
            line = line.replaceAll("[;:'\n,\\-\"]", "");

            System.out.println("Line: " + line);

            sentence = line.split(" ");

            List<String> stopWords = stopWords();

            for (int i = 0; i<sentence.length; i++)
            {
                if (!stopWords.contains(sentence[i])) {
                    stemmedWord = stem.stem(sentence[i]);
                    //System.out.println("Word: " + stemmedWord + "\n");
                    stemSentence.add(stemmedWord);
                }

            }

            sentencesList.add(stemSentence);
            System.out.println(stemSentence);
            stemSentence.clear();
        }

        return sentencesList;
    }

    private List<String> stopWords() {
        List<String> words = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(stopWords))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
