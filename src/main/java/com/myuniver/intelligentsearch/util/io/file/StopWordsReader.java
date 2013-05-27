package com.myuniver.intelligentsearch.util.io.file;

import com.myuniver.intelligentsearch.util.Config;
import com.myuniver.intelligentsearch.util.io.ResourceReader;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Dmitry Fateev
 * Date: 22.05.13
 * Time: 3:57
 */
public class StopWordsReader {

    public final static String STOP_WORD_FILE = "dictionary.stopwords";
    ResourceReader<String> reader;
    private Set<String> stopWords = new HashSet<>();

    public StopWordsReader() {
        reader = FileReader.createByFile(Config.getConfig().getProperty(STOP_WORD_FILE));
    }

    public StopWordsReader(String file) {
        reader = FileReader.createByFile(file);
    }

    public Set<String> getStopWords() {
        reader.open();
        for (String line : reader) {
            stopWords.add(line);
        }
        return stopWords;
    }
}
