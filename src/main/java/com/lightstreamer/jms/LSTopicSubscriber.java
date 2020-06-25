package com.lightstreamer.jms;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

class LSTopicSubscriber extends LSMessageConsumer implements TopicSubscriber {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTopicSubscriber(
      LSSessionImpl session,
      LSTopic destination,
      String subscriptionName,
      boolean durable,
      boolean noLocal,
      boolean shared,
      String selector) {
    super(session, destination, subscriptionName, durable, noLocal, shared, selector);
  }

  /////////////////////////////////////////////////////////////////////////
  // TopicSubscriber interface

  @Override
  public Topic getTopic() throws JMSException {
    return (LSTopic) destination;
  }

  @Override
  public boolean getNoLocal() throws JMSException {
    return noLocal;
  }
}
