package com.smile.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smile.backend.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode objectNodeFormat(Map<String, Object> map) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            JsonNode node = objectMapper.valueToTree(entry.getValue());
            objectNode.set(entry.getKey(), node);
        }
        return objectNode;
    }
    public static <T> T StringToObject(String content, Class<T> valueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T) objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            logger.info("StringToObject failed!");
            return null;
        }
    }

    public static String objectToJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.debug("objectToJsonString failed!");
        }
        return jsonString;
    }
}
