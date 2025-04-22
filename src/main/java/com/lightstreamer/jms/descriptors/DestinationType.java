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
package com.lightstreamer.jms.descriptors;

public enum DestinationType {
  TOPIC(Constants.DESTINATION_TYPE_TOPIC, "TOPIC"),

  QUEUE(Constants.DESTINATION_TYPE_QUEUE, "QUEUE"),

  EXCEPTIONS(Constants.DESTINATION_TYPE_EXCEPTIONS, "EXCEPTIONS"),

  OUTCOMES(Constants.DESTINATION_TYPE_OUTCOMES, "OUTCOMES");

  private int id;

  private String name;

  private DestinationType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static DestinationType valueOf(int id) {
    switch (id) {
      case Constants.DESTINATION_TYPE_TOPIC:
        return TOPIC;

      case Constants.DESTINATION_TYPE_QUEUE:
        return QUEUE;

      case Constants.DESTINATION_TYPE_EXCEPTIONS:
        return EXCEPTIONS;

      case Constants.DESTINATION_TYPE_OUTCOMES:
        return OUTCOMES;

      default:
        throw new IllegalArgumentException("Not a valid destination type ID: " + id);
    }
  }
}
