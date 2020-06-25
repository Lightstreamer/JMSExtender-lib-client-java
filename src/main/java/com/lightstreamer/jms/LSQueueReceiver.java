package com.lightstreamer.jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

class LSQueueReceiver extends LSMessageConsumer implements QueueReceiver {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueueReceiver(LSSessionImpl session, LSQueue destination, String selector) {
    super(session, destination, null, false, false, false, selector);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueReceiver interface

  @Override
  public Queue getQueue() throws JMSException {
    return (LSQueue) destination;
  }
}
