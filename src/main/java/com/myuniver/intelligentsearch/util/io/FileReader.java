package com.myuniver.intelligentsearch.util.io;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

/**
 * User: Dmitry Fateev
 * Date: 09.04.13
 * Time: 23:31
 */
public class FileReader implements ResourceReader {
    private final String file;
    private Scanner scanner;

    private FileReader(String path) {
        this.file = path;
    }

    public static FileReader createByFile(String file) {
        return new FileReader(file);
    }

    @Override
    @SuppressWarnings("uncheked")
    public FileReader open() throws IOException {
        scanner = new Scanner(Paths.get(file), Charsets.UTF_8.name());
        return this;
    }

    @Override
    @SuppressWarnings("uncheced")
    public Iterator<String> iterator() {
        return new FileIterator();
    }

    private class FileIterator implements Iterator<String> {
        @Override
        public boolean hasNext() {
            return scanner.hasNextLine();
        }

        @Override
        public String next() {
            return scanner.nextLine();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


}
