package io.github.chikitlo.common.util;

import io.github.chikitlo.common.constant.DateConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapType;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * JSON utilities backed by a single, pre-configured Jackson {@link tools.jackson.databind.ObjectMapper}.
 * <p>
 * Provides thread-safe static helpers for common JSON serialization/deserialization tasks used across the project.
 * The underlying mapper is configured with a consistent date/time format, timezone and LocalDateTime (de)serializers.
 * </p>
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/20 15:56
 */
@Slf4j
public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = JsonMapper
            .builder()
            .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
            .configure(EnumFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .addModule(new SimpleModule()
                    .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateConstants.YYYY_MM_DD_HH_MM_SS)))
                    .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateConstants.YYYY_MM_DD_HH_MM_SS))))
            .defaultTimeZone(TimeZone.getDefault())
            .defaultDateFormat(new SimpleDateFormat(DateConstants.YYYY_MM_DD_HH_MM_SS))
            .build();

    private JsonUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Set the default time zone of the global {@link ObjectMapper}.
     *
     * @param timeZone
     * @return
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 16:22
     */
    public static synchronized void setTimeZone(TimeZone timeZone) {
        OBJECT_MAPPER.rebuild().defaultTimeZone(timeZone).build();
    }

    /**
     * Create an empty {@link ArrayNode} using the shared mapper configuration.
     *
     * @param
     * @return tools.jackson.databind.node.ArrayNode
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 16:23
     */
    public static ArrayNode createArrayNode() {
        return OBJECT_MAPPER.createArrayNode();
    }

    /**
     * Create an empty {@link ObjectNode} using the shared mapper configuration.
     *
     * @param
     * @return tools.jackson.databind.node.ObjectNode
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 16:24
     */
    public static ObjectNode createObjectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    /**
     * Convert an arbitrary object into a {@link JsonNode} tree.
     *
     * @param obj
     * @return tools.jackson.databind.JsonNode
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 16:26
     */
    public static JsonNode toJsonNode(Object obj) {
        return OBJECT_MAPPER.valueToTree(obj);
    }

    /**
     * Serialize an object to JSON using a specific {@link SerializationFeature}.
     *
     * @param obj
     * @param feature
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 17:45
     */
    public static String toJsonString(Object obj, SerializationFeature feature) {
        ObjectWriter writer = OBJECT_MAPPER.writer(feature);
        return writer.writeValueAsString(obj);
    }

    /**
     * Deserialize a JSON string into the given class.
     *
     * @param json
     * @param clazz
     * @return T
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 17:48
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            log.error("JSON string is empty");
            return null;
        }

        return OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * Deserialize JSON bytes content into the given class.
     *
     * @param content
     * @param clazz
     * @return T
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 17:57
     */
    public static <T> T parseObject(byte[] content, Class<T> clazz) {
        if (content == null) {
            log.error("JSON content is null");
            return null;
        }

        return OBJECT_MAPPER.readValue(content, clazz);
    }

    /**
     * Deserialize from a {@link Reader} into the given class.
     *
     * @param src
     * @param clazz
     * @return T
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 17:59
     */
    public static <T> T parseObject(Reader src, Class<T> clazz) {
        if (src == null) {
            log.error("Reader is null");
            return null;
        }

        return OBJECT_MAPPER.readValue(src, clazz);
    }

    /**
     * Deserialize a JSON string into a generic type using {@link TypeReference}.
     *
     * @param json
     * @param typeReference
     * @return T
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:16
     */
    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(json)) {
            log.error("JSON string is empty");
            return null;
        }

        return OBJECT_MAPPER.readValue(json, typeReference);
    }

    /**
     * Parse JSON string to an {@link ObjectNode}.
     *
     * @param json
     * @return tools.jackson.databind.node.ObjectNode
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 19:05
     */
    public static ObjectNode parseObject(String json) {
        return parseObject(json, ObjectNode.class);
    }

    /**
     * Parse JSON string to an {@link ArrayNode}.
     *
     * @param json
     * @return tools.jackson.databind.node.ArrayNode
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 19:07
     */
    public static ArrayNode parseArray(String json) {
        return (ArrayNode) OBJECT_MAPPER.readTree(json);
    }

    /**
     * Check whether a string is valid JSON.
     *
     * @param json
     * @return boolean
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:05
     */
    public static boolean checkJson(String json) {
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JacksonException e) {
            log.error("Invalid JSON: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Find the first occurrence of the given property name in the node.
     *
     * @param jsonNode
     * @param propertyName
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:10
     */
    public static String findValue(JsonNode jsonNode, String propertyName) {
        JsonNode node = jsonNode.findValue(propertyName);
        return node == null ? null : node.asString();
    }

    /**
     * Deserialize a JSON array into a {@link List} of the given element type.
     *
     * @param json
     * @param clazz
     * @return java.util.List<T>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:02
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            log.error("JSON string is empty");
            return Collections.emptyList();
        }

        CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        return OBJECT_MAPPER.readValue(json, collectionType);
    }

    /**
     * Deserialize a JSON object into a {@code Map<String, String>}.
     *
     * @param json
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:17
     */
    public static Map<String, String> toMap(String json) {
        return parseObject(json, new TypeReference<>() {
        });
    }

    /**
     * Deserialize into a typed {@link Map} with generic key/value types.
     *
     * @param json
     * @param keyClass
     * @param valueClass
     * @return java.util.Map<K,V>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:21
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isEmpty(json)) {
            log.error("JSON string is empty");
            return Collections.emptyMap();
        }

        MapType mapType = OBJECT_MAPPER.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass);
        return OBJECT_MAPPER.readValue(json, mapType);
    }

    /**
     * From a key-value JSON string, obtain the value for the given key as a string.
     *
     * @param json
     * @param propertyName
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:56
     */
    public static String getValue(String json, String propertyName) {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(json).findValue(propertyName);
        return jsonNode == null ? null : jsonNode.asString();
    }

    /**
     * Serialize an object to a JSON string using the shared mapper configuration.
     *
     * @param obj
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 18:58
     */
    public static String toJsonString(Object obj) {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * Serialize an object to JSON bytes.
     *
     * @param obj
     * @return byte[]
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 19:03
     */
    public static <T> byte[] toJsonByteArray(T obj) {
        if (obj == null) {
            log.error("Object is null");
            return null;
        }

        return toJsonString(obj).getBytes(StandardCharsets.UTF_8);
    }
}