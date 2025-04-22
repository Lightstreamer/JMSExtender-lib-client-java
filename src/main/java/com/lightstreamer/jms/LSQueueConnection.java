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
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;

class LSQueueConnection extends LSConnection implements QueueConnection {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueueConnection(LightstreamerClient lsClient, String jmsConnector) throws JMSException {
    super(lsClient, jmsConnector);
  }

  LSQueueConnection(LightstreamerClient lsClient, String jmsConnector, boolean owned)
      throws JMSException {
    super(lsClient, jmsConnector, owned);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueConnection interface

  @Override
  public synchronized QueueSession createQueueSession(boolean transacted, int acknowledgeMode)
      throws JMSException {

    setUsed();

    LSQueueSession session = new LSQueueSession(this, transacted, acknowledgeMode);
    sessions.put(session.getLocalGuid(), session);

    if (isRunning()) {
      session.start();
    }

    return session;
  }

  @Override
  public ConnectionConsumer createConnectionConsumer(
      Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException("Connection consumers are not supported");
  }
}
