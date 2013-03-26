package com.myuniver.intelligentsearch.util.io;

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
    InputStream dataStream;

    public DictionaryReader(String filePath) throws FileNotFoundException {
        this(new FileInputStream(filePath));
    }

    public DictionaryReader(InputStream inputStream) {
        this.dataStream = inputStream;
    }

    public void openConnection() {

        Scanner scanner = new Scanner(dataStream);
        int index = 0;
        while (scanner.hasNext()) {
            System.out.println("line number: " + ++index + " ; current line: " + scanner.nextLine().trim());
        }

    }
}
