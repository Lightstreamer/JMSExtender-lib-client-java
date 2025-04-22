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

public enum MessageKind {
  TEXT_MESSAGE(Constants.MESSAGE_KIND_TEXT, "TEXT_MSG"),

  OBJECT_MESSAGE(Constants.MESSAGE_KIND_OBJECT, "OBJECT_MSG"),

  MAP_MESSAGE(Constants.MESSAGE_KIND_MAP, "MAP_MSG"),

  BYTES_MESSAGE(Constants.MESSAGE_KIND_BYTES, "BYTES_MSG"),

  READ_NEXT(Constants.MESSAGE_KIND_READ_NEXT, "READ_NEXT"),

  ACKNOWLEDGE(Constants.MESSAGE_KIND_ACK, "ACKNOWLEDGE"),

  UNSUBSCRIBE(Constants.MESSAGE_KIND_UNSUBSCRIBE, "UNSUBSCRIBE"),

  RECOVER(Constants.MESSAGE_KIND_RECOVER, "RECOVER"),

  COMMIT(Constants.MESSAGE_KIND_COMMIT, "COMMIT"),

  ROLLBACK(Constants.MESSAGE_KIND_ROLLBACK, "ROLLBACK"),

  CREATE_TEMP_QUEUE(Constants.MESSAGE_KIND_CREATE_TEMP_QUEUE, "CREATE_TEMP_QUEUE"),

  DELETE_TEMP_QUEUE(Constants.MESSAGE_KIND_DELETE_TEMP_QUEUE, "DELETE_TEMP_QUEUE"),

  CREATE_TEMP_TOPIC(Constants.MESSAGE_KIND_CREATE_TEMP_TOPIC, "CREATE_TEMP_TOPIC"),

  DELETE_TEMP_TOPIC(Constants.MESSAGE_KIND_DELETE_TEMP_TOPIC, "DELETE_TEMP_TOPIC");

  private int id;
  private String name;

  private MessageKind(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static MessageKind valueOf(int id) {
    switch (id) {
      case Constants.MESSAGE_KIND_TEXT:
        return TEXT_MESSAGE;

      case Constants.MESSAGE_KIND_OBJECT:
        return OBJECT_MESSAGE;

      case Constants.MESSAGE_KIND_MAP:
        return MAP_MESSAGE;

      case Constants.MESSAGE_KIND_BYTES:
        return BYTES_MESSAGE;

      case Constants.MESSAGE_KIND_READ_NEXT:
        return READ_NEXT;

      case Constants.MESSAGE_KIND_ACK:
        return ACKNOWLEDGE;

      case Constants.MESSAGE_KIND_UNSUBSCRIBE:
        return UNSUBSCRIBE;

      case Constants.MESSAGE_KIND_RECOVER:
        return RECOVER;

      case Constants.MESSAGE_KIND_COMMIT:
        return COMMIT;

      case Constants.MESSAGE_KIND_ROLLBACK:
        return ROLLBACK;

      case Constants.MESSAGE_KIND_CREATE_TEMP_QUEUE:
        return CREATE_TEMP_QUEUE;

      case Constants.MESSAGE_KIND_DELETE_TEMP_QUEUE:
        return DELETE_TEMP_QUEUE;

      case Constants.MESSAGE_KIND_CREATE_TEMP_TOPIC:
        return CREATE_TEMP_TOPIC;

      case Constants.MESSAGE_KIND_DELETE_TEMP_TOPIC:
        return DELETE_TEMP_TOPIC;

      default:
        throw new IllegalArgumentException("Not a valid message kind ID: " + id);
    }
  }
}
