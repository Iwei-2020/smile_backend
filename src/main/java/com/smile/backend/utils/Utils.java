package com.smile.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode objectNodeFormat(Map<String, Object> map) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            JsonNode node = objectMapper.valueToTree(entry.getValue());
            objectNode.set(entry.getKey(), node);
        }
        return objectNode;
    }
}
