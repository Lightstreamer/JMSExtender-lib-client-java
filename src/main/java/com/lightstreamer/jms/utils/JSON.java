package com.lightstreamer.jms.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class JSON {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.json");

  private static JsonFactory factory;
  private static ObjectMapper mapper;

  ///////////////////////////////////////////////////////////////////////////
  // Initialization

  static {
    factory = new JsonFactory();
    factory.disable(Feature.AUTO_CLOSE_TARGET);

    mapper = new ObjectMapper(factory);
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Public operations

  public static String stringify(Object object) {
    try {
      StringWriter jsonWriter = new StringWriter();
      mapper.writeValue(jsonWriter, object);
      jsonWriter.close();
      return jsonWriter.toString();
    } catch (IOException ioe) {
      log.error("I/O exception while stringifying object to JSON: " + ioe.getMessage(), ioe);
      return null;
    }
  }

  public static Map<String, Object> parseAsMap(String json) {
    try {
      return mapper.readValue(
          json,
          new TypeReference<Map<String, Object>>() {
            /* Nothing to do here */
          });
    } catch (IOException ioe) {
      log.error("I/O exception while parsing JSON as map: " + ioe.getMessage(), ioe);
      return null;
    }
  }

  public static Object parse(String json, Class<?> clazz) {
    try {
      return mapper.readValue(json, clazz);
    } catch (IOException ioe) {
      log.error("I/O exception while parsing JSON as object: " + ioe.getMessage(), ioe);
      return null;
    }
  }
}
