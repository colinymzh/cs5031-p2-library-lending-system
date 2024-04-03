package com.CS5031P2.backend.util;

import com.CS5031P2.backend.model.Member;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing member data from JSON files.
 */
public class MemberParser extends DataParser {

    /**
     * Parses a JSON object representing a member and adds it to the map of parsed objects.
     *
     * @param parseObjects the map of parsed objects
     * @param entry        the JSON object representing a member to parse
     */
    @Override
    protected void parseObject(Map<String, Object> parseObjects, Map<String, Object> entry) {
        String name = (String) entry.get("name");
        String address = (String) entry.get("address");
        Member member = new Member(name, address);
        parseObjects.put(member.getMemberId(), member);
    }

    /**
     * Parses members from a JSON file and returns them as a map.
     *
     * @return a map containing the parsed members
     * @throws IOException if an I/O error occurs while reading the file
     */
    public Map<String, Member> getParsedMembers(String filename) throws IOException {
        Map<String, Object> parsedObjects = parseObjects(filename);

        return parsedObjects.entrySet().stream()
                .collect(HashMap::new,
                        (result, entry) -> result.put(entry.getKey(), (Member) entry.getValue()),
                        HashMap::putAll);
    }
}
