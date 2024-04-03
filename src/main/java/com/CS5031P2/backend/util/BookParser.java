package com.CS5031P2.backend.util;

import com.CS5031P2.backend.model.Book;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing book data from JSON files.
 */
public class BookParser extends DataParser {

    /**
     * Parses a JSON object representing a book and adds it to the map of parsed objects.
     *
     * @param parseObjects the map of parsed objects
     * @param entry        the JSON object representing a book to parse
     */
    @Override
    protected void parseObject(Map<String, Object> parseObjects, Map<String, Object> entry) {
        String title = (String) entry.get("title");
        String author = (String) entry.get("author");
        Book book = new Book(title, author);
        parseObjects.put(book.getBookId(), book);
    }

    /**
     * Parses books from a JSON file and returns them as a map.
     *
     * @return a map containing the parsed books
     * @throws IOException if an I/O error occurs while reading the file
     */
    public Map<String, Book> parseBooks(String filename) throws IOException {
        Map<String, Object> parsedObjects = parseObjects(filename);

        return parsedObjects.entrySet().stream()
                .collect(HashMap::new,
                        (result, entry) -> result.put(entry.getKey(), (Book) entry.getValue()),
                        HashMap::putAll);
    }
}
