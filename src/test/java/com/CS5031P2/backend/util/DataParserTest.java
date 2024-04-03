package com.CS5031P2.backend.util;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DataParser class.
 */
class DataParserTest {

    /**
     * Test parsing objects from an existing file.
     */
    @Test
    void testParseObjects_FileExists() {
        DataParser dataParser = new DataParser();
        try {
            Map<String, Object> parsedObjects = dataParser.parseObjects("src/test/resources/test_data.json");
            assertNotNull(parsedObjects, "Parsed objects should not be null");
        } catch (IOException e) {
            fail("IOException should not occur when parsing existing file");
        }
    }

    /**
     * Test parsing objects from a non-existing file.
     */
    @Test
    void testParseObjects_FileDoesNotExist() {
        DataParser dataParser = new DataParser();
        assertThrows(IOException.class, () -> dataParser.parseObjects("non_existing_file.json"),
                "IOException should be thrown for non-existing file");
    }
}
