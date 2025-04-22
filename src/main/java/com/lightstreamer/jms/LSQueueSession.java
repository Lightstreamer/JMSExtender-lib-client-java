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

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

class LSQueueSession extends LSSessionImpl implements QueueSession {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueueSession(LSConnection connection, boolean transacted, int acknowledgeMode) {
    super(connection, transacted, acknowledgeMode);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueSession interface

  @Override
  public synchronized QueueReceiver createReceiver(Queue queue) throws JMSException {
    return (LSQueueReceiver) super.createConsumer(queue);
  }

  @Override
  public synchronized QueueReceiver createReceiver(Queue queue, String messageSelector)
      throws JMSException {
    return (LSQueueReceiver) super.createConsumer(queue, messageSelector);
  }

  @Override
  public synchronized QueueSender createSender(Queue queue) throws JMSException {
    return (LSQueueSender) super.createProducer(queue);
  }
}
