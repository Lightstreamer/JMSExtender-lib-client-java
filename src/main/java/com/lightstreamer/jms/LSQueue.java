package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.DestinationType;
import javax.jms.JMSException;
import javax.jms.Queue;

class LSQueue extends LSDestination implements Queue {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueue(LSSessionImpl session, String name) {
    super(session, DestinationType.QUEUE, name);
  }

  /////////////////////////////////////////////////////////////////////////
  // Queue interface

  @Override
  public String getQueueName() throws JMSException {
    return name;
  }
}
