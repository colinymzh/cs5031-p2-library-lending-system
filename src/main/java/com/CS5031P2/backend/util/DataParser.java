package com.CS5031P2.backend.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for parsing data from JSON files.
 */
public class DataParser {

    /**
     * Parses JSON objects from a file and returns them as a map.
     *
     * @param file the name of the JSON file to parse
     * @return a map containing the parsed JSON objects
     * @throws IOException if an I/O error occurs while reading the file
     */
    protected Map<String, Object> parseObjects(String file) throws IOException {
        Map<String, Object> parsedObjects = new HashMap<>();

        // Read the content of the JSON file
        String jsonContent = FileUtil.readFile(file);

        // Convert JSON content to a list of objects
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> jsonNodes = objectMapper.readValue(jsonContent, new TypeReference<>() {});

        for (Map<String, Object> jsonNode : jsonNodes) {
            parseObject(parsedObjects, jsonNode);
        }

        return parsedObjects;
    }

    /**
     * Parses a single JSON object map and adds it to the map of parsed objects.
     *
     * @param parseObjects the map of parsed objects
     * @param entry        the JSON object map to parse
     */
    protected void parseObject(Map<String, Object> parseObjects, Map<String, Object> entry) {}
}
