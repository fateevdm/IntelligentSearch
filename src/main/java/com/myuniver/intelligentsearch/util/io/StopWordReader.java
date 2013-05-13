package com.myuniver.intelligentsearch.util.io;

import com.myuniver.intelligentsearch.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * User: Dmitry Fateev
 * Date: 09.04.13
 * Time: 23:31
 */
public class StopWordReader {
    private final static String STOP_WORD_FILE = "dictionary.stopwords";
    private final Config config;
    private Set<String> stopWords;

    public StopWordReader() {
        this.config = Config.getConfig();
        this.stopWords = new HashSet<>();
    }

    public Set<String> getData() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(config.getProperty(STOP_WORD_FILE)));
        while (scanner.hasNextLine()) {
            String symbol = scanner.nextLine();
            stopWords.add(symbol.trim());
        }

        return stopWords;
    }
}
