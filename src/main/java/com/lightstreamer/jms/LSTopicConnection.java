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

import com.lightstreamer.client.LightstreamerClient;
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

class LSTopicConnection extends LSConnection implements TopicConnection {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTopicConnection(LightstreamerClient lsClient, String jmsConnector) throws JMSException {
    super(lsClient, jmsConnector);
  }

  LSTopicConnection(LightstreamerClient lsClient, String jmsConnector, boolean owned)
      throws JMSException {
    super(lsClient, jmsConnector, owned);
  }

  /////////////////////////////////////////////////////////////////////////
  // TopicConnection interface

  @Override
  public synchronized TopicSession createTopicSession(boolean transacted, int acknowledgeMode)
      throws JMSException {
    setUsed();

    LSTopicSession session = new LSTopicSession(this, transacted, acknowledgeMode);
    sessions.put(session.getLocalGuid(), session);

    if (isRunning()) {
      session.start();
    }

    return session;
  }

  @Override
  public ConnectionConsumer createConnectionConsumer(
      Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException("Connection consumers are not supported");
  }
}
