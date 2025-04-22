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

import com.lightstreamer.jms.LSSession;

public enum AcknowledgeMode {
  PRE_ACKNOWLEDGE(LSSession.PRE_ACKNOWLEDGE, "PRE_ACK"),

  SESSION_TRANSACTED(LSSession.SESSION_TRANSACTED, "TRANSACTED"),

  AUTO_ACKNOWLEDGE(LSSession.AUTO_ACKNOWLEDGE, "AUTO_ACK"),

  CLIENT_ACKNOWLEDGE(LSSession.CLIENT_ACKNOWLEDGE, "CLIENT_ACK"),

  DUPS_OK_ACKNOWLEDGE(LSSession.DUPS_OK_ACKNOWLEDGE, "DUPS_OK"),

  INDIVIDUAL_ACKNOWLEDGE(LSSession.INDIVIDUAL_ACKNOWLEDGE, "INDIVIDUAL_ACK");

  private int id;

  private String name;

  private AcknowledgeMode(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static AcknowledgeMode valueOf(int id) {
    switch (id) {
      case LSSession.PRE_ACKNOWLEDGE:
        return PRE_ACKNOWLEDGE;

      case LSSession.SESSION_TRANSACTED:
        return SESSION_TRANSACTED;

      case LSSession.AUTO_ACKNOWLEDGE:
        return AUTO_ACKNOWLEDGE;

      case LSSession.CLIENT_ACKNOWLEDGE:
        return CLIENT_ACKNOWLEDGE;

      case LSSession.DUPS_OK_ACKNOWLEDGE:
        return DUPS_OK_ACKNOWLEDGE;

      case LSSession.INDIVIDUAL_ACKNOWLEDGE:
        return INDIVIDUAL_ACKNOWLEDGE;

      default:
        throw new IllegalArgumentException("Not a valid acknowledge mode ID: " + id);
    }
  }
}
