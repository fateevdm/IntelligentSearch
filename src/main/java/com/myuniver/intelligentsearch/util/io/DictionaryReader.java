package com.myuniver.intelligentsearch.util.io;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.myuniver.intelligentsearch.Word;
import com.myuniver.intelligentsearch.morphology.PrototypeSimpleDictionary;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.util.Dictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * User: Dima
 * Date: 20.02.13
 * Time: 0:40
 */
public class DictionaryReader {
    private InputStream dataStream;
    private Dictionary<String, Word> dictionary = PrototypeSimpleDictionary.create();
    private Stemmer stemmer = SimpleStemmer.getStemmer();
    public static final Logger LOGGER = LoggerFactory.getLogger(DictionaryReader.class);
    private Multiset<Word> wordsSet = HashMultiset.create();
    private final String COMMENT = "#";

    public DictionaryReader(String filePath) throws FileNotFoundException {
        this(new FileInputStream(filePath));
    }

    public DictionaryReader(InputStream inputStream) {
        this.dataStream = inputStream;
    }

    /**
     * Читает
     *
     * @return
     */
    public Dictionary<String, Word> openConnection() {
        Scanner scanner = new Scanner(dataStream);
        int index = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.contains(COMMENT)) continue;
            String[] elements = line.split("\\s");
            String morpheme = elements[0].trim().toLowerCase();
            String normalForm = elements[1].trim().toLowerCase();
            String stemma;
            if (elements.length < 3) {
                stemma = stemmer.stemm(morpheme);
            } else {
                stemma = elements[2].trim().toLowerCase();
            }
            Word word = new Word(normalForm, stemma)
                    .addMorpheme(morpheme);
            dictionary.put(morpheme, word);
            wordsSet.add(word);
//            LOGGER.info("morpheme {}; normal {}; stemm {}", dictionary.getByValue(word), dictionary.get(morpheme));
        }
        return dictionary;
    }

    public Multiset<Word> getWordsSet() {
        return ImmutableMultiset.copyOf(wordsSet);
    }
}
