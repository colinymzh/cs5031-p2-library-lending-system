package com.CS5031P2.backend.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.CS5031P2.backend.model.Member;

import java.io.IOException;
import java.util.Map;

/**
 * Unit tests for the MemberParser class.
 */
public class MemberParserTest {

    private MemberParser memberParser;
    private static MockedStatic<FileUtil> mockedFileUtil;

    /**
     * Setup mock behavior before running any tests.
     * @throws IOException if an I/O error occurs
     */
    @BeforeAll
    public static void mockSetup() throws IOException {
        mockedFileUtil = Mockito.mockStatic(FileUtil.class);
        when(FileUtil.readFile("src/test/resources/member_test_data.json"))
                .thenReturn("[{\"name\":\"Member Name\",\"address\":\"Member Address\"}]");

        // Throw IOException for a specific test file to simulate read error
        when(FileUtil.readFile("src/test/resources/invalid_member_data.json")).thenThrow(IOException.class);
    }

    /**
     * Clean up after running all tests.
     */
    @AfterAll
    public static void tearDown() {
        mockedFileUtil.close(); // Unregister the mock
    }

    /**
     * Setup before each test.
     */
    @BeforeEach
    public void setUp() {
        memberParser = new MemberParser();
    }

    /**
     * Test successful parsing of valid member data.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSuccessfulParsing() throws IOException {
        Map<String, Member> members = memberParser.getParsedMembers("src/test/resources/member_test_data.json");

        assertNotNull( members, "The returned map should not be null");
        assertFalse(members.isEmpty(), "The map should contain at least one entry");
        assertTrue(members.values().stream().anyMatch(member -> "Member Name".equals(member.getName())),
                "The map should contain a member with the specified name");
        assertTrue(members.values().stream().anyMatch(member -> "Member Address".equals(member.getAddress())),
                "The map should contain a member with the specified address");
    }

    /**
     * Test that an IOException is thrown when parsing invalid member data.
     */
    @Test
    public void testIOExceptionThrown() {
        // Test IOException is thrown when reading an invalid file
        assertThrows(IOException.class, () -> {
            memberParser.getParsedMembers("src/test/resources/invalid_member_data.json");
        }, "IOException should be thrown when reading an invalid file");
    }
}
