package com.CS5031P2.backend.util;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {
    // To satisfy coverage
    FileUtil fileUtil = new FileUtil();

    @Test
    void testReadFile_WithContent() throws IOException {
        // Read the content using the method under test
        String content = FileUtil.readFile("src/test/resources/test.txt");

        // Verify the content
        assertEquals("This is a test file.", content, "The content read should match the content written.");
    }

    @Test
    void testReadFile_FileDoesNotExist() {
        // Attempt to read the non-existent file and verify that an IOException is thrown
        assertThrows(IOException.class, () -> FileUtil.readFile("non_existent.txt"),
                "Attempting to read a non-existent file should throw an IOException.");
    }
}