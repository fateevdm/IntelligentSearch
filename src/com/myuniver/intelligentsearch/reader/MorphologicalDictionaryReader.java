package com.myuniver.intelligentsearch.reader;


import com.myuniver.intelligentsearch.Word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Date: 24.10.12
 * Time: 0:44
 */
public class MorphologicalDictionaryReader {

    private Word word;

    Properties props;

    String dictionaryPath;

    public MorphologicalDictionaryReader(String dictionaryPath) {

        this.dictionaryPath = dictionaryPath;

    }

    public void read() {
//         props.getValue("path");
    }

    public void open() throws FileNotFoundException {
        InputStream is = new FileInputStream(dictionaryPath);
        Scanner scanner = new Scanner(is);
    }


}
