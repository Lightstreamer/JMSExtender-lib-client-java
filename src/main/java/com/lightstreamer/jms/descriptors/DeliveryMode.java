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

public enum DeliveryMode {
  PERSISTENT(javax.jms.DeliveryMode.PERSISTENT, "PERSISTENT"),

  NON_PERSISTENT(javax.jms.DeliveryMode.NON_PERSISTENT, "NON_PERSISTENT");

  private int id;

  private String name;

  private DeliveryMode(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static DeliveryMode valueOf(int id) {
    switch (id) {
      case javax.jms.DeliveryMode.NON_PERSISTENT:
        return NON_PERSISTENT;

      case javax.jms.DeliveryMode.PERSISTENT:
        return PERSISTENT;

      default:
        throw new IllegalArgumentException("Not a valid message kind ID: " + id);
    }
  }
}
