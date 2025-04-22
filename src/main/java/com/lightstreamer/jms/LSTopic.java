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
import javax.jms.JMSException;
import javax.jms.Topic;

class LSTopic extends LSDestination implements Topic {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTopic(LSSessionImpl session, String name) {
    super(session, DestinationType.TOPIC, name);
  }

  /////////////////////////////////////////////////////////////////////////
  // Topic interface

  @Override
  public String getTopicName() throws JMSException {
    return name;
  }
}
