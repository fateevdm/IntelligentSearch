package com.myuniver.intelligentsearch.util.io.file;

import com.myuniver.intelligentsearch.morphology.PrototypeSimpleDictionary;
import com.myuniver.intelligentsearch.morphology.Word;
import com.myuniver.intelligentsearch.stemmer.SimpleStemmer;
import com.myuniver.intelligentsearch.stemmer.Stemmer;
import com.myuniver.intelligentsearch.structure.Dictionary;

import java.io.IOException;
import java.util.Set;

/**
 * User: Dima
 * Date: 20.02.13
 * Time: 0:40
 */
public class DictionaryReader {
    private FileReader reader;
    private Dictionary<String, Word> dictionary = PrototypeSimpleDictionary.create();
    private Stemmer stemmer = SimpleStemmer.getStemmer();

    public DictionaryReader(String filePath) throws IOException {
        reader = FileReader.createByFile(filePath);
    }

    /**
     * Читает из файла строки с данными для словаря, парсит и складывает в словарь
     *
     * @return словарь пар <Стемма, Word>
     */
    public Dictionary<String, Word> open() {
        String COMMENT = "#";
        reader.open();
        for (String line : reader) {
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
        }
        return dictionary;
    }

    public Set<Word> getWordsSet() {
        return dictionary.values();
    }
}
