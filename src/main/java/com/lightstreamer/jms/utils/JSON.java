/*
 * Copyright (C) 2020 Lightstreamer Srl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
