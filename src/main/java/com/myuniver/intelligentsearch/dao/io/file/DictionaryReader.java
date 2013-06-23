package com.myuniver.intelligentsearch.dao.io.file;

import com.google.common.base.Throwables;
import com.myuniver.intelligentsearch.morphology.PrototypeSimpleDictionary;
import com.myuniver.intelligentsearch.morphology.Word;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.structure.Dictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.myuniver.intelligentsearch.util.Config.getConfig;

/**
 * User: Dima
 * Date: 20.02.13
 * Time: 0:40
 */
public class DictionaryReader {
    private FileReader reader;
    private Dictionary<String, Word> dictionary = PrototypeSimpleDictionary.create();
    private Stemmer stemmer = SimpleStemmer.getStemmer();
    public static final Logger log = LoggerFactory.getLogger(DictionaryReader.class);
    public static final String DEFAULT_DICTIONARY = "dictionary.simple";

    public static DictionaryReader createDefaultDictionary() {
        String dictionaryPath = getConfig().getProperty(DEFAULT_DICTIONARY);
        return new DictionaryReader(dictionaryPath);
    }

    public static DictionaryReader createDictionary(String pathToFile) {
        checkArgument(isNullOrEmpty(pathToFile), new IllegalArgumentException("should not be null or empty"));
        ;
        String dictionaryPath = getConfig().getProperty(pathToFile);
        return new DictionaryReader(dictionaryPath);
    }

    private DictionaryReader(String filePath) {
        reader = FileReader.createByFile(filePath);
    }

    public Dictionary<String, Word> open() {
        String COMMENT = "#";
        reader.open();
        int count = 0;

        for (String line : reader) {
            try {
                ++count;
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
                Word word = new Word(normalForm, stemma, morpheme);
                dictionary.put(morpheme, word);

            } catch (Exception e) {
                log.error("line {}, line {}", count, line, e);
                Throwables.propagate(e.getCause());
            }
        }
        return dictionary;
    }


    public Set<Word> getWordsSet() {
        return dictionary.values();
    }
}
