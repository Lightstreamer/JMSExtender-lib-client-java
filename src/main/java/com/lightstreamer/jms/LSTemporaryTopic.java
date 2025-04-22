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
import com.lightstreamer.jms.descriptors.MessageDescriptor;
import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.jms.utils.JSON;
import javax.jms.JMSException;
import javax.jms.TemporaryTopic;

class LSTemporaryTopic extends LSTopic implements TemporaryTopic {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTemporaryTopic(LSSessionImpl session, String name) {
    super(session, name);
  }

  /////////////////////////////////////////////////////////////////////////
  // TemporaryTopic interface

  @Override
  public void delete() throws JMSException {
    MessageDescriptor deleteDescriptor =
        new MessageDescriptor(
            session.getLSConnection().getJmsConnector(),
            session.getLSConnection().getClientID(),
            session.getLocalGuid(),

            // Must always be QUEUE, if we say TOPIC it could be deleted on the shared  session
            DestinationType.QUEUE,
            name,
            MessageKind.DELETE_TEMP_TOPIC);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = session.getLocalGuid().replace('-', '_');
    String deleteMessage = JSON.stringify(deleteDescriptor);

    session.getLSConnection().getLsClient().sendMessage(deleteMessage, sequenceId, -1, null, true);
  }
}
