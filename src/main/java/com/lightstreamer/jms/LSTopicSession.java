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
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

class LSTopicSession extends LSSessionImpl implements TopicSession {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTopicSession(LSConnection connection, boolean transacted, int acknowledgeMode) {
    super(connection, transacted, acknowledgeMode);
  }

  /////////////////////////////////////////////////////////////////////////
  // TopicSession interface

  @Override
  public synchronized TopicSubscriber createSubscriber(Topic topic) throws JMSException {
    return (LSTopicSubscriber) super.createConsumer(topic);
  }

  @Override
  public synchronized TopicSubscriber createSubscriber(
      Topic topic, String messageSelector, boolean noLocal) throws JMSException {

    return (LSTopicSubscriber) super.createConsumer(topic, messageSelector, noLocal);
  }

  @Override
  public synchronized TopicPublisher createPublisher(Topic topic) throws JMSException {
    return (LSTopicPublisher) super.createProducer(topic);
  }
}
