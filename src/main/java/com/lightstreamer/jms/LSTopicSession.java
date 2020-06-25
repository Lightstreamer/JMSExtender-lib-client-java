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
