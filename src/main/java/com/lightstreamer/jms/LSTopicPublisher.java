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
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicPublisher;

class LSTopicPublisher extends LSMessageProducer implements TopicPublisher {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTopicPublisher(LSSessionImpl session, LSTopic destination) {
    super(session, destination);
  }

  /////////////////////////////////////////////////////////////////////////
  // TopicPublisher interface

  @Override
  public Topic getTopic() throws JMSException {
    return (LSTopic) destination;
  }

  @Override
  public synchronized void publish(Message message) throws JMSException {
    super.send(message);
  }

  @Override
  public synchronized void publish(Message message, int deliveryMode, int priority, long timeToLive)
      throws JMSException {

    super.send(message, deliveryMode, priority, timeToLive);
  }

  @Override
  public synchronized void publish(Topic topic, Message message) throws JMSException {
    super.send(topic, message);
  }

  @Override
  public synchronized void publish(
      Topic topic, Message message, int deliveryMode, int priority, long timeToLive)
      throws JMSException {

    super.send(topic, message, deliveryMode, priority, timeToLive);
  }
}
