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
