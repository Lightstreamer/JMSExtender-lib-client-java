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
