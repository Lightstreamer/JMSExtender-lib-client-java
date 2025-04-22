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
package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.DestinationType;
import javax.jms.Destination;

class LSDestination implements Destination {

  protected final LSSessionImpl session;
  protected final DestinationType type;
  protected final String name;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSDestination(LSSessionImpl session, DestinationType type, String name) {
    this.session = session;
    this.type = type;
    this.name = name;
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  LSSessionImpl getLSSession() {
    return session;
  }

  DestinationType getLSDestinationType() {
    return type;
  }

  String getName() {
    return name;
  }
}
