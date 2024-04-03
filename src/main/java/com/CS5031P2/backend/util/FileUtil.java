package com.CS5031P2.backend.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class for reading files from the resources folder.
 */
public class FileUtil {

    /**
     * Reads the content of a file from the resources folder.
     *
     * @param filename The name of the file to read.
     * @return The content of the file as a string.
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static String readFile(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}
