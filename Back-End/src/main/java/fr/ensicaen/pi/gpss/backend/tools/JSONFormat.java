package fr.ensicaen.pi.gpss.backend.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONFormat {
    private static final ObjectMapper _objectMapper = new ObjectMapper();

    public JsonNode convertToJsonNode(Object o) {
        return _objectMapper.valueToTree(o);
    }
}